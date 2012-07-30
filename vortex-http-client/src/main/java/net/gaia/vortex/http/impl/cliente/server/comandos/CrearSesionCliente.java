/**
 * 29/07/2012 21:15:49 Copyright (C) 2011 Darío L. García
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
 * Esta clase representa el comando enviado por http para crear una sesión en el servidor
 * 
 * @author D. García
 */
public class CrearSesionCliente implements ComandoClienteHttp {

	private String idDeSesionCreada;
	public static final String idDeSesionCreada_FIELD = "idDeSesionCreada";

	/**
	 * @see net.gaia.vortex.http.impl.cliente.server.ComandoClienteHttp#crearRequest(java.lang.String)
	 */
	@Override
	public StringRequest crearRequest(final String urlDelSevidor) {
		final String urlParaCrearSesion = urlDelSevidor + HttpMetadata.URL_CREAR;
		final StringRequest requestDeCreacion = StringRequest.create(urlParaCrearSesion);
		return requestDeCreacion;
	}

	/**
	 * @see net.gaia.vortex.http.impl.cliente.server.ComandoClienteHttp#procesarRespuesta(ar.dgarcia.http.client.api.StringResponse)
	 */
	@Override
	public void procesarRespuesta(final StringResponse respuestaDelServidor) {
		final String idDeSesion = respuestaDelServidor.getContent();
		this.idDeSesionCreada = idDeSesion.trim();
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return ToString.de(this).con(idDeSesionCreada_FIELD, idDeSesionCreada).toString();
	}

	public String getIdDeSesionCreada() {
		return idDeSesionCreada;
	}

	public void setIdDeSesionCreada(final String idDeSesionCreada) {
		this.idDeSesionCreada = idDeSesionCreada;
	}

	public static CrearSesionCliente create() {
		final CrearSesionCliente crear = new CrearSesionCliente();
		return crear;
	}
}
