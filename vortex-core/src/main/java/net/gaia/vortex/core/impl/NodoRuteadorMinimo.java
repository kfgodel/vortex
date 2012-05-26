/**
 * 20/05/2012 19:23:22 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.core.impl;

import net.gaia.taskprocessor.api.TaskProcessor;
import net.gaia.taskprocessor.api.TaskProcessorConfiguration;
import net.gaia.taskprocessor.executor.ExecutorBasedTaskProcesor;
import net.gaia.vortex.core.api.Nodo;
import net.gaia.vortex.core.impl.tasks.EnviarMensajeAOtrosVecinosTask;

/**
 * Esta clase implementa la versión más simple del nodo ruteador.<br>
 * Esta versión funciona en memoria entregando a los vecinos el mensaje que recibe de otro nodo
 * 
 * @author D. García
 */
public class NodoRuteadorMinimo extends NodoSupport implements Nodo {

	private TaskProcessor processor;

	/**
	 * @see net.gaia.vortex.core.api.Nodo#recibirMensajeDesde(net.gaia.vortex.core.api.Nodo,
	 *      java.lang.Object)
	 */
	@Override
	public void recibirMensajeDesde(final Nodo emisor, final Object mensaje) {
		final EnviarMensajeAOtrosVecinosTask enviarAOtros = EnviarMensajeAOtrosVecinosTask.create(this, emisor,
				mensaje, getNodosVecinos(), processor);
		this.processor.process(enviarAOtros);
	}

	/**
	 * Crea un nodo de ruteo mínimo compartiendo el procesador de tareas indicado
	 * 
	 * @param procesadorDeTareas
	 *            El procesado a utilizar para rutear los mensajes asíncronamente
	 * @return El nodo ruteador creado
	 */
	public static NodoRuteadorMinimo create(final TaskProcessor procesadorDeTareas) {
		final NodoRuteadorMinimo nodo = new NodoRuteadorMinimo();
		nodo.processor = procesadorDeTareas;
		return nodo;
	}

	/**
	 * Crea un nodo por default creando también su procesador de tareas interno
	 * 
	 * @return El nodo ruteador creado listo para usar
	 */
	public static NodoRuteadorMinimo create() {
		final TaskProcessorConfiguration config = TaskProcessorConfiguration.create();
		config.setThreadPoolSize(4);
		final ExecutorBasedTaskProcesor procesadorDeTareas = ExecutorBasedTaskProcesor.create(config);
		final NodoRuteadorMinimo nodo = create(procesadorDeTareas);
		return nodo;
	}

	/**
	 * Invocado para indicar que este nodo no será más utilizado y puede liberar los recursos de
	 * procesamiento de tareas
	 */
	public void liberarRecursos() {
		this.processor.detener();
	}

	/**
	 * Devuelve el procesador utilizado por este nodo permitiendo compartirlo con otros nodos
	 * 
	 * @return El procesador de tareas utilizado por este nodo para rutear asíncronamente
	 */
	public TaskProcessor getProcessor() {
		return processor;
	}

}
