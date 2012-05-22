/**
 * 22/05/2012 09:59:00 Copyright (C) 2011 10Pines S.R.L.
 */
package net.gaia.taskprocessor.tests.util;

import net.gaia.taskprocessor.api.WorkUnit;

/**
 * Esta clase simula una tarea que tarda un tiempo en completarse
 * 
 * @author D. García
 */
public class TareaSimulada implements WorkUnit {

	private long duracionDeTarea;

	/**
	 * @see net.gaia.taskprocessor.api.WorkUnit#doWork()
	 */
	@Override
	public void doWork() throws InterruptedException {
		Thread.sleep(duracionDeTarea);
	}

	public static TareaSimulada create(final long milisEnCompletarTarea) {
		final TareaSimulada name = new TareaSimulada();
		name.duracionDeTarea = milisEnCompletarTarea;
		return name;
	}
}
