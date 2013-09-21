/**
 * Created on: Sep 21, 2013 2:19:58 PM by: Dario L. Garcia
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

import net.gaia.vortex.api.atomos.Filtro;
import net.gaia.vortex.api.basic.Receptor;
import net.gaia.vortex.api.basic.emisores.Conectable;
import net.gaia.vortex.api.builder.VortexCore;
import net.gaia.vortex.api.condiciones.Condicion;
import net.gaia.vortex.api.mensajes.MensajeVortex;
import ar.com.dgarcia.lang.strings.ToString;

/**
 * Esta clase representa la terminal sin duplicados que permite descartar los mensajes que ya
 * recibió en cualquiera de las direcciones de circulación de mensajes
 * 
 * @author dgarcia
 */
public class MoleculaTerminalSinDuplicados extends TerminalSupport {

	private Filtro filtroDesdeTerminales;
	public static final String filtroDesdeTerminales_FIELD = "filtroDesdeTerminales";

	private Filtro filtroHaciaTerminales;

	/**
	 * @see net.gaia.vortex.api.moleculas.Terminal#recibir(net.gaia.vortex.api.mensajes.MensajeVortex)
	 */
	public void recibir(final MensajeVortex mensaje) {
		filtroHaciaTerminales.recibir(mensaje);
	}

	/**
	 * @see net.gaia.vortex.api.moleculas.Terminal#getSalida()
	 */
	public Conectable getSalida() {
		return filtroDesdeTerminales;
	}

	/**
	 * @see net.gaia.vortex.api.moleculas.Terminal#getReceptorParaTerminales()
	 */
	public Receptor getReceptorParaTerminales() {
		return filtroDesdeTerminales;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return ToString.de(this).con(numeroDeInstancia_FIELD, getNumeroDeInstancia())
				.con(filtroDesdeTerminales_FIELD, filtroDesdeTerminales).toString();
	}

	public static MoleculaTerminalSinDuplicados create(final VortexCore builder) {
		final MoleculaTerminalSinDuplicados terminal = new MoleculaTerminalSinDuplicados();
		terminal.inicializarCon(builder);
		return terminal;
	}

	/**
	 * @see net.gaia.vortex.impl.moleculas.TerminalSupport#inicializarCon(net.gaia.vortex.api.builder.VortexCore)
	 */
	@Override
	protected void inicializarCon(final VortexCore builder) {
		super.inicializarCon(builder);
		// Filtramos los que mandamos a la salida sin duplicados
		filtroDesdeTerminales = builder.filtroSinMensajesDuplicados();

		// Compartimos la condicion entre los dos filtros para que tengan la misma memoria de
		// mensajes
		final Condicion condicionCompartida = filtroDesdeTerminales.getCondicion();
		this.filtroHaciaTerminales = builder.filtroDe(condicionCompartida);

		// Filtramos lo que mandamos a las otras terminales sin duplicados
		filtroHaciaTerminales.conectarCon(getMultiplexorDeCompartidos());
	}
}
