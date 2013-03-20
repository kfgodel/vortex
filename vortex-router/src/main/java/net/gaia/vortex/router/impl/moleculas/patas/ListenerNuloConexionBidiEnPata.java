/**
 * 28/01/2013 16:21:11 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.router.impl.moleculas.patas;

import ar.com.dgarcia.coding.caching.DefaultInstantiator;
import ar.com.dgarcia.coding.caching.WeakSingleton;

/**
 * Esta clase implementa el listner para evitar nulls
 * 
 * @author D. García
 */
public class ListenerNuloConexionBidiEnPata implements ListenerConexionBidiEnPata {

	private static final WeakSingleton<ListenerNuloConexionBidiEnPata> ultimaReferencia = new WeakSingleton<ListenerNuloConexionBidiEnPata>(
			DefaultInstantiator.create(ListenerNuloConexionBidiEnPata.class));

	public static ListenerNuloConexionBidiEnPata getInstancia() {
		return ultimaReferencia.get();
	}

	/**
	 * @see net.gaia.vortex.router.impl.moleculas.patas.ListenerConexionBidiEnPata#onConexionBidiPara(net.gaia.vortex.router.impl.moleculas.patas.PataBidireccional)
	 */
	
	public void onConexionBidiPara(final PataBidireccional pata) {
		// No hacemos nada
	}

}
