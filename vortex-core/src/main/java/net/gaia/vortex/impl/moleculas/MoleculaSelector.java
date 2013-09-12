/**
 * Created on: Aug 31, 2013 11:09:28 PM by: Dario L. Garcia
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

import net.gaia.vortex.api.atomos.Filtro;
import net.gaia.vortex.api.atomos.Multiplexor;
import net.gaia.vortex.api.basic.Receptor;
import net.gaia.vortex.api.builder.VortexCore;
import net.gaia.vortex.api.condiciones.Condicion;
import net.gaia.vortex.api.mensajes.MensajeVortex;
import net.gaia.vortex.api.moleculas.Selector;
import net.gaia.vortex.impl.condiciones.SiempreTrue;
import net.gaia.vortex.impl.support.EmisorSupport;
import ar.com.dgarcia.lang.strings.ToString;

/**
 * Esta clase representa la molecula vortex que al recibir un mensaje selecciona un conjunto de
 * selectores en base a condiciones para entregarles el mensaje.<br>
 * 
 * @author dgarcia
 */
public class MoleculaSelector extends EmisorSupport implements Selector {

	/**
	 * Atomo de entrada que multiplexa a las salidas que estaran condicionadas
	 */
	private Multiplexor entrada;
	public static final String entrada_FIELD = "entrada";

	/**
	 * Builder de los componentes creados internamente
	 */
	private VortexCore builder;

	/**
	 * @see net.gaia.vortex.api.basic.Receptor#recibir(net.gaia.vortex.api.mensajes.MensajeVortex)
	 */
	public void recibir(final MensajeVortex mensaje) {
		this.entrada.recibir(mensaje);
	}

	/**
	 * @see net.gaia.vortex.api.basic.emisores.Conectable#conectarCon(net.gaia.vortex.api.basic.Receptor)
	 */
	public void conectarCon(final Receptor destino) {
		filtrandoCon(SiempreTrue.getInstancia()).conectarCon(destino);
	}

	/**
	 * @see net.gaia.vortex.api.basic.emisores.Conectable#desconectarDe(net.gaia.vortex.api.basic.Receptor)
	 */
	public void desconectarDe(final Receptor destino) {
		final List<Receptor> conectados = this.entrada.getConectados();
		for (final Receptor conectado : conectados) {
			if (conectado instanceof Filtro) {
				final Filtro filtro = (Filtro) conectado;
				if (filtro.getConectado().equals(destino)) {
					// Es el filtro que buscabamos
					quitarFiltro(filtro);
					return;
				}
			}
		}
	}

	/**
	 * @see net.gaia.vortex.api.basic.emisores.Conectable#desconectar()
	 */
	public void desconectar() {
		this.entrada.desconectar();
	}

	/**
	 * @see net.gaia.vortex.api.moleculas.Selector#filtrandoCon(net.gaia.vortex.api.condiciones.Condicion)
	 */
	public Filtro filtrandoCon(final Condicion condicion) {
		final Filtro filtroCreado = builder.filtroDe(condicion);
		entrada.conectarCon(filtroCreado);
		return filtroCreado;
	}

	/**
	 * @see net.gaia.vortex.api.moleculas.Selector#quitarFiltro(net.gaia.vortex.api.atomos.Filtro)
	 */
	public void quitarFiltro(final Filtro filtroCondicionado) {
		entrada.desconectarDe(filtroCondicionado);
	}

	public static MoleculaSelector create(final VortexCore core) {
		final MoleculaSelector selector = new MoleculaSelector();
		selector.builder = core;
		selector.entrada = core.multiplexar();
		return selector;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return ToString.de(this).con(numeroDeInstancia_FIELD, getNumeroDeInstancia()).con(entrada_FIELD, entrada)
				.toString();
	}

}
