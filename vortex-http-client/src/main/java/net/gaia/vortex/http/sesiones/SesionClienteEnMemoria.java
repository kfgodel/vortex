/**
 * 29/07/2012 14:52:01 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.http.sesiones;

import java.util.List;

import net.gaia.taskprocessor.api.TaskProcessor;
import net.gaia.vortex.core.api.mensaje.MensajeVortex;
import net.gaia.vortex.http.external.json.VortexHttpTextualizer;
import net.gaia.vortex.http.impl.VortexHttpException;
import net.gaia.vortex.http.impl.cliente.server.ConexionConPollingHttpCliente;
import net.gaia.vortex.http.impl.cliente.server.HandlerHttpDeMensajesRecibidos;
import net.gaia.vortex.http.impl.cliente.server.ServerVortexHttpRemoto;
import net.gaia.vortex.http.impl.moleculas.NexoHttp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ar.com.dgarcia.lang.strings.ToString;

/**
 * Esta clase representa un sesión http del lado del cliente
 * 
 * @author D. García
 */
public class SesionClienteEnMemoria implements SesionVortexHttpEnCliente, HandlerHttpDeMensajesRecibidos {
	private static final Logger LOG = LoggerFactory.getLogger(SesionClienteEnMemoria.class);

	private ConexionConPollingHttpCliente conexionHttpCliente;
	public static final String conexionHttpCliente_FIELD = "conexionHttpCliente";

	private NexoHttp nexoAsociado;
	public static final String nexoAsociado_FIELD = "nexoAsociado";

	/**
	 * @see net.gaia.vortex.http.sesiones.SesionVortexHttp#getIdDeSesion()
	 */
	
	public String getIdDeSesion() {
		return conexionHttpCliente.getIdDeSesion();
	}

	/**
	 * @see net.gaia.vortex.http.sesiones.SesionVortexHttp#onMensajeDesdeVortex(net.gaia.vortex.core.api.mensaje.MensajeVortex)
	 */
	
	public void onMensajeDesdeVortex(final MensajeVortex mensaje) {
		conexionHttpCliente.enviar(mensaje);
	}

	/**
	 * @see net.gaia.vortex.http.sesiones.SesionVortexHttp#getNexoAsociado()
	 */
	
	public NexoHttp getNexoAsociado() {
		return nexoAsociado;
	}

	/**
	 * @see net.gaia.vortex.http.sesiones.SesionVortexHttp#setNexoAsociado(net.gaia.vortex.http.impl.moleculas.NexoHttp)
	 */
	
	public void setNexoAsociado(final NexoHttp nexoAsociado) {
		this.nexoAsociado = nexoAsociado;
	}

	public static SesionClienteEnMemoria create(final TaskProcessor processor, final ServerVortexHttpRemoto server,
			final VortexHttpTextualizer textualizer) {
		final SesionClienteEnMemoria sesion = new SesionClienteEnMemoria();
		sesion.conexionHttpCliente = ConexionConPollingHttpCliente.create(processor, server, textualizer, sesion);
		return sesion;
	}

	/**
	 * @see net.gaia.vortex.http.sesiones.SesionVortexHttpEnCliente#iniciarComunicacion()
	 */
	
	public void iniciarComunicacion() throws VortexHttpException {
		this.conexionHttpCliente.iniciarConexion();
	}

	/**
	 * @see net.gaia.vortex.http.sesiones.SesionVortexHttpEnCliente#detenerComunicacion()
	 */
	
	public void detenerComunicacion() throws VortexHttpException {
		this.conexionHttpCliente.terminarConexion();
	}

	/**
	 * @see net.gaia.vortex.http.impl.cliente.server.HandlerHttpDeMensajesRecibidos#onMensajesRecibidos(java.util.List)
	 */
	
	public void onMensajesRecibidos(final List<MensajeVortex> mensajesRecibidos) {
		if (nexoAsociado == null) {
			LOG.error("Se recibieron mensajes en la sesion[{}] sin nexo asociado. Ignorando mensajes: {}", this,
					mensajesRecibidos);
			return;
		}
		for (final MensajeVortex mensajeVortex : mensajesRecibidos) {
			nexoAsociado.onMensajeDesdeHttp(mensajeVortex);
		}
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	
	public String toString() {
		return ToString.de(this).con(nexoAsociado_FIELD, nexoAsociado)
				.con(conexionHttpCliente_FIELD, conexionHttpCliente).toString();
	}

}
