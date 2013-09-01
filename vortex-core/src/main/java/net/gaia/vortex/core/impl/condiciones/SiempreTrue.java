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
package net.gaia.vortex.core.impl.condiciones;

import java.util.Collections;
import java.util.List;

import net.gaia.vortex.api.annotations.paralelizable.Paralelizable;
import net.gaia.vortex.api.condiciones.Condicion;
import net.gaia.vortex.api.condiciones.ResultadoDeCondicion;
import net.gaia.vortex.api.mensajes.MensajeVortex;
import ar.com.dgarcia.coding.caching.DefaultInstantiator;
import ar.com.dgarcia.coding.caching.WeakSingleton;
import ar.com.dgarcia.coding.caching.WeakSingletonSupport;
import ar.com.dgarcia.lang.strings.ToString;

/**
 * Esta clase representa una condicion que acepta todos los mensajes
 * 
 * @author D. García
 */
@Paralelizable
public class SiempreTrue extends WeakSingletonSupport implements Condicion {

	private static final WeakSingleton<SiempreTrue> ultimaReferencia = new WeakSingleton<SiempreTrue>(
			DefaultInstantiator.create(SiempreTrue.class));

	public static SiempreTrue getInstancia() {
		return ultimaReferencia.get();
	}

	/**
	 * @see net.gaia.vortex.api.condiciones.Condicion#esCumplidaPor(net.gaia.vortex.api.mensajes.MensajeVortex)
	 */

	public ResultadoDeCondicion esCumplidaPor(@SuppressWarnings("unused") final MensajeVortex mensaje) {
		return ResultadoDeCondicion.TRUE;
	}

	/**
	 * @see java.lang.Object#toString()
	 */

	@Override
	public String toString() {
		return ToString.de(this).toString();
	}

	/**
	 * @see net.gaia.vortex.api.condiciones.Condicion#getSubCondiciones()
	 */

	public List<Condicion> getSubCondiciones() {
		return Collections.emptyList();
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(final Object obj) {
		final boolean esSiempreTrue = obj instanceof SiempreTrue;
		return esSiempreTrue;
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return getClass().hashCode();
	}
}
