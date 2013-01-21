/**
 * 24/08/2012 16:10:10 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.sets.impl.condiciones;

import java.util.ArrayList;
import java.util.List;

import net.gaia.vortex.core.api.condiciones.Condicion;
import net.gaia.vortex.core.api.condiciones.ResultadoDeCondicion;
import net.gaia.vortex.core.api.mensaje.MensajeVortex;
import ar.com.dgarcia.lang.strings.ToString;

/**
 * Esta clase representa la condición AND que combina otras condiciones todas aplicando el operador
 * AND desde la primera a la última cortando al primer false.
 * 
 * @author D. García
 */
public class AndCompuesto implements Condicion {

	private List<Condicion> condiciones;
	public static final String condiciones_FIELD = "condiciones";

	/**
	 * @see net.gaia.vortex.core.api.condiciones.Condicion#esCumplidaPor(net.gaia.vortex.core.api.mensaje.MensajeVortex)
	 */
	@Override
	public ResultadoDeCondicion esCumplidaPor(final MensajeVortex mensaje) {
		// Evaluamos cada condición que compone este AND
		boolean alMenosUnIndecidible = false;
		for (final Condicion condicion : condiciones) {
			final ResultadoDeCondicion resultado = condicion.esCumplidaPor(mensaje);

			if (ResultadoDeCondicion.FALSE.equals(resultado)) {
				// Un false hace false todo el AND, no es necesario seguir
				return ResultadoDeCondicion.FALSE;
			}
			if (ResultadoDeCondicion.INDECIDIBLE.equals(resultado)) {
				// Un indecidible hace indecidible todo a menos que haya un false
				alMenosUnIndecidible = true;
			}
		}
		// Si todas las condiciones fueron true menos una, dependiendo del valor de esa podría ser
		// true o false el and, no lo sabemos
		if (alMenosUnIndecidible) {
			return ResultadoDeCondicion.INDECIDIBLE;
		}
		// Todas fueron true
		return ResultadoDeCondicion.TRUE;
	}

	public static AndCompuesto de(final Condicion condicion1, final Condicion condicion2,
			final Condicion... adicionales) {
		final ArrayList<Condicion> condicionesEvaluadas = new ArrayList<Condicion>(adicionales.length + 2);
		condicionesEvaluadas.add(condicion1);
		condicionesEvaluadas.add(condicion2);
		for (final Condicion adicional : adicionales) {
			condicionesEvaluadas.add(adicional);
		}
		return create(condicionesEvaluadas);
	}

	public static AndCompuesto create(final List<Condicion> condicionesEvaluadas) {
		final AndCompuesto operador = new AndCompuesto();
		operador.condiciones = condicionesEvaluadas;
		return operador;
	}

	/**
	 * Agrega la condición pasada como parte de este operador
	 * 
	 * @param condicionExtra
	 *            La condición agregada para evaluar en el operador
	 */
	public void and(final Condicion condicionExtra) {
		this.condiciones.add(condicionExtra);
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return ToString.de(this).con(condiciones_FIELD, condiciones).toString();
	}

	public List<Condicion> getCondiciones() {
		return condiciones;
	}

	public void setCondiciones(final List<Condicion> condiciones) {
		this.condiciones = condiciones;
	}

	/**
	 * @see net.gaia.vortex.core.api.condiciones.Condicion#getSubCondiciones()
	 */
	@Override
	public List<Condicion> getSubCondiciones() {
		return getCondiciones();
	}

}
