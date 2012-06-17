/**
 * 17/06/2012 18:09:52 Copyright (C) 2011 Darío L. García
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

import net.gaia.vortex.core3.api.condiciones.Condicion;
import net.gaia.vortex.core3.api.mensaje.MensajeVortex;
import ar.com.dgarcia.coding.exceptions.UnhandledConditionException;

import com.google.common.base.Objects;

/**
 * Esta clase representa la condicion que solo admite instancias de una clase
 * 
 * @author D. García
 */
public class SoloInstancias implements Condicion {

	private Class<?> tipoEsperado;
	public static final String tipoEsperado_FIELD = "tipoEsperado";

	/**
	 * @see net.gaia.vortex.core3.api.condiciones.Condicion#esCumplidaPor(net.gaia.vortex.core3.api.mensaje.MensajeVortex)
	 */
	@Override
	public boolean esCumplidaPor(final MensajeVortex mensaje) {
		throw new UnhandledConditionException("No implementado");
	}

	public static SoloInstancias de(final Class<?> tipoEsperado) {
		final SoloInstancias condicion = new SoloInstancias();
		condicion.tipoEsperado = tipoEsperado;
		return condicion;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return Objects.toStringHelper(this).add(tipoEsperado_FIELD, tipoEsperado).toString();
	}
}
