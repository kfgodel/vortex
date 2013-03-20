/**
 * 13/06/2012 01:31:40 Copyright (C) 2011 Darío L. García
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

import net.gaia.vortex.core.api.annotations.Paralelizable;
import net.gaia.vortex.core.api.condiciones.Condicion;
import net.gaia.vortex.core.api.condiciones.ResultadoDeCondicion;
import net.gaia.vortex.core.api.mensaje.MensajeVortex;
import ar.com.dgarcia.coding.caching.DefaultInstantiator;
import ar.com.dgarcia.coding.caching.WeakSingleton;
import ar.com.dgarcia.lang.strings.ToString;

/**
 * Esta clase representa la condicion que no es cumplida por ningun mensaje
 * 
 * @author D. García
 */
@Paralelizable
public class SiempreFalse implements Condicion {

	private static final WeakSingleton<SiempreFalse> ultimaReferencia = new WeakSingleton<SiempreFalse>(
			DefaultInstantiator.create(SiempreFalse.class));

	public static SiempreFalse getInstancia() {
		return ultimaReferencia.get();
	}

	/**
	 * @see net.gaia.vortex.core.api.condiciones.Condicion#esCumplidaPor(net.gaia.vortex.core.api.mensaje.MensajeVortex)
	 */
	
	public ResultadoDeCondicion esCumplidaPor(@SuppressWarnings("unused") final MensajeVortex mensaje) {
		return ResultadoDeCondicion.FALSE;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	
	public String toString() {
		return ToString.de(this).toString();
	}

	/**
	 * @see net.gaia.vortex.core.api.condiciones.Condicion#getSubCondiciones()
	 */
	
	public List<Condicion> getSubCondiciones() {
		return Collections.emptyList();
	}

}
