/**
 * 20/05/2012 20:49:31 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.core.impl.tasks;

import java.util.concurrent.ConcurrentLinkedQueue;

import net.gaia.taskprocessor.api.TaskProcessor;
import net.gaia.taskprocessor.api.WorkUnit;
import net.gaia.vortex.core.api.Nodo;

/**
 * Esta clase representa la tarea que realiza el nodo en la que envía el mensaje recibido desde un
 * vecino al resto
 * 
 * @author D. García
 */
public class EnviarMensajeAOtrosVecinosTask implements WorkUnit {

	private Nodo nodoEmisorOriginal;
	private Object mensaje;
	private ConcurrentLinkedQueue<Nodo> vecinos;
	private TaskProcessor processor;
	private Nodo nodoRelay;

	/**
	 * @see net.gaia.taskprocessor.api.WorkUnit#doWork()
	 */
	@Override
	public void doWork() throws InterruptedException {
		for (final Nodo vecino : vecinos) {
			if (nodoEmisorOriginal.equals(vecino)) {
				// No se lo mandamos a sí mismo
				continue;
			}
			final EnviarMensajeAVecinoTask envioAVecino = EnviarMensajeAVecinoTask.create(nodoRelay, mensaje, vecino);
			processor.process(envioAVecino);
		}
	}

	public static EnviarMensajeAOtrosVecinosTask create(final Nodo nodoRelay, final Nodo emisorOriginal,
			final Object mensaje, final ConcurrentLinkedQueue<Nodo> nodosVecinos, final TaskProcessor processor) {
		final EnviarMensajeAOtrosVecinosTask envio = new EnviarMensajeAOtrosVecinosTask();
		envio.nodoEmisorOriginal = emisorOriginal;
		envio.nodoRelay = nodoRelay;
		envio.mensaje = mensaje;
		envio.vecinos = nodosVecinos;
		envio.processor = processor;
		return envio;
	}
}