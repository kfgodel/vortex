/**
 * 29/07/2012 21:19:50 Copyright (C) 2011 Darío L. García
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
 * Esta clase representa el comando enviado por el cliente http para eliminar la sesión usada
 * 
 * @author D. García
 */
public class CerrarSesionCliente implements ComandoClienteHttp {
	private String idDeSesionAEliminar;
	public static final String idDeSesionAEliminar_FIELD = "idDeSesionAEliminar";

	/**
	 * @see net.gaia.vortex.http.impl.cliente.server.ComandoClienteHttp#crearRequest(java.lang.String)
	 */
	
	public StringRequest crearRequest(final String urlDelSevidor) {
		final String urlParaEliminar = urlDelSevidor + HttpMetadata.URL_PREFFIX_ELIMINAR + idDeSesionAEliminar;
		final StringRequest requestParaEliminar = StringRequest.create(urlParaEliminar);
		return requestParaEliminar;
	}

	/**
	 * @see net.gaia.vortex.http.impl.cliente.server.ComandoClienteHttp#procesarRespuesta(ar.dgarcia.http.client.api.StringResponse)
	 */
	
	public void procesarRespuesta(final StringResponse respuestaDelServidor) {
		// No hay acción después de la eliminación
	}

	public static CerrarSesionCliente create(final String idDeSesionATerminar) {
		final CerrarSesionCliente eliminar = new CerrarSesionCliente();
		eliminar.idDeSesionAEliminar = idDeSesionATerminar;
		return eliminar;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	
	public String toString() {
		return ToString.de(this).con(idDeSesionAEliminar_FIELD, idDeSesionAEliminar).toString();
	}

}
