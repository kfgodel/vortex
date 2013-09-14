/**
 * Created on: Sep 14, 2013 5:32:26 PM by: Dario L. Garcia
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
package net.gaia.vortex.impl.builder;

import net.gaia.vortex.api.builder.VortexSockets;
import net.gaia.vortex.api.moleculas.NodoSocket;
import net.gaia.vortex.impl.atomos.Desocketizador;
import net.gaia.vortex.impl.atomos.Socketizador;
import net.gaia.vortex.impl.moleculas.MoleculaSocket;
import ar.dgarcia.objectsockets.api.ObjectSocket;

/**
 * Esta clase implementa el builder de componentes vortex para sockets
 * 
 * @author dgarcia
 */
public class VortexSocketsBuilder implements VortexSockets {

	/**
	 * @see net.gaia.vortex.api.builder.VortexSockets#nodoSocket(ar.dgarcia.objectsockets.api.ObjectSocket)
	 */
	public NodoSocket nodoSocket(final ObjectSocket socket) {
		return MoleculaSocket.create(socket, this);
	}

	/**
	 * @see net.gaia.vortex.api.builder.VortexSockets#socketizador(ar.dgarcia.objectsockets.api.ObjectSocket)
	 */
	public Socketizador socketizador(final ObjectSocket socket) {
		return Socketizador.create(socket);
	}

	/**
	 * @see net.gaia.vortex.api.builder.VortexSockets#desocketizado()
	 */
	public Desocketizador desocketizado() {
		return Desocketizador.create();
	}

	public static VortexSocketsBuilder create() {
		final VortexSocketsBuilder builder = new VortexSocketsBuilder();
		return builder;
	}
}
