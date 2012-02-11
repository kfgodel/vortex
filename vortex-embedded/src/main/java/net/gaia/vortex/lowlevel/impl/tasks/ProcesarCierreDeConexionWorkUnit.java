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

import net.gaia.taskprocessor.api.TaskCriteria;
import net.gaia.taskprocessor.api.TaskProcessor;
import net.gaia.taskprocessor.api.WorkUnit;
import net.gaia.vortex.lowlevel.impl.nodo.NodoVortexConTasks;
import net.gaia.vortex.lowlevel.impl.receptores.ReceptorVortex;
import net.gaia.vortex.lowlevel.impl.receptores.RegistroDeReceptores;
import net.gaia.vortex.lowlevel.impl.ruteo.MemoriaDeRuteos;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Esta clase representa la operación realizada por el nodo para cerrar la conexión asociada a un
 * receptor y limpiar sus datos
 * 
 * @author D. García
 */
public class ProcesarCierreDeConexionWorkUnit implements TareaParaReceptor {
	private static final Logger LOG = LoggerFactory.getLogger(ProcesarCierreDeConexionWorkUnit.class);

	private NodoVortexConTasks nodo;
	private ReceptorVortex receptorCerrado;

	public static ProcesarCierreDeConexionWorkUnit create(final NodoVortexConTasks nodo, final ReceptorVortex receptor) {
		final ProcesarCierreDeConexionWorkUnit cierre = new ProcesarCierreDeConexionWorkUnit();
		cierre.nodo = nodo;
		cierre.receptorCerrado = receptor;
		return cierre;
	}

	/**
	 * @see net.gaia.vortex.lowlevel.impl.tasks.TareaParaReceptor#esPara(net.gaia.vortex.lowlevel.impl.receptores.ReceptorVortex)
	 */
	@Override
	public boolean esPara(final ReceptorVortex receptor) {
		return this.receptorCerrado == receptor;
	}

	/**
	 * @see net.gaia.taskprocessor.api.WorkUnit#doWork()
	 */
	@Override
	public void doWork() throws InterruptedException {
		LOG.debug("Limpiando tareas del receptor[{}] por cierre recibido", receptorCerrado);
		final TaskProcessor procesador = nodo.getProcesador();
		procesador.removeTasksMatching(new TaskCriteria() {
			@Override
			public boolean matches(final WorkUnit workUnit) {
				if ((workUnit instanceof TareaParaReceptor)) {
					// Si no es para un receptor no corresponde quitarla
					return false;
				}
				final TareaParaReceptor tareaDelNodo = (TareaParaReceptor) workUnit;
				final boolean correspondeRemoverla = tareaDelNodo.esPara(receptorCerrado);
				return correspondeRemoverla;
			}
		});

		LOG.debug("Quitando tags asociados al receptor[{}] por cierre", receptorCerrado);
		final RegistroDeReceptores registroReceptores = nodo.getRegistroReceptores();
		registroReceptores.quitar(receptorCerrado);

		LOG.debug("Quitando ruteos activos para el receptor[{}] por cierre", receptorCerrado);
		final MemoriaDeRuteos memoriaDeRuteos = nodo.getMemoriaDeRuteos();
		memoriaDeRuteos.eliminarRuteosActivosPara(receptorCerrado);
	}
}
