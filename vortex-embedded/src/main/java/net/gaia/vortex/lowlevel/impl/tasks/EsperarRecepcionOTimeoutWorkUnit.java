/**
 * 28/11/2011 01:08:00 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.lowlevel.impl.tasks;

import java.util.concurrent.TimeUnit;

import net.gaia.taskprocessor.api.TimeMagnitude;
import net.gaia.taskprocessor.api.WorkUnit;
import net.gaia.vortex.lowlevel.impl.ContextoDeRuteoDeMensaje;

/**
 * Esta clase representa la tarea de espera de timeout de un mensaje en el que se espera su
 * confirmación de recepción
 * 
 * @author D. García
 */
public class EsperarRecepcionOTimeoutWorkUnit implements WorkUnit {

	private ContextoDeRuteoDeMensaje contexto;

	/**
	 * Espera mínima por defecto para una confirmación de recepción
	 */
	public static final TimeMagnitude DEFAULT_TIMEOUT = TimeMagnitude.of(3, TimeUnit.SECONDS);

	/**
	 * @see net.gaia.taskprocessor.api.WorkUnit#doWork()
	 */
	public void doWork() throws InterruptedException {
		// Sacamos el mensaje en espera de confirmación (para apropiarnos de el)
		// Si no hay mensaje es que ya habrá llegado confirmación
		// Si hay, debemos solicitar la confirmación nuevamente

	}

	public static EsperarRecepcionOTimeoutWorkUnit create(final ContextoDeRuteoDeMensaje contexto) {
		final EsperarRecepcionOTimeoutWorkUnit espera = new EsperarRecepcionOTimeoutWorkUnit();
		espera.contexto = contexto;
		return espera;
	}
}
