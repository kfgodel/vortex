/**
 * 13/03/2012 22:49:10 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.climate;

import net.gaia.taskprocessor.api.TaskProcessor;
import net.gaia.taskprocessor.executor.ExecutorBasedTaskProcesor;
import net.gaia.vortex.climate.externals.ClimateMeasure;
import net.gaia.vortex.climate.externals.ClimateServer;
import net.gaia.vortex.climate.externals.ClimateStation;
import net.gaia.vortex.climate.externals.KnownClimateStation;
import net.gaia.vortex.climate.tasks.ActualizarClimaActualWorkUnit;
import net.gaia.vortex.climate.tasks.EnviarNuevaMedicionWorkUnit;
import net.gaia.vortex.hilevel.api.ClienteVortex;
import net.gaia.vortex.hilevel.api.HandlerDeMensajesApi;
import net.gaia.vortex.hilevel.api.MensajeVortexApi;
import net.gaia.vortex.hilevel.api.entregas.ReporteDeEntregaApi;
import net.gaia.vortex.hilevel.api.impl.ClienteVortexImpl;
import net.gaia.vortex.hilevel.api.impl.ConfiguracionClienteVortex;
import net.gaia.vortex.http.api.ConfiguracionDeNodoRemotoHttp;
import net.gaia.vortex.http.api.NodoRemotoHttp;
import net.gaia.vortex.http.api.config.ConfiguracionSinEncriptacion;
import net.gaia.vortex.lowlevel.api.NodoVortex;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Sets;

/**
 * Esta clase representa el programa que corre como instancia autónoma para informar el clima
 * 
 * @author D. García
 */
public class VortexClimateMain implements HandlerDeMensajesApi {
	private static final Logger LOG = LoggerFactory.getLogger(VortexClimateMain.class);

	public static VortexClimateMain I;

	private TaskProcessor processor;
	private ClienteVortex clienteVortex;
	private ClimateStation buenosAiresStation;

	public static void main(final String[] args) {
		I = VortexClimateMain.create();
	}

	public TaskProcessor getProcessor() {
		return processor;
	}

	public void setProcessor(final TaskProcessor processor) {
		this.processor = processor;
	}

	public ClienteVortex getClienteVortex() {
		return clienteVortex;
	}

	public void setClienteVortex(final ClienteVortex clienteVortex) {
		this.clienteVortex = clienteVortex;
	}

	public ClimateStation getBuenosAiresStation() {
		return buenosAiresStation;
	}

	public void setBuenosAiresStation(final ClimateStation buenosAiresStation) {
		this.buenosAiresStation = buenosAiresStation;
	}

	public static VortexClimateMain create() {
		final VortexClimateMain main = new VortexClimateMain();
		main.initialize();
		return main;
	}

	/**
	 * Inicializa esta instancia para funcionar global
	 */
	private void initialize() {
		crearClienteVortex();
		createClimateStation();
		createProcessor();
		createUpdateTask();
	}

	/**
	 * 
	 */
	private void createUpdateTask() {
		final ActualizarClimaActualWorkUnit actualizarClima = ActualizarClimaActualWorkUnit.create(processor);
		processor.process(actualizarClima);
	}

	/**
	 * 
	 */
	private void createProcessor() {
		processor = ExecutorBasedTaskProcesor.create();
	}

	/**
	 * 
	 */
	private void createClimateStation() {
		final ClimateServer server = ClimateServer.create();
		buenosAiresStation = server.getStation(KnownClimateStation.BUENOS_AIRES);
	}

	/**
	 * 
	 */
	private void crearClienteVortex() {
		final ConfiguracionDeNodoRemotoHttp configuracionNodo = ConfiguracionSinEncriptacion.create(
				"http://kfgodel.info/vortex/controllers/naked", null);
		final NodoVortex nodoOnline = NodoRemotoHttp.create(configuracionNodo, "Nodo vortex online");
		final ConfiguracionClienteVortex configuracionCliente = ConfiguracionClienteVortex.create(nodoOnline, this);
		clienteVortex = ClienteVortexImpl.create(configuracionCliente);
		clienteVortex.getFiltroDeMensajes().agregarATagsActivos(
				Sets.newHashSet("VORTEX.CLIMATE.BUENOS_AIRES.REQUEST_MEASURE"));
	}

	/**
	 * @see net.gaia.vortex.hilevel.api.HandlerDeMensajesApi#onMensajeRecibido(net.gaia.vortex.hilevel.api.MensajeVortexApi)
	 */
	@Override
	public void onMensajeRecibido(final MensajeVortexApi mensajeRecibido) {
		// No revisamos qué tipo de mensaje es, como por ahora solo nos interesa un tag asumimos que
		// nos piden el clima
		final ClimateMeasure lastMeasure = this.buenosAiresStation.getLastMeasure();
		final EnviarNuevaMedicionWorkUnit enviarMedicion = EnviarNuevaMedicionWorkUnit.create(lastMeasure);
		this.processor.process(enviarMedicion);
	}

	/**
	 * @see net.gaia.vortex.hilevel.api.HandlerDeMensajesApi#onReporteDeEntregaRecibido(net.gaia.vortex.hilevel.api.entregas.ReporteDeEntregaApi)
	 */
	@Override
	public void onReporteDeEntregaRecibido(final ReporteDeEntregaApi reporte) {
		LOG.debug("Mensaje vortex entregado?: {}", reporte.fueExitoso());
		final Long cantidadConsumidos = reporte.getCantidadConsumidos();
		if (cantidadConsumidos != null && cantidadConsumidos.longValue() > 0) {
			LOG.info("Clima informado a {} interesados", cantidadConsumidos);
		}
	}
}
