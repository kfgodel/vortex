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

import net.gaia.taskprocessor.api.WorkUnit;
import net.gaia.taskprocessor.perf.api.VariableTicks;
import net.gaia.taskprocessor.perf.impl.time.SystemMillisCronometro;

/**
 * Esta clase representa el medidor de ticks por segundo que se ejecuta en su propio hilo
 * 
 * @author D. García
 */
public class MedidorDeTicksPerSecond {

	private ThreadMedidorDeTicks threadActivo;

	private WorkUnit tareaDeMedicion;

	public static MedidorDeTicksPerSecond create(final VariableTicks variable) {
		final MedidorDeTicksPerSecond medidor = new MedidorDeTicksPerSecond();
		medidor.tareaDeMedicion = MedirTicksPorSegundoWorkUnit.create(SystemMillisCronometro.create(), variable);
		return medidor;
	}

	/**
	 * Comienza la ejecución de este medidor en un thread independiente
	 */
	public void iniciarMediciones() {
		if (threadActivo != null) {
			detenerMediciones();
		}
		threadActivo = ThreadMedidorDeTicks.create(tareaDeMedicion);
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
	}

	/**
	 * Genera una versión textual de los resultados (si es que hay alguno)
	 * 
	 * @return Los resultados de este medidor
	 */
	public String describirResultados() {
		return "Sin resultados";
	}
}
