/**
 * 23/01/2013 14:53:36 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.router.impl.messages.meta;

import ar.com.dgarcia.lang.strings.ToString;

/**
 * Esta clase implementa una parte base del metamensaje vortex
 * 
 * @author D. García
 */
public class MetamensajeSupport implements MetamensajeVortex {

	private final String tipoDeMensaje;

	public MetamensajeSupport(final String nombreDeTipo) {
		this.tipoDeMensaje = nombreDeTipo;
	}

	
	public String getTipoDeMensaje() {
		return tipoDeMensaje;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	
	public String toString() {
		return ToString.de(this).con(tipoDeMensaje_FIELD, tipoDeMensaje).toString();
	}

}
