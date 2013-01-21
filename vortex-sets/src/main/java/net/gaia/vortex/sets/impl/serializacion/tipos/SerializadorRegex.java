/**
 * 21/01/2013 19:41:34 Copyright (C) 2011 Darío L. García
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

import net.gaia.vortex.sets.impl.TextoRegexMatchea;
import net.gaia.vortex.sets.impl.serializacion.SerializadorDeTipo;
import ar.com.dgarcia.coding.caching.DefaultInstantiator;
import ar.com.dgarcia.coding.caching.WeakSingleton;

/**
 * Esta clase implementa el serializador de condiciones regex
 * 
 * @author D. García
 */
public class SerializadorRegex implements SerializadorDeTipo<TextoRegexMatchea> {
	private static final WeakSingleton<SerializadorRegex> ultimaReferencia = new WeakSingleton<SerializadorRegex>(
			DefaultInstantiator.create(SerializadorRegex.class));

	public static SerializadorRegex getInstancia() {
		return ultimaReferencia.get();
	}

}
