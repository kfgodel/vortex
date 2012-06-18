/**
 * 26/05/2012 00:31:42 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.core3.tests;

import java.util.concurrent.atomic.AtomicLong;

import net.gaia.taskprocessor.api.TaskProcessor;
import net.gaia.taskprocessor.api.TaskProcessorConfiguration;
import net.gaia.taskprocessor.executor.ExecutorBasedTaskProcesor;
import net.gaia.vortex.core3.api.Nodo;
import net.gaia.vortex.core3.api.atomos.Receptor;
import net.gaia.vortex.core3.api.mensaje.MensajeVortex;
import net.gaia.vortex.core3.api.metricas.MetricasDelNodo;
import net.gaia.vortex.core3.api.metricas.MetricasPorTiempo;
import net.gaia.vortex.core3.api.moleculas.portal.Portal;
import net.gaia.vortex.core3.impl.atomos.ReceptorNulo;
import net.gaia.vortex.core3.impl.atomos.forward.NexoEjecutor;
import net.gaia.vortex.core3.impl.condiciones.SoloInstancias;
import net.gaia.vortex.core3.impl.moleculas.portal.HandlerTipado;
import net.gaia.vortex.core3.impl.moleculas.portal.PortalMapeador;
import net.gaia.vortex.core3.impl.moleculas.ruteo.HubConNexo;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ar.com.dgarcia.lang.time.SystemChronometer;
import ar.com.dgarcia.testing.stress.StressGenerator;

/**
 * Esta clase intenta medir la performance de ruteo de los mensajes en varios casos
 * 
 * @author D. García
 */
public class TestMessagePerformance {
	private static final Logger LOG = LoggerFactory.getLogger(TestMessagePerformance.class);

	private HubConNexo ruteadorCentral;
	private Portal nodoEmisor;
	private Portal nodoReceptor;
	private AtomicLong contadorTotalDeRecibidos;
	private TaskProcessor processor;

	@Before
	public void crearRuteadorCentral() {
		final TaskProcessorConfiguration config = TaskProcessorConfiguration.create();
		config.setThreadPoolSize(4);
		processor = ExecutorBasedTaskProcesor.create(config);
		ruteadorCentral = HubConNexo.create(processor);
		nodoEmisor = PortalMapeador.create(processor, ruteadorCentral);
		nodoReceptor = PortalMapeador.create(processor, ruteadorCentral);
		contadorTotalDeRecibidos = new AtomicLong();
	}

	@After
	public void eliminarRuteadorCentral() {
		processor.detener();
	}

	@Test
	public void UnThreadGeneradorDiezNodosReceptoresSinEspera() throws InterruptedException {
		final int cantidadDeThreadsGeneradoresDeMensajes = 1;
		final int esperaEntreMensajesEnMillis = 0;
		final int cantidadDeNodosReceptores = 10;
		final int cantidadDeIntermediarios = 1;
		armarTopologia(cantidadDeIntermediarios, cantidadDeNodosReceptores);
		final String nombreDelTest = crearNombreDelTest(cantidadDeThreadsGeneradoresDeMensajes,
				esperaEntreMensajesEnMillis, cantidadDeIntermediarios, cantidadDeNodosReceptores);
		procesarYmedirRuteos(cantidadDeThreadsGeneradoresDeMensajes, esperaEntreMensajesEnMillis, nombreDelTest);
	}

	@Test
	public void UnThreadGeneradorUnNodoReceptorSinEsperaEntreMensajes() throws InterruptedException {
		final int cantidadDeThreadsGeneradoresDeMensajes = 1;
		final int esperaEntreMensajesEnMillis = 0;
		final int cantidadDeNodosReceptores = 1;
		final int cantidadDeIntermediarios = 1;
		armarTopologia(cantidadDeIntermediarios, cantidadDeNodosReceptores);
		final String nombreDelTest = crearNombreDelTest(cantidadDeThreadsGeneradoresDeMensajes,
				esperaEntreMensajesEnMillis, cantidadDeIntermediarios, cantidadDeNodosReceptores);
		procesarYmedirRuteos(cantidadDeThreadsGeneradoresDeMensajes, esperaEntreMensajesEnMillis, nombreDelTest);
	}

	@Test
	public void DiezThreadsGeneradoresDiezNodosReceptoresConEsperadDe1ms() throws InterruptedException {
		final int cantidadDeThreadsGeneradoresDeMensajes = 10;
		final int esperaEntreMensajesEnMillis = 1;
		final int cantidadDeNodosReceptores = 10;
		final int cantidadDeIntermediarios = 1;
		armarTopologia(cantidadDeIntermediarios, cantidadDeNodosReceptores);
		final String nombreDelTest = crearNombreDelTest(cantidadDeThreadsGeneradoresDeMensajes,
				esperaEntreMensajesEnMillis, cantidadDeIntermediarios, cantidadDeNodosReceptores);
		procesarYmedirRuteos(cantidadDeThreadsGeneradoresDeMensajes, esperaEntreMensajesEnMillis, nombreDelTest);
	}

	@Test
	public void UnThreadGeneradorUnNodoReceptorDiezInterMediariosIndependientesSinEspera() throws InterruptedException {
		final int cantidadDeThreadsGeneradoresDeMensajes = 1;
		final int esperaEntreMensajesEnMillis = 0;
		final int cantidadDeIntermediarios = 10;
		final int cantidadDeNodosReceptores = 1;
		armarTopologia(cantidadDeIntermediarios, cantidadDeNodosReceptores);
		final String nombreDelTest = crearNombreDelTest(cantidadDeThreadsGeneradoresDeMensajes,
				esperaEntreMensajesEnMillis, cantidadDeIntermediarios, cantidadDeNodosReceptores);
		procesarYmedirRuteos(cantidadDeThreadsGeneradoresDeMensajes, esperaEntreMensajesEnMillis, nombreDelTest);
	}

	@Test
	public void DiezThreadGeneradorUnNodoReceptorDiezInterMediariosIndependientesConEsperadDe1ms()
			throws InterruptedException {
		final int cantidadDeThreadsGeneradoresDeMensajes = 10;
		final int esperaEntreMensajesEnMillis = 1;
		final int cantidadDeIntermediarios = 10;
		final int cantidadDeNodosReceptores = 1;
		armarTopologia(cantidadDeIntermediarios, cantidadDeNodosReceptores);
		final String nombreDelTest = crearNombreDelTest(cantidadDeThreadsGeneradoresDeMensajes,
				esperaEntreMensajesEnMillis, cantidadDeIntermediarios, cantidadDeNodosReceptores);
		procesarYmedirRuteos(cantidadDeThreadsGeneradoresDeMensajes, esperaEntreMensajesEnMillis, nombreDelTest);
	}

	private String crearNombreDelTest(final int cantidadDeThreadsGeneradoresDeMensajes,
			final int esperaEntreMensajesEnMillis, final int cantidadDeIntermediarios,
			final int cantidadDeNodosReceptores) {
		final String nombreDelTest = cantidadDeThreadsGeneradoresDeMensajes + "/" + esperaEntreMensajesEnMillis
				+ "ms ->(" + cantidadDeIntermediarios + ") ->" + cantidadDeNodosReceptores;
		return nombreDelTest;
	}

	private void armarTopologia(final int cantidadDeIntermediarios, final int cantidadDeNodosReceptores) {
		interconectarCon(nodoEmisor, ruteadorCentral);

		Nodo ultimoIntermediario = ruteadorCentral;
		for (int i = 1; i < cantidadDeIntermediarios; i++) {
			final HubConNexo intermediarioAdicional = HubConNexo.create(processor);
			interconectarCon(ultimoIntermediario, intermediarioAdicional);
			ultimoIntermediario = intermediarioAdicional;
		}
		nodoReceptor.desconectarDe(ruteadorCentral);
		interconectarCon(nodoReceptor, ultimoIntermediario);

		// Agregamos los otros receptores nulos al último nodo ruteador
		for (int i = 1; i < cantidadDeNodosReceptores; i++) {
			final HubConNexo receptorAdicional = HubConNexo.create(processor);
			final Receptor incrementarRecibidos = new Receptor() {
				@Override
				public void recibir(final MensajeVortex mensaje) {
					contadorTotalDeRecibidos.incrementAndGet();
				}
			};
			receptorAdicional.setNexoCore(NexoEjecutor.create(processor, incrementarRecibidos,
					ReceptorNulo.getInstancia()));
			interconectarCon(ultimoIntermediario, receptorAdicional);
		}
	}

	/**
	 * Realiza los ruteos con threads en paralelo en base a los parámetros indicados y la red
	 * definida
	 * 
	 * @param cantidadDeThreadsGeneradoresDeMensajes
	 *            La cantidad de threads para generar mensajes en paralelo
	 * @param esperaEntreMensajesEnMillis
	 *            La espera que cada thread realiza entre mensajes en milis
	 * @param nombreDelTest
	 *            El nombre de este test
	 * @throws InterruptedException
	 *             Si se interrumpe la espera mientras se envían mensajes
	 */
	private void procesarYmedirRuteos(final int cantidadDeThreadsGeneradoresDeMensajes,
			final int esperaEntreMensajesEnMillis, final String nombreDelTest) throws InterruptedException {
		final StressGenerator stressGenerator = StressGenerator.create();
		stressGenerator.setCantidadDeThreadsEnEjecucion(cantidadDeThreadsGeneradoresDeMensajes);
		stressGenerator.setEsperaEntreEjecucionesEnMilis(esperaEntreMensajesEnMillis);

		// Contabilizamos cada mensaje enviado
		final AtomicLong contadorDeEnviados = new AtomicLong();
		final Object mensaje = "Hola mundo!";
		stressGenerator.setEjecutable(new Runnable() {
			@Override
			public void run() {
				contadorDeEnviados.incrementAndGet();
				nodoEmisor.enviar(mensaje);
			}
		});
		// Y cada recibido
		final AtomicLong contadorIndividialDeRecibidos = new AtomicLong();
		nodoReceptor.recibirCon(new HandlerTipado<String>(SoloInstancias.de(String.class)) {
			@Override
			public void onMensajeRecibido(final String mensaje) {
				contadorIndividialDeRecibidos.incrementAndGet();
				contadorTotalDeRecibidos.incrementAndGet();
			}
		});

		// Comenzamos el envío de los mensajes
		LOG.debug("[{}]: Iniciando mediciones...", nombreDelTest);
		final SystemChronometer crono = SystemChronometer.create();
		stressGenerator.start();

		// Esperamos 10s para medir la perf
		Thread.sleep(10000);
		final long cantidadIndividualDeRecibidos = contadorIndividialDeRecibidos.get();
		final long cantidadEnviada = contadorDeEnviados.get();
		final long cantidadTotalDeRecibidos = contadorTotalDeRecibidos.get();
		final long millisTranscurridos = crono.getElapsedMillis();
		LOG.info("[{}]: En {} ms se enviaron {} mensajes y se recibieron {} de {} recepciones", new Object[] {
				nombreDelTest, millisTranscurridos, cantidadEnviada, cantidadIndividualDeRecibidos,
				cantidadTotalDeRecibidos });
		final double deliveryMeasure = (100d * cantidadIndividualDeRecibidos) / cantidadEnviada;
		final double inputMeasure = ((double) cantidadEnviada) / millisTranscurridos;
		final double individualOutputMeasure = ((double) cantidadIndividualDeRecibidos) / millisTranscurridos;
		final double totalOutputMeasure = ((double) cantidadTotalDeRecibidos) / millisTranscurridos;
		LOG.info("[{}]: Delivery:{}% Input:{} msg/ms Output(i):{} msg/ms OutputT:{} msg/ms", new Object[] {
				nombreDelTest, deliveryMeasure, inputMeasure, individualOutputMeasure, totalOutputMeasure });

		final MetricasDelNodo metricas = ruteadorCentral.getMetricas();
		final MetricasPorTiempo metricasTotales = metricas.getMetricasTotales();
		LOG.debug("[{}]: En nodo Totales - Delivery:{}% Input:{} msg/ms Output(i):{} msg/ms", new Object[] {
				nombreDelTest, metricasTotales.getTasaDeDelivery() * 100, metricasTotales.getVelocidadDeRecepcion(),
				metricasTotales.getVelocidadDeEnvio() });
		final MetricasPorTiempo cada10 = metricas.getMetricasEnBloqueDe5Segundos();
		LOG.debug("[{}]: En nodo Cada 10s - Delivery:{}% Input:{} msg/ms Output(i):{} msg/ms",
				new Object[] { nombreDelTest, cada10.getTasaDeDelivery() * 100, cada10.getVelocidadDeRecepcion(),
						cada10.getVelocidadDeEnvio() });
		final MetricasPorTiempo cada1 = metricas.getMetricasEnBloqueDeUnSegundo();
		LOG.debug(
				"[{}]: En nodo Cada 1s - Delivery:{}% Input:{} msg/ms Output(i):{} msg/ms",
				new Object[] { nombreDelTest, cada1.getTasaDeDelivery() * 100, cada1.getVelocidadDeRecepcion(),
						cada1.getVelocidadDeEnvio() });

		stressGenerator.detenerThreads();
	}

	/**
	 * Crea una conexión bidireccional entre los nodos pasados
	 * 
	 * @param nodoOrigen
	 *            Uno de los nodos
	 * @param nodoDestino
	 *            El otro
	 */
	private void interconectarCon(final Nodo nodoOrigen, final Nodo nodoDestino) {
		nodoOrigen.conectarCon(nodoDestino);
		nodoDestino.conectarCon(nodoOrigen);
	}

}
