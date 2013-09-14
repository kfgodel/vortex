/**
 * Created on: Sep 14, 2013 4:15:48 PM by: Dario L. Garcia
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
package net.gaia.vortex.impl.moleculas;

import java.util.List;

import net.gaia.vortex.api.basic.Receptor;
import net.gaia.vortex.api.builder.VortexSockets;
import net.gaia.vortex.api.mensajes.MensajeVortex;
import net.gaia.vortex.api.moleculas.NodoSocket;
import net.gaia.vortex.impl.atomos.Desocketizador;
import net.gaia.vortex.impl.atomos.Socketizador;
import net.gaia.vortex.impl.support.ReceptorSupport;
import ar.com.dgarcia.lang.strings.ToString;
import ar.dgarcia.objectsockets.api.Disposable;
import ar.dgarcia.objectsockets.api.ObjectReceptionHandler;
import ar.dgarcia.objectsockets.api.ObjectSocket;

/**
 * Esta clase representa un componente vortex que une redes vortex con un socket de manera que los
 * mensajes entre máquinas remotas.<br>
 * 
 * @author dgarcia
 */
public class MoleculaSocket extends ReceptorSupport implements NodoSocket, Disposable {

	private ObjectSocket socket;
	public static final String socket_FIELD = "socket";

	private Socketizador desdeVortex;

	private Desocketizador haciaVortex;

	/**
	 * @see net.gaia.vortex.api.basic.emisores.Conectable#conectarCon(net.gaia.vortex.api.basic.Receptor)
	 */
	public void conectarCon(final Receptor destino) {
		haciaVortex.conectarCon(destino);
	}

	/**
	 * @see net.gaia.vortex.api.basic.emisores.Conectable#desconectar()
	 */
	public void desconectar() {
		haciaVortex.desconectar();
	}

	/**
	 * @see net.gaia.vortex.api.basic.emisores.Conectable#desconectarDe(net.gaia.vortex.api.basic.Receptor)
	 */
	public void desconectarDe(final Receptor destino) {
		haciaVortex.desconectarDe(destino);
	}

	/**
	 * @see net.gaia.vortex.api.basic.emisores.Conectable#getConectados()
	 */
	public List<Receptor> getConectados() {
		return haciaVortex.getConectados();
	}

	/**
	 * @see net.gaia.vortex.api.basic.Receptor#recibir(net.gaia.vortex.api.mensajes.MensajeVortex)
	 */
	public void recibir(final MensajeVortex mensaje) {
		desdeVortex.recibir(mensaje);
	}

	/**
	 * @see ar.dgarcia.objectsockets.api.Disposable#closeAndDispose()
	 */
	public void closeAndDispose() {
		socket.closeAndDispose();
	}

	/**
	 * @see net.gaia.vortex.api.moleculas.NodoSocket#getObjectReceptionHandler()
	 */
	public ObjectReceptionHandler getObjectReceptionHandler() {
		return haciaVortex;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return ToString.de(this).con(numeroDeInstancia_FIELD, getNumeroDeInstancia()).con(socket_FIELD, socket)
				.toString();
	}

	public static MoleculaSocket create(final ObjectSocket socket, final VortexSockets builder) {
		final MoleculaSocket molecula = new MoleculaSocket();
		molecula.socket = socket;
		molecula.desdeVortex = builder.socketizador(socket);
		molecula.haciaVortex = builder.desocketizado();
		return molecula;
	}

	/**
	 * @see net.gaia.vortex.api.moleculas.NodoSocket#getSocket()
	 */
	public ObjectSocket getSocket() {
		return socket;
	}

}
