/**
 * 24/08/2012 16:27:46 Copyright (C) 2011 Darío L. García
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

import java.util.ArrayList;
import java.util.List;

import net.gaia.vortex.core.api.condiciones.Condicion;
import net.gaia.vortex.core.api.mensaje.MensajeVortex;
import ar.com.dgarcia.lang.strings.ToString;

/**
 * Esta clase representa la condicion OR que combina otras condiciones con el operador OR ante la
 * primera condicion true corta la evaluación
 * 
 * @author D. García
 */
public class Or implements Condicion {

	private List<Condicion> condiciones;
	public static final String condiciones_FIELD = "condiciones";

	/**
	 * @see net.gaia.vortex.core.api.condiciones.Condicion#esCumplidaPor(net.gaia.vortex.core.api.mensaje.MensajeVortex)
	 */
	@Override
	public boolean esCumplidaPor(final MensajeVortex mensaje) {
		for (final Condicion condicion : condiciones) {
			if (condicion.esCumplidaPor(mensaje)) {
				return true;
			}
		}
		return false;
	}

	public static Or create(final Condicion condicion1, final Condicion condicion2, final Condicion... adicionales) {
		final ArrayList<Condicion> condicionesEvaluadas = new ArrayList<Condicion>(adicionales.length + 2);
		condicionesEvaluadas.add(condicion1);
		condicionesEvaluadas.add(condicion2);
		for (final Condicion adicional : adicionales) {
			condicionesEvaluadas.add(adicional);
		}
		return create(condicionesEvaluadas);
	}

	public static Or create(final List<Condicion> condicionesEvaluadas) {
		final Or operador = new Or();
		operador.condiciones = condicionesEvaluadas;
		return operador;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return ToString.de(this).con(condiciones_FIELD, condiciones).toString();
	}

}