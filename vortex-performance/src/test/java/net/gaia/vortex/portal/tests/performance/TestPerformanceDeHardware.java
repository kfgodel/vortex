/**
 * Created on: Sep 13, 2013 9:15:17 PM by: Dario L. Garcia
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
package net.gaia.vortex.portal.tests.performance;

import java.util.List;

import net.gaia.vortex.impl.builder.VortexCoreBuilder;
import net.gaia.vortex.impl.builder.VortexPortalBuilder;
import net.gaia.vortex.portal.tests.VortexHardwareTester;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Esta clase prueba la capacidad de procesamiendo de la maquina donde es ejecutado
 * 
 * @author dgarcia
 */
public class TestPerformanceDeHardware {
	private static final Logger LOG = LoggerFactory.getLogger(TestPerformanceDeHardware.class);

	private VortexHardwareTester tester;

	@Before
	public void crearProcesador() {
		tester = VortexHardwareTester.create(VortexPortalBuilder.create(VortexCoreBuilder.create(null)));
	}

	@Test
	public void medirPerformanceCon1Thread() throws InterruptedException {
		final List<Long> mediciones = tester.medirPerformanceEnMensajesPorMilisegundo();
		LOG.info("Con un thread: {} msg/ms", mediciones.get(0));
		LOG.info("Tantos threads como cores: {} msg/ms", mediciones.get(1));
	}

}
