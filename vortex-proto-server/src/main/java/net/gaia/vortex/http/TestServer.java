package net.gaia.vortex.http;

import org.eclipse.jetty.server.Server;

public class TestServer {

	public static void main(final String[] args) throws Exception {
		final Server server = new Server(8080);
		server.setHandler(VortexHttpHandler.create());

		server.start();
		server.join();
	}
}