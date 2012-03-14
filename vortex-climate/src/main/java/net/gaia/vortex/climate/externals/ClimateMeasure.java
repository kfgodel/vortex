/**
 * 13/03/2012 22:17:18 Copyright (C) 2011 Darío L. García
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

/**
 * Esta clase representa una medición metereológica con variables climáticas y sus valores
 * 
 * @author D. García
 */
public class ClimateMeasure {

	private Date measurementMoment;
	private Double temperature;
	private Integer humidity;
	private Double pressure;

	/**
	 * Devuelve el momento de medición de esta medida
	 * 
	 * @return
	 */
	public Date getMeasurementMoment() {
		return measurementMoment;
	}

	/**
	 * Devuelve la temperatura medida
	 * 
	 * @return
	 */
	public Double getTemperature() {
		return temperature;
	}

	/**
	 * Devuelve la humedad medida
	 * 
	 * @return
	 */
	public Integer getHumidity() {
		return humidity;
	}

	/**
	 * Devuelve la presión medida en hpa
	 * 
	 * @return
	 */
	public Double getPressure() {
		return pressure;
	}

	public static ClimateMeasure create(final Date measurementMoment) {
		final ClimateMeasure measure = new ClimateMeasure();
		measure.measurementMoment = measurementMoment;
		return measure;
	}

	public void setMeasurementMoment(final Date measurementMoment) {
		this.measurementMoment = measurementMoment;
	}

	public void setTemperature(final Double temperature) {
		this.temperature = temperature;
	}

	public void setHumidity(final Integer humidity) {
		this.humidity = humidity;
	}

	public void setPressure(final Double pressure) {
		this.pressure = pressure;
	}

}
