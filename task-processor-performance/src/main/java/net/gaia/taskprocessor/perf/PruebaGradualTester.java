/**
 * 16/07/2013 21:03:17 Copyright (C) 2013 Darío L. García
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

import net.gaia.taskprocessor.perf.api.time.CronometroMilis;
import net.gaia.taskprocessor.perf.api.variables.EstrategiaDeVariablesPorThread;
import net.gaia.taskprocessor.perf.api.variables.VariableTicks;
import net.gaia.taskprocessor.perf.impl.medidor.MedidorDeTicksPerSecond;
import net.gaia.taskprocessor.perf.impl.tests.ThreadIncrementadorBruto;
import net.gaia.taskprocessor.perf.impl.time.SystemMillisCronometro;
import net.gaia.taskprocessor.perf.impl.variables.estrategias.UnaVariableSinConcurrenciaPorThread;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * En esta clase voy intentando agregar los elementos
 * 
 * @author D. García
 */
public class PruebaGradualTester {
	private static final Logger LOG = LoggerFactory.getLogger(PruebaGradualTester.class);

	public static void main(final String[] args) throws InterruptedException {
		Thread.currentThread().setName("<> - Principal");

		final CronometroMilis clock = SystemMillisCronometro.create();
		mostrarMensajeYEsperarInput("<ENTER> Para empezar prueba");

		final EstrategiaDeVariablesPorThread estrategiaDeVariables = UnaVariableSinConcurrenciaPorThread.create();
		final VariableTicks variableDelThread = estrategiaDeVariables.getVariableParaNuevoThread();
		clock.reset();

		final ThreadIncrementadorBruto threadEjecutor = ThreadIncrementadorBruto.create(variableDelThread, 0);
		threadEjecutor.ejecutar();
		Thread.sleep(15000);

		threadEjecutor.detener();

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
