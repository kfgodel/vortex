/**
 * 17/06/2012 20:55:17 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.portal.impl.tasks;

import net.gaia.taskprocessor.api.InterruptedThreadException;
import net.gaia.taskprocessor.api.WorkParallelizer;
import net.gaia.taskprocessor.api.WorkUnit;
import net.gaia.vortex.portal.api.mensaje.HandlerDeObjetos;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ar.com.dgarcia.lang.strings.ToString;

/**
 * Esta clase representa la tarea realizada por un componente vortex para entregar el objeto al
 * handler asociado
 * 
 * @author D. García
 */
public class InvocarHandler<T> implements WorkUnit {
	private static final Logger LOG = LoggerFactory.getLogger(InvocarHandler.class);

	private T mensajeRecibido;
	public static final String mensajeRecibido_FIELD = "mensajeRecibido";

	private HandlerDeObjetos<? super T> handler;
	public static final String handler_FIELD = "handler";

	/**
	 * @see net.gaia.taskprocessor.api.WorkUnit#doWork()
	 */

	public void doWork(final WorkParallelizer parallelizer) throws InterruptedThreadException {
		try {
			handler.onObjetoRecibido(mensajeRecibido);
		} catch (final Exception e) {
			LOG.error("Se produjo un error en el handler[" + handler + "] al recibir el mensaje[" + mensajeRecibido
					+ "]", e);
		}
	}

	public static <T> InvocarHandler<T> create(final T mensajeRecibido, final HandlerDeObjetos<? super T> handler) {
		final InvocarHandler<T> invocacion = new InvocarHandler<T>();
		invocacion.handler = handler;
		invocacion.mensajeRecibido = mensajeRecibido;
		return invocacion;
	}

	/**
	 * @see java.lang.Object#toString()
	 */

	@Override
	public String toString() {
		return ToString.de(this).add(mensajeRecibido_FIELD, mensajeRecibido).add(handler_FIELD, handler).toString();
	}
}
