/**
 * 13/03/2012 22:15:27 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.climate.externals;

import java.util.Date;

import de.mbenning.weather.wunderground.api.domain.DataSet;
import de.mbenning.weather.wunderground.api.domain.WeatherStation;
import de.mbenning.weather.wunderground.impl.services.HttpDataReaderService;

/**
 * Esta clase representa una estación climatica y permite conocer el estado actual del clima
 * 
 * @author D. García
 */
public class ClimateStation {

	private WeatherStation internalStation;

	private ClimateMeasure lastMeasure;

	public static ClimateStation create(final WeatherStation internalStation) {
		final ClimateStation station = new ClimateStation();
		station.internalStation = internalStation;
		return station;
	}

	/**
	 * Devuelve la última medición climática realizada
	 */
	public ClimateMeasure getLastMeasure() {
		if (lastMeasure == null) {
			lastMeasure = obtainCurrentMeasure();
		}
		return lastMeasure;
	}

	/**
	 * Busca la medición actual para esta estación
	 * 
	 * @return
	 */
	private ClimateMeasure obtainCurrentMeasure() {
		final HttpDataReaderService dataReader = new HttpDataReaderService();
		dataReader.setWeatherStation(this.internalStation);
		final DataSet current = dataReader.getCurrentData();

		final Date measurementMoment = current.getDateTime();
		final Integer humidity = current.getHumidity();
		final Double pressure = current.getPressurehPa();
		final Double temperature = current.getTemperature();
		final ClimateMeasure measure = ClimateMeasure.create(measurementMoment);
		measure.setHumidity(humidity);
		measure.setPressure(pressure);
		measure.setTemperature(temperature);
		return measure;
	}

}
