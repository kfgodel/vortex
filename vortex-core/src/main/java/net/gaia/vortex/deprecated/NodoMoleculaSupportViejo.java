/**
 * 13/01/2013 21:01:46 Copyright (C) 2011 Darío L. García
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

import net.gaia.vortex.api.annotations.clases.Molecula;
import net.gaia.vortex.api.basic.Receptor;
import net.gaia.vortex.api.mensajes.MensajeVortex;
import net.gaia.vortex.core.prog.Loggers;
import ar.com.dgarcia.lang.strings.ToString;

/**
 * Esta clase es base para nodos que representan moleculas y cuyo comportamiendo se define por un
 * flujo de atomos interconectados
 * 
 * @author D. García
 */
@Molecula
@Deprecated
public class NodoMoleculaSupportViejo extends NodoSupportViejo {

	private FlujoVortexViejo flujoInterno;
	public static final String flujoInterno_FIELD = "flujoInterno";

	/**
	 * @see net.gaia.vortex.deprecated.EmisorViejo#conectarCon(net.gaia.vortex.api.basic.Receptor)
	 */

	public void conectarCon(final Receptor destino) {
		final EmisorViejo componenteSalida = flujoInterno.getSalida();
		componenteSalida.conectarCon(destino);
	}

	/**
	 * @see net.gaia.vortex.deprecated.EmisorViejo#desconectarDe(net.gaia.vortex.api.basic.Receptor)
	 */

	public void desconectarDe(final Receptor destino) {
		final EmisorViejo componenteSalida = flujoInterno.getSalida();
		componenteSalida.desconectarDe(destino);
	}

	/**
	 * @see net.gaia.vortex.api.basic.Receptor#recibir(net.gaia.vortex.api.mensajes.MensajeVortex)
	 */

	public void recibir(final MensajeVortex mensaje) {
		Loggers.ATOMOS.trace("Recibido en nodo[{}] el mensaje[{}]", this.toShortString(), mensaje);
		final Receptor componenteEntrada = flujoInterno.getEntrada();
		Loggers.ATOMOS.debug("Delegando a nodo input[{}] el mensaje[{}] desde[{}]",
				new Object[] { componenteEntrada.toShortString(), mensaje, this.toShortString() });
		componenteEntrada.recibir(mensaje);
	}

	/**
	 * Inicializa este nodo recibiendo el flujo que define el comportamiento al recibir mensajes
	 * 
	 * @param flujoInterno
	 *            El flujo con los componentes del proceso
	 */
	protected void initializeWith(final FlujoVortexViejo flujoInterno) {
		this.flujoInterno = flujoInterno;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return ToString.de(this).con(numeroDeInstancia_FIELD, getNumeroDeInstancia())
				.con(flujoInterno_FIELD, flujoInterno).toString();
	}

	/**
	 * Devuelve el componente de este nodo que sirve de entrada de los mensajes
	 * 
	 * @return El componente al que se envían todos los mensajes recibidos
	 */
	protected Receptor getEntrada() {
		return flujoInterno.getEntrada();
	}

	/**
	 * Devuelve el componente de este nodo que se encarga de las salidas de los mensajes
	 * 
	 * @return El componente de salida de los mensajes de este nodo
	 */
	protected EmisorViejo getSalida() {
		return flujoInterno.getSalida();
	}

}
