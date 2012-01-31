/**
 * 31/01/2012 18:14:25 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.externals.time;

import org.joda.time.DateTime;

/**
 * Esta clase sirve de referencia para obtener el tiempo actual en el código
 * 
 * @author D. García
 */
public class VortexTime {

	/**
	 * Devuelve el DateTime que se considera como momento actual. <br>
	 * Este método cambia el objeto devuelto en el tiempo
	 * 
	 * @return El momento considerado actual
	 */
	public static DateTime currentMoment() {
		return new DateTime();
	}

}
