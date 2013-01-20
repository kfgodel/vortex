/**
 * 24/08/2012 16:41:36 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.sets.impl;

import net.gaia.vortex.core.api.condiciones.Condicion;
import net.gaia.vortex.core.api.condiciones.ResultadoDeCondicion;
import net.gaia.vortex.core.api.mensaje.MensajeVortex;
import ar.com.dgarcia.lang.strings.ToString;

/**
 * Esta clase representa la condición que niega el valor de verdad de otra condición
 * 
 * @author D. García
 */
public class Not implements Condicion {

	private Condicion condicionNegada;
	public static final String condicionNegada_FIELD = "condicionNegada";

	/**
	 * @see net.gaia.vortex.core.api.condiciones.Condicion#esCumplidaPor(net.gaia.vortex.core.api.mensaje.MensajeVortex)
	 */
	@Override
	public ResultadoDeCondicion esCumplidaPor(final MensajeVortex mensaje) {
		final ResultadoDeCondicion resultado = condicionNegada.esCumplidaPor(mensaje);
		// La negación de true es false
		if (ResultadoDeCondicion.TRUE.equals(resultado)) {
			return ResultadoDeCondicion.FALSE;
		}
		// La negación de false es true
		if (ResultadoDeCondicion.FALSE.equals(resultado)) {
			return ResultadoDeCondicion.TRUE;
		}
		// La negación de indecidible sigue siendo indecidible
		return ResultadoDeCondicion.INDECIDIBLE;
	}

	public static Not de(final Condicion condicionANegar) {
		final Not condicion = new Not();
		condicion.condicionNegada = condicionANegar;
		return condicion;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return ToString.de(this).con(condicionNegada_FIELD, condicionNegada).toString();
	}

}
