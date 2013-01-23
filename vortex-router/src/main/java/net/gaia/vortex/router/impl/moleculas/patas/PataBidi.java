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

import java.util.concurrent.atomic.AtomicLong;

import net.gaia.taskprocessor.api.TaskProcessor;
import net.gaia.vortex.core.api.annotations.Molecula;
import net.gaia.vortex.core.api.atomos.Receptor;
import net.gaia.vortex.core.api.condiciones.Condicion;
import net.gaia.vortex.core.api.mensaje.MensajeVortex;
import net.gaia.vortex.core.api.moleculas.FlujoVortex;
import net.gaia.vortex.core.api.moleculas.condicional.Selector;
import net.gaia.vortex.core.impl.atomos.emisores.EmisorNulo;
import net.gaia.vortex.core.impl.atomos.memoria.NexoSinDuplicados;
import net.gaia.vortex.core.impl.atomos.transformacion.NexoTransformador;
import net.gaia.vortex.core.impl.condiciones.SiempreTrue;
import net.gaia.vortex.core.impl.moleculas.condicional.SelectorConFiltros;
import net.gaia.vortex.core.impl.moleculas.flujos.FlujoInmutable;
import net.gaia.vortex.core.impl.moleculas.support.NodoMoleculaSupport;
import net.gaia.vortex.portal.impl.conversion.api.ConversorDeMensajesVortex;
import net.gaia.vortex.portal.impl.transformaciones.GenerarIdEnMensaje;
import net.gaia.vortex.router.api.moleculas.NodoBidireccional;
import net.gaia.vortex.router.impl.condiciones.EsPedidoDeIdRemoto;
import net.gaia.vortex.router.impl.condiciones.EsRespuestaDeIdRemoto;
import net.gaia.vortex.router.impl.filtros.ParteDeCondiciones;
import net.gaia.vortex.router.impl.messages.MensajeBidireccional;
import net.gaia.vortex.router.impl.messages.PedidoDeIdRemoto;
import net.gaia.vortex.router.impl.moleculas.memoria.MemoriaDeMetamensajes;
import net.gaia.vortex.router.impl.transformaciones.ResponderPedidoDeId;

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

	private static final int CANTIDAD_DE_MENSAJES_RECORDADOS = 100;

	private static final AtomicLong proximoId = new AtomicLong(0);

	private NodoBidireccional nodoLocal;
	public static final String nodoLocal_FIELD = "nodoLocal";

	private Receptor nodoRemoto;
	public static final String nodoRemoto_FIELD = "nodoRemoto";

	private Long idLocal;
	public static final String idLocal_FIELD = "idLocal";

	private Long idRemoto;
	public static final String idRemoto_FIELD = "idRemoto";

	private ParteDeCondiciones filtroDeSalida;
	public static final String filtroDeSalida_FIELD = "filtroDeSalida";

	private Condicion filtroDeEntrada;
	public static final String filtroDeEntrada_FIELD = "filtroDeEntrada";

	private Condicion filtroDeEntradaPublicado;
	public static final String filtroDeEntradaPublicado_FIELD = "filtroDeEntradaPublicado";

	private MemoriaDeMetamensajes memoriaDeMetamensajes;

	private ConversorDeMensajesVortex mapeador;

	private GenerarIdEnMensaje generadorDeIds;

	private NexoTransformador identificadorParaPedidosYRespuestas;

	private NexoTransformador identificadorParaConfirmaciones;

	public static PataBidi create(final NodoBidireccional nodoLocal, final Receptor nodoRemoto,
			final TaskProcessor taskProcessor, final ParteDeCondiciones parteDeCondicion,
			final ConversorDeMensajesVortex mapeador, final GenerarIdEnMensaje generadorDeIds) {
		final PataBidi pata = new PataBidi();
		pata.mapeador = mapeador;
		pata.filtroDeSalida = parteDeCondicion;
		pata.setIdLocal(proximoId.getAndIncrement());
		pata.nodoLocal = nodoLocal;
		pata.nodoRemoto = nodoRemoto;
		pata.generadorDeIds = generadorDeIds;
		pata.memoriaDeMetamensajes = MemoriaDeMetamensajes.create(CANTIDAD_DE_MENSAJES_RECORDADOS);
		pata.initializeWith(taskProcessor);
		pata.inicializarFiltros();
		return pata;
	}

	/**
	 * Inicializa el estado de esta pata y las conexiones internas
	 */
	private void initializeWith(final TaskProcessor taskProcessor) {

		// Creamos los componentes que se usarán a la salida de los mensajes
		crearComponentesCompartidosEntreSalidas(taskProcessor);

		// Creamos un selector para tomar un camino segun el tipo de mensaje
		final Selector selectorDeEntrada = SelectorConFiltros.create(taskProcessor);

		// Al recibir un pedido enviamos la respuesta identificando esta pata
		final Receptor procesoAlRecibirPedidoDeId = crearProcesoAlRecibirPedidoDeIds(taskProcessor);
		selectorDeEntrada.conectarCon(procesoAlRecibirPedidoDeId, EsPedidoDeIdRemoto.create());

		// Al recibir una respuesta registramos el id de la pata remota y enviamos confirmacion
		final Receptor procesoAlRecibirRespuestaDeId = crearProcesoAlRecibirRespuestaDeIds(taskProcessor);
		selectorDeEntrada.conectarCon(procesoAlRecibirRespuestaDeId, EsRespuestaDeIdRemoto.create());

		final FlujoVortex flujo = FlujoInmutable.create(selectorDeEntrada, EmisorNulo.getInstancia());
		initializeWith(flujo);
	}

	/**
	 * Crea el proceso realizado al recibir la respuesta de ID
	 * 
	 * @param taskProcessor
	 *            El procesador para crear los componentes
	 * @return El componente que es el inicio del proceso
	 */
	private Receptor crearProcesoAlRecibirRespuestaDeIds(final TaskProcessor taskProcessor) {

		return null;
	}

	/**
	 * Crea los componentes compartidos por varias salidas para que estén disponibles posteriormente
	 */
	private void crearComponentesCompartidosEntreSalidas(final TaskProcessor taskProcessor) {
		// Los pedidos y respuestas enviados necesitamos registrarlos
		final NexoSinDuplicados registradorDeEnviados = NexoSinDuplicados.create(taskProcessor, memoriaDeMetamensajes,
				getNodoRemoto());
		// Además los identificamos
		identificadorParaPedidosYRespuestas = NexoTransformador.create(taskProcessor, generadorDeIds,
				registradorDeEnviados);

		// Las confirmaciones y mensajes normales sólo los identificamos
		identificadorParaConfirmaciones = NexoTransformador.create(taskProcessor, generadorDeIds, getNodoRemoto());
	}

	/**
	 * Crea el proceso realizado al recibir mensajes por pedido de ID
	 * 
	 * @param taskProcessor
	 *            El procesador de tareas para crear los componentes
	 * @return El proceso creado
	 */
	private Receptor crearProcesoAlRecibirPedidoDeIds(final TaskProcessor taskProcessor) {
		// Al recibir un pedido enviamos la respuesta identificando esta pata
		final NexoTransformador procesoAlRecibirPedidoDeId = NexoTransformador.create(taskProcessor,
				ResponderPedidoDeId.create(getIdLocal(), mapeador), identificadorParaPedidosYRespuestas);

		return procesoAlRecibirPedidoDeId;
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
		return idRemoto;
	}

	public void setIdRemoto(final Long idRemoto) {
		this.idRemoto = idRemoto;
	}

	/**
	 * Comienza el intercambio de meta-mensajes para determinar con qué otra pata se está hablando,
	 * y poder establecer comunicaciones bidireccionales.<br>
	 * Esta acción mandará un meta-mensaje por esta pata para solicitar un ID de pata remota
	 */
	public void conseguirIdRemoto() {
		final PedidoDeIdRemoto pedido = PedidoDeIdRemoto.create(getIdLocal());
		enviarMetamensaje(pedido);
	}

	/**
	 * Envía el objeto indicado como metamensaje, metiéndolo en un mensaje vortex
	 * 
	 * @param metamensaje
	 *            El metamensaje a enviar
	 */
	private void enviarMetamensaje(final MensajeBidireccional metamensaje) {
		final MensajeVortex mensaje = mapeador.convertirAVortex(metamensaje);
		nexoIdentificadorDeMensajesSalientes.recibir(mensaje);
	}

	/**
	 * Envía el mensaje pasado al nodo remoto sólo si no lo envió antes
	 * 
	 * @param mensaje
	 *            El mensaje a enviar
	 */
	private void enviarMensaje(final MensajeVortex mensaje) {
		if (!memoriaDeMetamensajes.registrarNuevo(mensaje)) {
			LOG.debug("  No enviando el mensaje[{}] recibido en [{}] por figurar como ya enviado", mensaje, this);
			return;
		}
		getNodoRemoto().recibir(mensaje);
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
