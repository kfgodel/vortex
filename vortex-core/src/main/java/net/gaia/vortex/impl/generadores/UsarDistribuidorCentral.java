/**
 * Created on: Sep 14, 2013 1:58:03 PM by: Dario L. Garcia
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
package net.gaia.vortex.impl.generadores;

import net.gaia.vortex.api.basic.Nodo;
import net.gaia.vortex.api.builder.Nodos;
import net.gaia.vortex.api.generadores.EstrategiaDeConexionDeNodos;
import net.gaia.vortex.api.moleculas.Distribuidor;
import net.gaia.vortex.api.moleculas.Terminal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Esta clase representa la estrategia de conexion de nodos que utiliza un distribuidor central para
 * no enviar los mensajes al nodo que lo emite, y no requerir ID para identificarlos
 * 
 * @author dgarcia
 */
public class UsarDistribuidorCentral implements EstrategiaDeConexionDeNodos {
	private static final Logger LOG = LoggerFactory.getLogger(UsarDistribuidorCentral.class);

	private Distribuidor distribuidorCentral;

	/**
	 * @see net.gaia.vortex.api.generadores.EstrategiaDeConexionDeNodos#conectarNodo(net.gaia.vortex.api.basic.Nodo)
	 */
	public void conectarNodo(final Nodo nodoConectable) {
		final Terminal terminalConectada = distribuidorCentral.crearTerminal();
		Nodos.interconectar(terminalConectada, nodoConectable);
	}

	/**
	 * @see net.gaia.vortex.api.generadores.EstrategiaDeConexionDeNodos#desconectarNodo(net.gaia.vortex.api.basic.Nodo)
	 */
	public void desconectarNodo(final Nodo nodoDesconectable) {
		final Terminal terminalConectada = distribuidorCentral.getTerminalConectadaA(nodoDesconectable);
		if (terminalConectada == null) {
			LOG.warn(
					"Se intento desconectar un nodo[{}] y no existe terminal para desconectarla en el distribuidor[{}]",
					nodoDesconectable, distribuidorCentral);
			return;
		}
		Nodos.desinterconectar(terminalConectada, nodoDesconectable);
	}

	public static UsarDistribuidorCentral create(final Distribuidor distribuidorCentral) {
		final UsarDistribuidorCentral estrategia = new UsarDistribuidorCentral();
		estrategia.distribuidorCentral = distribuidorCentral;
		return estrategia;
	}
}
