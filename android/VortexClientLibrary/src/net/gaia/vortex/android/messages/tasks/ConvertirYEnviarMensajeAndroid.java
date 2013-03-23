/**
 * 23/03/2013 14:34:57 Copyright (C) 2011 Darío L. García
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
import net.gaia.vortex.android.messages.atomos.Messageficador;
import net.gaia.vortex.android.messages.conversor.CannotMessagificarException;
import net.gaia.vortex.android.messages.conversor.ConversorVortexMessage;
import net.gaia.vortex.core.api.mensaje.MensajeVortex;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.os.Message;
import ar.com.dgarcia.lang.strings.ToString;
import ar.com.iron.android.extensions.services.remote.api.RemoteSession;

/**
 * Esta clase representa la tarea realizada por el componente {@link Messageficador} para convertir
 * el mensaje vortex y enviarlo como mensaje android
 * 
 * @author D. García
 */
public class ConvertirYEnviarMensajeAndroid implements WorkUnit {
	private static final Logger LOG = LoggerFactory.getLogger(ConvertirYEnviarMensajeAndroid.class);

	private RemoteSession sesion;
	public static final String sesion_FIELD = "sesion";

	private MensajeVortex mensajeVortex;
	public static final String mensajeVortex_FIELD = "mensajeVortex";

	private ConversorVortexMessage converter;
	public static final String converter_FIELD = "converter";

	/**
	 * @see net.gaia.taskprocessor.api.WorkUnit#doWork()
	 */

	@Override
	public WorkUnit doWork() throws InterruptedException {
		Message message;
		try {
			message = converter.convertToMessage(mensajeVortex);
		} catch (CannotMessagificarException e) {
			LOG.error("Se produjo un error convirtiendo a message[" + mensajeVortex + "]. Ignorando mensaje", e);
			return null;
		}
		return EnviarMensajeAndroid.create(sesion, message);
	}

	public static ConvertirYEnviarMensajeAndroid create(ConversorVortexMessage converter, RemoteSession sesion,
			MensajeVortex mensaje) {
		ConvertirYEnviarMensajeAndroid convertir = new ConvertirYEnviarMensajeAndroid();
		convertir.converter = converter;
		convertir.mensajeVortex = mensaje;
		convertir.sesion = sesion;
		return convertir;
	}

	/**
	 * @see java.lang.Object#toString()
	 */

	@Override
	public String toString() {
		return ToString.de(this).add(sesion_FIELD, sesion).add(mensajeVortex_FIELD, mensajeVortex)
				.con(converter_FIELD, converter).toString();
	}
}
