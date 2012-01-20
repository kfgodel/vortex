/**
 * 16/01/2012 19:46:23 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.lowlevel.impl.tags;

import java.util.Set;

import net.gaia.vortex.lowlevel.impl.ReceptorVortex;

/**
 * Esta interfaz representa el sumarizador de tags que posee el nodo para tomar conocimiento de los
 * tags que debe tomar como propios en base a los tags de sus clientes
 * 
 * @author D. García
 */
public interface TagSummarizer {

	/**
	 * Agrega los tags indicados asociados al receptor pasado. Si los nuevos tags implican nuevos
	 * tags para este sumarizador, entonces se generará un evento para notificar al listener
	 * 
	 * @param receptorAModificar
	 *            El receptor involucrado
	 * @param nuevosTags
	 *            Los tags que declara como agregados
	 */
	void agregarTagsPara(ReceptorVortex receptorAModificar, Set<String> nuevosTags);

	/**
	 * Elimina los tags pasados de los tags del receptor, notificando al listener si hay cambios
	 * globales
	 * 
	 * @param receptorAModificar
	 *            El receptor al que se le quitarán los tags
	 * @param nuevosTags
	 *            Los tags a quitar
	 */
	void quitarTagsPara(ReceptorVortex receptorAModificar, Set<String> nuevosTags);

	/**
	 * Reemplaza los tags del receptor indicado, notificando al listener si hay cambios en los tags
	 * globales
	 * 
	 * @param receptorAModificar
	 *            El receptor al que se le reemplazarán los tags
	 * @param nuevosTags
	 *            Los nuevos tags del receptor
	 */
	void reemplazarTagsPara(ReceptorVortex receptorAModificar, Set<String> nuevosTags);

	/**
	 * Elimina todos los tags asociados al receptor indicado, notificando al listener si hay cambios
	 * en los tags globales
	 * 
	 * @param receptorAModificar
	 *            El receptor a modificar
	 */
	void limpiarTagsPara(ReceptorVortex receptorAModificar);

}
