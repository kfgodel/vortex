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

import java.util.List;

import net.gaia.vortex.api.atomos.Multiplexor;
import net.gaia.vortex.api.builder.VortexCore;
import net.gaia.vortex.api.mensajes.MensajeVortex;
import net.gaia.vortex.api.moleculas.Terminal;
import net.gaia.vortex.api.proto.Conector;
import net.gaia.vortex.impl.support.MonoConectableSupport;

/**
 * Esta clase representa la terminal desde la cual otros componentes pueden comunicarse con una
 * parte de la red
 * 
 * @author dgarcia
 */
public class MoleculaTerminal extends MonoConectableSupport implements Terminal {

	private Multiplexor multiplexorRecibidos;
	public static final String multiplexorRecibidos_FIELD = "multiplexorRecibidos";

	/**
	 * @see net.gaia.vortex.api.basic.Receptor#recibir(net.gaia.vortex.api.mensajes.MensajeVortex)
	 */
	public void recibir(final MensajeVortex mensaje) {
		multiplexorRecibidos.recibir(mensaje);
	}

	public Multiplexor getMultiplexorDeRecibidos() {
		return multiplexorRecibidos;
	}

	public static MoleculaTerminal create(final VortexCore builder) {
		final MoleculaTerminal terminal = new MoleculaTerminal();
		terminal.inicializar();
		terminal.multiplexorRecibidos = builder.multiplexar();
		return terminal;
	}

	/**
	 * @see net.gaia.vortex.api.moleculas.Terminal#enviarRecibidosA(net.gaia.vortex.api.moleculas.Terminal)
	 */
	public void enviarRecibidosA(final Terminal otraTerminal) {
		this.multiplexorRecibidos.crearConector().conectarCon(otraTerminal.getConectorDeSalida());
	}

	/**
	 * @see net.gaia.vortex.api.moleculas.Terminal#noEnviarRecibidosA(net.gaia.vortex.api.moleculas.Terminal)
	 */
	public void noEnviarRecibidosA(final Terminal otraTerminal) {
		final Conector conectorDeLaTerminal = otraTerminal.getConectorDeSalida();

		final List<Conector> allConectores = this.multiplexorRecibidos.getConectores();
		for (final Conector conector : allConectores) {
			if (conector.getDestino().equals(conectorDeLaTerminal)) {
				this.multiplexorRecibidos.eliminarConector(conector);
				return;
			}
		}
	}

}
