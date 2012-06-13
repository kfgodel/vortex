/**
 * 13/06/2012 01:50:02 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.core2.impl.moleculas;

import net.gaia.taskprocessor.api.TaskProcessor;
import net.gaia.vortex.core2.api.annon.Molecula;
import net.gaia.vortex.core2.api.nodos.Hub;

import com.google.common.base.Objects;

/**
 * Esta clase representa un {@link Hub} en su implementación mínima
 * 
 * @author D. García
 */
@Molecula
public class HubMinimo implements Hub {

	public static HubMinimo create(final TaskProcessor processor) {
		final HubMinimo name = new HubMinimo();
		return name;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return Objects.toStringHelper(this).add(comportamientoDeEntrada_FIELD, comportamientoDeEntrada)
				.add(comportamientoDeSalida_FIELD, comportamientoDeSalida).toString();
	}
}
