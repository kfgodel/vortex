package net.gaia.vortex.climate.externals;

import de.mbenning.weather.wunderground.api.domain.WeatherStation;
import de.mbenning.weather.wunderground.impl.services.WeatherStationService;

/**
 * 13/03/2012 22:08:45 Copyright (C) 2011 Darío L. García
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

/**
 * Esta clase representa el servidor de climas del cual se obtiene el estado climatico
 * 
 * @author D. García
 */
public class ClimateServer {

	public static ClimateServer create() {
		final ClimateServer server = new ClimateServer();
		return server;
	}

	/**
	 * Devuelve la estación climática indicada por enum
	 * 
	 * @param knownStation
	 *            La estación conocida
	 * @return La estación referida
	 */
	public ClimateStation getStation(final KnownClimateStation knownStation) {
		final WeatherStationService weatherStationService = new WeatherStationService();
		final String countryName = knownStation.getCountryName();
		final String stationId = knownStation.getStationId();
		final WeatherStation internalStation = weatherStationService.getWeatherStation(countryName, stationId);
		final ClimateStation climateStation = ClimateStation.create(internalStation);
		return climateStation;
	}
}
