/**
 * 29/07/2012 20:48:47 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.http.impl.cliente.server;

import net.gaia.vortex.http.impl.VortexHttpException;
import ar.com.dgarcia.lang.strings.ToString;
import ar.dgarcia.http.client.api.ConnectionProblemException;
import ar.dgarcia.http.client.api.HttpResponseProvider;
import ar.dgarcia.http.client.api.StringRequest;
import ar.dgarcia.http.client.api.StringResponse;

/**
 * Esta clase representa un proxy al server remoto que permite interactuar a través de comandos que
 * el cliente envía en forma de requests HTTP
 * 
 * @author D. García
 */
public class ServerVortexHttpRemoto {

	private String urlDelServidor;
	public static final String urlDelServidor_FIELD = "urlDelServidor";

	private HttpResponseProvider httpProvider;

	/**
	 * Envía el comando indicado al servidor procesando la respuesta.<br>
	 * El comando obtendrá la respuesta del servidor si hubo comunicación, reteniendo el resultado
	 * 
	 * @param comando
	 *            El comando a procesar
	 * @throws VortexHttpException
	 *             Si hubo un problema con la conexión al servidor
	 */
	public void enviarComando(final ComandoClienteHttp comando) throws VortexHttpException {
		final StringRequest requestAlServer = comando.crearRequest(urlDelServidor);
		StringResponse serverResponse;
		try {
			serverResponse = httpProvider.sendRequest(requestAlServer);
		} catch (final ConnectionProblemException e) {
			throw new VortexHttpException("Se produjo un error al enviar el request[" + requestAlServer
					+ "] al servidor[" + urlDelServidor + "], procesando el comando[" + comando + "]", e);
		}
		if (!serverResponse.hasOkStatus()) {
			throw new VortexHttpException("El servidor rechazó el comando[" + comando + "] con la respuesta: "
					+ serverResponse);
		}
		comando.procesarRespuesta(serverResponse);
	}

	public static ServerVortexHttpRemoto create(final String urlDelServidor, final HttpResponseProvider provider) {
		final ServerVortexHttpRemoto proxy = new ServerVortexHttpRemoto();
		proxy.httpProvider = provider;
		proxy.urlDelServidor = urlDelServidor;
		return proxy;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	
	public String toString() {
		return ToString.de(this).con(urlDelServidor_FIELD, urlDelServidor).toString();
	}

}
