/**
 * 27/11/2011 22:45:41 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.lowlevel.impl.ruteo;

import java.util.HashSet;
import java.util.Set;

import net.gaia.vortex.lowlevel.impl.receptores.ReceptorVortex;

/**
 * Esta clase representa la selección de un conjunto de receptores a los cuales rutear un mensaje
 * 
 * @author D. García
 */
public class SeleccionDeReceptores {

	private ReceptorVortex receptorExcluido;
	private Set<ReceptorVortex> seleccionados;

	/**
	 * Crea esta selección para excluir de sus seleccionados al receptor indicado
	 * 
	 * @param emisorExcluido
	 *            El receptor a excluir de los seleccionados
	 * @return La selección creada
	 */
	public static SeleccionDeReceptores create(final ReceptorVortex emisorExcluido) {
		final SeleccionDeReceptores seleccion = new SeleccionDeReceptores();
		seleccion.receptorExcluido = emisorExcluido;
		return seleccion;
	}

	public Set<ReceptorVortex> getSeleccionados() {
		if (seleccionados == null) {
			seleccionados = new HashSet<ReceptorVortex>();
		}
		return seleccionados;
	}

	public void setSeleccionados(final Set<ReceptorVortex> seleccionados) {
		this.seleccionados = seleccionados;
	}

	/**
	 * Registra el receptor pasado como incluido en la selección
	 * 
	 * @param receptor
	 *            El receptor a incluir como seleccionado
	 */
	public void incluirTodos(final Set<ReceptorVortex> receptores) {
		for (final ReceptorVortex receptorIncluible : receptores) {
			// Agregamos todos menos al excluido
			if (receptorIncluible.equals(receptorExcluido)) {
				continue;
			}
			this.getSeleccionados().add(receptorIncluible);
		}
	}

	/**
	 * Indica si esta selección no incluye a ningún receptor
	 * 
	 * @return true si esta selección no tiene a ningún receptor seleccionado
	 */
	public boolean esVacia() {
		return this.seleccionados.isEmpty();
	}

}
