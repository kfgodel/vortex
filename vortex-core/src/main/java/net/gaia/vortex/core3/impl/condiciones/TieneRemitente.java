/**
 * 17/06/2012 15:55:59 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.core3.impl.condiciones;

import net.gaia.vortex.core3.api.atomos.Receptor;
import net.gaia.vortex.core3.api.condiciones.Condicion;
import net.gaia.vortex.core3.api.mensaje.MensajeVortex;
import ar.com.dgarcia.coding.caching.DefaultInstantiator;
import ar.com.dgarcia.coding.caching.WeakSingleton;

import com.google.common.base.Objects;

/**
 * Esta clase representa la condición que evalua si el mensaje recibido tiene remitente o no
 * 
 * @author D. García
 */
public class TieneRemitente implements Condicion {

	private static final WeakSingleton<TieneRemitente> ultimaReferencia = new WeakSingleton<TieneRemitente>(
			DefaultInstantiator.create(TieneRemitente.class));

	public static TieneRemitente getInstancia() {
		return ultimaReferencia.get();
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return Objects.toStringHelper(this).toString();
	}

	/**
	 * @see net.gaia.vortex.core3.api.condiciones.Condicion#esCumplidaPor(net.gaia.vortex.core3.api.mensaje.MensajeVortex)
	 */
	@Override
	public boolean esCumplidaPor(final MensajeVortex mensaje) {
		final Receptor remitente = mensaje.getRemitenteDirecto();
		final boolean tieneRemitente = remitente != null;
		return tieneRemitente;
	}
}
