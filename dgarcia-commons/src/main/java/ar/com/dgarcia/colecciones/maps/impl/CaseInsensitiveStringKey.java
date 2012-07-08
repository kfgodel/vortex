/**
 * 08/07/2012 11:46:26 Copyright (C) 2011 Darío L. García
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
package ar.com.dgarcia.colecciones.maps.impl;

/**
 * Esta clase representa una key de tipo String utilizada en un mapa case-insensitive.<br>
 * Esta instancia toma un string como base para establecer relacion de igualdad con otros strings
 * sin tomar en cuenta el case
 * 
 * @author D. García
 */
public class CaseInsensitiveStringKey {

	private String originalString;
	private String insensitiveCaseString;

	public static CaseInsensitiveStringKey create(final String unString) {
		final CaseInsensitiveStringKey name = new CaseInsensitiveStringKey();
		name.originalString = unString;
		name.insensitiveCaseString = convertToInsensitiveCaseRepresentation(unString);
		return name;
	}

	/**
	 * Crea una versión que se utilizará como base de esta instancia para las comparaciones
	 * 
	 * @param unString
	 *            El texto original
	 * @return La versión insensitive
	 */
	public static String convertToInsensitiveCaseRepresentation(final String unString) {
		return unString.toUpperCase();
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(final Object obj) {
		if (obj instanceof CaseInsensitiveStringKey) {
			final CaseInsensitiveStringKey that = (CaseInsensitiveStringKey) obj;
			final boolean esIgualTransformada = insensitiveCaseString.equals(that.insensitiveCaseString);
			return esIgualTransformada;
		} else if (obj instanceof CharSequence) {
			final CharSequence sequence = (CharSequence) obj;
			final String comparedString = sequence.toString();
			final boolean esIgualAOriginal = originalString.equals(comparedString);
			if (esIgualAOriginal) {
				return true;
			}
			final boolean esIgualSinTransformar = insensitiveCaseString.equals(comparedString);
			if (esIgualSinTransformar) {
				return true;
			}
			final String insensitiveVersion = convertToInsensitiveCaseRepresentation(comparedString);
			final boolean esIgualTransformada = insensitiveCaseString.equals(insensitiveVersion);
			return esIgualTransformada;
		}
		return false;
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return insensitiveCaseString.hashCode();
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return insensitiveCaseString;
	}

	public String getOriginalString() {
		return originalString;
	}

	public String getInsensitiveCaseString() {
		return insensitiveCaseString;
	}

}
