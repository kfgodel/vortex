/**
 * 13/03/2012 22:14:14 Copyright (C) 2011 Darío L. García
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

/**
 * Este enum representa las estaciones metereológicas conocidas y usadas en el código
 * 
 * @author D. García
 */
public enum KnownClimateStation {

	/**
	 * La estación que registra la info de Buenos Aires
	 */
	BUENOS_AIRES("Argentina", "ICABABUE10");

	String stationId;
	String countryName;

	private KnownClimateStation(final String countryName, final String stationId) {
		this.stationId = stationId;
		this.countryName = countryName;
	}

	/**
	 * Devuelve el identificador del país de la estación
	 */
	public String getCountryName() {
		return countryName;
	}

	/**
	 * Devuelve el identificador de la estación
	 * 
	 * @return
	 */
	public String getStationId() {
		return stationId;
	}

}
