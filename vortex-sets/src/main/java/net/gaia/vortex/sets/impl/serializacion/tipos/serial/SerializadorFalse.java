/**
 * 21/01/2013 19:17:02 Copyright (C) 2011 Darío L. García
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

import net.gaia.vortex.helpers.VortexMap;
import net.gaia.vortex.impl.condiciones.SiempreFalse;
import net.gaia.vortex.sets.impl.serializacion.tipos.ContextoDeSerializacion;
import net.gaia.vortex.sets.impl.serializacion.tipos.MetadataDeSerializacion;
import net.gaia.vortex.sets.impl.serializacion.tipos.SerializadorDeTipo;
import ar.com.dgarcia.coding.caching.DefaultInstantiator;
import ar.com.dgarcia.coding.caching.WeakSingleton;
import ar.com.dgarcia.coding.caching.WeakSingletonSupport;

/**
 * Esta clase implementa la serialización de condiciones siempre false
 * 
 * @author D. García
 */
public class SerializadorFalse extends WeakSingletonSupport implements SerializadorDeTipo<SiempreFalse> {
	private static final WeakSingleton<SerializadorFalse> ultimaReferencia = new WeakSingleton<SerializadorFalse>(
			DefaultInstantiator.create(SerializadorFalse.class));

	public static SerializadorFalse getInstancia() {
		return ultimaReferencia.get();
	}

	/**
	 * @see net.gaia.vortex.sets.impl.serializacion.tipos.SerializadorDeTipo#serializarDesde(java.lang.Object,
	 *      net.gaia.vortex.sets.impl.serializacion.tipos.ContextoDeSerializacion)
	 */

	public Map<String, Object> serializarDesde(final SiempreFalse origen, final ContextoDeSerializacion contexto) {
		final Map<String, Object> serializado = new VortexMap();
		serializado.put(MetadataDeSerializacion.ATRIBUTO_TIPO, MetadataDeSerializacion.TIPO_FALSE);
		return serializado;
	}

}
