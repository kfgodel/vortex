/**
 * 29/07/2012 01:41:35 Copyright (C) 2011 Darío L. García
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

import net.gaia.taskprocessor.api.processor.TaskProcessor;
import net.gaia.vortex.core.impl.moleculas.support.MultiplexorSinDuplicadosSupportViejo;
import net.gaia.vortex.http.impl.VortexHttpException;
import net.gaia.vortex.http.impl.server.ServidorDeNexoHttp;
import net.gaia.vortex.server.impl.RealizarConexiones;

/**
 * Esta clase representa un {@link NodoMultiplexor} que hace de servidor http permitiendo a clientes
 * remotos de vortex comunicarse con la red interna mediante requests.<br>
 * Los clientes externos deberán crear sesiones que internamente producirán {@link NexoHttp}
 * conectados a este nodo. Al cerrar esas sesiones, los nexos se desconectarán de este nodo
 * 
 * @author D. García
 */
public class NodoServerHttp extends MultiplexorSinDuplicadosSupportViejo {

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
	public static NodoServerHttp createAndAcceptRequestsOnPort(final int listeningPort, final TaskProcessor processor)
			throws VortexHttpException {
		final NodoServerHttp nodoHttp = new NodoServerHttp();
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
