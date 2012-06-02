/**
 * 13/03/2012 22:09:37 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.climate.tests;

import junit.framework.Assert;
import net.gaia.vortex.climate.externals.ClimateMeasure;
import net.gaia.vortex.climate.externals.ClimateServer;
import net.gaia.vortex.climate.externals.ClimateStation;
import net.gaia.vortex.climate.externals.KnownClimateStation;

import org.junit.Test;

/**
 * Esta clase testea la api de obtención de la información climática
 * 
 * @author D. García
 */
public class ClimateServerApiTest {

	@Test
	public void deberiaPermitirConocerLosdDatosClimaticosBasicosDeBuenosAires() {
		final ClimateServer server = ClimateServer.create();

		final ClimateStation station = server.getStation(KnownClimateStation.BUENOS_AIRES);
		Assert.assertNotNull(station);

		final ClimateMeasure lastMeasure = station.getLastMeasure();
		Assert.assertNotNull(lastMeasure);

		final Long measurementMoment = lastMeasure.getMeasurementMoment();
		Assert.assertNotNull(measurementMoment);

		final Double currentTemperature = lastMeasure.getTemperature();
		Assert.assertNotNull(currentTemperature);

		final Integer currentHumidity = lastMeasure.getHumidity();
		Assert.assertNotNull(currentHumidity);

		final Double currentPressure = lastMeasure.getPressure();
		Assert.assertNotNull(currentPressure);
	}

	@Test
	public void deberiaPermitirActualizarLaUltimaMedicion() {
		final ClimateServer server = ClimateServer.create();
		final ClimateStation station = server.getStation(KnownClimateStation.BUENOS_AIRES);
		final boolean replaced = station.refresh();
		// La primera vez siempre reemplaza
		Assert.assertTrue(replaced);
	}
}
