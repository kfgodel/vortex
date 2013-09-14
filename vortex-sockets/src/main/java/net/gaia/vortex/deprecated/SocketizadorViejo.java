/**
 * 18/06/2012 22:32:51 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.deprecated;

import net.gaia.taskprocessor.api.WorkUnit;
import net.gaia.taskprocessor.api.processor.TaskProcessor;
import net.gaia.vortex.api.mensajes.MensajeVortex;
import net.gaia.vortex.deprecated.ReceptorConProcesador;
import net.gaia.vortex.sockets.impl.tasks.EnviarPorSocket;
import ar.com.dgarcia.lang.strings.ToString;
import ar.dgarcia.objectsockets.api.ObjectSocket;

/**
 * Esta clase representa un componente vortex que los mensajes recibidos los envía por un socket
 * 
 * @author D. García
 */
@Deprecated
public class SocketizadorViejo extends ReceptorConProcesador {

	private ObjectSocket socket;
	public static final String socket_FIELD = "socket";

	/**
	 * @see net.gaia.vortex.deprecated.ReceptorConProcesador#crearTareaAlRecibir(net.gaia.vortex.api.mensajes.MensajeVortex)
	 */

	@Override
	protected WorkUnit crearTareaAlRecibir(final MensajeVortex mensaje) {
		final EnviarPorSocket envio = EnviarPorSocket.create(socket, mensaje);
		return envio;
	}

	public static SocketizadorViejo create(final TaskProcessor processor, final ObjectSocket socket) {
		final SocketizadorViejo socketizador = new SocketizadorViejo();
		socketizador.initializeWith(processor);
		socketizador.socket = socket;
		return socketizador;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return ToString.de(this).con(numeroDeInstancia_FIELD, getNumeroDeInstancia()).add(socket_FIELD, socket)
				.toString();
	}
}
