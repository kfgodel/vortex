/**
 * 13/06/2012 11:40:08 Copyright (C) 2011 10Pines S.R.L.
 */
package net.gaia.vortex.core.impl.atomos.support.procesador;

import java.util.concurrent.RejectedExecutionException;

import net.gaia.taskprocessor.api.WorkUnit;
import net.gaia.taskprocessor.api.processor.TaskProcessor;
import net.gaia.vortex.impl.support.ComponenteSupport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Esta clase define comportamiento base para los componentes que requieren procesar tareas en
 * background con un procesador de tareas propio
 * 
 * @author D. García
 */
public abstract class ComponenteConProcesadorSupport extends ComponenteSupport {
	private static final Logger LOG = LoggerFactory.getLogger(ComponenteConProcesadorSupport.class);

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
		try {
			processor.process(tarea);
		} catch (final RejectedExecutionException e) {
			if (processor.isDetenido()) {
				LOG.debug("El procesador esta detenido. No podemos procesar la tarea[{}]", tarea);
			} else {
				LOG.error("El procesador rechazó la tarea[" + tarea + "]", e);
			}
		}
	}

	protected void initializeWith(final TaskProcessor processor) {
		if (processor == null) {
			throw new IllegalArgumentException("El procesador de tareas en el componente[" + this
					+ "] no puede ser null");
		}
		this.processor = processor;
	}
}
