/**
 * 23/03/2013 19:31:16 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.android.messages.impl;

import net.gaia.taskprocessor.api.TaskProcessor;
import net.gaia.vortex.android.messages.moleculas.NexoMessager;
import net.gaia.vortex.core.impl.atomos.receptores.ReceptorNulo;
import net.gaia.vortex.server.api.EstrategiaDeConexionDeNexos;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.os.Message;
import ar.com.dgarcia.coding.exceptions.UnhandledConditionException;
import ar.com.iron.android.extensions.services.remote.api.RemoteSession;
import ar.com.iron.android.extensions.services.remote.api.support.RemoteSessionHandlerSupport;
import ar.com.iron.annotations.MayBeNull;

/**
 * Esta clase representa el handler de sesiones de mensjesria android para vortex
 * 
 * @author D. García
 */
public class VortexSessionHandler extends RemoteSessionHandlerSupport {
	private static final Logger LOG = LoggerFactory.getLogger(VortexSessionHandler.class);

	private EstrategiaDeConexionDeNexos estrategiaDeConexion;

	private TaskProcessor processor;

	public EstrategiaDeConexionDeNexos getEstrategiaDeConexion() {
		return estrategiaDeConexion;
	}

	public void setEstrategiaDeConexion(EstrategiaDeConexionDeNexos estrategiaDeConexion) {
		this.estrategiaDeConexion = estrategiaDeConexion;
	}

	/**
	 * @see ar.com.iron.android.extensions.services.remote.api.support.RemoteSessionHandlerSupport#onSessionStarted(ar.com.iron.android.extensions.services.remote.api.RemoteSession)
	 */
	@Override
	public void onSessionStarted(RemoteSession startedSession) {
		LOG.debug("Creando nexo para la sesion remota[{}] ", startedSession);
		final NexoMessager nuevoNexo = NexoMessager.create(processor, startedSession, ReceptorNulo.getInstancia());
		startedSession.setUserObject(nuevoNexo);
		try {
			estrategiaDeConexion.onNexoCreado(nuevoNexo);
		} catch (final Exception e) {
			LOG.error("Se produjo un error en la estrategia de conexion[" + estrategiaDeConexion
					+ "] al pasarle el nexo[" + nuevoNexo + "]. Ignorando error", e);
		}
	}

	/**
	 * @see ar.com.iron.android.extensions.services.remote.api.support.RemoteSessionHandlerSupport#onSessionStopped(ar.com.iron.android.extensions.services.remote.api.RemoteSession)
	 */
	@Override
	public void onSessionStopped(RemoteSession sesionCerrada) {
		LOG.debug("Cerrando nexo para la sesion remota[{}]", sesionCerrada);
		final NexoMessager nexoCerrado = getNexoDelSocket(sesionCerrada);
		if (nexoCerrado == null) {
			LOG.error("Se cerró una sesion[{}] que no tiene nexo asociado?", sesionCerrada);
			return;
		}
		try {
			estrategiaDeConexion.onNexoCerrado(nexoCerrado);
		} catch (final Exception e) {
			LOG.error("Se produjo un error en la estrategia de desconexion[" + estrategiaDeConexion
					+ "] al pasarle el nexo[" + nexoCerrado + "]. Ignorando error", e);
		}
		sesionCerrada.setUserObject(null);
	}

	/**
	 * Devuelve el nexo asociado a la sesion, si es que hay uno
	 * 
	 * @param sesion
	 *            El socket del que se quiere obtener el nexo
	 * @return
	 */
	@MayBeNull
	public static NexoMessager getNexoDelSocket(final RemoteSession sesion) {
		final Object objeto = sesion.getUserObject();
		if (objeto == null) {
			return null;
		}
		NexoMessager nexoCerrado;
		try {
			nexoCerrado = (NexoMessager) objeto;
		} catch (final ClassCastException e) {
			throw new UnhandledConditionException(
					"Se obtuvo de la sesion un objeto[" + objeto + "] que no es un nexo?", e);
		}
		return nexoCerrado;
	}

	/**
	 * @see ar.com.iron.android.extensions.services.remote.api.support.RemoteSessionReceptionHandler#onMessageReceived(ar.com.iron.android.extensions.services.remote.api.RemoteSession,
	 *      android.os.Message)
	 */
	@Override
	public void onMessageReceived(RemoteSession sesion, Message mensaje) {
		NexoMessager nexoDelSocket = getNexoDelSocket(sesion);
		if (nexoDelSocket == null) {
			LOG.error("Se recibio un mensaje[" + mensaje + "] para una sesion sin nexo? Ignorando mensaje");
			return;
		}
		nexoDelSocket.onMessageReceived(sesion, mensaje);
	}

	/**
	 * @see ar.com.iron.android.extensions.services.remote.api.support.RemoteSessionHandlerSupport#onExceptionCaught(ar.com.iron.android.extensions.services.remote.api.RemoteSession,
	 *      java.lang.Throwable)
	 */
	@Override
	public void onExceptionCaught(RemoteSession failedSession, Throwable error) {
		NexoMessager nexo = getNexoDelSocket(failedSession);
		LOG.error("Se produjo un error en la sesion[" + failedSession + "] con nexo[" + nexo + "]. Cerrando", error);
		failedSession.close();
	}

	public static VortexSessionHandler create(TaskProcessor processor, EstrategiaDeConexionDeNexos estrategiaDeConexion) {
		VortexSessionHandler handler = new VortexSessionHandler();
		handler.estrategiaDeConexion = estrategiaDeConexion;
		handler.processor = processor;
		return handler;
	}
}
