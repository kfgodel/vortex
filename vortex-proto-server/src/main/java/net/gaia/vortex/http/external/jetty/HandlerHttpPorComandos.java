/**
 * 25/07/2012 12:15:27 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.http.external.jetty;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.gaia.vortex.http.respuestas.RespuestaDeErrorDelServidor;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Esta clase implementa el handler de requests http de Jetty, interpretando cada request como un
 * comando ejecutable, del cual se obtendrá un resultado enviable como response
 * 
 * @author D. García
 */
public abstract class HandlerHttpPorComandos extends AbstractHandler {
	private static final Logger LOG = LoggerFactory.getLogger(HandlerHttpPorComandos.class);

	/**
	 * @see org.eclipse.jetty.server.Handler#handle(java.lang.String,
	 *      org.eclipse.jetty.server.Request, javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public void handle(final String target, final Request baseRequest, final HttpServletRequest request,
			final HttpServletResponse response) throws IOException, ServletException {
		LOG.debug("Nuevo request recibido: {} \"{}\" ", baseRequest.getMethod(), baseRequest.getUri());
		try {
			final ComandoHttp comandoRecibido = interpretarComandoDesde(target, baseRequest);
			LOG.debug("Comando elegido: {}", comandoRecibido);
			final RespuestaHttp respuesta = comandoRecibido.ejecutar();
			LOG.debug("Respuesta generada: {}", respuesta);
			respuesta.reflejarEn(response);
		} catch (final Throwable e) {
			LOG.error("Se produjo un error interno no esperado: " + e.getMessage(), e);
			final RespuestaDeErrorDelServidor respuestaDeError = RespuestaDeErrorDelServidor.create(e);
			respuestaDeError.reflejarEn(response);
		}
		baseRequest.setHandled(true);
	}

	/**
	 * Genera un comando como interpretación del request recibido
	 * 
	 * @param target
	 *            La URL solicitada relativa al servidor
	 * @param baseRequest
	 *            El request
	 * @return El comando http interpretado que debe ser ejecutado
	 */
	protected abstract ComandoHttp interpretarComandoDesde(final String target, final Request baseRequest);
}
