/**
 * 20/05/2012 20:45:22 Copyright (C) 2011 Darío L. García
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

import java.util.concurrent.atomic.AtomicReference;

import net.gaia.taskprocessor.api.WorkUnit;
import net.gaia.vortex.core.api.HandlerDeMensajesVecinos;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Esta calse representa la tarea que hace el nodo al recibir un mensaje de vecino avisando al
 * handler designado
 * 
 * @author D. García
 */
public class InvocarHandlerDeMensajesTask implements WorkUnit {
	private static final Logger LOG = LoggerFactory.getLogger(InvocarHandlerDeMensajesTask.class);

	private AtomicReference<HandlerDeMensajesVecinos> handlerRef;
	private Object mensaje;

	/**
	 * @see net.gaia.taskprocessor.api.WorkUnit#doWork()
	 */
	@Override
	public void doWork() throws InterruptedException {
		final HandlerDeMensajesVecinos handler = handlerRef.get();
		try {
			handler.onMensajeDeVecinoRecibido(mensaje);
		} catch (final Exception e) {
			LOG.error("Se produjo un error no manejable al invocar el handler: " + handler, e);
		}
	}

	public static InvocarHandlerDeMensajesTask create(final AtomicReference<HandlerDeMensajesVecinos> handlerRef,
			final Object mensaje) {
		final InvocarHandlerDeMensajesTask invocacion = new InvocarHandlerDeMensajesTask();
		invocacion.handlerRef = handlerRef;
		invocacion.mensaje = mensaje;
		return invocacion;
	}
}
