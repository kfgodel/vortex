/**
 * 10/05/2012 00:21:04 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.bluevortex.impl;

import net.gaia.vortex.bluevortex.api.ConexionVortex;

/**
 * Esta clase es la implementación de la conexión vortex
 * 
 * @author D. García
 */
public class ConexionVortexImpl implements ConexionVortex {

	/**
	 * Crea una nueva conexión básica
	 * 
	 * @return La conexión creada
	 */
	public static ConexionVortexImpl create() {
		final ConexionVortexImpl conexion = new ConexionVortexImpl();
		return conexion;
	}
}
