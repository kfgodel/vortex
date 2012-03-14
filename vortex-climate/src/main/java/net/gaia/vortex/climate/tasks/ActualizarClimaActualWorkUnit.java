/**
 * 13/03/2012 22:59:45 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.climate.tasks;

import java.util.concurrent.TimeUnit;

import net.gaia.taskprocessor.api.TaskProcessor;
import net.gaia.taskprocessor.api.TimeMagnitude;
import net.gaia.taskprocessor.api.WorkUnit;
import net.gaia.vortex.climate.VortexClimateMain;
import net.gaia.vortex.climate.externals.ClimateMeasure;
import net.gaia.vortex.climate.externals.ClimateStation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Esta clase representa la tarea de actualización del clima en la red vortex
 * 
 * @author D. García
 */
public class ActualizarClimaActualWorkUnit implements WorkUnit {
	private static final Logger LOG = LoggerFactory.getLogger(ActualizarClimaActualWorkUnit.class);

	private TaskProcessor processor;

	/**
	 * @see net.gaia.taskprocessor.api.WorkUnit#doWork()
	 */
	@Override
	public void doWork() throws InterruptedException {
		LOG.debug("Verificando novedades en datos climáticos");
		final ClimateStation climateStation = VortexClimateMain.I.getBuenosAiresStation();
		final boolean refreshed = climateStation.refresh();
		if (refreshed) {
			final ClimateMeasure newMeasure = climateStation.getLastMeasure();
			LOG.debug("Datos climáticos actualizados: {}", newMeasure);
			final WorkUnit enviarNevaMedicion = EnviarNuevaMediciónWorkUnit.create(newMeasure);
			processor.process(enviarNevaMedicion);
		}
		// Nos re-ejecutamos dentro de 30min
		processor.processDelayed(TimeMagnitude.of(30, TimeUnit.MINUTES), this);
	}

	public static ActualizarClimaActualWorkUnit create(final TaskProcessor processor) {
		final ActualizarClimaActualWorkUnit actualizar = new ActualizarClimaActualWorkUnit();
		actualizar.processor = processor;
		return actualizar;
	}
}
