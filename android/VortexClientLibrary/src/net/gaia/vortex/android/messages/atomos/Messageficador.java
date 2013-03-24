/**
 * 23/03/2013 14:16:07 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.android.messages.atomos;

import net.gaia.taskprocessor.api.TaskProcessor;
import net.gaia.taskprocessor.api.WorkUnit;
import net.gaia.vortex.android.messages.conversor.ConversorVortexMessage;
import net.gaia.vortex.android.messages.tasks.ConvertirYEnviarMensajeAndroid;
import net.gaia.vortex.core.api.mensaje.MensajeVortex;
import net.gaia.vortex.core.impl.atomos.support.procesador.ReceptorConProcesador;
import ar.com.dgarcia.lang.strings.ToString;
import ar.com.iron.android.extensions.services.remote.api.RemoteSession;

/**
 * Esta clase representa el componente android que convierte los mensajes vortex en mensajes android
 * para ser enviados por la mensajería android
 * 
 * @author D. García
 */
public class Messageficador extends ReceptorConProcesador {

	private RemoteSession sesionRemota;
	public static final String sesionRemota_FIELD = "sesionRemota";

	private ConversorVortexMessage converter;

	/**
	 * @see net.gaia.vortex.core.impl.atomos.support.procesador.ReceptorConProcesador#crearTareaAlRecibir(net.gaia.vortex.core.api.mensaje.MensajeVortex)
	 */

	@Override
	protected WorkUnit crearTareaAlRecibir(final MensajeVortex mensaje) {
		ConvertirYEnviarMensajeAndroid tarea = ConvertirYEnviarMensajeAndroid.create(converter, sesionRemota, mensaje);
		return tarea;
	}

	public static Messageficador create(final TaskProcessor processor, final RemoteSession sesion,
			ConversorVortexMessage converter) {
		final Messageficador messageficador = new Messageficador();
		messageficador.initializeWith(processor);
		messageficador.sesionRemota = sesion;
		messageficador.converter = converter;
		return messageficador;
	}

	/**
	 * @see java.lang.Object#toString()
	 */

	@Override
	public String toString() {
		return ToString.de(this).con(numeroDeInstancia_FIELD, getNumeroDeInstancia())
				.add(sesionRemota_FIELD, sesionRemota).toString();
	}
}
