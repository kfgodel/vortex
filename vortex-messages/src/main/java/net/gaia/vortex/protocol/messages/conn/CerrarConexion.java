/**
 * 12/01/2012 00:09:32 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.protocol.messages.conn;

import net.gaia.vortex.protocol.messages.MetamensajeVortex;

import com.google.common.base.Objects;

/**
 * Esta clase representa el metamensaje de cierre de conexion. Con el cual una parte comunicante
 * pide que se cierre el canal
 * 
 * @author D. García
 */
public class CerrarConexion implements MetamensajeVortex {

	public static CerrarConexion create() {
		final CerrarConexion cierre = new CerrarConexion();
		return cierre;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return Objects.toStringHelper(this).toString();
	}
}