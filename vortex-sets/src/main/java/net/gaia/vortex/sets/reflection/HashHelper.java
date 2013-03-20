/**
 * 20/03/2013 01:05:28 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.sets.reflection;

import java.util.TreeMap;

/**
 * Esta ayuda al calculo de hash
 * 
 * @author D. García
 */
public class HashHelper {
	/**
	 * Calcula y devuelve el hash para un par de objetos.<br>
	 * Este método es una facilidad basado en la implementacion de
	 * {@link TreeMap.Entry#equals(Object)}
	 * 
	 * @param primero
	 *            El primero obejto (puede sr null)
	 * @param segundo
	 *            El segundo (puede ser null)
	 * @return El valor de hash para representar los objetos pasados como una sola cosa
	 */
	public static int hashDeDosValores(final Object primero, final Object segundo) {
		final int keyHash = (primero == null ? 0 : primero.hashCode());
		final int valueHash = (segundo == null ? 0 : segundo.hashCode());
		final int valorDeHash = keyHash ^ valueHash;
		return valorDeHash;
	}

}
