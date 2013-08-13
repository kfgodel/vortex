/**
 * 06/08/2012 20:10:38 Copyright (C) 2011 Darío L. García
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

import net.gaia.taskprocessor.api.processor.TaskProcessor;
import net.gaia.taskprocessor.api.processor.TaskProcessorConfiguration;
import net.gaia.taskprocessor.forkjoin.ForkJoinTaskProcessor;
import net.gaia.vortex.core.api.mensaje.MensajeVortex;
import net.gaia.vortex.core.impl.atomos.support.basicos.ReceptorSupport;
import net.gaia.vortex.http.impl.moleculas.NodoClienteHttp;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ar.dgarcia.http.simple.impl.ApacheResponseProvider;

/**
 * Esta clase prueba la conexión con el
 * 
 * @author D. García
 */
@Ignore("Este test requiere el server mosquito para correr")
public class TestNodoClienteHttp {
	private static final Logger LOG = LoggerFactory.getLogger(TestNodoClienteHttp.class);

	private TaskProcessor procesador;

	@Before
	public void crearProcesador() {
		procesador = ForkJoinTaskProcessor.create(TaskProcessorConfiguration.createOptimun());
	}

	@After
	public void detenerProcesador() {
		procesador.detener();
	}

	@Test
	public void deberiaPoderMandarYRecibirEntreDosClientesNexo() throws InterruptedException {
		final NodoClienteHttp nodoCliente = NodoClienteHttp.createAndConnectTo("http://kfgodel.info:62626", procesador,
				ApacheResponseProvider.create());
		nodoCliente.conectarCon(new ReceptorSupport() {

			public void recibir(final MensajeVortex mensaje) {
				LOG.info("Recibido en nexo http: {}", mensaje);
			}
		});
		Thread.sleep(2 * 60 * 1000);

		nodoCliente.cerrarYLiberar();
	}
}
