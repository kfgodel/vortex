/**
 * 29/07/2012 00:59:51 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.http.impl.server;

import net.gaia.taskprocessor.api.processor.TaskProcessor;
import net.gaia.vortex.deprecated.EstrategiaDeConexionDeNexosViejo;
import net.gaia.vortex.http.api.ServidorDeHttpVortex;
import net.gaia.vortex.http.impl.VortexHttpException;
import net.gaia.vortex.http.impl.moleculas.NexoHttp;

import org.eclipse.jetty.server.Server;

/**
 * Esta clase representa la implementación del servidor http vortex utilizando un servidor jetty
 * desde el cual se crean {@link NexoHttp} por las sesiones creadas
 * 
 * @author D. García
 */
public class ServidorDeNexoHttp implements ServidorDeHttpVortex {

	private int listeningPort;
	public static final String listeningPort_FIELD = "listeningPort";
	private Server internalServer;
	private VortexHttpHandler vortexHttpHandler;

	/**
	 * @see net.gaia.vortex.deprecated.GeneradorDeNexosViejo#getEstrategiaDeConexion()
	 */
	
	public EstrategiaDeConexionDeNexosViejo getEstrategiaDeConexion() {
		return this.vortexHttpHandler.getEstrategiaDeConexion();
	}

	/**
	 * @see net.gaia.vortex.deprecated.GeneradorDeNexosViejo#setEstrategiaDeConexion(net.gaia.vortex.deprecated.EstrategiaDeConexionDeNexosViejo)
	 */
	
	public void setEstrategiaDeConexion(final EstrategiaDeConexionDeNexosViejo estrategia) {
		this.vortexHttpHandler.setEstrategiaDeConexion(estrategia);
	}

	/**
	 * @see net.gaia.vortex.http.api.ServidorDeHttpVortex#iniciarServidorHttp()
	 */
	
	public void iniciarServidorHttp() throws VortexHttpException {
		try {
			internalServer = new Server(listeningPort);
			internalServer.setHandler(vortexHttpHandler);
			internalServer.start();
		} catch (final Exception e) {
			throw new VortexHttpException("Se produjo un error al intentar iniciar el servidor jetty para el puerto: "
					+ listeningPort, e);
		}
	}

	/**
	 * @see net.gaia.vortex.http.api.ServidorDeHttpVortex#detenerServidor()
	 */
	
	public void detenerServidor() throws VortexHttpException {
		try {
			this.internalServer.stop();
		} catch (final Exception e) {
			throw new VortexHttpException("Se produjo un error al detener el servidor jetty del puerto: "
					+ listeningPort, e);
		}
		this.vortexHttpHandler.detenerYLiberarRecursos();
	}

	public static ServidorDeNexoHttp create(final TaskProcessor processor, final int listeningPort,
			final EstrategiaDeConexionDeNexosViejo estrategiaDeConexion) {
		final ServidorDeNexoHttp servidor = new ServidorDeNexoHttp();
		servidor.listeningPort = listeningPort;
		servidor.vortexHttpHandler = VortexHttpHandler.create(processor, estrategiaDeConexion);
		return servidor;
	}
}
