/**
 * 22/05/2012 09:53:22 Copyright (C) 2011 10Pines S.R.L.
 */
package net.gaia.taskprocessor.tests.executor;

import java.util.concurrent.TimeUnit;

import net.gaia.taskprocessor.api.TaskProcessor;
import net.gaia.taskprocessor.api.TaskProcessorConfiguration;
import net.gaia.taskprocessor.api.TimeMagnitude;
import net.gaia.taskprocessor.executor.ExecutorBasedTaskProcesor;
import net.gaia.taskprocessor.tests.util.CounterTaskListener;
import net.gaia.taskprocessor.tests.util.StressGenerator;
import net.gaia.taskprocessor.tests.util.TareaSimulada;
import net.gaia.util.SystemChronometer;
import net.gaia.util.WaitBarrier;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Esta clase testea la capacidad de procesamiento del procesador con una cantidad fija de tareas
 * contabilizando el tiempo transcurrido
 * 
 * @author D. García
 */
public class TestProcessorPerformanceWithFixedTaskCount {
	private static final Logger LOG = LoggerFactory.getLogger(TestProcessorPerformanceWithFixedTaskCount.class);

	@Test
	public void miniTareasUnThreadGeneradorUnThreadProcesador() {
		final int threadsDeProcesador = 1;
		final int cantidadDeThreadsGeneradores = 1;
		final int ejecucionesPorThread = 1000;
		final long esperaEntreEjecucionesEnMilis = 10;
		final long duracionDeTareaEnMillis = 10;
		final TimeMagnitude esperaDeThreads = TimeMagnitude.of(1, TimeUnit.MINUTES);

		// Prepramos un thread que va a agregar tareas de 10ms cada 10ms
		procesarYMedirRendimiento("mini(1 -> 1)", threadsDeProcesador, cantidadDeThreadsGeneradores,
				ejecucionesPorThread, esperaEntreEjecucionesEnMilis, duracionDeTareaEnMillis, esperaDeThreads);
	}

	@Test
	public void miniTareasDosThreasGeneradoresUnThreadProcesador() {
		final int threadsDeProcesador = 1;
		final int cantidadDeThreadsGeneradores = 2;
		final int ejecucionesPorThread = 1000;
		final long esperaEntreEjecucionesEnMilis = 10;
		final long duracionDeTareaEnMillis = 10;
		final TimeMagnitude esperaDeThreads = TimeMagnitude.of(1, TimeUnit.MINUTES);

		// Prepramos un thread que va a agregar tareas de 10ms cada 10ms
		procesarYMedirRendimiento("mini(2 -> 1)", threadsDeProcesador, cantidadDeThreadsGeneradores,
				ejecucionesPorThread, esperaEntreEjecucionesEnMilis, duracionDeTareaEnMillis, esperaDeThreads);
	}

	@Test
	public void miniTareasDosThreadGeneradoresDosProcesadores() {
		final int threadsDeProcesador = 2;
		final int cantidadDeThreadsGeneradores = 2;
		final int ejecucionesPorThread = 1000;
		final long esperaEntreEjecucionesEnMilis = 10;
		final long duracionDeTareaEnMillis = 10;
		final TimeMagnitude esperaDeThreads = TimeMagnitude.of(1, TimeUnit.MINUTES);

		// Prepramos un thread que va a agregar tareas de 10ms cada 10ms
		procesarYMedirRendimiento("mini(2 -> 2)", threadsDeProcesador, cantidadDeThreadsGeneradores,
				ejecucionesPorThread, esperaEntreEjecucionesEnMilis, duracionDeTareaEnMillis, esperaDeThreads);
	}

	@Test
	public void tareas1MsCuatroThreadsGeneradorDosProcesadores() {
		final int threadsDeProcesador = 2;
		final int cantidadDeThreadsGeneradores = 4;
		final int ejecucionesPorThread = 1000;
		final long esperaEntreEjecucionesEnMilis = 0;
		final long duracionDeTareaEnMillis = 1;
		final TimeMagnitude esperaDeThreads = TimeMagnitude.of(1, TimeUnit.MINUTES);

		// Prepramos un thread que va a agregar tareas de 10ms cada 10ms
		procesarYMedirRendimiento("1Ms(4 -> 2)", threadsDeProcesador, cantidadDeThreadsGeneradores,
				ejecucionesPorThread, esperaEntreEjecucionesEnMilis, duracionDeTareaEnMillis, esperaDeThreads);
	}

	@Test
	public void tareasLimiteCuatroThreadsGeneradorDosProcesadores() {
		final int threadsDeProcesador = 2;
		final int cantidadDeThreadsGeneradores = 4;
		final int ejecucionesPorThread = 100000;
		final long esperaEntreEjecucionesEnMilis = 0;
		final long duracionDeTareaEnMillis = 0;
		final TimeMagnitude esperaDeThreads = TimeMagnitude.of(1, TimeUnit.MINUTES);

		// Prepramos un thread que va a agregar tareas de 10ms cada 10ms
		procesarYMedirRendimiento("limite(4 -> 2)", threadsDeProcesador, cantidadDeThreadsGeneradores,
				ejecucionesPorThread, esperaEntreEjecucionesEnMilis, duracionDeTareaEnMillis, esperaDeThreads);
	}

	@Test
	public void tareasLimiteCuatroThreadsGeneradorCuatroProcesadores() {
		final int threadsDeProcesador = 4;
		final int cantidadDeThreadsGeneradores = 4;
		final int ejecucionesPorThread = 100000;
		final long esperaEntreEjecucionesEnMilis = 0;
		final long duracionDeTareaEnMillis = 0;
		final TimeMagnitude esperaDeThreads = TimeMagnitude.of(1, TimeUnit.MINUTES);

		// Prepramos un thread que va a agregar tareas de 10ms cada 10ms
		procesarYMedirRendimiento("limite(4 -> 4)", threadsDeProcesador, cantidadDeThreadsGeneradores,
				ejecucionesPorThread, esperaEntreEjecucionesEnMilis, duracionDeTareaEnMillis, esperaDeThreads);
	}

	@Test
	public void tareasLentasUnThreadGeneradorUnThreadProcesador() {
		final int threadsDeProcesador = 1;
		final int cantidadDeThreadsGeneradores = 1;
		final int ejecucionesPorThread = 1000;
		final long esperaEntreEjecucionesEnMilis = 10;
		final long duracionDeTareaEnMillis = 100;
		final TimeMagnitude esperaDeThreads = TimeMagnitude.of(1, TimeUnit.MINUTES);

		// Prepramos un thread que va a agregar tareas de 10ms cada 10ms
		procesarYMedirRendimiento("lentas(1 -> 1)", threadsDeProcesador, cantidadDeThreadsGeneradores,
				ejecucionesPorThread, esperaEntreEjecucionesEnMilis, duracionDeTareaEnMillis, esperaDeThreads);
	}

	@Test
	public void tareasLentasUnThreadGeneradorDosProcesadores() {
		final int threadsDeProcesador = 2;
		final int cantidadDeThreadsGeneradores = 1;
		final int ejecucionesPorThread = 1000;
		final long esperaEntreEjecucionesEnMilis = 10;
		final long duracionDeTareaEnMillis = 100;
		final TimeMagnitude esperaDeThreads = TimeMagnitude.of(1, TimeUnit.MINUTES);

		// Prepramos un thread que va a agregar tareas de 10ms cada 10ms
		procesarYMedirRendimiento("lentas(1 -> 2)", threadsDeProcesador, cantidadDeThreadsGeneradores,
				ejecucionesPorThread, esperaEntreEjecucionesEnMilis, duracionDeTareaEnMillis, esperaDeThreads);
	}

	private void procesarYMedirRendimiento(final String nombreDeTest, final int threasParaEjecucion,
			final int cantidadDeThreadsGeneradores, final int ejecucionesPorThread,
			final long esperaEntreEjecucionesEnMilis, final long duracionDeTareaEnMillis,
			final TimeMagnitude esperaDeThreads) {
		final TaskProcessorConfiguration config = TaskProcessorConfiguration.create();
		config.setThreadPoolSize(threasParaEjecucion);
		final TaskProcessor procesor = crearProcessor(config);
		final StressGenerator stressGenerator = StressGenerator.create();
		stressGenerator.setCantidadDeThreadsEnEjecucion(cantidadDeThreadsGeneradores);
		stressGenerator.setCantidadDeEjecucionesPorThread(ejecucionesPorThread);
		stressGenerator.setEsperaEntreEjecucionesEnMilis(esperaEntreEjecucionesEnMilis);

		final int tareasDisparadas = cantidadDeThreadsGeneradores * ejecucionesPorThread;
		final WaitBarrier barreraDeTareas = WaitBarrier.create(tareasDisparadas);
		stressGenerator.setEjecutable(new Runnable() {
			@Override
			public void run() {
				final TareaSimulada tarea = TareaSimulada.create(duracionDeTareaEnMillis, barreraDeTareas);
				procesor.process(tarea);
			}
		});
		// Agregamos el listener para contabilizar las operaciones
		final CounterTaskListener counterListener = CounterTaskListener.create();
		procesor.setProcessorListener(counterListener);

		// Realizamos las ejecuciones
		LOG.debug("[{}]: Iniciando {} Threads con {} tareas = {} totales esperando {} ms para ejecutar {} ms",
				new Object[] { nombreDeTest, cantidadDeThreadsGeneradores, ejecucionesPorThread, tareasDisparadas,
						esperaEntreEjecucionesEnMilis, duracionDeTareaEnMillis });
		final double millisPorThread = (tareasDisparadas * duracionDeTareaEnMillis) / threasParaEjecucion;
		LOG.debug("[{}]: Espera estimada: {} ms", nombreDeTest, millisPorThread);
		final SystemChronometer cronometro = SystemChronometer.create();
		stressGenerator.start();
		// Esperamos que terminen todos los threads y mostramos el estado
		stressGenerator.esperarTerminoDeThreads(esperaDeThreads);
		final long recibidasPorElProcesador = counterListener.getTasksAccepted().get();
		final long iniciadasPorElProcesador = counterListener.getTasksStarted().get();
		final long completadasPorElProcesador = counterListener.getTasksCompleted().get();
		LOG.debug("[{}]: Tareas recibidas: {}, iniciadas: {}, completadas: {}", new Object[] { nombreDeTest,
				recibidasPorElProcesador, iniciadasPorElProcesador, completadasPorElProcesador });

		barreraDeTareas.waitForReleaseUpTo(TimeMagnitude.of(5, TimeUnit.MINUTES));
		final long millisEnTareas = cronometro.getElapsedMillis();
		final long completadasAlFinal = counterListener.getTasksCompleted().get();
		LOG.debug("[{}]: {} tareas completadas en {} seg", new Object[] { nombreDeTest, completadasAlFinal,
				millisEnTareas / 1000d });
		procesor.detener();
	}

	/**
	 * Crea el procesador default basado en executors
	 * 
	 * @param config
	 *            La configuración para crearlo
	 * @return El procesador creado
	 */
	protected TaskProcessor crearProcessor(final TaskProcessorConfiguration config) {
		final ExecutorBasedTaskProcesor procesor = ExecutorBasedTaskProcesor.create(config);
		return procesor;
	}
}
