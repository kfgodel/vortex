/**
 * 17/03/2012 23:14:34 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.android.pcvolume.messages;

/**
 * Esta clase representa el mensaje de cambio de volumen
 * 
 * @author D. García
 */
public class PcVolumenChange {

	private String computerId;
	private Integer volumenLevel;

	public String getComputerId() {
		return computerId;
	}

	public void setComputerId(final String computerId) {
		this.computerId = computerId;
	}

	public Integer getVolumenLevel() {
		return volumenLevel;
	}

	public void setVolumenLevel(final Integer volumenLevel) {
		this.volumenLevel = volumenLevel;
	}

	public static PcVolumenChange create(final String computerId, final Integer volumen) {
		final PcVolumenChange name = new PcVolumenChange();
		name.computerId = computerId;
		name.volumenLevel = volumen;
		return name;
	}

}
