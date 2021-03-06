/**
 * Created on: Sep 21, 2013 2:14:09 PM by: Dario L. Garcia
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
package net.gaia.vortex.core.tests.perf;

import net.gaia.vortex.api.flujos.FlujoVortex;
import net.gaia.vortex.api.moleculas.Distribuidor;
import net.gaia.vortex.api.moleculas.Terminal;
import net.gaia.vortex.impl.flujos.FlujoInmutable;

/**
 * Esta clase prueba la performance del distribuidor de mensajes sin duplicados utilizado desde sus
 * terminales (el uso tipico)
 * 
 * @author dgarcia
 */
public class TestDePerformanceDistribuidorTerminalSinDuplicados extends TestDePerformanceNodoSupport {

	/**
	 * @see net.gaia.vortex.core.tests.perf.TestDePerformanceNodoSupport#crearFlujoATestear()
	 */
	@Override
	protected FlujoVortex crearFlujoATestear() {
		final Distribuidor distribuidor = getBuilder().distribuidorSinDuplicados();
		final Terminal terminalEnvio = distribuidor.crearTerminal();
		final Terminal terminalRecepcion = distribuidor.crearTerminal();
		return FlujoInmutable.create(terminalEnvio, terminalRecepcion);
	}

}
