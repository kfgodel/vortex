/**
 * 22/12/2012 19:08:16 Copyright (C) 2011 Darío L. García
 * 
 * <a rel="license" href="http://creativecommons.org/licenses/by/3.0/"><img
 * alt="Creative Commons License" style="border-width:0"
 * src="http://i.creativecommons.org/l/by/3.0/88x31.png" /></a><br />
 * <span xmlns:dct="http://purl.org/dc/terms/" href="http://purl.org/dc/dcmitype/Text"
 * property="dct:title" rel="dct:type">Software</span> by <span
 * xmlns:cc="http://creativecommons.org/ns#" property="cc:attributionName">Darío García</span> is
 * licensed under a <a rel="license" href="http://creativecommons.org/licenses/by/3.0/">Creative
 * Commons Attribution 3.0 Unported License</a>.
 */
package net.gaia.vortex.router.impl.moleculas.patas;

import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import net.gaia.taskprocessor.api.TaskProcessor;
import net.gaia.vortex.core.api.annotations.Molecula;
import net.gaia.vortex.core.api.atomos.Receptor;
import net.gaia.vortex.core.api.condiciones.Condicion;
import net.gaia.vortex.core.api.mensaje.MensajeVortex;
import net.gaia.vortex.core.api.moleculas.FlujoVortex;
import net.gaia.vortex.core.api.moleculas.condicional.Selector;
import net.gaia.vortex.core.impl.atomos.condicional.NexoFiltro;
import net.gaia.vortex.core.impl.atomos.emisores.EmisorNulo;
import net.gaia.vortex.core.impl.atomos.receptores.ReceptorNulo;
import net.gaia.vortex.core.impl.atomos.transformacion.NexoTransformador;
import net.gaia.vortex.core.impl.condiciones.SiempreTrue;
import net.gaia.vortex.core.impl.moleculas.condicional.SelectorConFiltros;
import net.gaia.vortex.core.impl.moleculas.flujos.FlujoInmutable;
import net.gaia.vortex.core.impl.moleculas.support.NodoMoleculaSupport;
import net.gaia.vortex.portal.impl.conversion.api.ConversorDeMensajesVortex;
import net.gaia.vortex.portal.impl.transformaciones.GenerarIdEnMensaje;
import net.gaia.vortex.router.api.moleculas.NodoBidireccional;
import net.gaia.vortex.router.impl.condiciones.EsConfirmacionDeIdRemoto;
import net.gaia.vortex.router.impl.condiciones.EsConfirmacionParaEstaPata;
import net.gaia.vortex.router.impl.condiciones.EsPedidoDeIdRemoto;
import net.gaia.vortex.router.impl.condiciones.EsReconfirmacionDeIdRemoto;
import net.gaia.vortex.router.impl.condiciones.EsReconfirmacionParaEstaPata;
import net.gaia.vortex.router.impl.condiciones.EsRespuestaDeIdRemoto;
import net.gaia.vortex.router.impl.condiciones.EsRespuestaParaEstaPata;
import net.gaia.vortex.router.impl.filtros.ParteDeCondiciones;
import net.gaia.vortex.router.impl.messages.bidi.PedidoDeIdRemoto;
import net.gaia.vortex.router.impl.moleculas.memoria.MemoriaDePedidosDeId;
import net.gaia.vortex.router.impl.transformaciones.ConvertirPedidoEnRespuestaDeId;
import net.gaia.vortex.router.impl.transformaciones.RegistrarIdRemotoYEnviarConfirmacion;
import net.gaia.vortex.router.impl.transformaciones.RegistrarIdRemotoYEnviarReconfirmacion;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Esta clase es la implementación de la pata bidireccional
 * 
 * @author D. García
 */
@Molecula
public class PataBidi extends NodoMoleculaSupport implements PataBidireccional {
	private static final Logger LOG = LoggerFactory.getLogger(PataBidi.class);

	private static final AtomicLong proximoId = new AtomicLong(0);

	private NodoBidireccional nodoLocal;
	public static final String nodoLocal_FIELD = "nodoLocal";

	private Receptor nodoRemoto;
	public static final String nodoRemoto_FIELD = "nodoRemoto";

	private Long idLocal;
	public static final String idLocal_FIELD = "idLocal";

	private AtomicLong idRemoto;
	public static final String idRemoto_FIELD = "idRemoto";

	private ParteDeCondiciones filtroDeSalida;
	public static final String filtroDeSalida_FIELD = "filtroDeSalida";

	private Condicion filtroDeEntrada;
	public static final String filtroDeEntrada_FIELD = "filtroDeEntrada";

	private Condicion filtroDeEntradaPublicado;
	public static final String filtroDeEntradaPublicado_FIELD = "filtroDeEntradaPublicado";

	private MemoriaDePedidosDeId memoriaDePedidosEnviados;

	private ConversorDeMensajesVortex mapeador;

	private GenerarIdEnMensaje generadorDeIds;

	/**
	 * Asigna ID a los metamensajes que son generados en esta pata
	 */
	private NexoTransformador identificadorDeMetamensajes;

	public static PataBidi create(final NodoBidireccional nodoLocal, final Receptor nodoRemoto,
			final TaskProcessor taskProcessor, final ParteDeCondiciones parteDeCondicion,
			final ConversorDeMensajesVortex mapeador, final GenerarIdEnMensaje generadorDeIds) {
		final PataBidi pata = new PataBidi();
		pata.mapeador = mapeador;
		pata.filtroDeSalida = parteDeCondicion;
		pata.setIdLocal(proximoId.getAndIncrement());
		pata.nodoLocal = nodoLocal;
		pata.idRemoto = new AtomicLong();
		pata.nodoRemoto = nodoRemoto;
		pata.generadorDeIds = generadorDeIds;
		pata.memoriaDePedidosEnviados = MemoriaDePedidosDeId.create();
		pata.identificadorDeMetamensajes = NexoTransformador.create(taskProcessor, generadorDeIds, nodoRemoto);
		pata.initializeWith(taskProcessor);
		pata.inicializarFiltros();
		return pata;
	}

	/**
	 * Inicializa el estado de esta pata y las conexiones internas
	 */
	private void initializeWith(final TaskProcessor taskProcessor) {
		// Creamos un selector para tomar un camino según el tipo de mensaje
		final Selector selectorDeEntrada = SelectorConFiltros.create(taskProcessor);

		// Al recibir un pedido enviamos la respuesta identificando esta pata
		final Receptor procesoAlRecibirPedidoDeId = crearProcesoParaRecibirPedidoDeIds(taskProcessor);
		selectorDeEntrada.conectarCon(procesoAlRecibirPedidoDeId, EsPedidoDeIdRemoto.create());

		// Al recibir una respuesta registramos el id de la pata remota y enviamos confirmación
		final Receptor procesoAlRecibirRespuestaDeId = crearProcesoParaRecibirRespuestaDeIds(taskProcessor);
		selectorDeEntrada.conectarCon(procesoAlRecibirRespuestaDeId, EsRespuestaDeIdRemoto.create());

		// Al recibir una confirmación registramos el id de la pata remota y enviamos una
		// re-confirmación
		final Receptor procesoAlRecibirConfirmacionDeId = crearProcesoParaRecibirConfirmacionDeIds(taskProcessor);
		selectorDeEntrada.conectarCon(procesoAlRecibirConfirmacionDeId, EsConfirmacionDeIdRemoto.create());

		// Al recibir una reconfirmación registramos el evento de nueva conexion bidi
		final Receptor procesoAlRecibirReconfirmacionDeId = crearProcesoParaRecibirReconfirmacionDeIds(taskProcessor);
		selectorDeEntrada.conectarCon(procesoAlRecibirReconfirmacionDeId, EsReconfirmacionDeIdRemoto.create());

		final FlujoVortex flujo = FlujoInmutable.create(selectorDeEntrada, EmisorNulo.getInstancia());
		initializeWith(flujo);
	}

	/**
	 * Crea el proceso utlizado para procesar el mensaje que corresponde a una reconfirmación de id
	 * 
	 * @param taskProcessor
	 *            El procesador para crear los componentes
	 * @return El receptor de entrada para los mensajes
	 */
	private Receptor crearProcesoParaRecibirReconfirmacionDeIds(final TaskProcessor taskProcessor) {
		// Primero descartamos las confirmaciones que son para otras patas
		final NexoFiltro descartadorDeReconfirmacionesAjenas = NexoFiltro.create(taskProcessor,
				EsReconfirmacionParaEstaPata.create(getIdLocal()), ReceptorNulo.getInstancia());

		return descartadorDeReconfirmacionesAjenas;
	}

	/**
	 * Crea el proceso utilizado al recibir mensajes de confirmación
	 * 
	 * @param taskProcessor
	 *            El procesador para crear los componentes
	 * @return El componente de entrada para los mensajes
	 */
	private Receptor crearProcesoParaRecibirConfirmacionDeIds(final TaskProcessor taskProcessor) {
		// Primero descartamos las confirmaciones que son para otras patas
		final NexoFiltro descartadorDeConfirmacionesAjenas = NexoFiltro.create(taskProcessor,
				EsConfirmacionParaEstaPata.create(getIdLocal()), ReceptorNulo.getInstancia());

		// disparamos el evento de nueva conexion

		// Finalmente registramos id remoto y convertimos la respuesta en confirmación
		final NexoTransformador registradorDeId = NexoTransformador.create(taskProcessor,
				RegistrarIdRemotoYEnviarReconfirmacion.create(getIdLocal(), idRemoto, mapeador),
				identificadorDeMetamensajes);
		descartadorDeConfirmacionesAjenas.conectarCon(registradorDeId);

		return descartadorDeConfirmacionesAjenas;
	}

	/**
	 * Crea el proceso realizado al recibir la respuesta de ID
	 * 
	 * @param taskProcessor
	 *            El procesador para crear los componentes
	 * @return El componente que es el inicio del proceso
	 */
	private Receptor crearProcesoParaRecibirRespuestaDeIds(final TaskProcessor taskProcessor) {
		// Primero descartamos las respuestas que son para otras patas
		final NexoFiltro descartadorDeRespuestasAjenas = NexoFiltro.create(taskProcessor,
				EsRespuestaParaEstaPata.create(memoriaDePedidosEnviados), ReceptorNulo.getInstancia());

		// Finalmente registramos id remoto y convertimos la respuesta en confirmación
		final NexoTransformador registradorDeId = NexoTransformador.create(taskProcessor,
				RegistrarIdRemotoYEnviarConfirmacion.create(getIdLocal(), idRemoto, mapeador),
				identificadorDeMetamensajes);
		descartadorDeRespuestasAjenas.conectarCon(registradorDeId);

		return descartadorDeRespuestasAjenas;
	}

	/**
	 * Crea el proceso realizado al recibir mensajes por pedido de ID
	 * 
	 * @param taskProcessor
	 *            El procesador de tareas para crear los componentes
	 * @return El proceso creado
	 */
	private Receptor crearProcesoParaRecibirPedidoDeIds(final TaskProcessor taskProcessor) {

		// Convertimos el pedido recibido en una respuesta con nuestro id de pata
		final NexoTransformador convertidorEnRespuesta = NexoTransformador.create(taskProcessor,
				ConvertirPedidoEnRespuestaDeId.create(getIdLocal(), mapeador), identificadorDeMetamensajes);

		return convertidorEnRespuesta;
	}

	/**
	 * Establece el estado inicial de los filtros en esta pata
	 */
	private void inicializarFiltros() {
		// Inicialmente entra y sale todo lo que recibamos
		this.filtroDeSalida.cambiarA(SiempreTrue.getInstancia());
		this.filtroDeEntrada = SiempreTrue.getInstancia();
		this.resetearFiltroDeEntradaPublicado();
	}

	/**
	 * Cambia el estado de publicación del filtro de entrada. Marcándolo como si el filtro de
	 * entrada aún no estuviese publicado (para posterior publicación)
	 */
	private void resetearFiltroDeEntradaPublicado() {
		this.filtroDeEntradaPublicado = FiltroNoPublicado.getInstancia();
	}

	public NodoBidireccional getNodoLocal() {
		return nodoLocal;
	}

	public void setNodoLocal(final NodoBidireccional nodoLocal) {
		this.nodoLocal = nodoLocal;
	}

	public Receptor getNodoRemoto() {
		return nodoRemoto;
	}

	public void setNodoRemoto(final Receptor nodoRemoto) {
		this.nodoRemoto = nodoRemoto;
	}

	public Long getIdLocal() {
		return idLocal;
	}

	public void setIdLocal(final Long idLocal) {
		this.idLocal = idLocal;
	}

	public Long getIdRemoto() {
		return idRemoto.get();
	}

	public void setIdRemoto(final Long idRemoto) {
		this.idRemoto.set(idRemoto);
	}

	/**
	 * Comienza el intercambio de meta-mensajes para determinar con qué otra pata se está hablando,
	 * y poder establecer comunicaciones bidireccionales.<br>
	 * Esta acción mandará un meta-mensaje por esta pata para solicitar un ID de pata remota
	 */
	public void conseguirIdRemoto() {
		enviarPedidoDeId();
	}

	/**
	 * Genera un nuevo pedido de id registrándolo y enviándolo al nodo remoto
	 */
	private void enviarPedidoDeId() {
		final PedidoDeIdRemoto pedido = PedidoDeIdRemoto.create();
		final MensajeVortex mensajeDelPedido = mapeador.convertirAVortex(pedido);
		final MensajeVortex mensajeConId = generadorDeIds.transformar(mensajeDelPedido);
		final Map<String, Object> idDeMensajeComoMapa = mensajeConId.getContenido().getIdDeMensajeComoMapa();
		memoriaDePedidosEnviados.registrarPedidoConId(idDeMensajeComoMapa);
		getNodoRemoto().recibir(mensajeConId);
	}

	/**
	 * @see net.gaia.vortex.router.impl.moleculas.patas.PataBidireccional#tieneComoNodoRemotoA(net.gaia.vortex.core.api.atomos.Receptor)
	 */
	@Override
	public boolean tieneComoNodoRemotoA(final Receptor nodo) {
		return getNodoRemoto().equals(nodo);
	}

	/**
	 * @see net.gaia.vortex.router.impl.moleculas.patas.PataBidireccional#getParteDeCondicion()
	 */
	@Override
	public ParteDeCondiciones getParteDeCondicion() {
		return this.filtroDeSalida;
	}

}
