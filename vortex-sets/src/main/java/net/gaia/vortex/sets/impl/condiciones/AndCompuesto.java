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
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import net.gaia.vortex.api.annotations.paralelizable.Paralelizable;
import net.gaia.vortex.api.condiciones.Condicion;
import net.gaia.vortex.api.condiciones.ResultadoDeCondicion;
import net.gaia.vortex.api.mensajes.MensajeVortex;
import net.gaia.vortex.core.impl.condiciones.SiempreFalse;
import net.gaia.vortex.core.impl.condiciones.SiempreTrue;
import ar.com.dgarcia.lang.strings.ToString;

/**
 * Esta clase representa la condición AND que combina otras condiciones todas aplicando el operador
 * AND desde la primera a la última cortando al primer false.
 * 
 * @author D. García
 */
@Paralelizable
public class AndCompuesto implements Condicion, Simplificable {

	private List<Condicion> condiciones;
	public static final String condiciones_FIELD = "condiciones";

	/**
	 * @see net.gaia.vortex.api.condiciones.Condicion#esCumplidaPor(net.gaia.vortex.api.mensajes.MensajeVortex)
	 */
	
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
	 * @see net.gaia.vortex.api.condiciones.Condicion#getSubCondiciones()
	 */
	
	public List<Condicion> getSubCondiciones() {
		return getCondiciones();
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	
	public boolean equals(final Object obj) {
		if (!(obj instanceof AndCompuesto)) {
			return false;
		}
		final AndCompuesto that = (AndCompuesto) obj;
		final Set<Condicion> thisCondiciones = new HashSet<Condicion>(this.condiciones);
		final Set<Condicion> thatCondiciones = new HashSet<Condicion>(that.condiciones);
		final boolean mismoConjuntoDeCondiciones = thisCondiciones.equals(thatCondiciones);
		return mismoConjuntoDeCondiciones;
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	
	public int hashCode() {
		return this.condiciones.hashCode();
	}

	/**
	 * @see net.gaia.vortex.sets.impl.condiciones.Simplificable#simplificar()
	 */
	
	public Condicion simplificar() {
		boolean huboAlMenosUnaSimplificacion = false;

		final List<Condicion> condicionesAnteriores = getCondiciones();
		// Metemos en un set para descartar las condiciones iguales
		final Set<Condicion> condicionesSimplificadas = new LinkedHashSet<Condicion>(condicionesAnteriores.size());

		for (final Condicion condicion : condicionesAnteriores) {
			Condicion condicionActual = condicion;

			if (condicionActual instanceof Simplificable) {
				// Intentamos simplificarla
				final Condicion simplificada = ((Simplificable) condicionActual).simplificar();
				if (simplificada != condicionActual) {
					huboAlMenosUnaSimplificacion = true;
					condicionActual = simplificada;
				}
			}
			if (condicionActual instanceof SiempreFalse) {
				// Caso de corte para el AND. Si alguna es false, hace false a todo el OR
				return condicionActual;
			}
			if (condicionActual instanceof SiempreTrue) {
				// El true es redundante en el and (A and true = A). Lo omitimos
				huboAlMenosUnaSimplificacion = true;
				continue;
			}
			condicionesSimplificadas.add(condicionActual);
		}
		if (condicionesSimplificadas.isEmpty()) {
			// Eran todos true
			return SiempreTrue.getInstancia();
		}
		if (condicionesSimplificadas.size() == 1) {
			// Si es una condicion, no hace falta el AND
			final Iterator<Condicion> iterator = condicionesSimplificadas.iterator();
			iterator.hasNext();
			final Condicion unicaCondicion = iterator.next();
			return unicaCondicion;
		}
		if (!huboAlMenosUnaSimplificacion) {
			return this;
		}
		final AndCompuesto andSimplificada = AndCompuesto.create(new ArrayList<Condicion>(condicionesSimplificadas));
		return andSimplificada;
	}
}
