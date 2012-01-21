/**
 * 21/01/2012 19:05:23 Copyright (C) 2011 Darío L. García
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
import net.gaia.vortex.lowlevel.impl.NodoVortexConTasks;
import net.gaia.vortex.lowlevel.impl.receptores.ReceptorVortex;

/**
 * Esta clase representa la operación realizada por el nodo para cerrar la conexión asociada a un
 * receptor y limpiar sus datos
 * 
 * @author D. García
 */
public class ProcesarCierreDeConexionWorkUnit implements WorkUnit {

	private NodoVortexConTasks nodo;
	private ReceptorVortex receptorCerrado;

	public static ProcesarCierreDeConexionWorkUnit create(final NodoVortexConTasks nodo, final ReceptorVortex receptor) {
		final ProcesarCierreDeConexionWorkUnit cierre = new ProcesarCierreDeConexionWorkUnit();
		cierre.nodo = nodo;
		cierre.receptorCerrado = receptor;
		return cierre;
	}

	/**
	 * @see net.gaia.taskprocessor.api.WorkUnit#doWork()
	 */
	@Override
	public void doWork() throws InterruptedException {
		// TODO Auto-generated method stub

	}
}
