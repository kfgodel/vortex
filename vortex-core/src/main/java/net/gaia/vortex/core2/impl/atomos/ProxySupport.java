/**
 * 13/06/2012 00:47:39 Copyright (C) 2011 Darío L. García
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

import net.gaia.vortex.core2.api.MensajeVortex;
import net.gaia.vortex.core2.api.atomos.ComponenteProxy;
import net.gaia.vortex.core2.api.atomos.ComponenteVortex;

/**
 * Esta clase implementa comportamiento base para las sub clases de {@link ComponenteProxy}
 * 
 * @author D. García
 */
public abstract class ProxySupport implements ComponenteProxy {

	private ComponenteVortex delegado;

	public ProxySupport() {
		setDelegado(ComponenteNulo.getInstancia());
	}

	/**
	 * @see net.gaia.vortex.core2.api.atomos.ComponenteProxy#setDelegado(net.gaia.vortex.core2.api.atomos.ComponenteVortex)
	 */
	@Override
	public void setDelegado(final ComponenteVortex delegado) {
		if (delegado == null) {
			throw new IllegalArgumentException("El delegado del proxy no puede ser null. A lo sumo un "
					+ ComponenteNulo.class);
		}
		this.delegado = delegado;
	}

	public ComponenteVortex getDelegado() {
		return delegado;
	}

	/**
	 * @see net.gaia.vortex.core2.api.atomos.ComponenteVortex#recibirMensaje(net.gaia.vortex.core2.api.MensajeVortex)
	 */
	@Override
	public void recibirMensaje(final MensajeVortex mensaje) {
		// Lo delegamos en el delegado
		this.delegado.recibirMensaje(mensaje);
	}
}
