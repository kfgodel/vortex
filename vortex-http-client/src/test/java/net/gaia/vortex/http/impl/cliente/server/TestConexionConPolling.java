/**
 * 05/08/2012 18:18:59 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.http.impl.cliente.server;

import java.util.List;

import net.gaia.taskprocessor.api.TaskProcessor;
import net.gaia.taskprocessor.executor.ExecutorBasedTaskProcesor;
import net.gaia.vortex.core.api.mensaje.MensajeVortex;
import net.gaia.vortex.http.external.json.JacksonHttpTextualizer;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ar.dgarcia.http.simple.impl.ApacheResponseProvider;

/**
 * Esta clase prueba la conexión http con polling
 * 
 * @author D. García
 */
public class TestConexionConPolling {
	private static final Logger LOG = LoggerFactory.getLogger(TestConexionConPolling.class);

	private TaskProcessor procesador;

	@Before
	public void crearProcesador() {
		procesador = ExecutorBasedTaskProcesor.createOptimun();
	}

	@After
	public void detenerProcesador() {
		procesador.detener();
	}

	@Test
	public void deberiaRecibirLosMensajesDelServer() throws InterruptedException {
		final ServerVortexHttpRemoto servidor = ServerVortexHttpRemoto.create("http://kfgodel.info:62626",
				ApacheResponseProvider.create());
		final ConexionConPollingHttpCliente conexion = ConexionConPollingHttpCliente.create(procesador, servidor,
				JacksonHttpTextualizer.create(), new HandlerHttpDeMensajesRecibidos() {
					@Override
					public void onMensajesRecibidos(final List<MensajeVortex> mensajesRecibidos) {
						LOG.info("Recibidos del server: {}", mensajesRecibidos);
					}
				});
		conexion.iniciarConexion();
		Thread.sleep(2 * 60 * 1000);
		conexion.terminarConexion();
	}

}
