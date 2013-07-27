/**
 * 21/07/2013 13:34:00 Copyright (C) 2013 Darío L. García
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
package net.gaia.taskprocessor.parallel;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;

import net.gaia.taskprocessor.api.SubmittedTask;
import net.gaia.taskprocessor.api.WorkUnit;
import net.gaia.taskprocessor.api.processor.TaskProcessor;
import net.gaia.taskprocessor.executor.SubmittedRunnableTask;
import net.gaia.taskprocessor.parallelizer.ListCollectorParallelizer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ar.com.dgarcia.lang.strings.ToString;

/**
 * Esta clase representa uno de lo hilos worker de un {@link ParallelProcessor} que procesa las
 * tareas en paralelo con su propio espacio de memoria, evitando compartir recursos con otros
 * workers
 * 
 * @author D. García
 */
public class ParallelWorker implements Runnable {
	private static final Logger LOG = LoggerFactory.getLogger(ParallelWorker.class);

	/**
	 * Tamaño para el lote de tareas pasado a la cola exclusiva
	 */
	private static final int TAMANIO_COLA_EXCLUSIVA = 1000;
	/**
	 * Cantidad de milis que esperamos como máximo para tener una tarea propia disponible.<br>
	 * Pasado este tiempo, si no tenemos tareas propias buscamos robarle a otro workers
	 */
	private static final int OWN_TASK_TIMEOUT_MILLIS = 2 * 1000;

	/**
	 * Cantidad máxima de tareas que se ejecutan como desprendidas de otra. Superado este valor, las
	 * tareas se agregan al procesador como cualquier otra
	 */
	private static final int CANTIDAD_MAXIMA_DE_TAREAS_DESPRENDIDAS = 100;

	/**
	 * Array de colas compartidas entre todos los workers
	 */
	private BlockingDeque<SubmittedTask>[] sharedQueues;

	/**
	 * Indice identificador de este worker con el que se accede a la cola compartida
	 */
	private int workerIndex;
	public static final String workerIndex_FIELD = "workerIndex";

	/**
	 * Cola exclusiva para procesar las tareas
	 */
	private List<SubmittedTask> exclusiveQueue;

	/**
	 * Flag para saber cuándo nos detuvieron
	 */
	private volatile boolean running;

	/**
	 * Paralelizar usado por este worker para procesar las tareas desprendidas de otras
	 */
	private ListCollectorParallelizer parallelizer;

	/**
	 * Procesador de tareas al que se delegan las tareas desprendidas no procesables directamente
	 * por este thread
	 */
	private TaskProcessor ownerProcessor;

	/**
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		ejecutarBucleInterno();
	}

	/**
	 * Realiza el ciclo de ejecución de este worker
	 */
	private void ejecutarBucleInterno() {
		while (running) {
			// Esperamos un tiempo que nos dejen tareas para este worker
			SubmittedTask nextTask;
			try {
				final BlockingDeque<SubmittedTask> ownSharedQueue = getOwnSharedQueue();
				nextTask = ownSharedQueue.poll(OWN_TASK_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS);
			} catch (final InterruptedException e) {
				// Interrumpieron la espera, puede que hayan detenido el thread, chequeamos el flag
				continue;
			}
			if (nextTask != null) {
				// Al menos una tarea disponible para nosotros, tomamos todas las que hayan
				tomarLoteDeTareasDeColaCompartida(nextTask);
			} else {
				// Si no hay en nuestra cola, buscamos en el resto
				robarTareasDeOtroWorker();
			}
			procesarTareasDeColaExclusiva();
		}
		cancelarTareasPendientes();
	}

	/**
	 * Todas las tareas que aún estén pendientes al detener este worker no serán procesadas.<br>
	 * Las cancelamos para que nadie espere su conclusión
	 */
	private void cancelarTareasPendientes() {
		cancelarTareasExclusivas();
		cancelarTareasCompartidas();
	}

	/**
	 * Cancela todas las tareas pendientes de la cola compartida
	 */
	private void cancelarTareasCompartidas() {
		final BlockingDeque<SubmittedTask> ownSharedQueue = getOwnSharedQueue();
		SubmittedTask tareaCompartidaPendiente;
		while ((tareaCompartidaPendiente = ownSharedQueue.poll()) != null) {
			tareaCompartidaPendiente.cancelExecution(true);
		}
	}

	/**
	 * Cancela todas las tareas exclusivas que estén pendientes
	 */
	private void cancelarTareasExclusivas() {
		for (final SubmittedTask tareaPendiente : exclusiveQueue) {
			tareaPendiente.cancelExecution(true);
		}
		exclusiveQueue.clear();
	}

	/**
	 * Devuelve la cola compartida de este worker
	 * 
	 * @return La cola propia que es compartida con otros workers
	 */
	private BlockingDeque<SubmittedTask> getOwnSharedQueue() {
		return sharedQueues[workerIndex];
	}

	/**
	 * Intentamos tomar tareas de cualquier otro worker que no hayan sido tomadas.<br>
	 * Si lo logramos, balanceamos la carga de trabajo
	 */
	private void robarTareasDeOtroWorker() {
		for (int i = 0; i < sharedQueues.length; i++) {
			final int tareasRobadas = tomarTareasDeColaConIndice(i, TAMANIO_COLA_EXCLUSIVA);
			if (tareasRobadas > 0) {
				// Ya logramos quitarle tareas a alguien
				break;
			}
		}
	}

	/**
	 * Intenta tomar todas las tareas disponibles en la cola compartida, con un limite máximo igual
	 * al tamaño de la cola privada exclusiva. Permitiendo que queden tareas publicas para ser
	 * tomadas por otros threads
	 * 
	 * @param nextTask
	 *            Tarea que ya quitamos de la cola compartida
	 */
	private void tomarLoteDeTareasDeColaCompartida(final SubmittedTask nextTask) {
		exclusiveQueue.add(nextTask);
		// Ya tomamos una, intentamos agarrar todas las que estén disponibles hasta el limite
		final int cantidadAQuitar = TAMANIO_COLA_EXCLUSIVA - 1;
		tomarTareasDeColaConIndice(workerIndex, cantidadAQuitar);
	}

	/**
	 * Toma las tareas de la cola indicada por indice guardandolas en la cola exclusiva.<br>
	 * Devuelve la cantidad tomada siendo el minimo entre las disponibles y la cantidad maxima
	 * indicad
	 * 
	 * @param indiceDeCola
	 *            Identificador de la cola de la cual se tomarán las tareas
	 * @param cantidadAQuitar
	 *            La cantidad máxima a tomar
	 * @return La cantidad realmente tomada
	 */
	private int tomarTareasDeColaConIndice(final int indiceDeCola, final int cantidadAQuitar) {
		final BlockingDeque<SubmittedTask> sharedQueue = sharedQueues[indiceDeCola];
		final int tareasRobadas = sharedQueue.drainTo(exclusiveQueue, cantidadAQuitar);
		return tareasRobadas;
	}

	/**
	 * Procesamos todas las tareas que están disponibles en la cola exclusiva de este hilo
	 */
	private void procesarTareasDeColaExclusiva() {
		final int cantidadPendiente = exclusiveQueue.size();
		if (cantidadPendiente == 0) {
			return;
		}
		for (int i = 0; i < cantidadPendiente && running; i++) {
			final SubmittedTask tareaExclusiva = exclusiveQueue.get(i);
			procesarTarea(tareaExclusiva);
		}
		// Si todavía quedan tareas es porque nos detuvieron antes
		if (!exclusiveQueue.isEmpty()) {
			cancelarTareasExclusivas();
		}

		// La vaciamos para evitar referencias
		exclusiveQueue.clear();
	}

	/**
	 * Ejecuta la tarea indicada como parte de este thread
	 * 
	 * @param nextTask
	 *            La tarea a ejecutar
	 */
	private void procesarTarea(final SubmittedTask nextTask) {
		SubmittedRunnableTask runnableTask;
		try {
			runnableTask = (SubmittedRunnableTask) nextTask;
		} catch (final ClassCastException e) {
			LOG.error("Se intentó ejecutar una tarea[" + nextTask + "] no compatible con este worker[" + this
					+ "]. Se mezclaron los workers?");
			return;
		}
		try {
			runnableTask.executeWorkUnit(parallelizer);
		} catch (final Exception e) {
			LOG.error("Se escapo una excepción no controlada de la tarea ejecutada. Omitiendo error", e);
		}
		procesarTareasDesprendidasDe(nextTask);
	}

	/**
	 * Intentamos ejecutar las tareas adicionales a indicada recolectadas por el paralelizador.<br>
	 * El criterio es que las tareas desprendidas pueden ser ejecutadas en forma más liviana, sin
	 * pasar por el procesador utilizando la misma configuración que la tarea original.<br>
	 * Después de cierta cantidad
	 * 
	 * @param tareaEjecutada
	 *            Tarea ya ejecutada de la cual se obtiene la configuracion de handlers y listeners
	 */
	private void procesarTareasDesprendidasDe(final SubmittedTask tareaEjecutada) {
		// Mientras hayan tareas desprendidas y estemos corriendo y estemos dentro del limite de
		// veces que lo podemos hacer
		for (int i = 0; i < CANTIDAD_MAXIMA_DE_TAREAS_DESPRENDIDAS && running && parallelizer.tieneTareasDesprendidas(); i++) {
			final WorkUnit tareaDesprendida = parallelizer.tomarTareaDesprendida();
			procesarDesprendidaDe(tareaEjecutada, tareaDesprendida);
		}
		// Aún quedan tareas desprendidas y estamos corriendo, pero ya no podemos seguir
		// ejecutándolas en este thread, las delegamos al procesador
		while (running && parallelizer.tieneTareasDesprendidas()) {
			final WorkUnit tareaDeOtroThread = parallelizer.tomarTareaDesprendida();
			delegarAProcesador(tareaDeOtroThread);
		}
		// Aún quedan tareas, pero ya no estamos corriendo y nadie puede ser notificado.
		// Simplemente las descartamos
		if (parallelizer.tieneTareasDesprendidas()) {
			LOG.debug("Al detener este worker, quedaban {} tareas desprendidas por ejecutar que seran ignoradas",
					parallelizer.getCantidadDeTareasDesprendidas());
			parallelizer.eliminarTareasDesprendidas();
		}
	}

	/**
	 * Procesamos la tarea desprendida de una manera más liviana que una {@link SubmittedTask}
	 * utilizando la configuracion de la tareaEjecutada
	 * 
	 * @param tareaEjecutada
	 *            La tarea que define los listeners y handlers a utilizar
	 * @param tareaDesprendida
	 *            La tarea a ejecutar en este thread
	 */
	private void procesarDesprendidaDe(final SubmittedTask tareaEjecutada, final WorkUnit tareaDesprendida) {
		try {
			tareaDesprendida.doWork(parallelizer);
		} catch (final Exception e) {
			LOG.error("Error en tarea desprendida que no será handleadlo", e);
		}
	}

	/**
	 * Intentamos delegarle la tarea al procesador de este worker para que redistribuya el trabajo
	 * con otros threads
	 * 
	 * @param tareaDeOtroThread
	 *            La tarea a ejecutar
	 */
	private void delegarAProcesador(final WorkUnit tareaDeOtroThread) {
		try {
			this.ownerProcessor.process(tareaDeOtroThread);
		} catch (final RejectedExecutionException e) {
			if (ownerProcessor.isDetenido()) {
				LOG.debug("El procesador esta detenido y rechazo la tarea paralela[{}]", tareaDeOtroThread);
			} else {
				LOG.error("El procesador rechazó la tarea siguiente[" + tareaDeOtroThread + "]", e);
			}
		}
	}

	/**
	 * Crea un nuevo worker para ocupar la posición indicada por el workerIndex en el array de colas
	 * compartidas, creando también la cola utilizada por el worker en esa posición.
	 * 
	 * @param workerIndex
	 *            El índice asignado a este worker
	 * @param sharedQueues
	 *            El array de colas compartidas entre todos los workers
	 * @return El worker creado
	 */
	public static ParallelWorker create(final int workerIndex, final BlockingDeque<SubmittedTask>[] sharedQueues) {
		final ParallelWorker worker = new ParallelWorker();
		worker.exclusiveQueue = new ArrayList<SubmittedTask>(TAMANIO_COLA_EXCLUSIVA);
		worker.workerIndex = workerIndex;
		worker.sharedQueues = sharedQueues;
		worker.running = true;
		worker.parallelizer = ListCollectorParallelizer.create();
		// Creamos la cola compartida en el array
		sharedQueues[workerIndex] = new LinkedBlockingDeque<SubmittedTask>();
		return worker;
	}

	/**
	 * Detiene la ejecución de este worker apenas es posible (no detiene inmediatamente si el worker
	 * está en una espera).<br>
	 * Si el thread está bloqueado esperando más tareas, esta instancia dejará de ejecutar apenas se
	 * interrumpa la ejecución del thread
	 */
	public void stopRunning() {
		this.running = false;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return ToString.de(this).con(workerIndex_FIELD, workerIndex).con("exclusivas", exclusiveQueue.size())
				.con("compartidas", getOwnSharedQueue().size()).toString();
	}
}
