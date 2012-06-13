/**
 * 13/06/2012 01:25:20 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.core2.impl.atomos.condicional;

import net.gaia.vortex.core2.api.MensajeVortex;
import net.gaia.vortex.core2.api.atomos.Condicion;
import ar.com.dgarcia.coding.caching.DefaultInstantiator;
import ar.com.dgarcia.coding.caching.WeakSingleton;

/**
 * Esta clase representa una condicion que acepta todos los mensajes
 * 
 * @author D. García
 */
public class CondicionTodos implements Condicion {

	private static final WeakSingleton<CondicionTodos> ultimaReferencia = new WeakSingleton<CondicionTodos>(
			DefaultInstantiator.create(CondicionTodos.class));

	public static CondicionTodos getInstancia() {
		return ultimaReferencia.get();
	}

	/**
	 * @see net.gaia.vortex.core2.api.atomos.Condicion#esCumplidaPor(net.gaia.vortex.core2.api.MensajeVortex)
	 */
	@Override
	public boolean esCumplidaPor(final MensajeVortex mensaje) {
		return true;
	}

	public static CondicionTodos create() {
		final CondicionTodos condicion = new CondicionTodos();
		return condicion;
	}
}
