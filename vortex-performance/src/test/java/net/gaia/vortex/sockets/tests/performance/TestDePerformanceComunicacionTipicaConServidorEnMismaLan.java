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

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;

/**
 * Esta clase prueba una comunicación tipica 1 a 1, utilizando un servidor vortex con nodosockets y
 * con portales como input/output.<br>
 * En este test el servidor es levantado en la misma VM
 * 
 * @author D. García
 */
@Ignore("Este test sólo debe correrse si existe un servidor en la misma lan levantado")
public class TestDePerformanceComunicacionTipicaConServidorEnMismaLan extends TestDeComunicacionTipicaSupport {

	@Before
	public void crearProcesador() {
		final InetSocketAddress sharedAddress = new InetSocketAddress("192.168.1.101", 61616);

		crearNodosClientes(sharedAddress);
	}

	@Override
	@After
	public void liberarRecursos() {
		super.liberarRecursos();
	}
}
