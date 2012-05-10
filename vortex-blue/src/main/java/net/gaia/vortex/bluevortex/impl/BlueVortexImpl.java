/**
 * 09/05/2012 20:58:37 Copyright (C) 2011 Darío L. García
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

import net.gaia.vortex.bluevortex.api.BlueVortex;
import net.gaia.vortex.bluevortex.api.ConexionVortex;

/**
 * Esta clase es la implementación default de {@link BlueVortex}
 * 
 * @author D. García
 */
public class BlueVortexImpl implements BlueVortex {

	/**
	 * Crea una nueva instancia de vortex que representa una red de nodos para mensajes
	 * 
	 * @return La nueva instancia creada
	 */
	public static BlueVortexImpl create() {
		final BlueVortexImpl vortex = new BlueVortexImpl();
		return vortex;
	}

	/**
	 * @see net.gaia.vortex.bluevortex.api.BlueVortex#crearConexion()
	 */
	@Override
	public ConexionVortex crearConexion() {
		return ConexionVortexImpl.create();
	}

}
