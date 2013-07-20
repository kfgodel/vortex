/**
 * 07/07/2013 15:17:33 Copyright (C) 2013 Darío L. García
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
package net.gaia.taskprocessor.perf.impl.medidor;

import java.util.List;

import net.gaia.taskprocessor.perf.api.time.CronometroMilis;
import net.gaia.taskprocessor.perf.api.variables.EstrategiaDeVariablesPorThread;
import net.gaia.taskprocessor.perf.api.variables.VariableTicks;
import net.gaia.taskprocessor.perf.impl.time.SystemMillisCronometro;

/**
 * Esta clase representa el medidor de ticks por segundo que se ejecuta en su propio hilo
 * 
 * @author D. García
 */
public class MedidorDeTicksPerSecond {

	private CronometroMilis clock;

	private EstrategiaDeVariablesPorThread estrategiaDeVariables;

	public static MedidorDeTicksPerSecond create(final EstrategiaDeVariablesPorThread estrategiaDeVariables) {
		final MedidorDeTicksPerSecond medidor = new MedidorDeTicksPerSecond();
		medidor.clock = SystemMillisCronometro.create();
		medidor.estrategiaDeVariables = estrategiaDeVariables;
		return medidor;
	}

	/**
	 * Comienza la ejecución de este medidor en un thread independiente
	 */
	public void iniciarMediciones() {
		clock.reset();
	}

	/**
	 * Detiene la ejecución de este medidor dejando de tomar medidas de ticks.<br>
	 * Debería liberar los recursos utilizados para le ejecución
	 */
	public void detenerMediciones() {
		clock.stop();
	}

	/**
	 * Genera una versión textual de los resultados (si es que hay alguno)
	 * 
	 * @return Los resultados de este medidor
	 */
	public String describirResultados() {
		return describirResultadosCon(clock, estrategiaDeVariables);
	}

	/**
	 * Genera una cadena como descripción de los resultados para el reloj usado y la estrategia de
	 * variables indicada
	 * 
	 * @return Una cadena con los resultados de las pruebas
	 */
	public static String describirResultadosCon(final CronometroMilis clock,
			final EstrategiaDeVariablesPorThread estrategiaDeVariables) {
		final StringBuilder builder = new StringBuilder();
		final long cantidadDeTicksTotal = calcularTicksAmculuadosPara(estrategiaDeVariables);
		final double milisTotales = clock.getTotalMilis();
		final double ticksPerMilis = cantidadDeTicksTotal / milisTotales;
		builder.append("Ticks totales: ");
		builder.append(cantidadDeTicksTotal);
		builder.append(" segs: ");
		builder.append(milisTotales / 1000d);
		builder.append(" s\n");
		builder.append("Ticks per milis: ");
		builder.append(ticksPerMilis);
		return builder.toString();
	}

	/**
	 * Calcula la cantidad de ticks acumulados en todas las variables de la estrategia pasada
	 * 
	 * @param estrategiaDeVariables
	 *            La estrategia usada
	 * @return La cantidad de ticks totales de la estrategia
	 */
	public static long calcularTicksAmculuadosPara(final EstrategiaDeVariablesPorThread estrategiaDeVariables) {
		final List<VariableTicks> variablesCreadas = estrategiaDeVariables.getVariablesCreadas();
		long cantidadDeTicksTotal = 0;
		for (final VariableTicks variableTicks : variablesCreadas) {
			cantidadDeTicksTotal += variableTicks.getCantidadActual();
		}
		return cantidadDeTicksTotal;
	}
}
