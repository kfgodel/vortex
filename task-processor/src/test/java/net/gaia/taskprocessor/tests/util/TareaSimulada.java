/**
 * 22/05/2012 09:59:00 Copyright (C) 2011 10Pines S.R.L.
 */
package net.gaia.taskprocessor.tests.util;

import net.gaia.taskprocessor.api.WorkUnit;
import net.gaia.util.WaitBarrier;

/**
 * Esta clase simula una tarea que tarda un tiempo en completarse
 * 
 * @author D. García
 */
public class TareaSimulada implements WorkUnit {

	private long duracionDeTarea;
	private WaitBarrier barreraDeTareas;

	/**
	 * @see net.gaia.taskprocessor.api.WorkUnit#doWork()
	 */
	@Override
	public void doWork() throws InterruptedException {
		Thread.sleep(duracionDeTarea);
		if (barreraDeTareas != null) {
			barreraDeTareas.release();
		}
	}

	public static TareaSimulada create(final long milisEnCompletarTarea, final WaitBarrier barreraDeTareas) {
		final TareaSimulada tarea = new TareaSimulada();
		tarea.duracionDeTarea = milisEnCompletarTarea;
		tarea.barreraDeTareas = barreraDeTareas;
		return tarea;
	}
}
