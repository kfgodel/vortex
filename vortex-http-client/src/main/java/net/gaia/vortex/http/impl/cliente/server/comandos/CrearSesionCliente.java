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
import net.gaia.vortex.http.impl.VortexHttpException;
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

	private String parametrosDeSesion;
	public static final String parametrosDeSesion_FIELD = "parametrosDeSesion";

	private String idDeSesionCreada;
	public static final String idDeSesionCreada_FIELD = "idDeSesionCreada";

	/**
	 * @see net.gaia.vortex.http.impl.cliente.server.ComandoClienteHttp#crearRequest(java.lang.String)
	 */
	@Override
	public StringRequest crearRequest(final String urlDelSevidor) {
		final String urlParaCrearSesion = urlDelSevidor + HttpMetadata.URL_CREAR;
		final StringRequest requestDeCreacion = StringRequest.create(urlParaCrearSesion);
		requestDeCreacion.addPostParameter(HttpMetadata.MENSAJES_PARAMETER_NAME, parametrosDeSesion);
		return requestDeCreacion;
	}

	/**
	 * @see net.gaia.vortex.http.impl.cliente.server.ComandoClienteHttp#procesarRespuesta(ar.dgarcia.http.client.api.StringResponse)
	 */
	@Override
	public void procesarRespuesta(final StringResponse respuestaDelServidor) {
		final String idDeSesion = respuestaDelServidor.getContent();
		if (idDeSesion == null) {
			throw new VortexHttpException("El servidor respondió OK, pero no envío ID de sesión");
		}
		this.idDeSesionCreada = idDeSesion.trim();
		if (idDeSesionCreada.length() == 0) {
			throw new VortexHttpException("El servidor respondió OK, pero el ID de sesión es vacío");
		}
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return ToString.de(this).con(idDeSesionCreada_FIELD, idDeSesionCreada)
				.con(parametrosDeSesion_FIELD, parametrosDeSesion).toString();
	}

	public String getIdDeSesionCreada() {
		return idDeSesionCreada;
	}

	public void setIdDeSesionCreada(final String idDeSesionCreada) {
		this.idDeSesionCreada = idDeSesionCreada;
	}

	public static CrearSesionCliente create(final String parametrosDeLaSesion) {
		final CrearSesionCliente crear = new CrearSesionCliente();
		crear.parametrosDeSesion = parametrosDeLaSesion;
		return crear;
	}
}
