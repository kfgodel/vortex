/**
 * 13/01/2013 19:57:38 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.deprecated;

import net.gaia.vortex.api.basic.Receptor;
import ar.com.dgarcia.lang.strings.ToString;

/**
 * Esta clase es la implementación del {@link FlujoVortexViejo} que se crea con los componentes de
 * entrada y salida ya definidos y no pueden ser cambiados
 * 
 * @author D. García
 */
@Deprecated
public class FlujoInmutableViejo implements FlujoVortexViejo {

	private Receptor entrada;
	public static final String entrada_FIELD = "entrada";

	private EmisorViejo salida;
	public static final String salida_FIELD = "salida";

	/**
	 * @see net.gaia.vortex.deprecated.FlujoVortexViejo#getEntrada()
	 */

	public Receptor getEntrada() {
		return entrada;
	}

	/**
	 * @see net.gaia.vortex.deprecated.FlujoVortexViejo#getSalida()
	 */

	public EmisorViejo getSalida() {
		return salida;
	}

	public static FlujoInmutableViejo create(final Receptor componenteDeEntrada, final EmisorViejo componenteDeSalida) {
		final FlujoInmutableViejo flujo = new FlujoInmutableViejo();
		flujo.entrada = componenteDeEntrada;
		flujo.salida = componenteDeSalida;
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
