/**
 * 07/07/2012 00:49:33 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.impl.support;

import net.gaia.vortex.api.proto.ComponenteVortex;
import net.gaia.vortex.impl.helpers.SecuenciadorDeInstancias;
import net.gaia.vortex.impl.helpers.ToShortString;

/**
 * Esta clase define el comportamiento básico de componentes vortex para ser extendido por las
 * subclases
 * 
 * @author D. García
 */
public abstract class ComponenteSupport implements ComponenteVortex {

	private final long numeroDeInstancia;
	public static final String numeroDeInstancia_FIELD = "numeroDeInstancia";

	public ComponenteSupport() {
		this.numeroDeInstancia = SecuenciadorDeInstancias.getProximoNumero();
	}

	/**
	 * @see net.gaia.vortex.api.proto.ComponenteVortex#getNumeroDeInstancia()
	 */
	
	public long getNumeroDeInstancia() {
		return numeroDeInstancia;
	}

	/**
	 * @see net.gaia.vortex.api.proto.ComponenteVortex#toShortString()
	 */
	
	public String toShortString() {
		return ToShortString.from(this);
	}

}