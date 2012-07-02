/**
 * 23/05/2012 20:54:34 Copyright (C) 2011 Darío L. García
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
package net.gaia.taskprocessor.tests.executor;

import java.util.concurrent.TimeUnit;

import net.gaia.taskprocessor.api.TaskProcessor;
import net.gaia.taskprocessor.api.TaskProcessorConfiguration;
import net.gaia.taskprocessor.executor.ExecutorBasedTaskProcesor;
import net.gaia.taskprocessor.tests.util.CounterTaskListener;
import net.gaia.taskprocessor.tests.util.TareaSimulada;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ar.com.dgarcia.lang.time.TimeMagnitude;
import ar.com.dgarcia.testing.stress.StressGenerator;

/**
 * Esta clase testea la capacidad de ejecución de tareas del procesador durante una cantidad fija de
 * tiempo
 * 
 * @author D. García
 */
public class TestProcessorPerformanceWithFixedTimebox {

	private static final Logger LOG = LoggerFactory.getLogger(TestProcessorPerformanceWithFixedTaskCount.class);

	@Test
	public void miniTareasUnThreadGeneradorUnThreadProcesador() {
		final int threadsDeProcesador = 1;
		final int cantidadDeThreadsGeneradores = 1;
		final long esperaEntreEjecucionesEnMilis = 10;
		final long duracionDeTareaEnMillis = 10;
		final TimeMagnitude esperaDeThreads = TimeMagnitude.of(10, TimeUnit.SECONDS);

		// Prepramos un thread que va a agregar tareas de 10ms cada 10ms
		procesarYMedirRendimiento("mini(1 -> 1)", threadsDeProcesador, cantidadDeThreadsGeneradores,
				esperaEntreEjecucionesEnMilis, duracionDeTareaEnMillis, esperaDeThreads);
	}

	@Test
	public void miniTareasDosThreasGeneradoresUnThreadProcesador() {
		final int threadsDeProcesador = 1;
		final int cantidadDeThreadsGeneradores = 2;
		final long esperaEntreEjecucionesEnMilis = 10;
		final long duracionDeTareaEnMillis = 10;
		final TimeMagnitude esperaDeThreads = TimeMagnitude.of(10, TimeUnit.SECONDS);

		// Prepramos un thread que va a agregar tareas de 10ms cada 10ms
		procesarYMedirRendimiento("mini(2 -> 1)", threadsDeProcesador, cantidadDeThreadsGeneradores,
				esperaEntreEjecucionesEnMilis, duracionDeTareaEnMillis, esperaDeThreads);
	}

	@Test
	public void miniTareasDosThreadGeneradoresDosProcesadores() {
		final int threadsDeProcesador = 2;
		final int cantidadDeThreadsGeneradores = 2;
		final long esperaEntreEjecucionesEnMilis = 10;
		final long duracionDeTareaEnMillis = 10;
		final TimeMagnitude esperaDeThreads = TimeMagnitude.of(10, TimeUnit.SECONDS);

		// Prepramos un thread que va a agregar tareas de 10ms cada 10ms
		procesarYMedirRendimiento("mini(2 -> 2)", threadsDeProcesador, cantidadDeThreadsGeneradores,
				esperaEntreEjecucionesEnMilis, duracionDeTareaEnMillis, esperaDeThreads);
	}

	@Test
	public void tareas1MsCuatroThreadsGeneradorDosProcesadores() {
		final int threadsDeProcesador = 2;
		final int cantidadDeThreadsGeneradores = 4;
		final long esperaEntreEjecucionesEnMilis = 0;
		final long duracionDeTareaEnMillis = 1;
		final TimeMagnitude esperaDeThreads = TimeMagnitude.of(10, TimeUnit.SECONDS);

		// Prepramos un thread que va a agregar tareas de 10ms cada 10ms
		procesarYMedirRendimiento("1Ms(4 -> 2)", threadsDeProcesador, cantidadDeThreadsGeneradores,
				esperaEntreEjecucionesEnMilis, duracionDeTareaEnMillis, esperaDeThreads);
	}

	@Test
	public void tareasLimiteCuatroThreadsGeneradorDosProcesadores() {
		final int threadsDeProcesador = 2;
		final int cantidadDeThreadsGeneradores = 4;
		final long esperaEntreEjecucionesEnMilis = 0;
		final long duracionDeTareaEnMillis = 0;
		final TimeMagnitude esperaDeThreads = TimeMagnitude.of(10, TimeUnit.SECONDS);

		// Prepramos un thread que va a agregar tareas de 10ms cada 10ms
		procesarYMedirRendimiento("limite(4 -> 2)", threadsDeProcesador, cantidadDeThreadsGeneradores,
				esperaEntreEjecucionesEnMilis, duracionDeTareaEnMillis, esperaDeThreads);
	}

	@Test
	public void tareasLimiteCuatroThreadsGeneradorCuatroProcesadores() {
		final int threadsDeProcesador = 4;
		final int cantidadDeThreadsGeneradores = 4;
		final long esperaEntreEjecucionesEnMilis = 0;
		final long duracionDeTareaEnMillis = 0;
		final TimeMagnitude esperaDeThreads = TimeMagnitude.of(10, TimeUnit.SECONDS);

		// Prepramos un thread que va a agregar tareas de 10ms cada 10ms
		procesarYMedirRendimiento("limite(4 -> 4)", threadsDeProcesador, cantidadDeThreadsGeneradores,
				esperaEntreEjecucionesEnMilis, duracionDeTareaEnMillis, esperaDeThreads);
	}

	@Test
	public void tareasLentasUnThreadGeneradorUnThreadProcesador() {
		final int threadsDeProcesador = 1;
		final int cantidadDeThreadsGeneradores = 1;
		final long esperaEntreEjecucionesEnMilis = 10;
		final long duracionDeTareaEnMillis = 100;
		final TimeMagnitude esperaDeThreads = TimeMagnitude.of(10, TimeUnit.SECONDS);

		// Prepramos un thread que va a agregar tareas de 10ms cada 10ms
		procesarYMedirRendimiento("lentas(1 -> 1)", threadsDeProcesador, cantidadDeThreadsGeneradores,
				esperaEntreEjecucionesEnMilis, duracionDeTareaEnMillis, esperaDeThreads);
	}

	@Test
	public void tareasLentasUnThreadGeneradorDosProcesadores() {
		final int threadsDeProcesador = 2;
		final int cantidadDeThreadsGeneradores = 1;
		final long esperaEntreEjecucionesEnMilis = 10;
		final long duracionDeTareaEnMillis = 100;
		final TimeMagnitude esperaDeThreads = TimeMagnitude.of(10, TimeUnit.SECONDS);

		// Prepramos un thread que va a agregar tareas de 10ms cada 10ms
		procesarYMedirRendimiento("lentas(1 -> 2)", threadsDeProcesador, cantidadDeThreadsGeneradores,
				esperaEntreEjecucionesEnMilis, duracionDeTareaEnMillis, esperaDeThreads);
	}

	private void procesarYMedirRendimiento(final String nombreDeTest, final int threasParaEjecucion,
			final int cantidadDeThreadsGeneradores, final long esperaEntreEjecucionesEnMilis,
			final long duracionDeTareaEnMillis, final TimeMagnitude tiempoDeTesteo) {
		// Configuramos y preparamos el procesador
		final TaskProcessorConfiguration config = TaskProcessorConfiguration.create();
		config.setMinimunThreadPoolSize(threasParaEjecucion);
		final TaskProcessor procesor = crearProcessor(config);
		final StressGenerator stressGenerator = StressGenerator.create();
		stressGenerator.setCantidadDeThreadsEnEjecucion(cantidadDeThreadsGeneradores);
		stressGenerator.setEsperaEntreEjecucionesEnMilis(esperaEntreEjecucionesEnMilis);

		stressGenerator.setEjecutable(new Runnable() {
			@Override
			public void run() {
				final TareaSimulada tarea = TareaSimulada.create(duracionDeTareaEnMillis, null);
				procesor.process(tarea);
			}
		});
		// Agregamos el listener para contabilizar las operaciones
		final CounterTaskListener counterListener = CounterTaskListener.create();
		procesor.setProcessorListener(counterListener);

		// Realizamos las ejecuciones
		LOG.debug("[{}]: Iniciando {} Threads con tareas de {} ms, esperando {} ms entre ellas", new Object[] {
				nombreDeTest, cantidadDeThreadsGeneradores, duracionDeTareaEnMillis, esperaEntreEjecucionesEnMilis });
		final long tiempoDeTesteoEnMillis = tiempoDeTesteo.getMillis();
		LOG.debug("[{}]: Espera estimada: {} ms", nombreDeTest, tiempoDeTesteoEnMillis);
		stressGenerator.start();

		// Esperamos el tiempo de testeo necesario y detenemos las ejecuciones
		try {
			Thread.sleep(tiempoDeTesteoEnMillis);
		} catch (final InterruptedException e) {
			throw new RuntimeException("Se interrumpió la espera mientras se testeaba el procesador", e);
		}
		final long recibidasPorElProcesador = counterListener.getTasksAccepted().get();
		final long iniciadasPorElProcesador = counterListener.getTasksStarted().get();
		final long completadasPorElProcesador = counterListener.getTasksCompleted().get();
		LOG.debug("[{}]: Tareas recibidas: {}, iniciadas: {}, completadas: {}", new Object[] { nombreDeTest,
				recibidasPorElProcesador, iniciadasPorElProcesador, completadasPorElProcesador });

		stressGenerator.detenerThreads();
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
