/**
 * 21/01/2013 19:40:36 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.sets.impl.serializacion.tipos.serial;

import java.util.Map;

import net.gaia.vortex.impl.helpers.VortexMap;
import net.gaia.vortex.sets.impl.condiciones.AtributoEmpieza;
import net.gaia.vortex.sets.impl.serializacion.tipos.ContextoDeSerializacion;
import net.gaia.vortex.sets.impl.serializacion.tipos.MetadataDeSerializacion;
import net.gaia.vortex.sets.impl.serializacion.tipos.SerializadorDeTipo;
import ar.com.dgarcia.coding.caching.DefaultInstantiator;
import ar.com.dgarcia.coding.caching.WeakSingleton;
import ar.com.dgarcia.coding.caching.WeakSingletonSupport;

/**
 * Esta clase representa el serializador de las condiciones que verifican un prefijo
 * 
 * @author D. García
 */
public class SerializadorEmpieza extends WeakSingletonSupport implements SerializadorDeTipo<AtributoEmpieza> {
	private static final WeakSingleton<SerializadorEmpieza> ultimaReferencia = new WeakSingleton<SerializadorEmpieza>(
			DefaultInstantiator.create(SerializadorEmpieza.class));

	public static SerializadorEmpieza getInstancia() {
		return ultimaReferencia.get();
	}

	/**
	 * @see net.gaia.vortex.sets.impl.serializacion.tipos.SerializadorDeTipo#serializarDesde(java.lang.Object,
	 *      net.gaia.vortex.sets.impl.serializacion.tipos.ContextoDeSerializacion)
	 */

	public Map<String, Object> serializarDesde(final AtributoEmpieza origen, final ContextoDeSerializacion contexto) {
		final Map<String, Object> serializado = new VortexMap();
		serializado.put(MetadataDeSerializacion.ATRIBUTO_TIPO, MetadataDeSerializacion.TIPO_EMPIEZA);

		serializado.put(MetadataDeSerializacion.TIPO_EMPIEZA_CLAVE, origen.getValueAccessor().getPropertyPath());
		serializado.put(MetadataDeSerializacion.TIPO_EMPIEZA_PREFIJO, origen.getPrefijoEsperado());
		return serializado;
	}

}
