/**
 * 17/07/2012 23:47:34 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.sockets.tests.performance;

import java.net.InetSocketAddress;
import java.net.SocketAddress;

import net.gaia.taskprocessor.api.processor.TaskProcessor;
import net.gaia.vortex.impl.helpers.VortexProcessorFactory;
import net.gaia.vortex.sockets.impl.moleculas.NodoSocketViejo;

import org.junit.After;
import org.junit.Before;

/**
 * Esta clase prueba una comunicación tipica 1 a 1, utilizando un servidor vortex con nodosockets y
 * con portales como input/output.<br>
 * En este test el servidor es levantado en la misma VM
 * 
 * @author D. García
 */
public class TestDePerformanceComunicacionTipicaConServidorEmbebido extends TestDeComunicacionTipicaSupport {

	private TaskProcessor procesadorDelNodoServidor;
	private NodoSocketViejo nodoServidor;

	@Before
	public void crearProcesador() {
		final SocketAddress sharedAddress = new InetSocketAddress(10000);

		procesadorDelNodoServidor = VortexProcessorFactory.createProcessor();
		nodoServidor = NodoSocketViejo.createAndListenTo(sharedAddress, procesadorDelNodoServidor);

		crearNodosClientes(sharedAddress);
	}

	@Override
	@After
	public void liberarRecursos() {
		super.liberarRecursos();
		nodoServidor.closeAndDispose();
		procesadorDelNodoServidor.detener();
	}
}
