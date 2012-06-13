/**
 * 13/03/2012 23:10:14 Copyright (C) 2011 Darío L. García
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

import net.gaia.taskprocessor.api.WorkUnit;
import net.gaia.vortex.climate.VortexClimateMain;
import net.gaia.vortex.climate.externals.ClimateMeasure;
import net.gaia.vortex.dependencies.json.InterpreteJson;
import net.gaia.vortex.dependencies.json.impl.InterpreteJackson;
import net.gaia.vortex.hilevel.api.ClienteVortex;
import net.gaia.vortex.hilevel.api.MensajeVortexApi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Esta clase representa la tarea de envio de nueva medición mediante vortex
 * 
 * @author D. García
 */
public class EnviarNuevaMedicionWorkUnit implements WorkUnit {
	private static final Logger LOG = LoggerFactory.getLogger(EnviarNuevaMedicionWorkUnit.class);

	private ClimateMeasure measure;
	private InterpreteJson interpreteJson;

	/**
	 * @see net.gaia.taskprocessor.api.WorkUnit#doWork()
	 */
	@Override
	public void doWork() throws InterruptedException {
		final ClienteVortex clienteVortex = VortexClimateMain.I.getClienteVortex();
		final String datosComoJson = interpreteJson.toJson(measure);
		final MensajeVortexApi mensajeDeUpdate = MensajeVortexApi.create(datosComoJson, "vortex.climate.measure",
				"VORTEX.CLIMATE.BUENOS_AIRES.UPDATE_EVENT");
		clienteVortex.enviar(mensajeDeUpdate);
		LOG.debug("Datos enviados por vortex");
	}

	public static EnviarNuevaMedicionWorkUnit create(final ClimateMeasure newMeasure) {
		final EnviarNuevaMedicionWorkUnit envio = new EnviarNuevaMedicionWorkUnit();
		envio.interpreteJson = InterpreteJackson.create();
		envio.measure = newMeasure;
		return envio;
	}
}
