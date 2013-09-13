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
import net.gaia.vortex.api.mensajes.MensajeVortex;
import net.gaia.vortex.impl.flujos.FlujoInmutable;
import net.gaia.vortex.impl.proto.ConectorSupport;

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
	 * Tarea agregada al pool cada vez que un hilo de test manda un mensaje
	 * 
	 * @author dgarcia
	 */
	@SuppressWarnings("serial")
	public static class TareaDelPool extends RecursiveAction {
		private Receptor receptorFinal;
		private MensajeVortex mensaje;

		@Override
		protected void compute() {
			receptorFinal.recibir(mensaje);
		}

		public static TareaDelPool create(final Receptor receptor, final MensajeVortex mensaje) {
			final TareaDelPool tarea = new TareaDelPool();
			tarea.mensaje = mensaje;
			tarea.receptorFinal = receptor;
			return tarea;
		}
	}

	/**
	 * Pseudo conector para derivar las tareas al pool
	 * 
	 * @author dgarcia
	 */
	public static class ConectorParaElTestDePool extends ConectorSupport {

		private ForkJoinPool pool;

		/**
		 * @see net.gaia.vortex.api.basic.Receptor#recibir(net.gaia.vortex.api.mensajes.MensajeVortex)
		 */
		public void recibir(final MensajeVortex mensaje) {
			pool.submit(TareaDelPool.create(getConectado(), mensaje));
		}

		public static ConectorParaElTestDePool create(final ForkJoinPool pool) {
			final ConectorParaElTestDePool conector = new ConectorParaElTestDePool();
			conector.pool = pool;
			return conector;
		}
	}

	/**
	 * @see net.gaia.vortex.core.tests.perf.TestDePerformanceNodoSupport#crearFlujoATestear()
	 */
	@Override
	protected FlujoVortex crearFlujoATestear() {
		final ConectorParaElTestDePool conector = ConectorParaElTestDePool.create(pool);
		final FlujoInmutable flujoATestear = FlujoInmutable.create(conector, conector);
		return flujoATestear;
	}
}
