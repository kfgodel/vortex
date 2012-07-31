/**
 * 30/05/2012 19:10:50 Copyright (C) 2011 Darío L. García
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
package ar.dgarcia.textualizer.api;

/**
 * Esta interfaz representa el contrato que debe cumplir una clase que permite representar el estado
 * de un objeto como {@link String} y realizar la operación inversa
 * 
 * @author D. García
 */
public interface ObjectTextualizer {

	/**
	 * Convierte el objeto pasado en una representación de texto que permite reconstruirlo
	 * 
	 * @param value
	 *            el objeto a convertir
	 * @return La representación del objeto
	 */
	public String convertToString(final Object value) throws CannotTextSerializeException;

	/**
	 * Recrea el objeto original a partir de la cadena recibida
	 * 
	 * @param value
	 *            El objeto representado como cadena
	 * @return El objeto real
	 */
	public Object convertFromString(final String value) throws CannotTextUnserializeException;
}
