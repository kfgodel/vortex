/**
 * 06/07/2012 00:23:57 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.core.impl.atomos.receptores;

import net.gaia.vortex.core.api.atomos.Receptor;
import net.gaia.vortex.core.impl.atomos.SecuenciadorDeComponentes;
import net.gaia.vortex.core.impl.atomos.ToShortString;

/**
 * Esta clase define el comportamiento base para definir un receptor. Las subclases sólo deben
 * definir el método {@link Receptor#recibir(net.gaia.vortex.core.api.mensaje.MensajeVortex)}
 * 
 * @author D. García
 */
public abstract class ReceptorSupport implements Receptor {

	private final long numeroDeComponente;
	public static final String numeroDeComponente_FIELD = "numeroDeComponente";

	public ReceptorSupport() {
		numeroDeComponente = SecuenciadorDeComponentes.getProximoNumero();
	}

	/**
	 * @see net.gaia.vortex.core.api.atomos.ComponenteVortex#getNumeroDeComponente()
	 */
	@Override
	public long getNumeroDeComponente() {
		return numeroDeComponente;
	}

	/**
	 * @see net.gaia.vortex.core.api.atomos.ComponenteVortex#toShortString()
	 */
	@Override
	public String toShortString() {
		return ToShortString.from(this);
	}

}