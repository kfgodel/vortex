/**
 * 26/11/2011 15:41:51 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.dependencies.json;

/**
 * Esta interfaz define los métodos utilizados por el intérprete vortex para convertir los mensajes
 * desde y hacia Json. <br>
 * 
 * @author D. García
 */
public interface InterpreteJson {

	/**
	 * Convierte el objeto pasado en una cadena Json
	 * 
	 * @param anyObject
	 *            El objeto a convertir
	 * @return La cadena que lo representa en formato json
	 * @throws JsonConversionException
	 *             Si se produce un error en la conversión
	 */
	public String toJson(Object anyObject) throws JsonConversionException;

	/**
	 * Convierte la cadena json pasada interpretándola como una instancia de la clase indicada
	 * 
	 * @param jsonText
	 *            El texto que representa un objeto
	 * @param expectedtype
	 *            El tipo esperado de la conversion
	 * @return La instancia creada
	 * @throws JsonConversionException
	 *             Si se produce un error en la deserialización del objeto
	 */
	public <T> T fromJson(final String jsonText, final Class<T> expectedtype) throws JsonConversionException;

}
