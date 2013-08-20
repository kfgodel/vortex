/**
 * 28/01/2013 16:33:28 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.router.impl.moleculas.support;

import net.gaia.vortex.api.basic.Receptor;
import net.gaia.vortex.router.api.moleculas.NodoBidireccional;
import net.gaia.vortex.router.impl.moleculas.listeners.ListenerDeConexionesBidiEnNodo;
import net.gaia.vortex.router.impl.moleculas.patas.PataBidireccional;
import ar.com.dgarcia.coding.caching.DefaultInstantiator;
import ar.com.dgarcia.coding.caching.WeakSingleton;

/**
 * Esta clase implementa el listener nulo de conexiones bidi para evitar nulls
 * 
 * @author D. García
 */
public class ListenerNuloDeConexionBidiEnNodo implements ListenerDeConexionesBidiEnNodo {

	private static final WeakSingleton<ListenerNuloDeConexionBidiEnNodo> ultimaReferencia = new WeakSingleton<ListenerNuloDeConexionBidiEnNodo>(
			DefaultInstantiator.create(ListenerNuloDeConexionBidiEnNodo.class));

	public static ListenerNuloDeConexionBidiEnNodo getInstancia() {
		return ultimaReferencia.get();
	}

	/**
	 * @see net.gaia.vortex.router.impl.moleculas.listeners.ListenerDeConexionesBidiEnNodo#onConexionBidiDe(net.gaia.vortex.router.api.moleculas.NodoBidireccional,
	 *      net.gaia.vortex.api.basic.Receptor,
	 *      net.gaia.vortex.router.impl.moleculas.patas.PataBidireccional)
	 */
	
	public void onConexionBidiDe(final NodoBidireccional origen, final Receptor destino,
			final PataBidireccional pataConectada) {
		// No hacemos nada
	}

}
