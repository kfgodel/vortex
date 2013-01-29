/**
 * 26/01/2013 15:07:44 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.http.impl.moleculas;

import net.gaia.taskprocessor.api.TaskProcessor;
import net.gaia.vortex.http.impl.VortexHttpException;
import net.gaia.vortex.http.impl.server.ServidorDeNexoHttp;
import net.gaia.vortex.router.impl.moleculas.support.RouterSupport;
import net.gaia.vortex.server.impl.RealizarConexiones;

/**
 * Esta clase representa el router de mensajes bidi que acepta conexiones entrantes por http
 * 
 * @author D. García
 */
public class RouterServerHttp extends RouterSupport {

	private ServidorDeNexoHttp servidorInterno;

	/**
	 * Crea un nuevo nodo servidor http que aceptará requests externos en el puerto indicado
	 * 
	 * @param listeningPort
	 *            El puerto en el que se escucharán requests http
	 * @param processor
	 *            El procesador utilizado para procesar los mensajes vortex
	 * @return El nodo servidor iniciado y escuchando conexiones
	 * @throws VortexHttpException
	 *             Si no se puede iniciar el servidor
	 */
	public static RouterServerHttp createAndAcceptRequestsOnPort(final int listeningPort, final TaskProcessor processor)
			throws VortexHttpException {
		final RouterServerHttp nodoHttp = new RouterServerHttp();
		nodoHttp.initializeWith(processor);
		nodoHttp.servidorInterno = ServidorDeNexoHttp
				.create(processor, listeningPort, RealizarConexiones.con(nodoHttp));
		nodoHttp.servidorInterno.iniciarServidorHttp();
		return nodoHttp;
	}

	/**
	 * Cierra el puerto de escucha de requests y libera los recursos de este nodo
	 */
	public void cerrarYLiberar() throws VortexHttpException {
		this.servidorInterno.detenerServidor();
	}

}
