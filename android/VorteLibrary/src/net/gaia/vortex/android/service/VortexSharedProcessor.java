/**
 * 16/07/2012 15:34:09 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.android.service;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import net.gaia.taskprocessor.api.TaskProcessor;
import net.gaia.taskprocessor.api.TaskProcessorConfiguration;
import net.gaia.taskprocessor.executor.ExecutorBasedTaskProcesor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Esta clase representa un punto de entrada al procesador de tareas utilizado por los compontes
 * vortex en la aplicación android.<br>
 * Se debe utilizar este procesador en la medida de lo posible para no sobrecargar la aplicación
 * 
 * @author D. García
 */
public class VortexSharedProcessor {
	/**
	 * Espera máxima que podemos ocupar un thread de android
	 */
	private static final int MAX_ALLOWED_ANDROID_WAIT = 1500;

	private static final Logger LOG = LoggerFactory.getLogger(VortexSharedProcessor.class);

	private static TaskProcessor processor;
	private static Lock creationLock = new ReentrantLock();

	public static TaskProcessor getProcessor() {
		if (processor == null) {
			crearProcesador();
		}
		return processor;
	}

	/**
	 * Crea el procesador si es posible adquirir el lock exclusivo. Si no puede aborta la creación
	 */
	private static void crearProcesador() {
		TaskProcessorConfiguration androidConfig = createAndroidConfig();
		boolean acquired = false;
		try {
			acquired = creationLock.tryLock(MAX_ALLOWED_ANDROID_WAIT, TimeUnit.MILLISECONDS);
		} catch (InterruptedException e) {
			LOG.error("Se produjo un error al esperar para adquirir el lock", e);
		}
		if (!acquired) {
			LOG.error("No fue posible adquirir el lock para crear el procesador vortex. Puede que el procesador no se cree nunca?");
			return;
		}
		try {
			if (processor != null) {
				// Se nos adelantó otro thread
				return;
			}
			processor = ExecutorBasedTaskProcesor.create(androidConfig);
		} finally {
			creationLock.unlock();
		}
	}

	/**
	 * Crea una configuración compartida para android
	 */
	private static TaskProcessorConfiguration createAndroidConfig() {
		TaskProcessorConfiguration androidConfig = TaskProcessorConfiguration.createOptimun();
		// Dejamos fija la cantidad de threads al mínimo
		int fixedThreadPoolSize = androidConfig.getMinimunThreadPoolSize();
		if (fixedThreadPoolSize < 1) {
			fixedThreadPoolSize = 1;
		}
		if (fixedThreadPoolSize > 2) {
			fixedThreadPoolSize = 2;
		}
		androidConfig.setMinimunThreadPoolSize(fixedThreadPoolSize);
		androidConfig.setMaximunThreadPoolSize(fixedThreadPoolSize);
		return androidConfig;
	}

}
