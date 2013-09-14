/**
 * Created on: Sep 14, 2013 2:14:12 PM by: Dario L. Garcia
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
import net.gaia.vortex.api.generadores.EstrategiaDeConexionDeNodos;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ar.com.dgarcia.coding.caching.DefaultInstantiator;
import ar.com.dgarcia.coding.caching.WeakSingleton;
import ar.com.dgarcia.coding.caching.WeakSingletonSupport;

/**
 * Esta clase representa la estrategia de conexión nula que no realiza conexiones con los nodos
 * nuevos.<br>
 * Esta clase sirve
 * 
 * @author dgarcia
 */
public class EstrategiaNula extends WeakSingletonSupport implements EstrategiaDeConexionDeNodos {
	private static final Logger LOG = LoggerFactory.getLogger(EstrategiaNula.class);

	private static final WeakSingleton<EstrategiaNula> ultimaReferencia = new WeakSingleton<EstrategiaNula>(
			DefaultInstantiator.create(EstrategiaNula.class));

	public static EstrategiaNula getInstancia() {
		return ultimaReferencia.get();
	}

	/**
	 * @see net.gaia.vortex.api.generadores.EstrategiaDeConexionDeNodos#conectarNodo(net.gaia.vortex.api.basic.Nodo)
	 */
	public void conectarNodo(final Nodo nodoConectable) {
		if (LOG.isDebugEnabled()) {
			LOG.debug("La estrategia nula no conectara el nodo[{}]", nodoConectable);
		}
	}

	/**
	 * @see net.gaia.vortex.api.generadores.EstrategiaDeConexionDeNodos#desconectarNodo(net.gaia.vortex.api.basic.Nodo)
	 */
	public void desconectarNodo(final Nodo nodoDesconectable) {
		if (LOG.isDebugEnabled()) {
			LOG.debug("La estrategia nula no desconectara el nodo[{}]", nodoDesconectable);
		}
	}

}
