/**
 * 20/05/2012 20:38:44 Copyright (C) 2011 Darío L. García
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

import net.gaia.taskprocessor.api.WorkUnit;
import net.gaia.vortex.core.api.Nodo;

/**
 * Esta clase representa la tarea realizada por el nodo para enviar el mensaje recibido a un nodo
 * vecino
 * 
 * @author D. García
 */
public class EnviarMensajeAVecinoTask implements WorkUnit {

	private Nodo nodoVecino;
	private Object mensaje;
	private Nodo nodoEmisor;

	/**
	 * @see net.gaia.taskprocessor.api.WorkUnit#doWork()
	 */
	@Override
	public void doWork() throws InterruptedException {
		nodoVecino.recibirMensajeDesde(nodoEmisor, mensaje);
	}

	public static EnviarMensajeAVecinoTask create(final Nodo nodoEmisor, final Object mensaje, final Nodo nodoVecino) {
		final EnviarMensajeAVecinoTask envio = new EnviarMensajeAVecinoTask();
		envio.nodoVecino = nodoVecino;
		envio.mensaje = mensaje;
		envio.nodoEmisor = nodoEmisor;
		return envio;
	}
}
