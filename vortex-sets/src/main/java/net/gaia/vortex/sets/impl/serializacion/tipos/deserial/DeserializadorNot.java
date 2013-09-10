/**
 * 21/01/2013 18:33:42 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.sets.impl.serializacion.tipos.deserial;

import java.util.Map;

import net.gaia.vortex.api.condiciones.Condicion;
import net.gaia.vortex.sets.impl.condiciones.Negacion;
import net.gaia.vortex.sets.impl.serializacion.ProblemaDeSerializacionException;
import net.gaia.vortex.sets.impl.serializacion.tipos.ContextoDeSerializacion;
import net.gaia.vortex.sets.impl.serializacion.tipos.DeserializadorDeTipo;
import net.gaia.vortex.sets.impl.serializacion.tipos.MetadataDeSerializacion;
import ar.com.dgarcia.coding.caching.DefaultInstantiator;
import ar.com.dgarcia.coding.caching.WeakSingleton;
import ar.com.dgarcia.coding.caching.WeakSingletonSupport;

/**
 * Esta clase implementa el deserializador de la condicion NOT
 * 
 * @author D. García
 */
public class DeserializadorNot extends WeakSingletonSupport implements DeserializadorDeTipo<Negacion> {
	private static final WeakSingleton<DeserializadorNot> ultimaReferencia = new WeakSingleton<DeserializadorNot>(
			DefaultInstantiator.create(DeserializadorNot.class));

	public static DeserializadorNot getInstancia() {
		return ultimaReferencia.get();
	}

	/**
	 * @see net.gaia.vortex.sets.impl.serializacion.tipos.DeserializadorDeTipo#deserializarDesde(java.util.Map,
	 *      net.gaia.vortex.sets.impl.serializacion.tipos.ContextoDeSerializacion)
	 */

	public Negacion deserializarDesde(final Map<String, Object> mapaOrigen, final ContextoDeSerializacion contexto) {
		final Object valor = mapaOrigen.get(MetadataDeSerializacion.TIPO_NOT_FILTRO);
		if (!(valor instanceof Map)) {
			throw new ProblemaDeSerializacionException("El sub filtro de un " + MetadataDeSerializacion.TIPO_NOT
					+ " no es un mapa: " + valor);
		}
		@SuppressWarnings("unchecked")
		final Map<String, Object> subSerializado = (Map<String, Object>) valor;
		final Condicion subCondicion = contexto.obtenerDeserializadoDe(subSerializado);
		final Negacion negacion = Negacion.de(subCondicion);
		return negacion;
	}

}
