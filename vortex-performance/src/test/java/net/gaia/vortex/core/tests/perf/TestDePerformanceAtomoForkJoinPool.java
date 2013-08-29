/**
 * Created on: Aug 28, 2013 7:59:33 PM by: Dario L. Garcia
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

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinPool.ForkJoinWorkerThreadFactory;
import java.util.concurrent.RecursiveAction;

import net.gaia.vortex.api.basic.Receptor;
import net.gaia.vortex.api.flujos.FlujoVortex;
import net.gaia.vortex.core.api.mensaje.MensajeVortex;
import net.gaia.vortex.impl.flujos.FlujoInmutable;
import net.gaia.vortex.impl.proto.ComponenteConector;
import net.gaia.vortex.impl.support.ReceptorSupport;

import org.junit.After;
import org.junit.Before;

/**
 * Esta clase intenta probar la velocidad de procesamiento de tareas de un {@link ForkJoinPool} como
 * atomo
 * 
 * @author dgarcia
 */
public class TestDePerformanceAtomoForkJoinPool extends TestDePerformanceNodoSupport {

	private ForkJoinPool pool;

	@Before
	public void crearPool() {
		final ForkJoinWorkerThreadFactory threadFactory = ForkJoinPool.defaultForkJoinWorkerThreadFactory;
		pool = new ForkJoinPool(4, threadFactory, null, true);
	}

	@After
	public void eliminarPool() {
		pool.shutdownNow();
	}

	/**
	 * @see net.gaia.vortex.core.tests.perf.TestDePerformanceNodoSupport#crearFlujoATestear()
	 */
	@Override
	protected FlujoVortex crearFlujoATestear() {
		final ComponenteConector conector = ComponenteConector.create();
		final Receptor receptorDelPool = new ReceptorSupport() {
			@SuppressWarnings("serial")
			public void recibir(final MensajeVortex mensaje) {
				pool.submit(new RecursiveAction() {
					@Override
					protected void compute() {
						conector.recibir(mensaje);
					}
				});
			}
		};
		final FlujoInmutable flujoATestear = FlujoInmutable.create(receptorDelPool, conector);
		return flujoATestear;
	}
}
