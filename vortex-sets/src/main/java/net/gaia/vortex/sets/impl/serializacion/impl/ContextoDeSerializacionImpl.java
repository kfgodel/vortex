/**
 * 21/01/2013 16:54:49 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.sets.impl.serializacion.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.gaia.vortex.api.condiciones.Condicion;
import net.gaia.vortex.sets.impl.serializacion.tipos.ContextoDeSerializacion;
import ar.com.dgarcia.coding.exceptions.FaultyCodeException;

/**
 * Esta clase implementa el contexto de serializacion
 * 
 * @author D. García
 */
public class ContextoDeSerializacionImpl implements ContextoDeSerializacion {

	private Map<Condicion, Map<String, Object>> mapasPorCondicion;
	private Map<Map<String, Object>, Condicion> condicionesPorMapa;

	public Map<Condicion, Map<String, Object>> getMapasPorCondicion() {
		if (mapasPorCondicion == null) {
			mapasPorCondicion = new HashMap<Condicion, Map<String, Object>>();
		}
		return mapasPorCondicion;
	}

	public Map<Map<String, Object>, Condicion> getCondicionesPorMapa() {
		if (condicionesPorMapa == null) {
			condicionesPorMapa = new HashMap<Map<String, Object>, Condicion>();
		}
		return condicionesPorMapa;
	}

	public static ContextoDeSerializacionImpl create() {
		final ContextoDeSerializacionImpl contexto = new ContextoDeSerializacionImpl();
		return contexto;
	}

	/**
	 * @see net.gaia.vortex.sets.impl.serializacion.tipos.ContextoDeSerializacion#guardarSerializadoDe(net.gaia.vortex.api.condiciones.Condicion,
	 *      java.util.Map)
	 */
	
	public void guardarSerializadoDe(final Condicion condicionOriginal, final Map<String, Object> condicionSerializada) {
		getMapasPorCondicion().put(condicionOriginal, condicionSerializada);
	}

	/**
	 * @see net.gaia.vortex.sets.impl.serializacion.tipos.ContextoDeSerializacion#obtenerSerializadoDe(net.gaia.vortex.api.condiciones.Condicion)
	 */
	
	public Map<String, Object> obtenerSerializadoDe(final Condicion condicionRaiz) throws FaultyCodeException {
		final Map<String, Object> serializado = getMapasPorCondicion().get(condicionRaiz);
		return serializado;
	}

	/**
	 * @see net.gaia.vortex.sets.impl.serializacion.tipos.ContextoDeSerializacion#guardarDeserializadoDe(java.util.Map,
	 *      net.gaia.vortex.api.condiciones.Condicion)
	 */
	
	public void guardarDeserializadoDe(final Map<String, Object> mapaOriginal, final Condicion condicionDeserializada) {
		getCondicionesPorMapa().put(mapaOriginal, condicionDeserializada);
	}

	/**
	 * @see net.gaia.vortex.sets.impl.serializacion.tipos.ContextoDeSerializacion#obtenerDeserializadoDe(java.util.Map)
	 */
	
	public Condicion obtenerDeserializadoDe(final Map<String, Object> mapaRaiz) throws FaultyCodeException {
		final Condicion deserializada = getCondicionesPorMapa().get(mapaRaiz);
		return deserializada;
	}

	/**
	 * @see net.gaia.vortex.sets.impl.serializacion.tipos.ContextoDeSerializacion#obtenerSerializadosDe(java.util.List)
	 */
	
	public List<Map<String, Object>> obtenerSerializadosDe(final List<Condicion> condiciones)
			throws FaultyCodeException {
		final List<Map<String, Object>> serializados = new ArrayList<Map<String, Object>>(condiciones.size());
		for (final Condicion condicion : condiciones) {
			final Map<String, Object> subSerializado = obtenerSerializadoDe(condicion);
			serializados.add(subSerializado);
		}
		return serializados;
	}

	/**
	 * @see net.gaia.vortex.sets.impl.serializacion.tipos.ContextoDeSerializacion#obtenerDeserializadosDe(java.util.List)
	 */
	
	public List<Condicion> obtenerDeserializadosDe(final List<Map<String, Object>> mapas) throws FaultyCodeException {
		final List<Condicion> deserializados = new ArrayList<Condicion>(mapas.size());
		for (final Map<String, Object> mapa : mapas) {
			final Condicion condicion = obtenerDeserializadoDe(mapa);
			deserializados.add(condicion);
		}
		return deserializados;
	}
}
