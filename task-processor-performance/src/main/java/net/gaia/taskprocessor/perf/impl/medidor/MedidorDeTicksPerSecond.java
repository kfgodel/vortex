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

import net.gaia.taskprocessor.perf.api.VariableTicks;
import net.gaia.taskprocessor.perf.api.time.CronometroMilis;
import net.gaia.taskprocessor.perf.impl.time.SystemMillisCronometro;

/**
 * Esta clase representa el medidor de ticks por segundo que se ejecuta en su propio hilo
 * 
 * @author D. García
 */
public class MedidorDeTicksPerSecond {

	private ThreadBucleSupport threadActivo;

	private CronometroMilis clock;

	private VariableTicks variable;

	public static MedidorDeTicksPerSecond create(final VariableTicks variable) {
		final MedidorDeTicksPerSecond medidor = new MedidorDeTicksPerSecond();
		medidor.clock = SystemMillisCronometro.create();
		medidor.variable = variable;
		return medidor;
	}

	/**
	 * Comienza la ejecución de este medidor en un thread independiente
	 */
	public void iniciarMediciones() {
		if (threadActivo != null) {
			detenerMediciones();
		}
		final MedirTicksPorSegundoWorkUnit tarea = MedirTicksPorSegundoWorkUnit.create(clock, variable);
		threadActivo = ThreadMedidorDeTicks.create(tarea);
		clock.reset();
		threadActivo.ejecutar();
	}

	/**
	 * Detiene la ejecución de este medidor dejando de tomar medidas de ticks.<br>
	 * Debería liberar los recursos utilizados para le ejecución
	 */
	public void detenerMediciones() {
		if (threadActivo == null) {
			// No hay actividad que detener
			return;
		}
		threadActivo.detener();
		threadActivo = null;
		clock.stop();
	}

	/**
	 * Genera una versión textual de los resultados (si es que hay alguno)
	 * 
	 * @return Los resultados de este medidor
	 */
	public String describirResultados() {
		final long cantidadDeTicksTotal = variable.getCantidadActual();
		final double milisTotales = clock.getTotalMilis();
		final double ticksPerMilis = cantidadDeTicksTotal / milisTotales;
		final StringBuilder builder = new StringBuilder();
		builder.append("Ticks totales: ");
		builder.append(cantidadDeTicksTotal);
		builder.append(" segs: ");
		builder.append(milisTotales / 1000d);
		builder.append(" s\n");
		builder.append("Ticks per milis: ");
		builder.append(ticksPerMilis);
		return builder.toString();
	}
}