/**
 * 27/06/2012 21:24:09 Copyright (C) 2011 Darío L. García
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
package ar.com.dgarcia.lang.conc;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import ar.com.dgarcia.lang.time.TimeMagnitude;

/**
 * Esta clase prueba el funcionamiento del coordinador de escrituras y lecturas
 * 
 * @author D. García
 */
public class TestReadWriteCoordinator {

	private ReadWriteCoordinator coordinator;

	public static class TestThread extends Thread {
		private final ReadWriteCoordinator coordinador;
		private final WaitBarrier notificarComienzo;
		private final WaitBarrier barreraDeFin;
		public AtomicInteger cantidadDeOperaciones;
		private final boolean esLectura;

		public TestThread(final boolean esLectura, final WaitBarrier avisarAPrincipal,
				final ReadWriteCoordinator permisoParaEmpezar) {
			this.esLectura = esLectura;
			this.coordinador = permisoParaEmpezar;
			this.notificarComienzo = avisarAPrincipal;
			this.barreraDeFin = WaitBarrier.create();
			this.cantidadDeOperaciones = new AtomicInteger();
		}

		private final Callable<Object> operation = new Callable<Object>() {
			
			public Object call() throws Exception {
				notificarComienzo.release();
				cantidadDeOperaciones.incrementAndGet();
				barreraDeFin.waitForReleaseUpTo(TimeMagnitude.of(20, TimeUnit.SECONDS));
				return null;
			}
		};

		/**
		 * @see java.lang.Thread#run()
		 */
		
		public void run() {
			if (esLectura) {
				coordinador.doReadOperation(operation);
			} else {
				coordinador.doWriteOperation(operation);
			}
		}

		public void terminar() {
			barreraDeFin.release();
		}
	}

	@Before
	public void crearDependencias() {
		coordinator = ReadWriteCoordinator.create();
	}

	@Test
	public void dosThreadDeLecturaDeberíanPoderEjecutarConcurrentemente() throws InterruptedException {
		final WaitBarrier esperarComienzoDeAmbos = WaitBarrier.create(2);
		final TestThread lector1 = new TestThread(true, esperarComienzoDeAmbos, coordinator);
		final TestThread lector2 = new TestThread(true, esperarComienzoDeAmbos, coordinator);
		lector1.start();
		lector2.start();

		esperarComienzoDeAmbos.waitForReleaseUpTo(TimeMagnitude.of(1, TimeUnit.SECONDS));

		lector1.terminar();
		lector2.terminar();
	}

	@Test
	public void dosThreadDeEscrituraDeberíanExcluirseMutuamente() throws InterruptedException {
		final WaitBarrier esperarComienzoDeAlguno = WaitBarrier.create(1);
		final TestThread escritor1 = new TestThread(false, esperarComienzoDeAlguno, coordinator);
		final TestThread escritor2 = new TestThread(false, esperarComienzoDeAlguno, coordinator);
		escritor1.start();
		escritor2.start();

		esperarComienzoDeAlguno.waitForReleaseUpTo(TimeMagnitude.of(1, TimeUnit.SECONDS));
		// Esperamos para confirmar que el otro thread no pudo hacer nada hasta que no liberamos al
		// primero
		Thread.sleep(2000);

		final int operacionesRealizadas = escritor1.cantidadDeOperaciones.get() + escritor2.cantidadDeOperaciones.get();
		Assert.assertEquals("Debería existir una sola escritura", 1, operacionesRealizadas);

		escritor1.terminar();
		escritor2.terminar();
	}

	@Test
	public void mientrasEstaLeyendoNoDeberiaPermitirEscritura() throws InterruptedException {
		final WaitBarrier esperarComienzoDeAlguno = WaitBarrier.create(1);
		final TestThread lector1 = new TestThread(true, esperarComienzoDeAlguno, coordinator);
		lector1.start();

		esperarComienzoDeAlguno.waitForReleaseUpTo(TimeMagnitude.of(1, TimeUnit.SECONDS));

		final TestThread escritor2 = new TestThread(false, esperarComienzoDeAlguno, coordinator);
		escritor2.start();
		// Esperamos para confirmar que el otro thread no pudo hacer nada hasta que no liberamos al
		// primero
		Thread.sleep(2000);

		Assert.assertEquals("Debería existir una sola lectura", 1, lector1.cantidadDeOperaciones.get());
		Assert.assertEquals("No debería existir escritura", 0, escritor2.cantidadDeOperaciones.get());

		lector1.terminar();
		escritor2.terminar();

	}

	@Test
	public void mientrasEstaEscribiendoNoDeberiPermitirLectura() throws InterruptedException {
		final WaitBarrier esperarComienzoDeAlguno = WaitBarrier.create(1);
		final TestThread escritor1 = new TestThread(true, esperarComienzoDeAlguno, coordinator);
		escritor1.start();

		esperarComienzoDeAlguno.waitForReleaseUpTo(TimeMagnitude.of(1, TimeUnit.SECONDS));

		final TestThread lector2 = new TestThread(false, esperarComienzoDeAlguno, coordinator);
		lector2.start();
		// Esperamos para confirmar que el otro thread no pudo hacer nada hasta que no liberamos al
		// primero
		Thread.sleep(2000);

		Assert.assertEquals("Debería existir una sola escritura", 1, escritor1.cantidadDeOperaciones.get());
		Assert.assertEquals("No debería existir lectura", 0, lector2.cantidadDeOperaciones.get());

		escritor1.terminar();
		lector2.terminar();
	}

}
