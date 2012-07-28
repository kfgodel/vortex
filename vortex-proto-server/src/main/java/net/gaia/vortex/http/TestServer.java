package net.gaia.vortex.http;

import net.gaia.taskprocessor.api.TaskProcessor;
import net.gaia.vortex.core.external.VortexProcessorFactory;
import net.gaia.vortex.core.impl.moleculas.NodoMultiplexor;
import net.gaia.vortex.server.impl.RealizarConexiones;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;

public class TestServer {

	public static void main(final String[] args) throws Exception {
		final TaskProcessor procesador = VortexProcessorFactory.createProcessor();
		final NodoMultiplexor nodoCentral = NodoMultiplexor.create(procesador);
		final Handler vortexHttpHandler = VortexHttpHandler.create(procesador, RealizarConexiones.con(nodoCentral));
		final Server server = new Server(8080);
		server.setHandler(vortexHttpHandler);
		server.start();
		server.join();
	}
}