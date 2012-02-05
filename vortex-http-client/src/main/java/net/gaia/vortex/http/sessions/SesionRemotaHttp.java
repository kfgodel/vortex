/**
 * 01/02/2012 19:11:05 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.http.sessions;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import net.gaia.vortex.http.NodoRemotoHttp;
import net.gaia.vortex.http.tasks.ValidarMensajesPrevioEnvioWorkUnit;
import net.gaia.vortex.http.tasks.contexts.ContextoDeOperacionHttp;
import net.gaia.vortex.lowlevel.api.MensajeVortexHandler;
import net.gaia.vortex.lowlevel.api.SesionVortex;
import net.gaia.vortex.protocol.messages.MensajeVortex;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ar.com.dgarcia.coding.exceptions.UnhandledConditionException;

import com.google.common.base.Objects;
import com.google.common.collect.Lists;

/**
 * Esta clase representa una sesión vortex remota a través de HTTP
 * 
 * @author D. García
 */
public class SesionRemotaHttp implements SesionVortex, SesionConId {
	private static final Logger LOG = LoggerFactory.getLogger(SesionRemotaHttp.class);

	/**
	 * Valor utilizado como ID de sesión para que el nodo nos asigne uno válido
	 */
	private static final Long VALUE_TO_REQUEST_NEW_ID = Long.valueOf(-1);

	private Long sessionId;
	public static final String sessionId_FIELD = "sessionId";

	private NodoRemotoHttp nodo;
	public static final String nodo_FIELD = "nodo";

	private MensajeVortexHandler handlerDeMensajes;

	private final AtomicBoolean cerrada = new AtomicBoolean(false);

	/**
	 * @see net.gaia.vortex.lowlevel.api.SesionVortex#cerrar()
	 */
	@Override
	public void cerrar() {
		if (cerrada.get()) {
			LOG.error("Se intentó cerrar una sesión ya cerrada");
			return;
		}
		cerrada.set(true);
		LOG.debug("Sesion cerrada[{}]", this);
	}

	/**
	 * @see net.gaia.vortex.lowlevel.api.SesionVortex#enviar(net.gaia.vortex.protocol.messages.MensajeVortex)
	 */
	@Override
	public void enviar(final MensajeVortex mensaje) {
		if (cerrada.get()) {
			LOG.warn("Se itentó enviar un mensaje desde una sesión[{}] cerrada: {}", this, mensaje);
			return;
		}

		LOG.debug("Comienzo de envio en sesion[{}]: {}", this, mensaje.toPrettyPrint());

		// Realizamos la validación desde este thread para tirar excepciones si no está bien el
		// mensaje
		final List<MensajeVortex> mensajes = Lists.newArrayList(mensaje);
		final ContextoDeOperacionHttp contexto = ContextoDeOperacionHttp.create(nodo, this);
		final ValidarMensajesPrevioEnvioWorkUnit validacion = ValidarMensajesPrevioEnvioWorkUnit.create(contexto,
				mensajes);
		try {
			validacion.doWork();
		} catch (final InterruptedException e) {
			throw new UnhandledConditionException("Se interrumpió la ejecución de la validación", e);
		}
	}

	/**
	 * @see net.gaia.vortex.lowlevel.api.SesionVortex#estaCerrada()
	 */
	@Override
	public boolean estaCerrada() {
		return cerrada.get();
	}

	public static SesionRemotaHttp create(final NodoRemotoHttp nodo, final MensajeVortexHandler handlerDeMensajes) {
		final SesionRemotaHttp sesion = new SesionRemotaHttp();
		sesion.handlerDeMensajes = handlerDeMensajes;
		sesion.nodo = nodo;
		sesion.sessionId = VALUE_TO_REQUEST_NEW_ID;
		return sesion;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return Objects.toStringHelper(this).add(sessionId_FIELD, sessionId).add(nodo_FIELD, nodo).toString();
	}

	/**
	 * @see net.gaia.vortex.http.sessions.SesionConId#recibirDelNodo(net.gaia.vortex.protocol.messages.MensajeVortex)
	 */
	@Override
	public void recibirDelNodo(final MensajeVortex mensajeRecibido) {
		if (cerrada.get()) {
			LOG.warn("Se recibió un mensaje en una sesión[{}] cerrada: {}", this, mensajeRecibido.toPrettyPrint());
			return;
		}
		try {
			handlerDeMensajes.onMensajeRecibido(mensajeRecibido);
		} catch (final Exception e) {
			LOG.error(
					"Falló el handler de la sesión[" + this + "] al recibir el mensaje["
							+ mensajeRecibido.toPrettyPrint() + "]", e);
		}
	}

	/**
	 * @see net.gaia.vortex.http.sessions.SesionConId#getSessionId()
	 */
	@Override
	public Long getSessionId() {
		return sessionId;
	}

	/**
	 * @see net.gaia.vortex.http.sessions.SesionConId#cambiarId(java.lang.Long)
	 */
	@Override
	public void cambiarId(final Long nuevoId) {
		if (!sessionId.equals(nuevoId)) {
			LOG.info("Nuevo ID[{}] para sesión[{}]", nuevoId, this);
		}
		this.sessionId = nuevoId;
	}
}
