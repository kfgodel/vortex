/**
 * 21/01/2013 19:41:41 Copyright (C) 2011 Darío L. García
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
import net.gaia.vortex.sets.impl.serializacion.DeserializadorDeTipo;
import ar.com.dgarcia.coding.caching.DefaultInstantiator;
import ar.com.dgarcia.coding.caching.WeakSingleton;

/**
 * Esta clase implementa el deserializador de condiciones por regex
 * 
 * @author D. García
 */
public class DeserializadorRegex implements DeserializadorDeTipo<TextoRegexMatchea> {
	private static final WeakSingleton<DeserializadorRegex> ultimaReferencia = new WeakSingleton<DeserializadorRegex>(
			DefaultInstantiator.create(DeserializadorRegex.class));

	public static DeserializadorRegex getInstancia() {
		return ultimaReferencia.get();
	}

}
