/**
 * 04/07/2012 18:40:11 Copyright (C) 2011 Darío L. García
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
package net.gaia.taskprocessor.executor;

import java.util.Collection;

import net.gaia.taskprocessor.api.TaskProcessor;
import net.gaia.taskprocessor.api.TaskProcessorConfiguration;
import net.gaia.taskprocessor.knittle.KnittleProcessor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ar.com.dgarcia.lang.strings.ToString;

/**
 * Esta clase representa un retrasador de threads utilizado por el {@link TaskProcessor} para frenar
 * un poco las tareas entrantes cuando está muy saturado de tareas
 * 
 * @author D. García
 */
public class ThreadBouncer {
	/**
	 * Cantidad límite de tareas por thread que pueden aceptarse sin espera en el
	 * {@link ExecutorBasedTaskProcesor}
	 */
	private static final int TAREAS_POR_THREAD_EN_EXECUTOR = 75;
	private static final int TAREAS_POR_THREAD_EN_KNITTLE = 1000;

	private static final Logger LOG = LoggerFactory.getLogger(ThreadBouncer.class);

	private TaskProcessor processor;
	private Collection<?> colaDeTareas;
	private int cantidadDeTareasSinEspera;
	public static final String cantidadDeTareasSinEspera_FIELD = "cantidadDeTareasSinEspera";

	/**
	 * Crea un bouncer adaptado a la cantidad de threads como core del procesador de tipo
	 * {@link ExecutorBasedTaskProcesor}
	 * 
	 * @param config
	 *            La configuración usada para el procesador
	 * @param inmediatePendingTasks
	 * 
	 * @return El bouncer creado
	 */
	public static ThreadBouncer createForExecutorBased(final ExecutorBasedTaskProcesor processor,
			final TaskProcessorConfiguration config, final Collection<?> colaDeTareas) {
		return create(processor, config, colaDeTareas, TAREAS_POR_THREAD_EN_EXECUTOR);
	}

	/**
	 * Crea un bouncer adaptado a la cantidad de threads como core del procesador de tipo
	 * {@link KnittleProcessor}
	 * 
	 * @param config
	 *            La configuración usada para el procesador
	 * @param inmediatePendingTasks
	 * 
	 * @return El bouncer creado
	 */
	public static ThreadBouncer createForKnittle(final KnittleProcessor processor,
			final TaskProcessorConfiguration config, final Collection<?> colaDeTareas) {
		return create(processor, config, colaDeTareas, TAREAS_POR_THREAD_EN_KNITTLE);
	}

	/**
	 * Crea una nuevo bouncer que generará retrasos cuando la cola de tareas exceda el límite
	 * indicado como factor por thread
	 * 
	 * @param tasksPerThreadFactor
	 *            La cantidad de tareas máxima por thread del procesador que no tienen espera
	 * @param colaDeTareas
	 *            La cola de tareas del procesador
	 * @return El retrasador de tareas creado
	 */
	public static ThreadBouncer create(final TaskProcessor processor, final TaskProcessorConfiguration config,
			final Collection<?> colaDeTareas, final int tasksPerThreadFactor) {
		final ThreadBouncer bouncer = new ThreadBouncer();
		final int minimunPoolSize = config.getMinimunThreadPoolSize();
		bouncer.cantidadDeTareasSinEspera = minimunPoolSize * tasksPerThreadFactor;
		bouncer.colaDeTareas = colaDeTareas;
		bouncer.processor = processor;
		return bouncer;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return ToString.de(this).con(cantidadDeTareasSinEspera_FIELD, cantidadDeTareasSinEspera).toString();
	}

	/**
	 * Retrasa el pedido si está hecho desde un thread externo a este procesador y el procesado está
	 * muy saturado de tareas. Si el thread que realiza el pedido es interno de este procesador, se
	 * procesa sin espera.<br>
	 * Esta espera forzada permite un mejor desempeño del procesador al no priorizar tanto la
	 * aceptación de tareas si no está pudiendo procesar las anteriores
	 */
	public void retrasarPedidoExternoSiProcesadorSaturado() {
		// Verificamos si estamos saturados
		final int pendientes = colaDeTareas.size();
		if (pendientes < cantidadDeTareasSinEspera) {
			// No estamos saturados, no es necesario forzar la espera
			return;
		}

		// Sólo hacemos esperar final a threads externos
		final Thread currentThread = Thread.currentThread();
		if (currentThread instanceof ProcessorThread) {
			final ProcessorThread threadDeProcesador = (ProcessorThread) currentThread;
			if (threadDeProcesador.perteneceA(processor)) {
				// Es un thread propio, no lo hacemos esperar
				return;
			}
		}

		// Si es un thread externo lo hacemos esperar en proporción a lo retrasado
		final int esperaForzadaEnMillis = pendientes / cantidadDeTareasSinEspera;
		final int esperaForzadaEnMilis = esperaForzadaEnMillis;
		try {
			Thread.sleep(esperaForzadaEnMilis);
		} catch (final InterruptedException e) {
			LOG.error("Se interrumpió la espera forzada de un thread antes de aceptar nueva tarea");
		}
	}
}
