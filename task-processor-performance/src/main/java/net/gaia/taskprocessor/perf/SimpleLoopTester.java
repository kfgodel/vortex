/**
 * 08/07/2013 01:46:32 Copyright (C) 2013 Darío L. García
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

import net.gaia.taskprocessor.perf.api.VariableTicks;
import net.gaia.taskprocessor.perf.api.time.CronometroMilis;
import net.gaia.taskprocessor.perf.impl.time.SystemMillisCronometro;
import net.gaia.taskprocessor.perf.impl.variables.VariableTicksConcurrente;
import net.gaia.taskprocessor.perf.impl.variables.VariableTicksSinConcurrencia;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Esta clase prueba la velocidad de ejecucion de un unico thread en un for sin ningun elemento de
 * sync
 * 
 * @author D. García
 */
public class SimpleLoopTester {
	private static final Logger LOG = LoggerFactory.getLogger(SimpleLoopTester.class);

	/**
	 * Cantidad sin profiler
	 */
	private static final long TICKS_ESTIMADOS_POR_SEG = 16747700414L;

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
		for (long i = 0; i < TICKS_ESTIMADOS_POR_SEG; i++) {
			variable.incrementar();
		}
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
			int extraBytesFromEnter = System.lineSeparator().length() - 1;
			System.in.skip(extraBytesFromEnter);
		} catch (final IOException e) {
			LOG.error("Se produjo un error de IO esperando input", e);
		}
	}

}
