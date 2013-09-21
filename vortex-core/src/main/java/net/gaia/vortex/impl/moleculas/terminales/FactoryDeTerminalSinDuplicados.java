/**
 * Created on: Sep 21, 2013 2:15:27 PM by: Dario L. Garcia
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
package net.gaia.vortex.impl.moleculas.terminales;

import net.gaia.vortex.api.builder.VortexCore;
import net.gaia.vortex.api.moleculas.Terminal;
import ar.com.dgarcia.lang.strings.ToString;

/**
 * Esta clase implementa la factory de terminales sin duplicados, que permite descartar los mensajes
 * ya recibidos
 * 
 * @author dgarcia
 */
public class FactoryDeTerminalSinDuplicados implements TerminalFactory {

	private VortexCore builder;

	/**
	 * @see net.gaia.vortex.impl.moleculas.terminales.TerminalFactory#crearTerminal()
	 */
	public Terminal crearTerminal() {
		return builder.terminalSinDuplicados();
	}

	public static FactoryDeTerminalSinDuplicados create(final VortexCore builder) {
		final FactoryDeTerminalSinDuplicados factory = new FactoryDeTerminalSinDuplicados();
		factory.builder = builder;
		return factory;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return ToString.de(this).toString();
	}

}
