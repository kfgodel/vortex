/**
 * 20/08/2012 01:37:08 Copyright (C) 2011 Darío L. García
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

import java.util.Map;

/**
 * Esta interfaz define el contrato mínimo que un accessor de propiedades en los mensajes ofrece
 * para interactuar con los valores
 * 
 * @author D. García
 */
public interface ValueAccessor {

	/**
	 * Establece el valor pasado en la propiedad representada por este accessor dentro del mapa
	 * pasado
	 * 
	 * @param contenido
	 *            El mapa que será modificado de acuerdo a este accessor cambiando el valor de una
	 *            de sus propiedades
	 * @param value
	 *            El valor que se definirá en el mapa
	 */
	public void setValueInto(Map<String, Object> contenido, Object value) throws MissingPropertyException;

	/**
	 * Devuelve el valor de la propiedad representada por este accessor dentro del mapa pasado
	 * 
	 * @param contenido
	 *            El mapa del cual se tomará el valor
	 * @return El valor obtenido
	 */
	public Object getValueFrom(Map<String, Object> contenido);

}
