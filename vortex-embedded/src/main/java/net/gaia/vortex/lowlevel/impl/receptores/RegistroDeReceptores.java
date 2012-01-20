/**
 * 17/01/2012 23:46:13 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.lowlevel.impl.receptores;

import java.util.List;
import java.util.Set;

import net.gaia.vortex.lowlevel.impl.ReportePerformanceRuteo;
import net.gaia.vortex.lowlevel.impl.SeleccionDeReceptores;
import net.gaia.vortex.lowlevel.impl.tags.TagSummarizer;

/**
 * 
 * @author D. García
 */
public interface RegistroDeReceptores {

	/**
	 * Agrega el receptor pasado, permitiendo que sea visible al nodo como tal
	 * 
	 * @param nuevoReceptor
	 *            El receptor que comenzará a recibir mensajes
	 */
	public abstract void agregar(final ReceptorVortex nuevoReceptor);

	/**
	 * Devuelve la lista de receptores que declararon interés en recibir mensajes con alguno de los
	 * tags pasados
	 * 
	 * @param tagsDelMensaje
	 *            Tags del mensaje a rutear
	 * @return Los receptores que tienen al menos uno de los tags declarados como recibibles
	 */
	public abstract SeleccionDeReceptores getReceptoresInteresadosEn(final List<String> tagsDelMensaje);

	/**
	 * Ajusta los pesos de los tags de cada receptor de acuerdo a los resultados de performance
	 * indicados
	 * 
	 * @param reportePerformance
	 *            El resultado de un ruteo
	 */
	public abstract void ajustarPesosDeAcuerdoA(final ReportePerformanceRuteo reportePerformance);

	/**
	 * Devuelve el conjunto de todos los receptores registrados
	 * 
	 * @return
	 */
	public abstract Set<ReceptorVortex> getAllReceptores();

	/**
	 * Devuelve el tag summarizer de este registro
	 * 
	 * @return El tag summarizer para registrar información de los tags de cada receptor
	 */
	public abstract TagSummarizer getTagSummarizer();

	/**
	 * Elimina el receptor indicado de este registro, eliminando también sus tags y generando las
	 * notificaciones correspondientes por los tags quitados
	 * 
	 * @param receptorQuitado
	 *            El receptor a quitar
	 */
	public void quitar(ReceptorVortex receptorQuitado);
}