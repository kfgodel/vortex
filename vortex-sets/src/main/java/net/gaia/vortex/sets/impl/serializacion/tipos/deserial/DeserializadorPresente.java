/**
 * 21/01/2013 19:41:11 Copyright (C) 2011 Darío L. García
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

import net.gaia.vortex.sets.impl.condiciones.AtributoPresente;
import net.gaia.vortex.sets.impl.serializacion.ProblemaDeSerializacionException;
import net.gaia.vortex.sets.impl.serializacion.tipos.ContextoDeSerializacion;
import net.gaia.vortex.sets.impl.serializacion.tipos.DeserializadorDeTipo;
import net.gaia.vortex.sets.impl.serializacion.tipos.MetadataDeSerializacion;
import net.gaia.vortex.sets.reflection.accessors.PropertyChainAccessor;
import ar.com.dgarcia.coding.caching.DefaultInstantiator;
import ar.com.dgarcia.coding.caching.WeakSingleton;
import ar.com.dgarcia.coding.caching.WeakSingletonSupport;

/**
 * Esta clase implementa el deserializador de condiciones por atributo presente
 * 
 * @author D. García
 */
public class DeserializadorPresente extends WeakSingletonSupport implements DeserializadorDeTipo<AtributoPresente> {
	private static final WeakSingleton<DeserializadorPresente> ultimaReferencia = new WeakSingleton<DeserializadorPresente>(
			DefaultInstantiator.create(DeserializadorPresente.class));

	public static DeserializadorPresente getInstancia() {
		return ultimaReferencia.get();
	}

	/**
	 * @see net.gaia.vortex.sets.impl.serializacion.tipos.DeserializadorDeTipo#deserializarDesde(java.util.Map,
	 *      net.gaia.vortex.sets.impl.serializacion.tipos.ContextoDeSerializacion)
	 */

	public AtributoPresente deserializarDesde(final Map<String, Object> mapaOrigen,
			final ContextoDeSerializacion contexto) {
		final Object clave = mapaOrigen.get(MetadataDeSerializacion.TIPO_PRESENTE_CLAVE);
		if (!(clave instanceof String)) {
			throw new ProblemaDeSerializacionException("La clave de un " + MetadataDeSerializacion.TIPO_CONTIENE
					+ " no es un String: " + clave);
		}
		final String propertyPath = (String) clave;
		if (!PropertyChainAccessor.isPropertyChain(propertyPath)) {
			throw new ProblemaDeSerializacionException("La clave de un " + MetadataDeSerializacion.TIPO_CONTIENE
					+ " no respeta la forma de un property chain: " + propertyPath);
		}

		final AtributoPresente deserializado = AtributoPresente.conNombre(propertyPath);
		return deserializado;
	}

}
