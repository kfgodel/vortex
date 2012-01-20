/**
 * 18/01/2012 21:33:10 Copyright (C) 2011 Darío L. García
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


/**
 * Esta interfaz representa el contrato que debe cumplir el listener de cambios en los tags de un
 * nodo
 * 
 * @author D. García
 */
public interface TagChangeListener {
	/**
	 * Invocado cuando un cliente cambia los tags del nodo, de manera que es necesario avisar a
	 * otros clientes
	 * 
	 * @param reporte
	 *            El reporte de lo cambios generado
	 */
	public void onTagChanges(ReporteCambioDeTags reporte);
}
