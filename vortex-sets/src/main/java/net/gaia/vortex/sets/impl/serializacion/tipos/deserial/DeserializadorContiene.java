/**
 * 21/01/2013 19:41:26 Copyright (C) 2011 Darío L. García
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

import net.gaia.vortex.sets.impl.condiciones.ColeccionContiene;
import net.gaia.vortex.sets.impl.serializacion.ProblemaDeSerializacionException;
import net.gaia.vortex.sets.impl.serializacion.tipos.ContextoDeSerializacion;
import net.gaia.vortex.sets.impl.serializacion.tipos.DeserializadorDeTipo;
import net.gaia.vortex.sets.impl.serializacion.tipos.MetadataDeSerializacion;
import net.gaia.vortex.sets.reflection.accessors.PropertyChainAccessor;
import ar.com.dgarcia.coding.caching.DefaultInstantiator;
import ar.com.dgarcia.coding.caching.WeakSingleton;
import ar.com.dgarcia.coding.caching.WeakSingletonSupport;

/**
 * Esta clase implementa el deserializador de condiciones que verifica si una coleccion contiene un
 * valor
 * 
 * @author D. García
 */
public class DeserializadorContiene extends WeakSingletonSupport implements DeserializadorDeTipo<ColeccionContiene> {
	private static final WeakSingleton<DeserializadorContiene> ultimaReferencia = new WeakSingleton<DeserializadorContiene>(
			DefaultInstantiator.create(DeserializadorContiene.class));

	public static DeserializadorContiene getInstancia() {
		return ultimaReferencia.get();
	}

	/**
	 * @see net.gaia.vortex.sets.impl.serializacion.tipos.DeserializadorDeTipo#deserializarDesde(java.util.Map,
	 *      net.gaia.vortex.sets.impl.serializacion.tipos.ContextoDeSerializacion)
	 */

	public ColeccionContiene deserializarDesde(final Map<String, Object> mapaOrigen,
			final ContextoDeSerializacion contexto) {
		final Object clave = mapaOrigen.get(MetadataDeSerializacion.TIPO_CONTIENE_CLAVE);
		if (!(clave instanceof String)) {
			throw new ProblemaDeSerializacionException("La clave de un " + MetadataDeSerializacion.TIPO_CONTIENE
					+ " no es un String: " + clave);
		}
		final String propertyPath = (String) clave;
		if (!PropertyChainAccessor.isPropertyChain(propertyPath)) {
			throw new ProblemaDeSerializacionException("La clave de un " + MetadataDeSerializacion.TIPO_CONTIENE
					+ " no respeta la forma de un property chain: " + propertyPath);
		}

		final Object valor = mapaOrigen.get(MetadataDeSerializacion.TIPO_CONTIENE_VALOR);
		final ColeccionContiene deserializado = ColeccionContiene.alValor(valor, propertyPath);
		return deserializado;
	}

}
