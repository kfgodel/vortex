/**
 * 19/01/2012 22:56:46 Copyright (C) 2011 Darío L. García
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
 * Esta interfaz representa una operación atómica sobre los tags del nodo, que puede dar lugar a
 * notificaciones en un {@link ReporteCambioDeTags}
 * 
 * @author D. García
 */
public interface AtomicTagOperation {

	/**
	 * Ejecuta esta operación pasando el reporte sobre el cuál se deben registrar las notificaciones
	 * 
	 * @param reporte
	 *            El reporte que puede ser compartido por otras operaciones para notificar los
	 *            cambios efectuados
	 */
	public void execute(ReporteCambioDeTags reporte);

}
