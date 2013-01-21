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

import java.util.HashMap;
import java.util.Map;

import net.gaia.vortex.core.api.condiciones.Condicion;
import net.gaia.vortex.sets.impl.serializacion.ContextoDeSerializacion;
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
	 * @see net.gaia.vortex.sets.impl.serializacion.ContextoDeSerializacion#guardarSerializadoDe(net.gaia.vortex.core.api.condiciones.Condicion,
	 *      java.util.Map)
	 */
	@Override
	public void guardarSerializadoDe(final Condicion condicionOriginal, final Map<String, Object> condicionSerializada) {
		mapasPorCondicion.put(condicionOriginal, condicionSerializada);
	}

	/**
	 * @see net.gaia.vortex.sets.impl.serializacion.ContextoDeSerializacion#obtenerSerializadoDe(net.gaia.vortex.core.api.condiciones.Condicion)
	 */
	@Override
	public Map<String, Object> obtenerSerializadoDe(final Condicion condicionRaiz) throws FaultyCodeException {
		final Map<String, Object> serializado = mapasPorCondicion.get(condicionRaiz);
		return serializado;
	}

	/**
	 * @see net.gaia.vortex.sets.impl.serializacion.ContextoDeSerializacion#guardarDeserializadoDe(java.util.Map,
	 *      net.gaia.vortex.core.api.condiciones.Condicion)
	 */
	@Override
	public void guardarDeserializadoDe(final Map<String, Object> mapaOriginal, final Condicion condicionDeserializada) {
		condicionesPorMapa.put(mapaOriginal, condicionDeserializada);
	}

	/**
	 * @see net.gaia.vortex.sets.impl.serializacion.ContextoDeSerializacion#obtenerDeserializadoDe(java.util.Map)
	 */
	@Override
	public Condicion obtenerDeserializadoDe(final Map<String, Object> mapaRaiz) throws FaultyCodeException {
		final Condicion deserializada = condicionesPorMapa.get(mapaRaiz);
		return deserializada;
	}
}
