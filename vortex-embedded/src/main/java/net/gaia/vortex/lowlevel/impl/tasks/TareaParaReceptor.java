/**
 * 11/02/2012 14:00:53 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.lowlevel.impl.tasks;

import net.gaia.taskprocessor.api.WorkUnit;
import net.gaia.vortex.lowlevel.impl.receptores.ReceptorVortex;

/**
 * Esta interfaz es una extensión al {@link WorkUnit} que puede adjudicarse a un receptor en
 * particular
 * 
 * @author D. García
 */
public interface TareaParaReceptor extends WorkUnit {

	/**
	 * Indica si esta tarea está orientada a un proceso relacionado con el receptor indicado
	 * 
	 * @param receptor
	 *            El receptor a evaluar
	 * @return true si esta tarea la realiza el nodo para el receptor
	 */
	boolean esPara(ReceptorVortex receptor);

}
