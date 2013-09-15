/**
 * Created on: Sep 15, 2013 7:36:08 PM by: Dario L. Garcia
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

import java.net.SocketAddress;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import net.gaia.vortex.api.basic.Nodo;
import net.gaia.vortex.api.builder.VortexSockets;
import net.gaia.vortex.api.generadores.EstrategiaDeConexionDeNodos;
import net.gaia.vortex.api.mensajes.MensajeVortex;
import net.gaia.vortex.api.moleculas.NodoSocket;
import net.gaia.vortex.api.moleculas.SocketHost;
import net.gaia.vortex.impl.sockets.ClienteDeObjectSocket;
import net.gaia.vortex.impl.sockets.ServidorDeObjectSocket;
import net.gaia.vortex.impl.support.ConectableIndirectamenteSupport;
import ar.com.dgarcia.lang.strings.ToString;
import ar.dgarcia.objectsockets.api.Disposable;

/**
 * Esta clase representa un componente vortex que mantiene conexiones remotas mediante sockets
 * entrantes y salientes. Permite armar una red distribuida en distintas máquinas interconectadas
 * por sockets a un nodo central que actua como distribuidor de mensajes.<br>
 * <br>
 * Cada conexión se representa con {@link NodoSocket}s que abstrae el socket real dentro de la red
 * vortex
 * 
 * @author dgarcia
 */
public class MoleculaSocketHost<C extends Nodo> extends ConectableIndirectamenteSupport<C> implements SocketHost<C> {

	private VortexSockets builder;

	private C nodoCentral;
	public static final String nodoCentral_FIELD = "nodoCentral";

	private final List<Disposable> conexiones = new CopyOnWriteArrayList<Disposable>();
	public static final String conexiones_FIELD = "conexiones";

	private EstrategiaDeConexionDeNodos estrategiaDeConexion;

	/**
	 * @see net.gaia.vortex.impl.support.ComponenteSupport#toString()
	 */
	@Override
	public String toString() {
		return ToString.de(this).con(numeroDeInstancia_FIELD, getNumeroDeInstancia())
				.con(nodoCentral_FIELD, nodoCentral).con(conexiones_FIELD, conexiones).toString();
	}

	public EstrategiaDeConexionDeNodos getEstrategiaDeConexion() {
		return estrategiaDeConexion;
	}

	public void setEstrategiaDeConexion(final EstrategiaDeConexionDeNodos estrategiaDeConexion) {
		this.estrategiaDeConexion = estrategiaDeConexion;
	}

	public static <C extends Nodo> MoleculaSocketHost<C> create(final C nodoCentral,
			final EstrategiaDeConexionDeNodos estrategia, final VortexSockets builder) {
		final MoleculaSocketHost<C> host = new MoleculaSocketHost<C>();
		host.builder = builder;
		host.estrategiaDeConexion = estrategia;
		host.nodoCentral = nodoCentral;
		return host;
	}

	/**
	 * @see net.gaia.vortex.api.basic.Receptor#recibir(net.gaia.vortex.api.mensajes.MensajeVortex)
	 */
	public void recibir(final MensajeVortex mensaje) {
		nodoCentral.recibir(mensaje);
	}

	/**
	 * @see net.gaia.vortex.api.basic.emisores.ConectableIndirectamente#getSalida()
	 */
	public C getSalida() {
		return nodoCentral;
	}

	/**
	 * @see net.gaia.vortex.api.moleculas.SocketHost#listenConnectionsOn(java.net.SocketAddress)
	 */
	public ServidorDeObjectSocket listenConnectionsOn(final SocketAddress listeningAddress) {
		final ServidorDeObjectSocket servidor = ServidorDeObjectSocket.create(builder, listeningAddress,
				estrategiaDeConexion);
		servidor.aceptarConexionesRemotas();
		conexiones.add(servidor);
		return servidor;

	}

	/**
	 * @see net.gaia.vortex.api.moleculas.SocketHost#connectTo(java.net.SocketAddress)
	 */
	public ClienteDeObjectSocket connectTo(final SocketAddress listeningAddress) {
		final ClienteDeObjectSocket cliente = ClienteDeObjectSocket.create(builder, listeningAddress,
				estrategiaDeConexion);
		cliente.conectarASocketRomoto();
		conexiones.add(cliente);
		return cliente;
	}

	/**
	 * @see net.gaia.vortex.api.moleculas.SocketHost#closeAndDispose()
	 */
	public void closeAndDispose() {
		for (final Disposable conexion : conexiones) {
			conexion.closeAndDispose();
		}
	}
}
