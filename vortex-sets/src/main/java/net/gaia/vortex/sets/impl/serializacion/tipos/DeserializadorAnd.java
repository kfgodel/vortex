/**
 * 21/01/2013 18:33:13 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.sets.impl.serializacion.tipos;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.gaia.vortex.core.api.condiciones.Condicion;
import net.gaia.vortex.sets.impl.AndCompuesto;
import net.gaia.vortex.sets.impl.serializacion.ContextoDeSerializacion;
import net.gaia.vortex.sets.impl.serializacion.DeserializadorDeTipo;
import net.gaia.vortex.sets.impl.serializacion.MetadataDeSerializacion;
import net.gaia.vortex.sets.impl.serializacion.ProblemaDeSerializacionException;
import ar.com.dgarcia.coding.caching.DefaultInstantiator;
import ar.com.dgarcia.coding.caching.WeakSingleton;

/**
 * Esta clase es la implementación del deserializador de condiciones AND
 * 
 * @author D. García
 */
public class DeserializadorAnd implements DeserializadorDeTipo<AndCompuesto> {
	private static final WeakSingleton<DeserializadorAnd> ultimaReferencia = new WeakSingleton<DeserializadorAnd>(
			DefaultInstantiator.create(DeserializadorAnd.class));

	public static DeserializadorAnd getInstancia() {
		return ultimaReferencia.get();
	}

	/**
	 * @see net.gaia.vortex.sets.impl.serializacion.DeserializadorDeTipo#deserializarDesde(java.util.Map,
	 *      net.gaia.vortex.sets.impl.serializacion.ContextoDeSerializacion)
	 */
	@Override
	public AndCompuesto deserializarDesde(final Map<String, Object> mapaOrigen, final ContextoDeSerializacion contexto) {
		final Object valor = mapaOrigen.get(MetadataDeSerializacion.TIPO_AND_FILTROS);
		if (!(valor instanceof Iterable)) {
			throw new ProblemaDeSerializacionException("Los filtros de un AND no son iterables: " + valor);
		}
		@SuppressWarnings("unchecked")
		final Iterable<Object> subFiltros = (Iterable<Object>) valor;
		final List<Map<String, Object>> subSerializados = new ArrayList<Map<String, Object>>();
		int i = 0;
		for (final Object filtro : subFiltros) {
			if (!(filtro instanceof Map)) {
				throw new ProblemaDeSerializacionException("El sub filtro[" + i + "] de un AND no es un mapa: "
						+ filtro);
			}
			@SuppressWarnings("unchecked")
			final Map<String, Object> subSerializado = (Map<String, Object>) filtro;
			subSerializados.add(subSerializado);
			i++;
		}
		final List<Condicion> subCondiciones = contexto.obtenerDeserializadosDe(subSerializados);
		final AndCompuesto deserializado = AndCompuesto.create(subCondiciones);
		return deserializado;
	}

}
