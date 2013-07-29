/**
 * 05/08/2012 14:41:04 Copyright (C) 2011 Darío L. García
 * 
 * <a rel="license" href="http://creativecommons.org/licenses/by/3.0/"><img
 * alt="Creative Commons License" style="border-width:0"
 * src="http://i.creativecommons.org/l/by/3.0/88x31.png" /></a><br />
 * <span xmlns:dct="http://purl.org/dc/terms/" href="http://purl.org/dc/dcmitype/Text"
 * property="dct:title" rel="dct:type">Software</span> by <span
 * xmlns:cc="http://creativecommons.org/ns#" property="cc:attributionName">Darío García</span> is
 * licensed under a <a rel="license" href="http://creativecommons.org/licenses/by/3.0/">Creative
 * Commons Attribution 3.0 Unported License</a>.
 */
package net.gaia.taskprocessor.tests.tasks;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import junit.framework.Assert;
import net.gaia.taskprocessor.api.InterruptedThreadException;
import net.gaia.taskprocessor.api.WorkParallelizer;
import net.gaia.taskprocessor.api.WorkUnit;
import net.gaia.taskprocessor.api.processor.TaskProcessor;
import net.gaia.taskprocessor.api.processor.TaskProcessorConfiguration;
import net.gaia.taskprocessor.api.tasks.MinMaxWorkUnit;
import net.gaia.taskprocessor.forkjoin.ForkJoinTaskProcessor;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ar.com.dgarcia.lang.conc.WaitBarrier;
import ar.com.dgarcia.lang.time.TimeMagnitude;

/**
 * Esta clase prueba la ejecución de las tareas con limite máximo-mínimo
 * 
 * @author D. García
 */
public class TestMinMaxWorkUnit {
	private static final Logger LOG = LoggerFactory.getLogger(TestMinMaxWorkUnit.class);

	private TaskProcessor procesor;

	public static class TestWorkUnit implements WorkUnit {

		public List<Long> ejecuciones = new ArrayList<Long>();
		public WaitBarrier ejecutada = WaitBarrier.create();

		/**
		 * @see net.gaia.taskprocessor.api.WorkUnit#doWork()
		 */
		public void doWork(final WorkParallelizer parallelizer) throws InterruptedThreadException {
			final long currentMillis = System.currentTimeMillis();
			ejecuciones.add(currentMillis);
			LOG.debug("Tarea de test ejecutada por {}º vez: {}", ejecuciones.size(), currentMillis);
			ejecutada.release();
		}

	}

	@Before
	public void crearProcesador() {
		procesor = ForkJoinTaskProcessor.create(TaskProcessorConfiguration.createOptimun());
	}

	@After
	public void detenerProcesador() {
		procesor.detener();
	}

	@Test
	public void deberiaEjecutarseInmediatamenteLaPrimeraVez() {
		final TestWorkUnit testWorkUnit = new TestWorkUnit();
		Assert.assertEquals(0, testWorkUnit.ejecuciones.size());

		final MinMaxWorkUnit minmax = MinMaxWorkUnit.crearWrapperDe(testWorkUnit, procesor, 10000, 10000);
		procesor.process(minmax);

		// Verificamos que se ejecuta inmediatamante
		testWorkUnit.ejecutada.waitForReleaseUpTo(TimeMagnitude.of(30, TimeUnit.MILLISECONDS));
		Assert.assertEquals(1, testWorkUnit.ejecuciones.size());
	}

	@Test
	public void aunqueSePlanifiqueEjecucionPosteriorDeberiaEjecutarseAntesManualmenteLaPrimeraVez()
			throws InterruptedException {
		final TestWorkUnit testWorkUnit = new TestWorkUnit();
		Assert.assertEquals(0, testWorkUnit.ejecuciones.size());

		final MinMaxWorkUnit minmax = MinMaxWorkUnit.crearWrapperDe(testWorkUnit, procesor, 10000, 10000);
		procesor.processDelayed(TimeMagnitude.of(3, TimeUnit.SECONDS), minmax);

		// Verificamos que todavía no se ejecutó
		Thread.sleep(30);
		Assert.assertEquals(0, testWorkUnit.ejecuciones.size());

		procesor.process(minmax);
		// Verificamos que se ejecuta inmediatamante
		testWorkUnit.ejecutada.waitForReleaseUpTo(TimeMagnitude.of(30, TimeUnit.MILLISECONDS));
		Assert.assertEquals(1, testWorkUnit.ejecuciones.size());
	}

	@Test
	public void deberiarEjecutarseAutomaticamenteSiPasaElMaximoDeEspera() throws InterruptedException {
		final TestWorkUnit testWorkUnit = new TestWorkUnit();
		final MinMaxWorkUnit minmax = MinMaxWorkUnit.crearWrapperDe(testWorkUnit, procesor, 0, 1000);
		procesor.process(minmax);

		Thread.sleep(1500);

		// Verificamos que además de la primera se ejecutó una segunda vez al segundo
		Assert.assertEquals(2, testWorkUnit.ejecuciones.size());
	}

	@Test
	public void noDeberiaEjecutarseSiNoPasoElMinimoDeEspera() throws InterruptedException {
		final TestWorkUnit testWorkUnit = new TestWorkUnit();
		final MinMaxWorkUnit minmax = MinMaxWorkUnit.crearWrapperDe(testWorkUnit, procesor, 3000, 3000);
		// Ejecuta ahora
		procesor.process(minmax);

		// Intentamos una segunda ejecución manual que no tendrá efecto hasta los 3 segundos
		procesor.process(minmax);
		Thread.sleep(1500);

		// Verificamos que la segunda ejecución no tuvo efecto
		Assert.assertEquals(1, testWorkUnit.ejecuciones.size());
	}

	@Test
	public void lasInvocacionesMientrasSeEjecutaNoTienenEfectoPorQueSonCanceladasAlReplanificar()
			throws InterruptedException {
		final TestWorkUnit testWorkUnit = new TestWorkUnit();
		final MinMaxWorkUnit minmax = MinMaxWorkUnit.crearWrapperDe(testWorkUnit, procesor, 1000, 3000);
		// Ejecuta ahora
		procesor.process(minmax);

		// Intentamos una segunda ejecución manual que no tendrá efecto hasta el segundo
		procesor.process(minmax);
		Thread.sleep(500);
		// Verificamos que la segunda ejecución no tuvo efecto
		Assert.assertEquals(1, testWorkUnit.ejecuciones.size());

	}

	@Test
	public void alInvocarManualDespuesDeLaEjecucionYAntesDelMinimoSeReplanificaUnaEjecucionAutomaticamenteConElMinimoDeEspera()
			throws InterruptedException {
		final TestWorkUnit testWorkUnit = new TestWorkUnit();
		final MinMaxWorkUnit minmax = MinMaxWorkUnit.crearWrapperDe(testWorkUnit, procesor, 1000, 3000);
		// Ejecuta ahora
		procesor.process(minmax);

		testWorkUnit.ejecutada.waitForReleaseUpTo(TimeMagnitude.of(500, TimeUnit.MILLISECONDS));

		// Intentamos una segunda ejecución manual que no se ejecutará pero adelantará la ejecucion
		procesor.process(minmax);
		Thread.sleep(500);

		// Verificamos que aun no se ejecutó la segunda
		Assert.assertEquals(1, testWorkUnit.ejecuciones.size());

		Thread.sleep(600);
		// Verificamos que la segunda ejecucion se adelantó
		Assert.assertEquals(2, testWorkUnit.ejecuciones.size());
	}

	@Test
	public void deberiaEjecutarseSiYaPasoElMinimoYSeSolicitaSuEjecucionAntesDelMaximo() throws InterruptedException {
		final TestWorkUnit testWorkUnit = new TestWorkUnit();
		final MinMaxWorkUnit minmax = MinMaxWorkUnit.crearWrapperDe(testWorkUnit, procesor, 1000, 3000);
		// Ejecuta ahora
		procesor.process(minmax);

		// Esperamos que pase el minimo
		Thread.sleep(1500);
		// Intentamos una segunda ejecución manual que no tendrá efecto hasta el segundo
		procesor.process(minmax);

		// Verificamos que la segunda ejecución tuvo efecto
		Thread.sleep(30);
		Assert.assertEquals(2, testWorkUnit.ejecuciones.size());

	}

	@Test
	public void alEjecutarseManualmenteDeberiaEsperarElMaximoParaEjecutarseNuevamente() throws InterruptedException {
		final TestWorkUnit testWorkUnit = new TestWorkUnit();
		final MinMaxWorkUnit minmax = MinMaxWorkUnit.crearWrapperDe(testWorkUnit, procesor, 500, 1000);
		// Ejecuta ahora
		procesor.process(minmax);

		// Esperamos que pase el mínimo y ejecutamos manual
		Thread.sleep(600);
		procesor.process(minmax);

		// Esperamos un poco menos del máximo
		Thread.sleep(800);

		// Verificamos que aun no se ejecuto automaticamente porque se reseteo la espera
		Assert.assertEquals(2, testWorkUnit.ejecuciones.size());

		// Esperamos que se ejecute solo por auto planificacion
		Thread.sleep(300);
		// Verificamos que aun no se ejecuto automaticamente porque se reseteo la espera
		Assert.assertEquals(3, testWorkUnit.ejecuciones.size());
	}

}
