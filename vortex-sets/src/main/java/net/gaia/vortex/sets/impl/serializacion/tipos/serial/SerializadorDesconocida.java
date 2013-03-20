/**
 * 21/01/2013 17:57:35 Copyright (C) 2011 Darío L. García
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

import net.gaia.vortex.sets.impl.condiciones.CondicionDesconocida;
import net.gaia.vortex.sets.impl.serializacion.tipos.ContextoDeSerializacion;
import net.gaia.vortex.sets.impl.serializacion.tipos.SerializadorDeTipo;
import ar.com.dgarcia.coding.caching.DefaultInstantiator;
import ar.com.dgarcia.coding.caching.WeakSingleton;

/**
 * Esta clase representa el serializador de condiciones desconocidas que intenta regenerar el mapa
 * original si es conservado por la condicion
 * 
 * @author D. García
 */
public class SerializadorDesconocida implements SerializadorDeTipo<CondicionDesconocida> {

	private static final WeakSingleton<SerializadorDesconocida> ultimaReferencia = new WeakSingleton<SerializadorDesconocida>(
			DefaultInstantiator.create(SerializadorDesconocida.class));

	public static SerializadorDesconocida getInstancia() {
		return ultimaReferencia.get();
	}

	/**
	 * @see net.gaia.vortex.sets.impl.serializacion.tipos.SerializadorDeTipo#serializarDesde(java.lang.Object,
	 *      net.gaia.vortex.sets.impl.serializacion.tipos.ContextoDeSerializacion)
	 */
	
	public Map<String, Object> serializarDesde(final CondicionDesconocida origen, final ContextoDeSerializacion contexto) {
		final Map<String, Object> formaOriginal = origen.getFormaOriginal();
		if (formaOriginal != null) {
			// Preservamos el mapa original
			return formaOriginal;
		}
		// Si no tiene info original es un tipo anonimo
		final Map<String, Object> serializado = SerializadorAnonimo.getInstancia().serializarDesde(origen, contexto);
		return serializado;
	}

}
