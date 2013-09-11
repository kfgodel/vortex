/**
 * 19/08/2013 21:22:48 Copyright (C) 2013 Darío L. García
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
package net.gaia.vortex.impl.flujos;

import net.gaia.vortex.api.basic.Receptor;
import net.gaia.vortex.api.basic.emisores.Conectable;
import net.gaia.vortex.api.flujos.FlujoVortex;
import ar.com.dgarcia.lang.strings.ToString;

/**
 * Esta clase implementa el flujo de manera inmutable
 * 
 * @author D. García
 */
public class FlujoInmutable implements FlujoVortex {

	private Receptor entrada;
	public static final String entrada_FIELD = "entrada";

	private Conectable salida;
	public static final String salida_FIELD = "salida";

	/**
	 * @see net.gaia.vortex.api.flujos.FlujoVortex#getEntrada()
	 */
	public Receptor getEntrada() {
		return entrada;
	}

	/**
	 * @see net.gaia.vortex.api.flujos.FlujoVortex#getSalida()
	 */
	public Conectable getSalida() {
		return salida;
	}

	public static FlujoInmutable create(final Receptor entrada, final Conectable salida) {
		final FlujoInmutable flujo = new FlujoInmutable();
		flujo.entrada = entrada;
		flujo.salida = salida;
		return flujo;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return ToString.de(this).con(entrada_FIELD, entrada).con(salida_FIELD, salida).toString();
	}

}
