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
		final DataSet current = internalReadMeasure();
		final ClimateMeasure measure = ClimateMeasure.create(current);
		return measure;
	}

	/**
	 * Lee la medición actual de la estación
	 * 
	 * @return La medición de wunderground
	 */
	private DataSet internalReadMeasure() {
		final HttpDataReaderService dataReader = new HttpDataReaderService();
		dataReader.setWeatherStation(this.internalStation);
		final DataSet current = dataReader.getCurrentData();
		return current;
	}

	/**
	 * Actualiza la medición del clima de esta estación si existe una más moderna
	 * 
	 * @return Indica si la medición fue reemplazada
	 */
	public boolean refresh() {
		final DataSet current = internalReadMeasure();
		final Date newMeasureMoment = current.getDateTime();
		if (lastMeasure != null && lastMeasure.getMeasurementMoment().equals(newMeasureMoment.getTime())) {
			// Es la misma medición, no tiene sentido reemplazarla
			return false;
		}
		lastMeasure = ClimateMeasure.create(current);
		return true;
	}

}
