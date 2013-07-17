/**
 * 08/07/2013 11:55:32 Copyright (C) 2013 Darío L. García
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
package net.gaia.taskprocessor.perf;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import net.gaia.taskprocessor.perf.api.time.CronometroMilis;
import net.gaia.taskprocessor.perf.api.variables.EstrategiaDeVariablesPorThread;
import net.gaia.taskprocessor.perf.api.variables.VariableTicks;
import net.gaia.taskprocessor.perf.impl.medidor.MedidorDeTicksPerSecond;
import net.gaia.taskprocessor.perf.impl.tests.ThreadIteradorBrutoPorCantidad;
import net.gaia.taskprocessor.perf.impl.time.SystemMillisCronometro;
import net.gaia.taskprocessor.perf.impl.variables.estrategias.UnaVariableSinConcurrenciaPorThread;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ar.com.dgarcia.lang.conc.WaitBarrier;
import ar.com.dgarcia.lang.time.TimeMagnitude;

/**
 * Esta clase prueba la ejecución de los threads en un loop muy simple pero en
 * paralelo
 * 
 * @author D. García
 */
public class SimpleLoopMultiThreadTester {
	private static final Logger LOG = LoggerFactory
			.getLogger(SimpleLoopTester.class);

	/**
	 * Constante para un unico hilo
	 */
	public static final int _1_HILO_EJECUTANTE = 1;

	/**
	 * Constante para igual cantidad que cores hilo
	 */
	public static final int _4HILOS_EJECUTANTES = 4;

	/**
	 * Constante para mayor cantidad de hilos que cores
	 */
	public static final int _32HILOS_EJECUTANTES = 32;

	/**
	 * Cantidad sin profiler y sin concurrencia
	 */
	public static final long CANTIDAD_TICKS_SIN_CONC = SimpleLoopTester.CANTIDAD_TICKS_SIN_CONC;

	/**
	 * Cantidad sin profiler y conc windows
	 */
	public static final long CANTIDAD_TICKS_CON_CONC = SimpleLoopTester.CANTIDAD_TICKS_CON_CONC;

	/**
	 * Cantidad sin profiler con 4 hilos sin conc
	 */
	public static final long CANTIDAD_4HILOS_SIN_CONC = SimpleLoopTester.CANTIDAD_TICKS_SIN_CONC / 2;

	/**
	 * Cantidad sin profiler con 4 hilos con conc
	 */
	public static final long CANTIDAD_4HILOS_CON_CONC = CANTIDAD_4HILOS_SIN_CONC / 10;

	/**
	 * Cantidad con profiler
	 */
	// private static final long TICKS_ESTIMADOS_POR_SEG = 123456789L;

	public static void main(final String[] args) {
		Thread.currentThread().setName("<> - Principal");

		final CronometroMilis clock = SystemMillisCronometro.create();
		mostrarMensajeYEsperarInput("<ENTER> Para empezar prueba");

		final EstrategiaDeVariablesPorThread estrategiaDeVariables = UnaVariableSinConcurrenciaPorThread
				.create();
		final int cantidadDeHilos = _1_HILO_EJECUTANTE;
		final WaitBarrier esperarThreads = WaitBarrier.create(cantidadDeHilos);

		clock.reset();
		for (int i = 0; i < cantidadDeHilos; i++) {
			final VariableTicks variableParaNuevoThread = estrategiaDeVariables
					.getVariableParaNuevoThread();
			final ThreadIteradorBrutoPorCantidad hiloDisparado = ThreadIteradorBrutoPorCantidad
					.create(CANTIDAD_TICKS_SIN_CONC, variableParaNuevoThread,
							esperarThreads, i);
			hiloDisparado.start();
		}

		esperarThreads
				.waitForReleaseUpTo(TimeMagnitude.of(2, TimeUnit.MINUTES));
		clock.stop();

		LOG.info("Resultados:\n{}", MedidorDeTicksPerSecond.describirResultadosCon(clock, estrategiaDeVariables));

		mostrarMensajeYEsperarInput("<ENTER> Para terminar");
	}

	private static void mostrarMensajeYEsperarInput(final String mensaje) {
		try {
			LOG.info(mensaje);
			System.in.read();
			final int extraBytesFromEnter = System.lineSeparator().length() - 1;
			System.in.skip(extraBytesFromEnter);
		} catch (final IOException e) {
			LOG.error("Se produjo un error de IO esperando input", e);
		}
	}

}
