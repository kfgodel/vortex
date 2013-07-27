/**
 * 26/07/2013 19:27:39 Copyright (C) 2013 Darío L. García
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
package net.gaia.taskprocessor.parallelizer;

import java.util.ArrayList;
import java.util.List;

import net.gaia.taskprocessor.api.WorkParallelizer;
import net.gaia.taskprocessor.api.WorkUnit;

/**
 * Esta clase implementa el paralelizador de tareas colectando sólo colectándolas en una lista una
 * lista para ser procesadas en tandem
 * 
 * @author D. García
 */
public class ListCollectorParallelizer implements WorkParallelizer {

	private List<WorkUnit> submited;

	/**
	 * @see net.gaia.taskprocessor.api.WorkParallelizer#submitAndForget(net.gaia.taskprocessor.api.WorkUnit)
	 */
	public void submitAndForget(final WorkUnit otherWorkUnit) {
		// Sólo las
		safeGetSubmited().add(otherWorkUnit);
	}

	private List<WorkUnit> safeGetSubmited() {
		if (submited == null) {
			submited = new ArrayList<WorkUnit>();
		}
		return submited;
	}

	public static ListCollectorParallelizer create() {
		final ListCollectorParallelizer parallelizer = new ListCollectorParallelizer();
		return parallelizer;
	}

	/**
	 * Indica si este paralelizar colectó tareas para ejecutar
	 * 
	 * @return true si hay tareas para procesar
	 */
	public boolean tieneTareasDesprendidas() {
		if (submited == null) {
			return false;
		}
		boolean noEstaVacia = !this.submited.isEmpty();
		return noEstaVacia;
	}

	/**
	 * Quita la siguiente tarea desprendida de este paralelizador para ser ejecutada
	 * 
	 * @return La tarea a ejecutar, null si no hay ninguna
	 */
	public WorkUnit tomarTareaDesprendida() {
		if (this.submited == null) {
			return null;
		}
		if (this.submited.isEmpty()) {
			return null;
		}
		final WorkUnit tareaDesprendida = this.submited.remove(0);
		return tareaDesprendida;
	}

	/**
	 * Devuelve la cantidad de tareas desprendidas recolectadas en este paralelizador
	 * 
	 * @return La cantidad de tareas pendientes
	 */
	public int getCantidadDeTareasDesprendidas() {
		if (submited == null) {
			return 0;
		}
		return submited.size();
	}

	/**
	 * Elimina todas las tareas desprendidas de este paralelizador
	 */
	public void eliminarTareasDesprendidas() {
		if (submited != null) {
			submited.clear();
		}
	}
}
