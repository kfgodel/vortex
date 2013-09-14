/**
 * Created on: Sep 14, 2013 3:54:06 PM by: Dario L. Garcia
 * 
 * <a rel="license" href="http://creativecommons.org/licenses/by/2.5/ar/"><img
 * alt="Creative Commons License" style="border-width:0"
 * src="http://i.creativecommons.org/l/by/2.5/ar/88x31.png" /></a><br />
 * <span xmlns:dc="http://purl.org/dc/elements/1.1/"
 * href="http://purl.org/dc/dcmitype/InteractiveResource" property="dc:title"
 * rel="dc:type">Bean2Bean</span> by <a xmlns:cc="http://creativecommons.org/ns#"
 * href="https://sourceforge.net/projects/bean2bean/" property="cc:attributionName"
 * rel="cc:attributionURL">Dar&#237;o Garc&#237;a</a> is licensed under a <a rel="license"
 * href="http://creativecommons.org/licenses/by/2.5/ar/">Creative Commons Attribution 2.5 Argentina
 * License</a>.<br />
 * Based on a work at <a xmlns:dc="http://purl.org/dc/elements/1.1/"
 * href="https://bean2bean.svn.sourceforge.net/svnroot/bean2bean"
 * rel="dc:source">bean2bean.svn.sourceforge.net</a>
 */
package net.gaia.vortex.impl.atomos;

import net.gaia.vortex.api.mensajes.MensajeVortex;
import net.gaia.vortex.impl.support.ReceptorSupport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ar.com.dgarcia.lang.strings.ToString;
import ar.dgarcia.objectsockets.api.ObjectSocket;

/**
 * Esta clase representa el componente vortex que envía los mensajes recibidos por socket
 * 
 * @author dgarcia
 */
public class Socketizador extends ReceptorSupport {
	private static final Logger LOG = LoggerFactory.getLogger(Socketizador.class);

	private ObjectSocket socket;
	public static final String socket_FIELD = "socket";

	/**
	 * @see net.gaia.vortex.api.basic.Receptor#recibir(net.gaia.vortex.api.mensajes.MensajeVortex)
	 */
	public void recibir(final MensajeVortex mensaje) {
		try {
			socket.send(mensaje);
		}
		catch (final Exception e) {
			LOG.error("Se produjo un error enviado los datos[" + mensaje + "] por el socket[" + socket
					+ "]. Ignorando error", e);
		}
	}

	public static Socketizador create(final ObjectSocket socket) {
		final Socketizador socketizador = new Socketizador();
		socketizador.socket = socket;
		return socketizador;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return ToString.de(this).con(numeroDeInstancia_FIELD, getNumeroDeInstancia()).con(socket_FIELD, socket)
				.toString();
	}

}
