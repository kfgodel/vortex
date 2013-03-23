/**
 * 23/03/2013 14:36:19 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.android.messages.tasks;

import net.gaia.taskprocessor.api.WorkUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.os.Message;
import ar.com.dgarcia.lang.strings.ToString;
import ar.com.iron.android.extensions.services.remote.api.RemoteSession;

/**
 * Esta clase representa la tarea para enviar un mensaje android en la sesión remota
 * 
 * @author D. García
 */
public class EnviarMensajeAndroid implements WorkUnit {
	private static final Logger LOG = LoggerFactory.getLogger(EnviarMensajeAndroid.class);

	private RemoteSession sesion;
	public static final String sesion_FIELD = "sesion";

	private Message mensajeAndroid;
	public static final String mensajeAndroid_FIELD = "mensajeAndroid";

	/**
	 * @see net.gaia.taskprocessor.api.WorkUnit#doWork()
	 */
	@Override
	public WorkUnit doWork() throws InterruptedException {
		try {
			sesion.send(mensajeAndroid);
		} catch (final Exception e) {
			LOG.error("Se produjo un error enviado el message[" + mensajeAndroid + "] por la sesion[" + sesion
					+ "]. Ignorando error", e);
		}
		return null;
	}

	public static EnviarMensajeAndroid create(RemoteSession sesion, Message mensaje) {
		EnviarMensajeAndroid enviar = new EnviarMensajeAndroid();
		enviar.mensajeAndroid = mensaje;
		enviar.sesion = sesion;
		return enviar;
	}

	/**
	 * @see java.lang.Object#toString()
	 */

	@Override
	public String toString() {
		return ToString.de(this).add(sesion_FIELD, sesion).add(mensajeAndroid_FIELD, mensajeAndroid).toString();
	}

}
