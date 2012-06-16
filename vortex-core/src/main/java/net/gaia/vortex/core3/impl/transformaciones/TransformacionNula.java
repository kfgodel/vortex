/**
 * 13/06/2012 01:37:54 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.core3.impl.transformaciones;

import net.gaia.vortex.core3.api.mensaje.MensajeVortex;
import net.gaia.vortex.core3.api.transformaciones.Transformacion;
import ar.com.dgarcia.coding.caching.DefaultInstantiator;
import ar.com.dgarcia.coding.caching.WeakSingleton;

import com.google.common.base.Objects;

/**
 * Esta clase representa la transformación del mensaje que lo deja tal como lo recibe
 * 
 * @author D. García
 */
public class TransformacionNula implements Transformacion {

	private static final WeakSingleton<TransformacionNula> ultimaReferencia = new WeakSingleton<TransformacionNula>(
			DefaultInstantiator.create(TransformacionNula.class));

	public static TransformacionNula getInstancia() {
		return ultimaReferencia.get();
	}

	/**
	 * @see net.gaia.vortex.core3.api.transformaciones.Transformacion#transformar(net.gaia.vortex.core3.api.mensaje.MensajeVortex)
	 */
	@Override
	public MensajeVortex transformar(final MensajeVortex mensaje) {
		return mensaje;
	}

	public static TransformacionNula create() {
		final TransformacionNula transformacion = new TransformacionNula();
		return transformacion;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return Objects.toStringHelper(this).toString();
	}
}
