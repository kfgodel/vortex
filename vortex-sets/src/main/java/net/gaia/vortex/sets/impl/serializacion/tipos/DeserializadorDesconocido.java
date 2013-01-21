/**
 * 21/01/2013 17:55:38 Copyright (C) 2011 Darío L. García
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

import java.util.Map;

import net.gaia.vortex.core.api.condiciones.Condicion;
import net.gaia.vortex.sets.impl.CondicionDesconocida;
import net.gaia.vortex.sets.impl.serializacion.ContextoDeSerializacion;
import net.gaia.vortex.sets.impl.serializacion.DeserializadorDeTipo;
import ar.com.dgarcia.coding.caching.DefaultInstantiator;
import ar.com.dgarcia.coding.caching.WeakSingleton;

/**
 * Esta clase representa el deserializador para tipos desconocidos, que crea una condicion
 * desconocida para que sea evaluable y a la vez intentando conservar el mapa original
 * 
 * @author D. García
 */
public class DeserializadorDesconocido implements DeserializadorDeTipo<Condicion> {

	private static final WeakSingleton<DeserializadorDesconocido> ultimaReferencia = new WeakSingleton<DeserializadorDesconocido>(
			DefaultInstantiator.create(DeserializadorDesconocido.class));

	public static DeserializadorDesconocido getInstancia() {
		return ultimaReferencia.get();
	}

	/**
	 * @see net.gaia.vortex.sets.impl.serializacion.DeserializadorDeTipo#deserializarDesde(java.util.Map,
	 *      net.gaia.vortex.sets.impl.serializacion.ContextoDeSerializacion)
	 */
	@Override
	public Condicion deserializarDesde(final Map<String, Object> mapaOrigen, final ContextoDeSerializacion contexto) {
		final CondicionDesconocida condicionDesconocida = CondicionDesconocida.create(mapaOrigen);
		return condicionDesconocida;
	}

}
