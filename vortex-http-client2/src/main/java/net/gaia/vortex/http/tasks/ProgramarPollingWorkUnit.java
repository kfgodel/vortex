/**
 * 26/02/2012 00:11:48 Copyright (C) 2011 10Pines S.R.L.
 */
package net.gaia.vortex.http.tasks;

import java.util.concurrent.TimeUnit;

import net.gaia.taskprocessor.api.TaskCriteria;
import net.gaia.taskprocessor.api.TimeMagnitude;
import net.gaia.taskprocessor.api.WorkUnit;
import net.gaia.vortex.http.tasks.contexts.ContextoDeOperacionHttp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Esta clase representa la operación del cliente http para hacer polling de mensajes desde el nodo
 * server
 * 
 * @author D. García
 */
public class ProgramarPollingWorkUnit implements WorkUnit {
	private static final Logger LOG = LoggerFactory.getLogger(ProgramarPollingWorkUnit.class);

	private ContextoDeOperacionHttp contexto;

	private Long proximaEjecucion;

	public static ProgramarPollingWorkUnit create(final ContextoDeOperacionHttp contexto) {
		final ProgramarPollingWorkUnit solicitar = new ProgramarPollingWorkUnit();
		solicitar.contexto = contexto;
		return solicitar;
	}

	/**
	 * @see net.gaia.taskprocessor.api.WorkUnit#doWork()
	 */
	@Override
	public void doWork() throws InterruptedException {
		if (contexto.getSesionInvolucrada().estaCerrada()) {
			// La sesión está cerrada, dejamos de ejecutarnos
			LOG.debug("POLLING detenido por sesion cerrada[{}]", contexto.getSesionInvolucrada());
			return;
		}
		if (proximaEjecucion == null) {
			LOG.debug("Calculando POLLING automatico segun conf para sesion[{}]", contexto.getSesionInvolucrada());
			programarProximaEjecucion();
			return;
		}

		// Disparamos el polling
		LOG.debug("POLLING automatico disparado para sesion[{}]", contexto.getSesionInvolucrada());
		final RealizarPollingDeMensajesWorkUnit polling = RealizarPollingDeMensajesWorkUnit.create(contexto);
		contexto.getProcessor().process(polling);

		programarProximaEjecucion();
	}

	/**
	 * Agrega esta tarea postergada para ejecutarse despes del periodo indicado para polling
	 */
	private void programarProximaEjecucion() {
		final boolean debeEjecutar = calcularProximaEjecucion();
		if (!debeEjecutar) {
			LOG.debug("POLLING DESACTIVADO para sesion[{}]", contexto.getSesionInvolucrada());
			return;
		}
		final long segundosParaProximaEjecucion = calcularEsperaParaProximaEjecucion();
		LOG.debug("Proximo POLLING en {} segs para sesion[{}]", segundosParaProximaEjecucion,
				contexto.getSesionInvolucrada());
		contexto.getProcessor().processDelayed(TimeMagnitude.of(segundosParaProximaEjecucion, TimeUnit.SECONDS), this);
	}

	/**
	 * Calcula la cantidad de segundos necesarios para esperar la proxima ejecucion
	 */
	private long calcularEsperaParaProximaEjecucion() {
		final long espera = (proximaEjecucion - getCurrentMillis()) / 1000;
		return espera;
	}

	/**
	 * Calcula el proximo momento de polling segun la configuracion del contexto
	 * 
	 * @return true si se debe ejecutar una vez más, false si no
	 */
	private boolean calcularProximaEjecucion() {
		final Long periodoDePollingEnSegundos = contexto.getPeriodoDePollingEnSegundos();
		if (periodoDePollingEnSegundos == null) {
			return false;
		}
		proximaEjecucion = getCurrentMillis() + periodoDePollingEnSegundos * 1000;
		return true;
	}

	/**
	 * Devuelve el timestamp del tiempo considerado actual
	 * 
	 * @return El tiempo en milisegundos
	 */
	private long getCurrentMillis() {
		return System.currentTimeMillis();
	}

	/**
	 * Cancela las proximas ejecuciones de esta tarea
	 */
	public void cancelarEjecucion() {
		LOG.debug("Cancelando POLLING para sesion[{}]", contexto.getSesionInvolucrada().getSessionId());
		contexto.getProcessor().removeTasksMatching(new TaskCriteria() {
			@Override
			public boolean matches(final WorkUnit workUnit) {
				final boolean esEstaTarea = workUnit == ProgramarPollingWorkUnit.this;
				return esEstaTarea;
			}
		});
	}

}
