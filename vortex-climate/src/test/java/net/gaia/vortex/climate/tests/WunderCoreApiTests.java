/**
 * 13/03/2012 21:35:15 Copyright (C) 2011 Darío L. García
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

import org.junit.Assert;
import org.junit.Test;

import de.mbenning.weather.wunderground.api.domain.DataSet;
import de.mbenning.weather.wunderground.api.domain.WeatherStation;
import de.mbenning.weather.wunderground.impl.services.HttpDataReaderService;
import de.mbenning.weather.wunderground.impl.services.WeatherStationService;

/**
 * Esta clase prueba el comportamiento de la api core de testeo de Wunderground
 * 
 * @author D. García
 */
public class WunderCoreApiTests {

	@Test
	public void deberiaPermitirConocerLosDatosBasicosDeBuenosAires() {
		final WeatherStationService weatherStationService = new WeatherStationService();
		final WeatherStation bueStation = weatherStationService.getWeatherStation("Argentina", "ICABABUE10");
		Assert.assertNotNull(bueStation);

		final HttpDataReaderService dataReader = new HttpDataReaderService();
		dataReader.setWeatherStation(bueStation);

		final DataSet current = dataReader.getCurrentData();
		Assert.assertNotNull(current);

		final Double currentTemperature = current.getTemperature();
		Assert.assertNotNull(currentTemperature);

		final Integer currentHumidity = current.getHumidity();
		Assert.assertNotNull(currentHumidity);

		final Double currentPressure = current.getPressurehPa();
		Assert.assertNotNull(currentPressure);

		System.out.println(currentTemperature + "º");
		System.out.println(currentPressure + "hPa");
		System.out.println(currentHumidity + "%");
	}

}
