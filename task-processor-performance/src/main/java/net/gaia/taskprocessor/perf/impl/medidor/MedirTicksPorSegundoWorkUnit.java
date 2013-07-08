/**
 * 07/07/2013 17:01:26 Copyright (C) 2013 Darío L. García
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

import java.util.ArrayList;
import java.util.List;

import net.gaia.taskprocessor.api.WorkUnit;
import net.gaia.taskprocessor.perf.api.VariableTicks;
import net.gaia.taskprocessor.perf.api.time.CronometroMilis;

/**
 * Esta clase representa la tarea del medido que registra los ticks por segundo transcurridos
 * 
 * @author D. García
 */
public class MedirTicksPorSegundoWorkUnit implements WorkUnit {
	// private static final Logger LOG =
	// LoggerFactory.getLogger(MedirTicksPorSegundoWorkUnit.class);

	private static final int VALOR_INICIAL_TICKS = 0;

	private CronometroMilis clock;

	private VariableTicks variable;

	private long ultimoValorDeTicks;

	private List<Double> medicionesRealizadas;

	/**
	 * @see net.gaia.taskprocessor.api.WorkUnit#doWork()
	 */
	public WorkUnit doWork() throws InterruptedException {
		// Actualizamos el delta de ticks
		final long valorActual = variable.getCantidadActual();
		final long diferenciaDeTicks = valorActual - ultimoValorDeTicks;
		ultimoValorDeTicks = valorActual;

		// Calculamos cuanto tiempo real pasó
		final long milisTranscurridos = clock.getLastLapAndStartNew();
		final double perSecondProportion = milisTranscurridos / 1000d;

		double ticksPerSecond = 0;
		if (perSecondProportion != 0) {
			ticksPerSecond = diferenciaDeTicks / perSecondProportion;
		}
		medicionesRealizadas.add(ticksPerSecond);
		// LOG.debug("Ticks per second: {}", ticksPerSecond);

		return null;
	}

	public static MedirTicksPorSegundoWorkUnit create(final CronometroMilis clock, final VariableTicks variable) {
		final MedirTicksPorSegundoWorkUnit medir = new MedirTicksPorSegundoWorkUnit();
		medir.clock = clock;
		medir.variable = variable;
		medir.ultimoValorDeTicks = VALOR_INICIAL_TICKS;
		medir.medicionesRealizadas = new ArrayList<Double>();
		return medir;
	}

	public VariableTicks getVariable() {
		return variable;
	}

	public List<Double> getMedicionesRealizadas() {
		return medicionesRealizadas;
	}

}
