/**
 * Created on: Sep 8, 2013 11:17:57 AM by: Dario L. Garcia
 * 
 * <a rel="license" href="http://creativecommons.org/licenses/by/2.5/ar/"><img
 * alt="Creative Commons License" style="border-width:0"
 * src="http://i.creativecommons.org/l/by/2.5/ar/88x31.png" /></a><br />
 * <span xmlns:dc="http://purl.org/dc/elements/1.1/"
 * href="http://purl.org/dc/dcmitype/InteractiveResource" property="dc:title"
 * rel="dc:type">Bean2Bean</span> by <a xmlns:cc="http://creativecommons.org/ns#"
 * href="https://sourceforge.net/projects/bean2bean/" property="cc:attributionName"
 * rel="cc:attributionURL">Dar&#237;o Garc&#237;a</a> is licensed under a <a rel="license"
 * href="http://creativecommons.org/licenses/by/2.5/ar/">Creative Commons Attribution 2.5 Argentina
 * License</a>.<br />
 * Based on a work at <a xmlns:dc="http://purl.org/dc/elements/1.1/"
 * href="https://bean2bean.svn.sourceforge.net/svnroot/bean2bean"
 * rel="dc:source">bean2bean.svn.sourceforge.net</a>
 */
package net.gaia.vortex.impl.moleculas;

import net.gaia.vortex.api.atomos.Conector;
import net.gaia.vortex.api.basic.Receptor;
import net.gaia.vortex.api.basic.emisores.Conectable;
import net.gaia.vortex.api.builder.VortexCore;
import net.gaia.vortex.api.mensajes.MensajeVortex;
import ar.com.dgarcia.lang.strings.ToString;

/**
 * Esta clase representa la terminal desde la cual otros componentes pueden comunicarse con una
 * parte de la red
 * 
 * @author dgarcia
 */
public class MoleculaTerminal extends TerminalSupport {

	private Conector conectorSalida;
	public static final String conectorSalida_FIELD = "conectorSalida";

	/**
	 * @see net.gaia.vortex.api.basic.Receptor#recibir(net.gaia.vortex.api.mensajes.MensajeVortex)
	 */
	public void recibir(final MensajeVortex mensaje) {
		// Mandamos el mensaje directo a las otras terminales
		getMultiplexorDeCompartidos().recibir(mensaje);
	}

	public static MoleculaTerminal create(final VortexCore builder) {
		final MoleculaTerminal terminal = new MoleculaTerminal();
		terminal.inicializarCon(builder);
		terminal.conectorSalida = builder.conector();
		return terminal;
	}

	/**
	 * @see net.gaia.vortex.api.moleculas.Terminal#getSalida()
	 */
	public Conectable getSalida() {
		return conectorSalida;
	}

	/**
	 * @see net.gaia.vortex.api.moleculas.Terminal#getReceptorParaTerminales()
	 */
	public Receptor getReceptorParaTerminales() {
		// Todo lo que nos manden de otras terminales, va directo a la salida
		return conectorSalida;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return ToString.de(this).con(numeroDeInstancia_FIELD, getNumeroDeInstancia())
				.con(conectorSalida_FIELD, conectorSalida).toString();
	}
}
