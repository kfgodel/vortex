/**
 * 21/06/2012 10:59:22 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.server.mosquito;

import java.net.SocketAddress;
import java.util.concurrent.TimeUnit;

import net.gaia.taskprocessor.api.InterruptedThreadException;
import net.gaia.taskprocessor.api.SubmittedTask;
import net.gaia.taskprocessor.api.WorkParallelizer;
import net.gaia.taskprocessor.api.WorkUnit;
import net.gaia.taskprocessor.api.processor.TaskProcessor;
import net.gaia.vortex.core.external.VortexProcessorFactory;
import net.gaia.vortex.core.prog.Loggers;
import net.gaia.vortex.http.impl.moleculas.RouterServerHttp;
import net.gaia.vortex.server.mosquito.config.ContextConfiguration;
import net.gaia.vortex.server.mosquito.listeners.LoguearCambiosDeFiltrosRemotos;
import net.gaia.vortex.server.mosquito.listeners.LoguearRuteos;
import net.gaia.vortex.sockets.impl.moleculas.RouterSocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ar.com.dgarcia.lang.metrics.MetricasPorTiempo;
import ar.com.dgarcia.lang.metrics.impl.MetricasDeCargaImpl;
import ar.com.dgarcia.lang.strings.ToString;
import ar.com.dgarcia.lang.time.TimeMagnitude;

/**
 * Esta clase representa el servidor funcionando en mosquito para los sockets vortex
 * 
 * @author D. García
 */
public class MosquitoSever {
	private static final double KILOS_PER_MEGA = 1024d;
	private static final double BYTES_PER_KILO = 1024d;
	private static final int MILLIS_PER_SECOND = 1000;

	private static final TimeMagnitude PERIODO_ENTRE_LOGS_DE_TRANSFER = TimeMagnitude.of(5, TimeUnit.SECONDS);

	private static final Logger TRANSFER = LoggerFactory.getLogger("net.gaia.vortex.meta.Loggers.TRANSFER");

	private ContextConfiguration configuration;
	public static final String configuration_FIELD = "configuration";

	private RouterServerHttp hubDeHttp;
	public static final String hubDeHttp_FIELD = "hubDeHttp";

	private RouterSocket hubDeSockets;
	public static final String hubDeSockets_FIELD = "hubDeSockets";

	private TaskProcessor processor;

	public static MosquitoSever create(final ContextConfiguration configuration) {
		final MosquitoSever server = new MosquitoSever();
		server.configuration = configuration;
		server.processor = VortexProcessorFactory.createProcessor();
		return server;
	}

	/**
	 * Controlador de la tarea para detenerla
	 */
	private SubmittedTask controlDeTareDeLog;

	/**
	 * Tarea para registrar velocidades en log
	 */
	private final WorkUnit tareaDeLogDeTransfer = new WorkUnit() {
		public void doWork(final WorkParallelizer parallelizer) throws InterruptedThreadException {
			registrarTransferEnLog();
		}
	};

	/**
	 * @see java.lang.Object#toString()
	 */

	@Override
	public String toString() {
		return ToString.de(this).con(configuration_FIELD, configuration).con(hubDeSockets_FIELD, hubDeSockets)
				.con(hubDeHttp_FIELD, hubDeHttp).toString();
	}

	/**
	 * Registra en el log los datos de transferencia actuales y acumulados
	 */
	protected void registrarTransferEnLog() {
		escribirTransfersEnLog();
		planificarProximoLogDeTransfer();
	}

	/**
	 * Registra los valores de transferencias en el log
	 */
	private void escribirTransfersEnLog() {
		final MetricasDeCargaImpl metricas = hubDeSockets.getServidor().getMetricas();
		final MetricasPorTiempo ultimoSegundo = metricas.getMetricasEnBloqueDeUnSegundo();
		final double ultimoSegVelKiloInput = (ultimoSegundo.getVelocidadDeInput() * MILLIS_PER_SECOND) / BYTES_PER_KILO;
		final double ultimoSegVelKiloOutput = (ultimoSegundo.getVelocidadDeOutput() * MILLIS_PER_SECOND)
				/ BYTES_PER_KILO;

		final MetricasPorTiempo ultimos5 = metricas.getMetricasEnBloqueDe5Segundos();
		final double ultimos5SegsVelKiloInput = (ultimos5.getVelocidadDeInput() * MILLIS_PER_SECOND) / BYTES_PER_KILO;
		final double ultimos5SegsVelKiloOutput = (ultimos5.getVelocidadDeOutput() * MILLIS_PER_SECOND) / BYTES_PER_KILO;

		final MetricasPorTiempo totales = metricas.getMetricasTotales();
		final double totalesCantMegaInput = totales.getCantidadDeInputs() / (BYTES_PER_KILO * KILOS_PER_MEGA);
		final double totalesCantMegaOutput = totales.getCantidadDeOutputs() / (BYTES_PER_KILO * KILOS_PER_MEGA);
		final double totalesVelKiloInput = (totales.getVelocidadDeInput() * MILLIS_PER_SECOND) / BYTES_PER_KILO;
		final double totalesVelKiloOutput = (totales.getVelocidadDeOutput() * MILLIS_PER_SECOND) / BYTES_PER_KILO;

		TRANSFER.info(String.format(
				"I/O 1s:[%1$skB/s %2$skB/s] 5s:[%3$skB/s %4$skB/s] Ts:[%5$skB/s %6$skB/s, %7$sMB %8$sMB]",
				formatAsStringDecimal(ultimoSegVelKiloInput), formatAsStringDecimal(ultimoSegVelKiloOutput),
				formatAsStringDecimal(ultimos5SegsVelKiloInput), formatAsStringDecimal(ultimos5SegsVelKiloOutput),
				formatAsStringDecimal(totalesVelKiloInput), formatAsStringDecimal(totalesVelKiloOutput),
				formatAsStringDecimal(totalesCantMegaInput), formatAsStringDecimal(totalesCantMegaOutput)));
	}

	public String formatAsStringDecimal(final double numero) {
		final String numeroFormateado = String.format("%1$.3f", numero);
		final String paddeado = String.format("%1$8s", numeroFormateado);
		return paddeado;
	}

	/**
	 * Inicia este servidor aceptando conexiones entrantes al hub
	 */
	public void aceptarConexiones() {
		final SocketAddress listeningAddress = configuration.getListeningAddress();
		Loggers.RUTEO.info("Comenzando escucha de sockets en: {}", listeningAddress);
		hubDeSockets = RouterSocket.createAndListenTo(listeningAddress, processor);
		hubDeSockets.setListenerDeRuteos(LoguearRuteos.getInstancia());
		hubDeSockets.setListenerDeFiltrosRemotos(LoguearCambiosDeFiltrosRemotos.getInstancia());
		final Integer httpPort = configuration.getHttpListeningPort();
		if (httpPort == null) {
			// No se debe levantar el http
			return;
		}
		Loggers.RUTEO.info("Aceptando requests HTTP en: {}", httpPort);
		hubDeHttp = RouterServerHttp.createAndAcceptRequestsOnPort(httpPort, processor);
		hubDeHttp.setListenerDeRuteos(LoguearRuteos.getInstancia());
		hubDeHttp.setListenerDeFiltrosRemotos(LoguearCambiosDeFiltrosRemotos.getInstancia());

		// Interconectamos los nodos
		hubDeHttp.conectarCon(hubDeSockets);
		hubDeSockets.conectarCon(hubDeHttp);
		registrarTransferEnLog();
	}

	/**
	 * Comienza el registro por log separado de las tazas de transferencia
	 */
	private void planificarProximoLogDeTransfer() {
		controlDeTareDeLog = processor.processDelayed(PERIODO_ENTRE_LOGS_DE_TRANSFER, tareaDeLogDeTransfer);
	}

	/**
	 * Detiene las conexiones actuales forzadamente
	 */
	public void detenerConexiones() {
		detenerLogDeTransfer();
		if (hubDeHttp != null) {
			final Integer httpListeningPort = configuration.getHttpListeningPort();
			Loggers.RUTEO.info("Deteniendo server HTTP en: {}", httpListeningPort);
			hubDeSockets.desconectarDe(hubDeHttp);
			hubDeHttp.desconectarDe(hubDeSockets);
			hubDeHttp.cerrarYLiberar();
		}

		final SocketAddress listeningAddress = configuration.getListeningAddress();
		Loggers.RUTEO.info("Deteniendo escucha de sockets en: {}", listeningAddress);
		hubDeSockets.closeAndDispose();
	}

	/**
	 * Detiene la ejecución de la tarea para loguear la transferencia
	 */
	private void detenerLogDeTransfer() {
		if (controlDeTareDeLog != null) {
			controlDeTareDeLog.cancelExecution(true);
		}
	}
}
