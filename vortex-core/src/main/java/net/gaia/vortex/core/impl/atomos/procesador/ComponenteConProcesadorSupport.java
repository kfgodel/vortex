/**
 * 13/06/2012 11:40:08 Copyright (C) 2011 10Pines S.R.L.
 */
package net.gaia.vortex.core.impl.atomos.procesador;

import net.gaia.taskprocessor.api.TaskProcessor;
import net.gaia.taskprocessor.api.WorkUnit;
import net.gaia.vortex.core.impl.atomos.ComponenteSupport;

/**
 * Esta clase define comportamiento base para los componentes que requieren procesar tareas en
 * background con un procesador de tareas propio
 * 
 * @author D. García
 */
public abstract class ComponenteConProcesadorSupport extends ComponenteSupport {

	private TaskProcessor processor;

	public TaskProcessor getProcessor() {
		return processor;
	}

	public void setProcessor(final TaskProcessor processor) {
		this.processor = processor;
	}

	public void procesarEnThreadPropio(final WorkUnit tarea) {
		if (tarea == null) {
			throw new IllegalArgumentException("La tarea pasada a procesar no puede ser null");
		}
		processor.process(tarea);
	}

	protected void initializeWith(final TaskProcessor processor) {
		if (processor == null) {
			throw new IllegalArgumentException("El procesador de tareas en el componente[" + this
					+ "] no puede ser null");
		}
		this.processor = processor;
	}
}
