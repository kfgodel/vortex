/**
 * 13/10/2012 11:02:41 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.tests.router.impl;

import net.gaia.vortex.tests.router.Router;
import net.gaia.vortex.tests.router.Simulador;

/**
 * Esta clase implementa el roter para la simulacion
 * 
 * @author D. García
 */
public class RouterImpl extends NodoSupport implements Router {

	public static RouterImpl create(final String nombre, final Simulador simulador) {
		final RouterImpl router = new RouterImpl();
		router.setNombre(nombre);
		router.setSimulador(simulador);
		return router;
	}
}
