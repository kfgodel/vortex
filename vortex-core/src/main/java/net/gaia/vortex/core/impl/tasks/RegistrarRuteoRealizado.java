/**
 * 26/05/2012 11:15:00 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.core.impl.tasks;

import net.gaia.taskprocessor.api.WorkUnit;
import net.gaia.vortex.core.impl.metrics.MetricasDelNodoImpl;

/**
 * Esta clase representa la tarea realizada por el nodo para registrar un ruteo en sus métricas
 * 
 * @author D. García
 */
public class RegistrarRuteoRealizado implements WorkUnit {

	private MetricasDelNodoImpl metricas;

	/**
	 * @see net.gaia.taskprocessor.api.WorkUnit#doWork()
	 */
	@Override
	public void doWork() throws InterruptedException {
		metricas.registrarRuteo();
	}

	public static RegistrarRuteoRealizado create(final MetricasDelNodoImpl metricas) {
		final RegistrarRuteoRealizado registrar = new RegistrarRuteoRealizado();
		registrar.metricas = metricas;
		return registrar;
	}
}