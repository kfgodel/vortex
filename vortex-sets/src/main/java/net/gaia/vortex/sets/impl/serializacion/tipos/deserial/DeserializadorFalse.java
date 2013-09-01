/**
 * 21/01/2013 19:17:08 Copyright (C) 2011 Darío L. García
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

import net.gaia.vortex.core.impl.condiciones.SiempreFalse;
import net.gaia.vortex.sets.impl.serializacion.tipos.ContextoDeSerializacion;
import net.gaia.vortex.sets.impl.serializacion.tipos.DeserializadorDeTipo;
import ar.com.dgarcia.coding.caching.DefaultInstantiator;
import ar.com.dgarcia.coding.caching.WeakSingleton;
import ar.com.dgarcia.coding.caching.WeakSingletonSupport;

/**
 * Esta clase implementa la deserialización de condiciones false
 * 
 * @author D. García
 */
public class DeserializadorFalse extends WeakSingletonSupport implements DeserializadorDeTipo<SiempreFalse> {
	private static final WeakSingleton<DeserializadorFalse> ultimaReferencia = new WeakSingleton<DeserializadorFalse>(
			DefaultInstantiator.create(DeserializadorFalse.class));

	public static DeserializadorFalse getInstancia() {
		return ultimaReferencia.get();
	}

	/**
	 * @see net.gaia.vortex.sets.impl.serializacion.tipos.DeserializadorDeTipo#deserializarDesde(java.util.Map,
	 *      net.gaia.vortex.sets.impl.serializacion.tipos.ContextoDeSerializacion)
	 */

	public SiempreFalse deserializarDesde(final Map<String, Object> mapaOrigen, final ContextoDeSerializacion contexto) {
		return SiempreFalse.getInstancia();
	}

}
