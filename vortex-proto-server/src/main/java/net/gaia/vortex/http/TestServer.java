package net.gaia.vortex.http;

import net.gaia.taskprocessor.api.TaskProcessor;
import net.gaia.vortex.core.external.VortexProcessorFactory;
import net.gaia.vortex.http.impl.moleculas.NodoServerHttp;

public class TestServer {

	public static void main(final String[] args) throws Exception {
		final TaskProcessor procesador = VortexProcessorFactory.createProcessor();
		final NodoServerHttp nodoServer = NodoServerHttp.createAndAcceptRequestsOnPort(8080, procesador);

		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				nodoServer.cerrarYLiberar();
			}
		});
	}
}