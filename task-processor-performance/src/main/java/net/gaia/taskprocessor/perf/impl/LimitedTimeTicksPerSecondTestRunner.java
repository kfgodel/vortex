/**
 * 07/07/2013 15:01:27 Copyright (C) 2013 Darío L. García
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
package net.gaia.taskprocessor.perf.impl;

import java.io.IOException;

import net.gaia.taskprocessor.perf.api.TicksPerSecondTestRunner;
import net.gaia.taskprocessor.perf.api.TicksPerSecondTestUnit;
import net.gaia.taskprocessor.perf.api.VariableTicks;
import net.gaia.taskprocessor.perf.impl.medidor.MedidorDeTicksPerSecond;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ar.com.dgarcia.lang.time.TimeMagnitude;

/**
 * Esta clase implementa el runner de la manera mas simple que se me ocurre
 * 
 * @author D. García
 */
public class LimitedTimeTicksPerSecondTestRunner implements TicksPerSecondTestRunner {
	private static final Logger LOG = LoggerFactory.getLogger(LimitedTimeTicksPerSecondTestRunner.class);

	private VariableTicks variable;

	private TimeMagnitude runningTime;

	public static LimitedTimeTicksPerSecondTestRunner create(final VariableTicks variable,
			final TimeMagnitude limitedTime) {
		final LimitedTimeTicksPerSecondTestRunner runner = new LimitedTimeTicksPerSecondTestRunner();
		runner.variable = variable;
		runner.runningTime = limitedTime;
		return runner;
	}

	/**
	 * @see net.gaia.taskprocessor.perf.api.TicksPerSecondTestRunner#ejecutarIndefinidamente(net.gaia.taskprocessor.perf.api.TicksPerSecondTestUnit)
	 */
	public void ejecutarIndefinidamente(final TicksPerSecondTestUnit processorTest) {
		// Configuramos el test
		final VariableTicks variable = configurarTest(processorTest);

		// Describimos las condiciones del test
		describirTest(processorTest);

		// Esperamos que nos diga cuando empezar
		esperarParaArrancar();

		// Iniciamos el test
		final MedidorDeTicksPerSecond medidor = iniciarTest(processorTest, variable);

		// Esperamos que nos paren
		esperarTiempoDefinidoParaDetener();

		// Paramos todo
		detenerTest(processorTest, medidor);

		// Mostramos resultados de test
		mostrarResultadosYEsperar(medidor);
	}

	/**
	 * Muestra resultados y conclusiones del test
	 */
	private void mostrarResultadosYEsperar(final MedidorDeTicksPerSecond medidor) {
		final String resultados = medidor.describirResultados();
		LOG.info("Resultados del medido:\n{}\n<ENTER> para terminar", resultados);
		try {
			System.in.read();
		} catch (final IOException e) {
			LOG.error("Error de IO al esperar para terminar", e);
		}
	}

	/**
	 * Detiene la ejecución del test y el medidor de ticks
	 */
	private void detenerTest(final TicksPerSecondTestUnit processorTest, final MedidorDeTicksPerSecond medidor) {
		medidor.detenerMediciones();
		processorTest.detenerPruebas();
	}

	/**
	 * Espera input del usuario para detener los threads
	 */
	private void esperarTiempoDefinidoParaDetener() {
		try {
			Thread.sleep(runningTime.getMillis());
		} catch (final InterruptedException e) {
			LOG.error("Interrumpieron el thread principal mientras esperaba la ejecucion", e);
		}
	}

	/**
	 * Espera que el usuario indique cuando comenzar
	 */
	private void esperarParaArrancar() {
		mostrarMensajeYEsperarInput("\n<ENTER> para iniciar las pruebas\n");
	}

	/**
	 * @param mensaje
	 */
	private void mostrarMensajeYEsperarInput(final String mensaje) {
		try {
			LOG.info(mensaje);
			System.in.read();
			System.in.skip(1);
		} catch (final IOException e) {
			LOG.error("Se produjo un error de IO esperando input", e);
		}
	}

	/**
	 * Comienza la ejecución del test y el medido de ticks
	 */
	private MedidorDeTicksPerSecond iniciarTest(final TicksPerSecondTestUnit processorTest, final VariableTicks variable) {
		processorTest.comenzarPruebas();

		// Iniciamos las mediciones
		final MedidorDeTicksPerSecond medidor = MedidorDeTicksPerSecond.create(variable);
		medidor.iniciarMediciones();
		return medidor;
	}

	/**
	 * Genera una descripción del test para mostrar por pantalla
	 * 
	 * @param processorTest
	 *            El test descripto
	 */
	private void describirTest(final TicksPerSecondTestUnit processorTest) {
		final String descripcionDeTest = processorTest.getDescripcion();
		LOG.info("Iniciando ejecución de test: {}", descripcionDeTest);
	}

	/**
	 * Configura los parametros del test para que este tenga definidas todas sus dependencias
	 * 
	 * @param processorTest
	 *            El test a configurar
	 * @return La variable creada para mediciones
	 */
	private VariableTicks configurarTest(final TicksPerSecondTestUnit processorTest) {
		final IncrementarVariableWorkUnit workUnit = IncrementarVariableWorkUnit.create(variable);
		processorTest.incrementTicksWith(workUnit);
		return variable;
	}
}
