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

import net.gaia.taskprocessor.perf.api.VariableTicks;
import net.gaia.taskprocessor.perf.api.time.CronometroMilis;
import net.gaia.taskprocessor.perf.impl.tests.ThreadIteradorBrutoPorCantidad;
import net.gaia.taskprocessor.perf.impl.time.SystemMillisCronometro;
import net.gaia.taskprocessor.perf.impl.variables.VariableTicksSinConcurrencia;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ar.com.dgarcia.lang.conc.WaitBarrier;
import ar.com.dgarcia.lang.time.TimeMagnitude;

/**
 * Esta clase prueba la ejecución de los threads en un loop muy simple pero en paralelo
 * 
 * @author D. García
 */
public class SimpleLoopMultiThreadTester {
	private static final Logger LOG = LoggerFactory.getLogger(SimpleLoopTester.class);

	/**
	 * Cantidad de hilos disparados
	 */
	private static final int HILOS_EJECUTANTES = 1;

	/**
	 * Cantidad sin profiler y sin conc
	 */
	private static final long TICKS_ESTIMADOS_POR_SEG = 16747700414L;

	/**
	 * Cantidad sin profiler y conc con 1X
	 */
	// private static final long TICKS_ESTIMADOS_POR_SEG = 16747700414L / 10;

	/**
	 * Cantidad sin profiler con 4 hilos sin conc
	 */
	// private static final long TICKS_ESTIMADOS_POR_SEG = 16747700414L / 40;

	/**
	 * Cantidad sin profiler con 4 hilos con conc
	 */
	// private static final long TICKS_ESTIMADOS_POR_SEG = 16747700414L / 400;

	/**
	 * Cantidad con profiler
	 */
	// private static final long TICKS_ESTIMADOS_POR_SEG = 123456789L;

	public static void main(final String[] args) {
		Thread.currentThread().setName("<> - Principal");

		final CronometroMilis clock = SystemMillisCronometro.create();
		mostrarMensajeYEsperarInput("<ENTER> Para empezar prueba");
		clock.reset();

		final VariableTicks variable = VariableTicksSinConcurrencia.create();
		final WaitBarrier esperarThreads = WaitBarrier.create(HILOS_EJECUTANTES);
		for (int i = 0; i < HILOS_EJECUTANTES; i++) {
			final ThreadIteradorBrutoPorCantidad hiloDisparado = ThreadIteradorBrutoPorCantidad.create(
					TICKS_ESTIMADOS_POR_SEG, variable, esperarThreads, i);
			hiloDisparado.start();
		}

		esperarThreads.waitForReleaseUpTo(TimeMagnitude.of(2, TimeUnit.MINUTES));
		clock.stop();

		final long cantidadDeTicksTotal = variable.getCantidadActual();
		final double milisTotales = clock.getTotalMilis();
		final double ticksPerMilis = cantidadDeTicksTotal / milisTotales;
		LOG.info("Ticks totales: {} segs: {} s", cantidadDeTicksTotal, milisTotales / 1000d);
		LOG.info("Ticks Per Milis: {}", ticksPerMilis);
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
