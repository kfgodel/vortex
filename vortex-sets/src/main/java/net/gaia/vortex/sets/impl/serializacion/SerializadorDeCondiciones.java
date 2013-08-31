/**
 * 21/01/2013 15:40:03 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.sets.impl.serializacion;

import java.util.Map;

import net.gaia.vortex.api.condiciones.Condicion;

/**
 * Esta interfaz define los métodos disponibles para serializar las condiciones y deserializarlas
 * 
 * @author D. García
 */
public interface SerializadorDeCondiciones {

	/**
	 * Serializa la condición pasada en forma de mapa para poder ser enviada en un mensaje
	 * 
	 * @param condicion
	 *            La condición a serializar
	 * @return El mapa con la versión serializada
	 * @throws ProblemaDeSerializacionException
	 *             Si se produce un error en la conversión
	 */
	public Map<String, Object> serializar(Condicion condicion) throws ProblemaDeSerializacionException;

	/**
	 * Deserializa el mapa pasado interpretándolo como una condición
	 * 
	 * @param mapa
	 *            El mapa que contiene el estado suficiente para recrear la condición
	 * @return
	 * @throws ProblemaDeSerializacionException
	 */
	public Condicion deserializar(Map<String, Object> mapa) throws ProblemaDeSerializacionException;
}
