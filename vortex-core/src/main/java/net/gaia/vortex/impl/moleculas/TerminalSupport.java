/**
 * Created on: Sep 21, 2013 2:25:21 PM by: Dario L. Garcia
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
import net.gaia.vortex.api.basic.Receptor;
import net.gaia.vortex.api.basic.emisores.Conectable;
import net.gaia.vortex.api.builder.VortexCore;
import net.gaia.vortex.api.moleculas.Terminal;
import net.gaia.vortex.impl.nulos.ReceptorNulo;
import net.gaia.vortex.impl.support.ConectableIndirectamenteSupport;

/**
 * Esta clase sirve de base para las implementaciones de terminal que utilizan un multiplexor como
 * distribuidor principal de los mensajes para otras terminales
 * 
 * @author dgarcia
 */
public abstract class TerminalSupport extends ConectableIndirectamenteSupport<Conectable> implements Terminal {

	private Multiplexor multiplexorDeCompartidos;
	public static final String multiplexorDeCompartidos_FIELD = "multiplexorDeCompartidos";

	/**
	 * @see net.gaia.vortex.api.moleculas.Terminal#compartirMensajesCon(net.gaia.vortex.api.moleculas.Terminal)
	 */
	public void compartirMensajesCon(final Terminal otraTerminal) {
		final Receptor receptorDeOtraTerminal = otraTerminal.getReceptorParaTerminales();
		this.multiplexorDeCompartidos.conectarCon(receptorDeOtraTerminal);
	}

	/**
	 * @see net.gaia.vortex.api.moleculas.Terminal#descompartirMensajesA(net.gaia.vortex.api.moleculas.Terminal)
	 */
	public void descompartirMensajesA(final Terminal otraTerminal) {
		final Receptor receptorDeOtraTerminal = otraTerminal.getReceptorParaTerminales();
		multiplexorDeCompartidos.desconectarDe(receptorDeOtraTerminal);
	}

	/**
	 * Devuelve el multiplexor utilizado para enviar los mensajes a otras terminales
	 * 
	 * @return
	 */
	public Multiplexor getMultiplexorDeCompartidos() {
		return multiplexorDeCompartidos;
	}

	/**
	 * Inicializa el multiplexor interno
	 * 
	 * @param builder
	 *            El builder para crearlo
	 */
	protected void inicializarCon(final VortexCore builder) {
		multiplexorDeCompartidos = builder.multiplexar();
	}

	/**
	 * @see net.gaia.vortex.api.moleculas.Terminal#getConectado()
	 */
	public Receptor getConectado() {
		final List<Receptor> allConectados = getConectados();
		if (allConectados.size() != 1) {
			return ReceptorNulo.getInstancia();
		}
		final Receptor unicoConectado = allConectados.get(0);
		return unicoConectado;
	}
}