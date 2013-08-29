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
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

import net.gaia.taskprocessor.api.processor.TaskProcessor;
import net.gaia.vortex.api.basic.Receptor;
import net.gaia.vortex.core.api.annotations.Molecula;
import net.gaia.vortex.core.api.condiciones.Condicion;
import net.gaia.vortex.core.api.mensaje.MensajeVortex;
import net.gaia.vortex.core.api.moleculas.FlujoVortexViejo;
import net.gaia.vortex.core.api.moleculas.condicional.Selector;
import net.gaia.vortex.core.impl.atomos.condicional.NexoFiltroViejo;
import net.gaia.vortex.core.impl.atomos.emisores.EmisorNulo;
import net.gaia.vortex.core.impl.atomos.forward.NexoEjecutor;
import net.gaia.vortex.core.impl.atomos.transformacion.NexoTransformador;
import net.gaia.vortex.core.impl.condiciones.SiempreTrue;
import net.gaia.vortex.core.impl.moleculas.condicional.SelectorConFiltros;
import net.gaia.vortex.core.impl.moleculas.flujos.FlujoInmutableViejo;
import net.gaia.vortex.core.impl.moleculas.support.NodoMoleculaSupport;
import net.gaia.vortex.core.prog.Loggers;
import net.gaia.vortex.impl.nulos.ReceptorNulo;
import net.gaia.vortex.impl.support.ReceptorSupport;
import net.gaia.vortex.portal.impl.conversion.api.ConversorDeMensajesVortex;
import net.gaia.vortex.portal.impl.transformaciones.GenerarIdEnMensaje;
import net.gaia.vortex.router.api.listeners.ListenerDeRuteo;
import net.gaia.vortex.router.api.moleculas.NodoBidireccional;
import net.gaia.vortex.router.impl.condiciones.EsConfirmacionDeIdRemoto;
import net.gaia.vortex.router.impl.condiciones.EsMensajeNormal;
import net.gaia.vortex.router.impl.condiciones.EsPedidoDeIdRemoto;
import net.gaia.vortex.router.impl.condiciones.EsPublicacionDeFiltros;
import net.gaia.vortex.router.impl.condiciones.EsReconfirmacionDeIdRemoto;
import net.gaia.vortex.router.impl.condiciones.EsRespuestaDeIdRemoto;
import net.gaia.vortex.router.impl.condiciones.EsRespuestaParaEstaPata;
import net.gaia.vortex.router.impl.condiciones.LeInteresaElMensaje;
import net.gaia.vortex.router.impl.condiciones.VinoPorOtraPata;
import net.gaia.vortex.router.impl.ejecutors.CambiarFiltroDeSalida;
import net.gaia.vortex.router.impl.ejecutors.RegistrarRuteo;
import net.gaia.vortex.router.impl.filtros.ConjuntoDeCondiciones;
import net.gaia.vortex.router.impl.filtros.ParteDeCondiciones;
import net.gaia.vortex.router.impl.messages.PublicacionDeFiltros;
import net.gaia.vortex.router.impl.messages.bidi.PedidoDeIdRemoto;
import net.gaia.vortex.router.impl.moleculas.memoria.MemoriaDePedidosDeId;
import net.gaia.vortex.router.impl.transformaciones.AsignarIdLocalAlReceptor;
import net.gaia.vortex.router.impl.transformaciones.ConvertirPedidoEnRespuestaDeId;
import net.gaia.vortex.router.impl.transformaciones.RegistrarIdRemotoYEnviarConfirmacion;
import net.gaia.vortex.router.impl.transformaciones.RegistrarIdRemotoYEnviarReconfirmacion;
import net.gaia.vortex.sets.impl.serializacion.SerializadorDeCondiciones;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ar.com.dgarcia.lang.strings.ToString;

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

	private AtomicReference<Long> idRemoto;
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

	private AtomicBoolean habilitadaComoBidi;

	private ListenerConexionBidiEnPata listenerConexionBidi;

	/**
	 * Asigna ID a los metamensajes que son generados en esta pata
	 */
	private NexoTransformador identificadorDeMetamensajes;

	private SerializadorDeCondiciones serializador;

	/**
	 * Este receptor sólo sirve para que al ser invocado se ejecute el método indicado
	 */
	private final ReceptorSupport dispararEventoConexionBidi = new ReceptorSupport() {

		public void recibir(final MensajeVortex mensaje) {
			evento_nevaConexionBidireccional();
		}
	};

	private AtomicReference<ListenerDeRuteo> listenerDeRuteo;

	private ReceptorSupport wrapperDelRemotoConLogueo;

	public static PataBidi create(final NodoBidireccional nodoLocal, final Receptor nodoRemoto,
			final TaskProcessor taskProcessor, final ConjuntoDeCondiciones conjuntoDeCondiciones,
			final Condicion filtroDeEntradaParaPataNueva, final ConversorDeMensajesVortex mapeador,
			final GenerarIdEnMensaje generadorDeIds, final SerializadorDeCondiciones serializador,
			final AtomicReference<ListenerDeRuteo> listenerDeRuteo) {
		final PataBidi pata = new PataBidi();
		pata.serializador = serializador;
		pata.listenerConexionBidi = ListenerNuloConexionBidiEnPata.getInstancia();
		pata.mapeador = mapeador;
		pata.habilitadaComoBidi = new AtomicBoolean(false);
		pata.setIdLocal(proximoId.getAndIncrement());
		pata.nodoLocal = nodoLocal;
		pata.idRemoto = new AtomicReference<Long>(null);
		pata.nodoRemoto = nodoRemoto;
		pata.generadorDeIds = generadorDeIds;
		pata.memoriaDePedidosEnviados = MemoriaDePedidosDeId.create();
		// Este wrapper permite loguear el envio del metamensaje antes de su entrega real
		pata.wrapperDelRemotoConLogueo = new ReceptorSupport() {
			public void recibir(final MensajeVortex mensaje) {
				final Receptor nodoRemoto = pata.getNodoRemoto();
				Loggers.BIDI_MSG.debug(
						"  Enviando desde[{}] hasta[{}] por[{}] mensaje[{}]: {}",
						new Object[] { pata.getNodoLocal().toShortString(), nodoRemoto.toShortString(),
								pata.toShortString(), mensaje.toShortString(), mensaje.getContenido() });
				nodoRemoto.recibir(mensaje);
			}
		};
		pata.identificadorDeMetamensajes = NexoTransformador.create(taskProcessor, generadorDeIds,
				pata.wrapperDelRemotoConLogueo);
		pata.listenerDeRuteo = listenerDeRuteo;
		pata.inicializarFiltros(conjuntoDeCondiciones, filtroDeEntradaParaPataNueva);
		pata.initializeWith(taskProcessor);
		return pata;
	}

	public ListenerConexionBidiEnPata getListenerConexionBidi() {
		return listenerConexionBidi;
	}

	public void setListenerConexionBidi(final ListenerConexionBidiEnPata listenerConexionBidi) {
		if (listenerConexionBidi == null) {
			throw new IllegalArgumentException("El listener de conexiones bidi no puede ser null");
		}
		this.listenerConexionBidi = listenerConexionBidi;
	}

	/**
	 * Inicializa el estado de esta pata y las conexiones internas
	 */
	private void initializeWith(final TaskProcessor taskProcessor) {
		// Creamos un selector para tomar un camino según el tipo de mensaje
		final Selector selectorDeEntrada = SelectorConFiltros.create(taskProcessor);

		// Al recibir un mensaje normal (no meta)
		final Receptor procesoAlRecibirMensajeNormal = crearProcesoParaRecibirMensajesNormales(taskProcessor);
		selectorDeEntrada.conectarCon(procesoAlRecibirMensajeNormal, EsMensajeNormal.create());

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

		// Al recibir una publicación de filtros
		final Receptor procesoAlRecibirPublicacionDeFiltros = crearProcesoParaRecibirPublicacionDeFiltros(taskProcessor);
		selectorDeEntrada.conectarCon(procesoAlRecibirPublicacionDeFiltros, EsPublicacionDeFiltros.create());

		final FlujoVortexViejo flujo = FlujoInmutableViejo.create(selectorDeEntrada, EmisorNulo.getInstancia());
		initializeWith(flujo);
	}

	/**
	 * Crea el componente vortex que realiza el proceso de esta pata al realizar un mensaje de
	 * publicación de filtros
	 * 
	 * @param taskProcessor
	 *            El procesador para crear los componentes
	 * @return El comopnente creado como entrada
	 */
	private Receptor crearProcesoParaRecibirPublicacionDeFiltros(final TaskProcessor taskProcessor) {
		// Actualizamos el filtro que tenemos de salida en esta pata
		final CambiarFiltroDeSalida cambiadorDeFiltro = CambiarFiltroDeSalida.create(mapeador, serializador,
				filtroDeSalida, this);

		return cambiadorDeFiltro;
	}

	/**
	 * Crea el componente que procesa los mensajes normales (no metamensajes) antes de enviarlos a
	 * destino
	 * 
	 * @param taskProcessor
	 *            El procesador para los componentes creados
	 * @return El componente de entrada para los mensajes
	 */
	private Receptor crearProcesoParaRecibirMensajesNormales(final TaskProcessor taskProcessor) {
		// Descartamos los mensajes que vinieron de esta pata (evitando rebote de mensajes)
		final NexoFiltroViejo descartarMensajesPropios = NexoFiltroViejo.create(taskProcessor, VinoPorOtraPata.create(this),
				ReceptorNulo.getInstancia());

		// Verificamos si el nodo remoto acepta el mensaje según los filtros que nos dijo
		final NexoFiltroViejo descartarMensajesQueNoLeInteresan = NexoFiltroViejo.create(taskProcessor,
				LeInteresaElMensaje.create(filtroDeSalida, this), ReceptorNulo.getInstancia());
		descartarMensajesPropios.conectarCon(descartarMensajesQueNoLeInteresan);

		// Le registramos nuestro ID de pata para evitar rebotes
		final NexoTransformador asignadoDeIdRemoto = NexoTransformador.create(taskProcessor,
				AsignarIdLocalAlReceptor.create(idRemoto), ReceptorNulo.getInstancia());
		descartarMensajesQueNoLeInteresan.conectarCon(asignadoDeIdRemoto);

		// Registramos el ruteo que estamos haciendo del mensaje
		final NexoEjecutor registradorDeRuteo = NexoEjecutor.create(taskProcessor,
				RegistrarRuteo.create(listenerDeRuteo, this), getNodoRemoto());
		asignadoDeIdRemoto.conectarCon(registradorDeRuteo);

		return descartarMensajesPropios;
	}

	/**
	 * Crea el proceso utlizado para procesar el mensaje que corresponde a una reconfirmación de id
	 * 
	 * @param taskProcessor
	 *            El procesador para crear los componentes
	 * @return El receptor de entrada para los mensajes
	 */
	private Receptor crearProcesoParaRecibirReconfirmacionDeIds(final TaskProcessor taskProcessor) {
		return dispararEventoConexionBidi;
	}

	/**
	 * Crea el proceso utilizado al recibir mensajes de confirmación
	 * 
	 * @param taskProcessor
	 *            El procesador para crear los componentes
	 * @return El componente de entrada para los mensajes
	 */
	private Receptor crearProcesoParaRecibirConfirmacionDeIds(final TaskProcessor taskProcessor) {
		// Registramos id remoto y convertimos la confirmación en re-confirmación
		final NexoTransformador registradorDeId = NexoTransformador.create(taskProcessor,
				RegistrarIdRemotoYEnviarReconfirmacion.create(getIdLocal(), idRemoto, mapeador),
				ReceptorNulo.getInstancia());

		// Antes de mandar el mensaje disparamos el evento de nueva conexion bidi creada
		final NexoEjecutor disparadorDeNuevaConexion = NexoEjecutor.create(taskProcessor, dispararEventoConexionBidi,
				identificadorDeMetamensajes);
		registradorDeId.conectarCon(disparadorDeNuevaConexion);

		return registradorDeId;
	}

	/**
	 * Invocado al completarse los pasos para la comunicación bidireccional de esta pata
	 */
	protected void evento_nevaConexionBidireccional() {
		if (!habilitadaComoBidi.compareAndSet(false, true)) {
			// Ya estaba habilitada como bidi anteriormente
			return;
		}
		LOG.debug("En [{},{}] se estableció un conexion bidireccional", new Object[] {
				this.getNodoLocal().toShortString(), this.toShortString() });
		publicarFiltroDeEntrada();
		// Notificamos al listener de la nueva conexion
		this.listenerConexionBidi.onConexionBidiPara(this);
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
		final NexoFiltroViejo descartadorDeRespuestasAjenas = NexoFiltroViejo.create(taskProcessor,
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
	 * 
	 * @param conjuntoDeCondiciones
	 * @param filtroDeEntradaParaPataNueva
	 */
	private void inicializarFiltros(final ConjuntoDeCondiciones conjuntoDeCondiciones,
			final Condicion filtroDeEntradaParaPataNueva) {
		// Inicialmente entra y sale todo lo que recibamos
		this.filtroDeSalida = conjuntoDeCondiciones.crearNuevaParte(SiempreTrue.getInstancia());
		this.setFiltroDeEntrada(filtroDeEntradaParaPataNueva);
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
		LOG.debug("Enviando por [{}] pedido de id[{}] ", this.toShortString(), mensajeConId.toShortString());
		wrapperDelRemotoConLogueo.recibir(mensajeConId);
	}

	/**
	 * @see net.gaia.vortex.router.impl.moleculas.patas.PataBidireccional#tieneComoNodoRemotoA(net.gaia.vortex.api.basic.Receptor)
	 */

	public boolean tieneComoNodoRemotoA(final Receptor nodo) {
		return getNodoRemoto().equals(nodo);
	}

	/**
	 * @see net.gaia.vortex.router.impl.moleculas.patas.PataBidireccional#getParteDeCondicion()
	 */

	public ParteDeCondiciones getParteDeCondicion() {
		return this.filtroDeSalida;
	}

	/**
	 * @see net.gaia.vortex.router.impl.moleculas.patas.PataBidireccional#actualizarFiltroDeEntrada(net.gaia.vortex.core.api.condiciones.Condicion)
	 */

	public void actualizarFiltroDeEntrada(final Condicion nuevoFiltro) {
		this.setFiltroDeEntrada(nuevoFiltro);
		publicarFiltroDeEntrada();
	}

	public Condicion getFiltroDeEntrada() {
		return filtroDeEntrada;
	}

	public void setFiltroDeEntrada(final Condicion filtroDeEntrada) {
		this.filtroDeEntrada = filtroDeEntrada;
	}

	/**
	 * Publica el filtro actual de entrada sólo si es distinto del publicado previamente
	 */
	private void publicarFiltroDeEntrada() {
		final Condicion filtroAPublicar = this.getFiltroDeEntrada();
		if (!habilitadaComoBidi.get()) {
			LOG.debug("  En [{},{}] no se publica filtro {} porque la pata aun no es bidi", new Object[] {
					this.getNodoLocal().toShortString(), this.toShortString(), filtroAPublicar });
			// El otro nodo no está preparado para recibir la publicacion
			return;
		}

		final Long idRemotoDeEstaPata = getIdRemoto();
		if (idRemotoDeEstaPata == null) {
			LOG.debug("  En [{},{}] no se publica filtro {} porque no tenemos ID remoto", new Object[] {
					this.getNodoLocal().toShortString(), this.toShortString(), filtroAPublicar });
			// Aún no tenemos un ID remoto con el cual publicar los filtros
			return;
		}
		if (filtroAPublicar.equals(this.getFiltroDeEntradaPublicado())) {
			LOG.debug("  En [{},{}] no se re-publica porque no hubo cambio de filtro local: {}", new Object[] {
					this.getNodoLocal().toShortString(), this.toShortString(), filtroAPublicar });
			return;
		}
		LOG.debug("  En [{},{}] se publicará filtro local de {} a: {}", new Object[] {
				this.getNodoLocal().toShortString(), this.toShortString(), getFiltroDeEntradaPublicado(),
				filtroAPublicar });

		setFiltroDeEntradaPublicado(filtroAPublicar);
		final Map<String, Object> filtroSerializado = serializador.serializar(filtroAPublicar);
		final PublicacionDeFiltros publicacion = PublicacionDeFiltros.create(filtroSerializado, idRemotoDeEstaPata);
		final MensajeVortex mensajeDePublicacion = mapeador.convertirAVortex(publicacion);

		identificadorDeMetamensajes.recibir(mensajeDePublicacion);
	}

	public Condicion getFiltroDeEntradaPublicado() {
		return filtroDeEntradaPublicado;
	}

	public void setFiltroDeEntradaPublicado(final Condicion filtroDeEntradaPublicado) {
		this.filtroDeEntradaPublicado = filtroDeEntradaPublicado;
	}

	/**
	 * @see java.lang.Object#toString()
	 */

	@Override
	public String toString() {
		return ToString.de(this).con(numeroDeInstancia_FIELD, getNumeroDeInstancia()).con(idLocal_FIELD, idLocal)
				.con(idRemoto_FIELD, idRemoto).con(nodoLocal_FIELD, nodoLocal.toShortString())
				.con(nodoRemoto_FIELD, nodoRemoto.toShortString()).toString();
	}

}
