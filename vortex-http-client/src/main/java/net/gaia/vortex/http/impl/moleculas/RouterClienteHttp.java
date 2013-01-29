/**
 * 26/01/2013 15:09:41 Copyright (C) 2011 Darío L. García
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
import net.gaia.vortex.http.api.ClienteDeHttpVortex;
import net.gaia.vortex.http.impl.VortexHttpException;
import net.gaia.vortex.http.impl.cliente.ClienteDeNexoHttp;
import net.gaia.vortex.router.impl.moleculas.support.RouterSupport;
import net.gaia.vortex.server.impl.RealizarConexiones;
import ar.dgarcia.http.client.api.HttpResponseProvider;

/**
 * Esta clase representa el router de mensajes bidi que se conecta como cliente por http
 * 
 * @author D. García
 */
public class RouterClienteHttp extends RouterSupport {

	private ClienteDeHttpVortex clienteInterno;

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
	public static RouterClienteHttp createAndConnectTo(final String serverUrl, final TaskProcessor processor,
			final HttpResponseProvider provider) throws VortexHttpException {

		final RouterClienteHttp nodoHttp = new RouterClienteHttp();
		nodoHttp.initializeWith(processor);
		nodoHttp.clienteInterno = ClienteDeNexoHttp.create(processor, serverUrl, RealizarConexiones.con(nodoHttp),
				provider);
		nodoHttp.clienteInterno.conectarAlServidorHttp();
		return nodoHttp;
	}

	/**
	 * Cierra el puerto de escucha de requests y libera los recursos de este nodo
	 */
	public void cerrarYLiberar() throws VortexHttpException {
		this.clienteInterno.desconectarDelServidor();
	}

}
