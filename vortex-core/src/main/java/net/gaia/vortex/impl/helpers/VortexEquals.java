/**
 * 26/01/2013 13:36:09 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.impl.helpers;

/**
 * Esta clase redefine el equals según lo que vortex considera como equivalente.<br>
 * Es casi igual al default sólo que compara mejor los números
 * 
 * @author D. García
 */
public class VortexEquals {

	/**
	 * Compara dos objetos por igualdad tomando en cuenta que los números sean equivalentes por su
	 * valor y no por su clase, y que los textos sean iguales sin importar mayúsculas
	 * 
	 * @param first
	 *            El primer objeto
	 * @param second
	 *            El segundo
	 * @return True si son considerados iguales por vortex
	 */
	public static boolean areEquals(final Object first, final Object second) {
		if (first == second) {
			return true;
		}
		if (first == null) {
			return second == null;
		}
		// Si son números los comparamos como double
		if (first instanceof Number) {
			if (!(second instanceof Number)) {
				return false;
			}
			final boolean igualesComoNumbers = ((Number) first).doubleValue() == ((Number) second).doubleValue();
			return igualesComoNumbers;
		}
		// Si son cadenas los comparamos case insensitive
		if (first instanceof CharSequence) {
			if (!(second instanceof CharSequence)) {
				return false;
			}
			final boolean igualesComoCadenasCaseInsensitive = first.toString().equalsIgnoreCase(second.toString());
			return igualesComoCadenasCaseInsensitive;
		}
		// Si no usamos su default
		final boolean igualesSegunEqualsDefault = first.equals(second);
		return igualesSegunEqualsDefault;
	}

	/**
	 * Devuelve el valor de hash coherente con el equals, tomando a los números en el dominio de los
	 * doubles y a los Strings como si fueran todas mayúsculas
	 * 
	 * @param object
	 *            El objeto a evaluar
	 * @return El hash coherente con el equals de esta clase
	 */
	public static int hashCode(final Object object) {
		if (object == null) {
			return 0;
		}
		// Si es numero usamos el hash de los doubles para todos los tipos
		if (object instanceof Number) {
			final double doubleValue = ((Number) object).doubleValue();
			return Double.valueOf(doubleValue).hashCode();
		}
		// Si es un texto usamos su version en upper
		if (object instanceof CharSequence) {
			return object.toString().toUpperCase().hashCode();
		}
		// Si no el default
		return object.hashCode();
	}
}
