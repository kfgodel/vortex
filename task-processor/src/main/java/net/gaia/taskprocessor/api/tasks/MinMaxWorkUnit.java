/**
 * 05/08/2012 04:26:23 Copyright (C) 2011 Darío L. García
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
package net.gaia.taskprocessor.api.tasks;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

import net.gaia.taskprocessor.api.InterruptedThreadException;
import net.gaia.taskprocessor.api.SubmittedTask;
import net.gaia.taskprocessor.api.WorkParallelizer;
import net.gaia.taskprocessor.api.WorkUnit;
import net.gaia.taskprocessor.api.processor.TaskProcessor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ar.com.dgarcia.coding.exceptions.UnhandledConditionException;
import ar.com.dgarcia.lang.time.TimeMagnitude;

/**
 * Esta clase representa una tarea que sólo puede ejecutarse dentro de un margen de tiempo. Ni
 * demasiado pronto, ni demasiado tarde. Por lo tanto tiene un parámetro de mínima y uno de máxima
 * respecto de cuánto tiempo es necesario esperar para ejecutar de nuevo.<br>
 * Si se invoca antes del límite mínimo esta tarea no tiene efecto más que la re-planificación para
 * ejecución próxima, si no se ejecuta dentro del tiempo máxima de espera, la tarea se invoca a si
 * misma antes de que se exceda el límite máximo.
 * 
 * @author D. García
 */
public class MinMaxWorkUnit implements WorkUnit {
	/**
	 * Necesaria para codigo android
	 */
	private static final int SECONDS_PER_MINUTE = 60;

	private static final Logger LOG = LoggerFactory.getLogger(MinMaxWorkUnit.class);

	private static final long MAXIMO_PERMITIDO_PARA_ADQUIRIR_LOCK = TimeMagnitude.of(10 * SECONDS_PER_MINUTE,
			TimeUnit.SECONDS).getMillis();

	private long esperaMinima;
	public static final String esperaMinima_FIELD = "esperaMinima";

	private long esperaMaxima;
	public static final String esperaMaxima_FIELD = "esperaMaxima";

	private long momentoDeUltimaEjecucion;
	public static final String momentoDeUltimaEjecucion_FIELD = "momentoDeUltimaEjecucion";

	private long momentoDeProximaEjecucion;
	public static final String momentoDeProximaEjecucion_FIELD = "momentoDeProximaEjecucion";

	private SubmittedTask proximaPlanificacion;

	private TaskProcessor procesor;
	private ReentrantLock lockDeEjecucion;

	private WorkUnit tareaEjecutable;
	public static final String tareaEjecutable_FIELD = "tareaEjecutable";

	public static MinMaxWorkUnit crearWrapperDe(final WorkUnit delegada, final TaskProcessor processor,
			final long esperaMinima, final long esperaMaxima) {
		final MinMaxWorkUnit minmax = new MinMaxWorkUnit();
		minmax.initialize(processor, delegada, esperaMinima, esperaMaxima);
		return minmax;
	}

	/**
	 * Inicializa el estado de esta instancia
	 * 
	 * @param delegada
	 */
	protected void initialize(final TaskProcessor procesor, final WorkUnit delegada, final long esperaMinima,
			final long esperaMaxima) {
		this.lockDeEjecucion = new ReentrantLock();
		this.esperaMaxima = esperaMaxima;
		this.esperaMinima = esperaMinima;
		this.momentoDeUltimaEjecucion = 0;
		this.procesor = procesor;
		this.tareaEjecutable = delegada;
	}

	/**
	 * @see net.gaia.taskprocessor.api.WorkUnit#doWork()
	 */
	public void doWork(final WorkParallelizer parallelizer) {
		LOG.debug("Iniciando ejecucion min-max[{}]", this);
		boolean acquired;
		try {
			acquired = lockDeEjecucion.tryLock(MAXIMO_PERMITIDO_PARA_ADQUIRIR_LOCK, TimeUnit.MILLISECONDS);
		} catch (final InterruptedException e) {
			LOG.debug("Interrupción mientras esperabamos lock para ejecutar. Nos cancelaron?", e);
			return;
		}
		if (!acquired) {
			throw new UnhandledConditionException("No fue posible adquirir el lock para ejecutar la tarea[" + this
					+ "]. Hay un thread bloqueado?");
		}
		try {
			ejecutarSincronizado(parallelizer);
		} finally {
			lockDeEjecucion.unlock();
		}
	}

	/**
	 * Ejecuta esta tarea asegurando que es el único thread
	 * 
	 * @return El resultado de la ejecución
	 */
	private void ejecutarSincronizado(final WorkParallelizer parallelizer) {
		final long transcurridoDesdeUltimaEjecucion = getCurrentMillis() - momentoDeUltimaEjecucion;
		final long milisFaltantesParaEjecucion = esperaMinima - transcurridoDesdeUltimaEjecucion;
		if (milisFaltantesParaEjecucion > 0) {
			LOG.debug("Ejecución prematura. Todavía faltan {} milis para ejecucion de [{}]",
					milisFaltantesParaEjecucion, this);
			// Todavía es necesario esperar para ejecutar esta tarea
			replanificarEjecucionParaDentroDe(milisFaltantesParaEjecucion);
			return;
		}
		LOG.debug("Ejecucion aceptada. Ya pasaron {} milis desde ultima ejecucion de [{}]", new Object[] {
				transcurridoDesdeUltimaEjecucion, this });
		try {
			ejecutarTarea(parallelizer);
		} catch (final Exception e) {
			LOG.error("Se produjo un error ejecutando la tarea repetitiva[" + this
					+ "]. Abortando ejecuciones posteriores", e);
			cancelarPlanificacionesAnteriores();
			return;
		}
		momentoDeUltimaEjecucion = getCurrentMillis();
		replanificarEjecucionParaDentroDe(esperaMaxima);
	}

	/**
	 * Cancela todas las planificaciones de esta tarea a futuro
	 */
	private void cancelarPlanificacionesAnteriores() {
		if (proximaPlanificacion != null) {
			proximaPlanificacion.cancelExecution(true);
			LOG.debug("Ejecución previa cancelada para min-max[{}]", this);
		}
	}

	/**
	 * Ejecuta la acción específica de esta tarea devolviendo una unidad para ejecución inmediata
	 * como tarea posterior.<br>
	 * Esta tarea será ejecutada nuevamente al ser invocada a posteriori entre espera mínima y
	 * espera máxima
	 * 
	 * @return La continuación de esta tarea para ser agregada en la cola de tareas inmediatas o
	 *         null si no hay continuación
	 * @throws InterruptedException
	 *             Si este thread es interrumpido esperando
	 */
	protected void ejecutarTarea(final WorkParallelizer parallelizer) throws InterruptedThreadException {
		tareaEjecutable.doWork(parallelizer);
	}

	/**
	 * Re-planifica la ejecución de esta tarea esperando los milisegundos indicados
	 * 
	 * @param esperaEnMilis
	 *            La cantidad de milisegundos que debe esperar el procesador para ejecutar esta
	 *            tarea
	 */
	private void replanificarEjecucionParaDentroDe(final long esperaEnMilis) {
		cancelarPlanificacionesAnteriores();
		final long momentoParaEjecutar = getCurrentMillis() + esperaEnMilis;
		if (momentoDeProximaEjecucion == momentoParaEjecutar) {
			LOG.debug(" Replanificacion de [{}] innecesaria porque no cambia el momento de proxima ejecucion: {}",
					this, momentoDeProximaEjecucion);
			return;
		}
		momentoDeProximaEjecucion = momentoParaEjecutar;
		LOG.debug("Proxima ejecucion dentro de {} milis para [{}]", esperaEnMilis, this);
		proximaPlanificacion = procesor.processDelayed(TimeMagnitude.of(esperaEnMilis, TimeUnit.MILLISECONDS), this);
	}

	/**
	 * Devuelve el momento considerado actual en milisegundos
	 * 
	 * @return El timestamp actual
	 */
	public long getCurrentMillis() {
		return System.currentTimeMillis();
	}

	public long getEsperaMinima() {
		return esperaMinima;
	}

	public void setEsperaMinima(final long esperaMinima) {
		this.esperaMinima = esperaMinima;
	}

	public long getEsperaMaxima() {
		return esperaMaxima;
	}

	public void setEsperaMaxima(final long esperaMaxima) {
		this.esperaMaxima = esperaMaxima;
	}

	public TaskProcessor getProcesor() {
		return procesor;
	}

	public void setProcesor(final TaskProcessor procesor) {
		this.procesor = procesor;
	}

	public long getMomentoDeUltimaEjecucion() {
		return momentoDeUltimaEjecucion;
	}

	public WorkUnit getTareaEjecutable() {
		return tareaEjecutable;
	}

	public void setTareaEjecutable(final WorkUnit tareaEjecutable) {
		this.tareaEjecutable = tareaEjecutable;
	}

	/**
	 * @see java.lang.Object#toString()
	 */

	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append(getClass().getSimpleName());
		builder.append("#");
		builder.append(Long.toHexString(hashCode()));
		builder.append("[");
		builder.append(esperaMinima);
		builder.append(",");
		builder.append(esperaMaxima);
		builder.append("]");
		builder.append("{ultima: ");
		builder.append(momentoDeUltimaEjecucion);
		builder.append("}");
		return builder.toString();
	}

}
