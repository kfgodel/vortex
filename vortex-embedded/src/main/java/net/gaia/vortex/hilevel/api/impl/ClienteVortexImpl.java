/**
 * 26/01/2012 23:11:34 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.hilevel.api.impl;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

import net.gaia.vortex.hilevel.api.ClienteVortex;
import net.gaia.vortex.hilevel.api.FiltroDeMensajesDelCliente;
import net.gaia.vortex.hilevel.api.HandlerDeMensajesApi;
import net.gaia.vortex.hilevel.api.MensajeVortexApi;
import net.gaia.vortex.hilevel.api.TagsDelNodo;
import net.gaia.vortex.lowlevel.api.ErroresDelMensaje;
import net.gaia.vortex.lowlevel.api.MensajeVortexHandler;
import net.gaia.vortex.lowlevel.api.NodoVortex;
import net.gaia.vortex.lowlevel.api.SesionVortex;
import net.gaia.vortex.lowlevel.impl.ids.GeneradorDeMensajesImpl;
import net.gaia.vortex.lowlevel.impl.ids.GeneradorMensajesDeNodo;
import net.gaia.vortex.protocol.messages.ContenidoVortex;
import net.gaia.vortex.protocol.messages.MensajeVortex;
import net.gaia.vortex.protocol.messages.MetamensajeVortex;
import net.gaia.vortex.protocol.messages.conn.CerrarConexion;
import net.gaia.vortex.protocol.messages.routing.AcuseConsumo;
import net.gaia.vortex.protocol.messages.routing.AcuseDuplicado;
import net.gaia.vortex.protocol.messages.routing.AcuseFallaRecepcion;
import net.gaia.vortex.protocol.messages.routing.SolicitudAcuseConsumo;
import net.gaia.vortex.protocol.messages.routing.SolicitudEsperaAcuseConsumo;
import net.gaia.vortex.protocol.messages.tags.AgregarTags;
import net.gaia.vortex.protocol.messages.tags.LimpiarTags;
import net.gaia.vortex.protocol.messages.tags.QuitarTags;
import net.gaia.vortex.protocol.messages.tags.ReemplazarTags;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Esta es una implementación del cliente vortex en memoria
 * 
 * @author D. García
 */
public class ClienteVortexImpl implements ClienteVortex, MensajeVortexHandler {
	private static final Logger LOG = LoggerFactory.getLogger(ClienteVortexImpl.class);

	private AtomicReference<HandlerDeMensajesApi> handlerDeMensajes;
	private NodoVortex nodoVortex;
	private SesionVortex sesionVortex;
	private GeneradorMensajesDeNodo generadorMensajes;
	private FiltroDeMensajesDelCliente filtroDeMensajes;
	private TagsDelNodo tagsDelNodo;

	public TagsDelNodo getTagsDelNodo() {
		return tagsDelNodo;
	}

	/**
	 * @see net.gaia.vortex.hilevel.api.ClienteVortex#enviar(net.gaia.vortex.hilevel.api.MensajeVortexApi)
	 */
	@Override
	public void enviar(final MensajeVortexApi mensajeAEnviar) {
		final Object contenido = mensajeAEnviar.getContenido();
		final String tipoDeContenido = mensajeAEnviar.getTipoDeContenido();
		final Set<String> tags = mensajeAEnviar.getTagsDelMensaje();
		final MensajeVortex mensajeVortex = generadorMensajes.generarMensajePara(contenido, tipoDeContenido, tags);
		enviarMensajeVortex(mensajeVortex);
	}

	/**
	 * @param mensajeVortex
	 */
	private void enviarMensajeVortex(final MensajeVortex mensajeVortex) {
		sesionVortex.enviar(mensajeVortex);
	}

	/**
	 * @see net.gaia.vortex.hilevel.api.ClienteVortex#setHandlerDeMensajes(net.gaia.vortex.hilevel.api.HandlerDeMensajesApi)
	 */
	@Override
	public void setHandlerDeMensajes(final HandlerDeMensajesApi handler) {
		handlerDeMensajes.set(handler);
	}

	public static ClienteVortexImpl create(final NodoVortex nodoVortex, final HandlerDeMensajesApi handlerDeMensajes) {
		final ClienteVortexImpl cliente = new ClienteVortexImpl();
		cliente.nodoVortex = nodoVortex;
		cliente.handlerDeMensajes = new AtomicReference<HandlerDeMensajesApi>(handlerDeMensajes);
		cliente.generadorMensajes = GeneradorDeMensajesImpl.create();
		cliente.tagsDelNodo = TagsDelNodo.create();
		cliente.initialize();
		cliente.filtroDeMensajes = FiltroDeMensajesDelClienteImpl.create(cliente);
		return cliente;
	}

	/**
	 * Inicializa el estado de este cliente para poder enviar y recibir mensajes
	 */
	private void initialize() {
		this.sesionVortex = nodoVortex.crearNuevaSesion(this);
	}

	/**
	 * Invocado cuando llega un mensaje vortex desde el nodo
	 * 
	 * @see net.gaia.vortex.lowlevel.api.MensajeVortexHandler#onMensajeRecibido(net.gaia.vortex.protocol.messages.MensajeVortex)
	 */
	@Override
	public void onMensajeRecibido(final MensajeVortex nuevoMensaje) {
		if (nuevoMensaje.esMetaMensaje()) {
			procesarMetaMensaje(nuevoMensaje);
		}
		// Indicamos si consumimos el mensaje
		final boolean aceptamosElMensaje = contestarAlNodoSiAceptamos(nuevoMensaje);
		if (!aceptamosElMensaje) {
			LOG.debug("Ignorando mensaje recibido[{}] rechazado por el filtro", nuevoMensaje.toPrettyPrint());
			return;
		}

		// Derivamos el mensaje al handler
		entregarAHandlerDeclarado(nuevoMensaje);
	}

	/**
	 * Entrega el mensaje pasado al handler generando el objeto intermedio
	 * 
	 * @param nuevoMensaje
	 *            El mensaje recibido y aceptado
	 */
	private void entregarAHandlerDeclarado(final MensajeVortex nuevoMensaje) {
		final ContenidoVortex contenidoVortex = nuevoMensaje.getContenido();
		final Object contenido = contenidoVortex.getValor();
		final String tipoDeContenido = contenidoVortex.getTipoContenido();
		final Set<String> tags = new LinkedHashSet<String>(nuevoMensaje.getTagsDestino());
		final MensajeVortexApi mensajeApi = MensajeVortexApi.create(contenido, tipoDeContenido, tags);
		try {
			handlerDeMensajes.get().onMensajeRecibido(mensajeApi);
		} catch (final Exception e) {
			LOG.error("Se produjo un error al derivar el mensaje recibido al handler", e);
		}
	}

	/**
	 * Verifica con los filtros de este cliente si aceptamos el mensaje y le devolvemos un acuse de
	 * consumo al nodo indicando la decisión
	 * 
	 * @param nuevoMensaje
	 *            El mensaje recibido
	 * @return true si aceptamos el mensaje, false si lo rechazamos
	 */
	private boolean contestarAlNodoSiAceptamos(final MensajeVortex nuevoMensaje) {
		final boolean aceptamosElMensaje = filtroDeMensajes.aceptaA(nuevoMensaje);
		final AcuseConsumo acuseConsumo = AcuseConsumo.create();
		if (aceptamosElMensaje) {
			acuseConsumo.setCantidadConsumidos(1L);
		}
		enviarMetaMensaje(acuseConsumo);
		return aceptamosElMensaje;
	}

	/**
	 * Realiza la semántica correcta de acuerdo al tipo de metamensaje recibido
	 * 
	 * @param nuevoMensaje
	 *            El mensaje a procesar en este cliente
	 */
	private void procesarMetaMensaje(final MensajeVortex nuevoMensaje) {
		final ContenidoVortex contenido = nuevoMensaje.getContenido();
		final Object metaObjeto = contenido.getValor();
		if (!(metaObjeto instanceof MetamensajeVortex)) {
			LOG.error("Se recibió como metamensaje algo que no es: {}. Ignorando", metaObjeto);
			return;
		}
		if (metaObjeto instanceof AcuseDuplicado) {
			final AcuseDuplicado acuse = (AcuseDuplicado) metaObjeto;
			LOG.error("Se rebotó por duplicado un mensaje que enviamos: {}", acuse.getIdMensajeDuplicado());
		} else if (metaObjeto instanceof AcuseFallaRecepcion) {
			final AcuseFallaRecepcion acuse = (AcuseFallaRecepcion) metaObjeto;
			LOG.error("Se rebotó por falla[{}] un mensaje que enviamos: {}", acuse.getCodigoError(),
					acuse.getIdMensajeFallado());
		} else if (metaObjeto instanceof AcuseConsumo) {
			final AcuseConsumo acuse = (AcuseConsumo) metaObjeto;
			LOG.error("Recibimos un acuse, y no está implementada la lógica[{}]: {}", nuevoMensaje, acuse);
		} else if (metaObjeto instanceof SolicitudEsperaAcuseConsumo) {
			final SolicitudEsperaAcuseConsumo solicitud = (SolicitudEsperaAcuseConsumo) metaObjeto;
			LOG.error("Recibimos una solicitud de espera[{}] por el consumo que no pedimos", solicitud);
		} else if (metaObjeto instanceof SolicitudAcuseConsumo) {
			final SolicitudAcuseConsumo solicitud = (SolicitudAcuseConsumo) metaObjeto;
			LOG.error(
					"Recibimos una solicitud de consumo[{}] que no corresponde. O ya la mandamos, o no deberíamos mandarla",
					solicitud);
		} else if (metaObjeto instanceof AgregarTags) {
			final AgregarTags agregado = (AgregarTags) metaObjeto;
			tagsDelNodo.agregar(agregado.getTags());
		} else if (metaObjeto instanceof QuitarTags) {
			final QuitarTags quitado = (QuitarTags) metaObjeto;
			tagsDelNodo.quitar(quitado.getTags());
		} else if (metaObjeto instanceof ReemplazarTags) {
			final ReemplazarTags quitado = (ReemplazarTags) metaObjeto;
			tagsDelNodo.reemplazar(quitado.getTags());
		} else if (metaObjeto instanceof LimpiarTags) {
			tagsDelNodo.limpiar();
		} else if (metaObjeto instanceof CerrarConexion) {
			LOG.error("Nos pidieron cerrar la conexión y no está implementado");
		}
	}

	/**
	 * @see net.gaia.vortex.hilevel.api.ClienteVortex#getFiltroDeMensajes()
	 */
	@Override
	public FiltroDeMensajesDelCliente getFiltroDeMensajes() {
		return filtroDeMensajes;
	}

	/**
	 * Envía el metamensaje indicado al nodo, envolviéndolo en un mensaje vortex
	 * 
	 * @param metamensajeVortex
	 *            El metamensaje a mandar
	 */
	protected void enviarMetaMensaje(final MetamensajeVortex metamensajeVortex) {
		final MensajeVortex mensajeVortex = generadorMensajes.generarMetaMensajePara(metamensajeVortex);
		enviarMensajeVortex(mensajeVortex);
	}

	/**
	 * @see net.gaia.vortex.lowlevel.api.MensajeVortexHandler#onMensajeConErrores(net.gaia.vortex.protocol.messages.MensajeVortex,
	 *      net.gaia.vortex.lowlevel.api.ErroresDelMensaje)
	 */
	@Override
	public void onMensajeConErrores(final MensajeVortex mensajeFallido, final ErroresDelMensaje errores) {
		LOG.error("Hubo un error no manejado con el mensaje[" + mensajeFallido + "]: " + errores);
	}

	/**
	 * @see net.gaia.vortex.hilevel.api.ClienteVortex#poll()
	 */
	@Override
	public void poll() {
		sesionVortex.poll();
	}
}
