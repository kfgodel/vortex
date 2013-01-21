/**
 * 21/01/2013 13:04:50 Copyright (C) 2011 Darío L. García
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

import net.gaia.vortex.core.api.condiciones.Condicion;
import ar.com.dgarcia.coding.exceptions.FaultyCodeException;

/**
 * Esta interfaz representa la información asociada a una serialización o deserialización
 * 
 * @author D. García
 */
public interface ContextoDeSerializacion {

	/**
	 * Guarda en este contexto la version serializada de la condicion pasada para posterior
	 * obtención
	 * 
	 * @param condicionOriginal
	 *            La condicion original
	 * @param condicionSerializada
	 *            la versión serializada
	 */
	void guardarSerializadoDe(Condicion condicionOriginal, Map<String, Object> condicionSerializada);

	/**
	 * Obtiene la versión serializada de la condición pasada, previamente almacenada
	 * 
	 * @param condicionRaiz
	 *            La condición con la que se registró
	 * @return La forma serializada de la condición pasada
	 * @throws FaultyCodeException
	 *             Si nunca se registró la versión serializada de la condición pasada
	 */
	Map<String, Object> obtenerSerializadoDe(Condicion condicionRaiz) throws FaultyCodeException;

	/**
	 * Guarda en este contexto la condicion deserializada del mapa indicado
	 * 
	 * @param mapaOriginal
	 *            El mapa del cual se origina la condicion
	 * @param condicionDeserializada
	 *            La condicion deserializada
	 */
	void guardarDeserializadoDe(Map<String, Object> mapaOriginal, Condicion condicionDeserializada);

	/**
	 * Devuelve de este contexto la condicion deserializada para el mapa indicado
	 * 
	 * @param mapaRaiz
	 *            El mapa con el que se registró la condición
	 * @return La condición registrada
	 * @throws FaultyCodeException
	 *             Si nunca se registró la versión serializada de la condición pasada
	 */
	Condicion obtenerDeserializadoDe(Map<String, Object> mapaRaiz) throws FaultyCodeException;

}
