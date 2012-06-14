/**
 * 13/06/2012 00:48:48 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.core2.impl.atomos;

import net.gaia.vortex.core2.api.atomos.ComponenteVortex;
import net.gaia.vortex.core3.api.annon.Atomo;
import net.gaia.vortex.core3.api.atomos.MensajeVortex;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ar.com.dgarcia.coding.caching.DefaultInstantiator;
import ar.com.dgarcia.coding.caching.WeakSingleton;

import com.google.common.base.Objects;

/**
 * Esta clase representa el componente nulo utilizado para no tener referencias en null
 * 
 * @author D. García
 */
@Atomo
public class ComponenteNulo implements ComponenteVortex {
	private static final Logger LOG = LoggerFactory.getLogger(ComponenteNulo.class);

	private static final WeakSingleton<ComponenteNulo> ultimaReferencia = new WeakSingleton<ComponenteNulo>(
			DefaultInstantiator.create(ComponenteNulo.class));

	public static ComponenteNulo getInstancia() {
		return ultimaReferencia.get();
	}

	/**
	 * @see net.gaia.vortex.core2.api.atomos.ComponenteVortex#recibirMensaje(net.gaia.vortex.core3.api.atomos.MensajeVortex)
	 */
	@Override
	public void recibirMensaje(final MensajeVortex mensaje) {
		LOG.debug("Se recibió un mensaje[{}] en el componente nulo", mensaje);
	}

	public static ComponenteNulo create() {
		final ComponenteNulo componente = new ComponenteNulo();
		return componente;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return Objects.toStringHelper(this).toString();
	}
}
