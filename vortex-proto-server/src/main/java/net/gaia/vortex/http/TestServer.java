package net.gaia.vortex.http;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.AbstractHandler;

public class TestServer extends AbstractHandler {
	@Override
	public void handle(final String target, final Request baseRequest, final HttpServletRequest request,
			final HttpServletResponse response) throws IOException, ServletException {
		response.setContentType("text/html;charset=utf-8");
		response.setStatus(HttpServletResponse.SC_OK);
		baseRequest.setHandled(true);
		final String textoDeRespuesta = decidirRespuestaSegunUrl(target, baseRequest);
		response.getWriter().println(textoDeRespuesta);
	}

	private String decidirRespuestaSegunUrl(final String target, final Request request) {
		if ("/vortex/create".equals(target)) {
			return "Devolviendo id N";
		}
		if (target.startsWith("/vortex/session/")) {
			final String sessionId = target.substring("/vortex/session/".length(), target.length());
			return "Intercambiando mensajes para la sesion: " + sessionId;
		}
		if (target.startsWith("/vortex/destroy/")) {
			final String sessionId = target.substring("/vortex/destroy/".length(), target.length());
			return "Eliminando la sesion: " + sessionId;
		}
		return "No se entendio el pedido: " + target;
	}

	public static void main(final String[] args) throws Exception {
		final Server server = new Server(8080);

		server.setHandler(new TestServer());

		server.start();
		server.join();
	}
}