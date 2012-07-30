/**
 * 29/07/2012 21:22:09 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.http.impl.cliente.server.comandos;

import net.gaia.vortex.http.api.HttpMetadata;
import net.gaia.vortex.http.impl.cliente.server.ComandoClienteHttp;
import ar.com.dgarcia.lang.strings.ToString;
import ar.dgarcia.http.client.api.StringRequest;
import ar.dgarcia.http.client.api.StringResponse;

/**
 * Esta clase representa el comando enviado desde el cliente http para intercambiar mensajes con el
 * servidor vortex http
 * 
 * @author D. García
 */
public class IntercambiarMensajesCliente implements ComandoClienteHttp {

	private String idDeSesion;
	public static final String idDeSesion_FIELD = "idDeSesion";
	private String mensajesEnJsonAEnviar;
	public static final String mensajesEnJsonAEnviar_FIELD = "mensajesEnJsonAEnviar";
	private String mensajesEnJsonRecibidos;
	public static final String mensajesEnJsonRecibidos_FIELD = "mensajesEnJsonRecibidos";

	/**
	 * @see net.gaia.vortex.http.impl.cliente.server.ComandoClienteHttp#crearRequest(java.lang.String)
	 */
	@Override
	public StringRequest crearRequest(final String urlDelSevidor) {
		final String urlParaIntercambio = urlDelSevidor + HttpMetadata.URL_PREFFIX_INTERCAMBIAR + idDeSesion;
		final StringRequest requestParaIntercambio = StringRequest.create(urlParaIntercambio);
		if (mensajesEnJsonAEnviar != null) {
			requestParaIntercambio.addPostParameter(HttpMetadata.MENSAJES_PARAMETER_NAME, mensajesEnJsonAEnviar);
		}
		return requestParaIntercambio;
	}

	/**
	 * @see net.gaia.vortex.http.impl.cliente.server.ComandoClienteHttp#procesarRespuesta(ar.dgarcia.http.client.api.StringResponse)
	 */
	@Override
	public void procesarRespuesta(final StringResponse respuestaDelServidor) {
		mensajesEnJsonRecibidos = respuestaDelServidor.getContent();
	}

	public static IntercambiarMensajesCliente create(final String idDeSesion, final String mensajesEnJson) {
		final IntercambiarMensajesCliente intercambiar = new IntercambiarMensajesCliente();
		intercambiar.idDeSesion = idDeSesion;
		intercambiar.mensajesEnJsonAEnviar = mensajesEnJson;
		return intercambiar;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		final ToString helper = ToString.de(this).con(idDeSesion_FIELD, idDeSesion);
		if (this.mensajesEnJsonRecibidos == null) {
			// Todavía no recibimos respuesta
			helper.con(mensajesEnJsonAEnviar_FIELD, mensajesEnJsonAEnviar);
		} else {
			helper.con(mensajesEnJsonRecibidos_FIELD, mensajesEnJsonRecibidos);
		}
		return helper.toString();
	}

	public String getMensajesEnJsonRecibidos() {
		return mensajesEnJsonRecibidos;
	}

	public void setMensajesEnJsonRecibidos(final String mensajesEnJsonRecibidos) {
		this.mensajesEnJsonRecibidos = mensajesEnJsonRecibidos;
	}

}
