/**
 * 18/06/2012 22:01:25 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.sockets.impl.tasks;

import net.gaia.taskprocessor.api.InterruptedThreadException;
import net.gaia.taskprocessor.api.WorkParallelizer;
import net.gaia.taskprocessor.api.WorkUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ar.com.dgarcia.lang.strings.ToString;
import ar.dgarcia.objectsockets.api.ObjectSocket;

/**
 * Esta clase representa la tarea realizada por un componente para enviar los datos por un socket
 * 
 * @author D. García
 */
public class EnviarPorSocket implements WorkUnit {
	private static final Logger LOG = LoggerFactory.getLogger(EnviarPorSocket.class);

	private ObjectSocket socket;
	public static final String socket_FIELD = "socket";

	private Object datos;
	public static final String datos_FIELD = "datos";

	/**
	 * @see net.gaia.taskprocessor.api.WorkUnit#doWork()
	 */

	@Override
	public void doWork(final WorkParallelizer parallelizer) throws InterruptedThreadException {
		try {
			socket.send(datos);
		} catch (final Exception e) {
			LOG.error("Se produjo un error enviado los datos[" + datos + "] por el socket[" + socket
					+ "]. Ignorando error", e);
		}
	}

	public static EnviarPorSocket create(final ObjectSocket socket, final Object datos) {
		final EnviarPorSocket envio = new EnviarPorSocket();
		envio.socket = socket;
		envio.datos = datos;
		return envio;
	}

	/**
	 * @see java.lang.Object#toString()
	 */

	@Override
	public String toString() {
		return ToString.de(this).add(socket_FIELD, socket).add(datos_FIELD, datos).toString();
	}
}
