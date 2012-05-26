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

	public static NodoRuteadorMinimo create() {
		final NodoRuteadorMinimo nodo = new NodoRuteadorMinimo();
		nodo.processor = ExecutorBasedTaskProcesor.create();
		return nodo;
	}

	/**
	 * Invocado para indicar que este nodo no será más utilizado y puede liberar los recursos de
	 * procesamiento de tareas
	 */
	public void liberarRecursos() {
		this.processor.detener();
	}
}
