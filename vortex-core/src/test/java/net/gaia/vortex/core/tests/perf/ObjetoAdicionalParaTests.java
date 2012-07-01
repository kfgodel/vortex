/**
 * 30/06/2012 21:10:50 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.core.tests.perf;

import ar.com.dgarcia.lang.strings.ToString;

/**
 * Esta calse representa un estado adicional acompañando al mensaje modelo
 * 
 * @author D. García
 */
public class ObjetoAdicionalParaTests {

	private String valorExtra;
	public static final String valorExtra_FIELD = "valorExtra";

	private Double numeroConComa;
	public static final String numeroConComa_FIELD = "numeroConComa";

	private Boolean booleano;
	public static final String booleano_FIELD = "booleano";

	public String getValorExtra() {
		return valorExtra;
	}

	public void setValorExtra(final String valorExtra) {
		this.valorExtra = valorExtra;
	}

	public Double getNumeroConComa() {
		return numeroConComa;
	}

	public void setNumeroConComa(final Double numeroConComa) {
		this.numeroConComa = numeroConComa;
	}

	public Boolean getBooleano() {
		return booleano;
	}

	public void setBooleano(final Boolean booleano) {
		this.booleano = booleano;
	}

	public static ObjetoAdicionalParaTests create() {
		final ObjetoAdicionalParaTests adicional = new ObjetoAdicionalParaTests();
		adicional.setBooleano(true);
		adicional.setNumeroConComa(120.0);
		adicional.setValorExtra("Super extra");
		return adicional;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return ToString.de(this).con(booleano_FIELD, booleano).con(numeroConComa_FIELD, numeroConComa)
				.con(valorExtra_FIELD, valorExtra).toString();
	}

}
