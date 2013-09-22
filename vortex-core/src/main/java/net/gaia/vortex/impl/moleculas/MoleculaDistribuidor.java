/**
 * Created on: Sep 8, 2013 11:16:17 AM by: Dario L. Garcia
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

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import net.gaia.vortex.api.basic.Receptor;
import net.gaia.vortex.api.mensajes.MensajeVortex;
import net.gaia.vortex.api.moleculas.Distribuidor;
import net.gaia.vortex.api.moleculas.Terminal;
import net.gaia.vortex.impl.moleculas.terminales.TerminalFactory;
import net.gaia.vortex.impl.support.ReceptorSupport;
import ar.com.dgarcia.lang.strings.ToString;

/**
 * Esta clase representa el distribuidor de mensajes que utiliza multiplexores internos conectados a
 * los terminales para discriminar los mensajes recibidos
 * 
 * @author dgarcia
 */
public class MoleculaDistribuidor extends ReceptorSupport implements Distribuidor {

	private TerminalFactory builder;

	private List<Terminal> terminales;
	public static final String terminales_FIELD = "terminales";

	/**
	 * @see net.gaia.vortex.api.basic.Receptor#recibir(net.gaia.vortex.api.mensajes.MensajeVortex)
	 */
	public void recibir(final MensajeVortex mensaje) {
		// Si no conocemos el origen se lo mandamos a todos
		for (final Terminal terminal : terminales) {
			terminal.getReceptorParaTerminales().recibir(mensaje);
		}
	}

	/**
	 * @see net.gaia.vortex.api.moleculas.Distribuidor#crearTerminal()
	 */
	public Terminal crearTerminal() {
		final Terminal nuevaTerminal = builder.crearTerminal();
		for (final Terminal terminalExistente : terminales) {
			nuevaTerminal.compartirMensajesCon(terminalExistente);
			terminalExistente.compartirMensajesCon(nuevaTerminal);
		}
		terminales.add(nuevaTerminal);
		return nuevaTerminal;
	}

	/**
	 * @see net.gaia.vortex.api.moleculas.Distribuidor#eliminarTerminal(net.gaia.vortex.api.moleculas.Terminal)
	 */
	public void eliminarTerminal(final Terminal eliminable) {
		terminales.remove(eliminable);
		for (final Terminal terminalMantenida : terminales) {
			eliminable.descompartirMensajesA(terminalMantenida);
			terminalMantenida.descompartirMensajesA(eliminable);
		}
	}

	public static MoleculaDistribuidor create(final TerminalFactory builder) {
		final MoleculaDistribuidor distribuidor = new MoleculaDistribuidor();
		distribuidor.builder = builder;
		distribuidor.terminales = new CopyOnWriteArrayList<Terminal>();
		return distribuidor;
	}

	/**
	 * @see net.gaia.vortex.impl.support.ComponenteSupport#toString()
	 */
	@Override
	public String toString() {
		return ToString.de(this).con(numeroDeInstancia_FIELD, getNumeroDeInstancia()).con(terminales_FIELD, terminales)
				.toString();
	}

	/**
	 * @see net.gaia.vortex.api.moleculas.Distribuidor#conectarCon(net.gaia.vortex.api.basic.Receptor)
	 */
	public void conectarCon(final Receptor destino) {
		final Terminal terminalCreada = crearTerminal();
		terminalCreada.conectarCon(destino);
	}

	/**
	 * @see net.gaia.vortex.api.moleculas.Distribuidor#desconectar()
	 */
	public void desconectar() {
		final List<Terminal> eliminables = new ArrayList<Terminal>(terminales);
		for (final Terminal terminalEliminable : eliminables) {
			terminalEliminable.desconectar();
			eliminarTerminal(terminalEliminable);
		}
	}

	/**
	 * @see net.gaia.vortex.api.moleculas.Distribuidor#desconectarDe(net.gaia.vortex.api.basic.Receptor)
	 */
	public void desconectarDe(final Receptor destino) {
		final Terminal terminal = getTerminalConectadaA(destino);
		if (terminal == null) {
			// No estaba conectada a ninguna terminal, nada que desconectar
			return;
		}
		eliminarTerminal(terminal);
	}

	/**
	 * @see net.gaia.vortex.api.moleculas.Distribuidor#getConectados()
	 */
	public List<Receptor> getConectados() {
		final List<Receptor> conectados = new ArrayList<Receptor>(terminales.size());
		for (final Terminal terminal : terminales) {
			final List<Receptor> conectadosATerminal = terminal.getConectados();
			conectados.addAll(conectadosATerminal);
		}
		return conectados;
	}

	/**
	 * @see net.gaia.vortex.api.moleculas.Distribuidor#getTerminalConectadaA(net.gaia.vortex.api.basic.Receptor)
	 */
	public Terminal getTerminalConectadaA(final Receptor receptor) {
		for (final Terminal terminalConectada : terminales) {
			final List<Receptor> receptoresConectados = terminalConectada.getConectados();
			for (final Receptor receptorConectado : receptoresConectados) {
				if (receptorConectado.equals(receptor)) {
					// Es la terminal que buscabamos
					return terminalConectada;
				}
			}
		}
		return null;
	}
}
