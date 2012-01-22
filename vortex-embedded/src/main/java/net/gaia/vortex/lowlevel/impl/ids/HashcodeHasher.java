/**
 * 22/08/2011 14:21:32 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.lowlevel.impl.ids;

/**
 * Esta clase es la implementación del hasher usando el hashcode del objeto
 * 
 * @author D. García
 */
public class HashcodeHasher implements StringHasher {

	/**
	 * @see net.gaia.vortex.lowlevel.impl.ids.StringHasher#hashDe(java.lang.Object)
	 */
	@Override
	public String hashDe(final Object contenido) {
		if (contenido == null) {
			return "0";
		}
		final int hashCode = contenido.hashCode();
		final String hashvalue = Integer.toHexString(hashCode);
		return hashvalue;
	}

	public static HashcodeHasher create() {
		final HashcodeHasher hasher = new HashcodeHasher();
		return hasher;
	}
}
