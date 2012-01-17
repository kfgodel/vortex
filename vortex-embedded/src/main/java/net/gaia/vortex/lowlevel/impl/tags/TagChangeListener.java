/**
 * 16/01/2012 19:58:24 Copyright (C) 2011 Darío L. García
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
 * Esta interfaz define el contrato del listener del {@link TagSummarizer} para ser notificado de
 * cambios globales de tags
 * 
 * @author D. García
 */
public interface TagChangeListener {

	/**
	 * Indica a este listener que el receptor pasado generó la ocurrencia de nuevos tags globales
	 * 
	 * @param tagAgregadosGlobalmente
	 *            Los tags agregados respecto del estado anterior
	 * @param receptorQueLosAgrega
	 *            El receptor que generó el cambio
	 */
	void onTagsAgregadosGlobalmente(Set<String> tagAgregadosGlobalmente, ReceptorVortex receptorQueLosAgrega);

	/**
	 * Indica a este listener que se quitar tags globales
	 * 
	 * @param tagQuitadosGlobalmente
	 *            Los tags quitados
	 * @param receptorQueLosQuito
	 *            El receptor que generó la diferencia
	 */
	void onTagsQuitadosGlobalmente(Set<String> tagQuitadosGlobalmente, ReceptorVortex receptorQueLosQuito);

}
