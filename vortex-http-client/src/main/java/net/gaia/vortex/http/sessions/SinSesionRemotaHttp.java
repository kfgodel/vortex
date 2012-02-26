/**
 * 01/02/2012 20:21:44 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.http.sessions;

import net.gaia.vortex.lowlevel.api.ErroresDelMensaje;
import net.gaia.vortex.lowlevel.api.SesionVortex;
import net.gaia.vortex.lowlevel.impl.receptores.ColaDeMensajesVortex;
import net.gaia.vortex.lowlevel.impl.receptores.NullColaDeMensajesVortex;
import net.gaia.vortex.protocol.messages.MensajeVortex;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Objects;

/**
 * Esta clase representa la no existencia de sesión http para enviar un mensaje sin sesion asociada
 * 
 * @author D. García
 */
public class SinSesionRemotaHttp implements SesionVortex, SesionConId {
	private static final Logger LOG = LoggerFactory.getLogger(SinSesionRemotaHttp.class);
	private ColaDeMensajesVortex nullCola;

	/**
	 * @see net.gaia.vortex.lowlevel.api.SesionVortex#enviar(net.gaia.vortex.protocol.messages.MensajeVortex)
	 */
	@Override
	public void enviar(final MensajeVortex mensajeEnviado) {
		LOG.error("Se intentó enviar un mensaje en la sesión nula");
	}

	/**
	 * @see net.gaia.vortex.lowlevel.api.SesionVortex#cerrar()
	 */
	@Override
	public void cerrar() {
		LOG.error("Se intentó cerrar la sesión nula");
	}

	/**
	 * @see net.gaia.vortex.lowlevel.api.SesionVortex#estaCerrada()
	 */
	@Override
	public boolean estaCerrada() {
		return false;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return Objects.toStringHelper(this).toString();
	}

	/**
	 * @see net.gaia.vortex.http.sessions.SesionConId#getSessionId()
	 */
	@Override
	public Long getSessionId() {
		return null;
	}

	/**
	 * @see net.gaia.vortex.http.sessions.SesionConId#cambiarId(java.lang.Long)
	 */
	@Override
	public void cambiarId(final Long nuevoId) {
		LOG.debug("Se asignó un ID a la sesión nula: {}", nuevoId);
	}

	/**
	 * @see net.gaia.vortex.http.sessions.SesionConId#recibirDelNodo(net.gaia.vortex.protocol.messages.MensajeVortex)
	 */
	@Override
	public void recibirDelNodo(final MensajeVortex mensajeRecibido) {
		LOG.error("Se recibió un mensaje para la sesión nula: {}", mensajeRecibido.toPrettyPrint());
	}

	/**
	 * @see net.gaia.vortex.http.sessions.SesionConId#getColaDeMensajes()
	 */
	@Override
	public ColaDeMensajesVortex getColaDeMensajes() {
		return nullCola;
	}

	public static SinSesionRemotaHttp create() {
		final SinSesionRemotaHttp sinSesion = new SinSesionRemotaHttp();
		sinSesion.nullCola = NullColaDeMensajesVortex.create();
		return sinSesion;
	}

	/**
	 * @see net.gaia.vortex.http.sessions.SesionConId#onErrorDeMensaje(net.gaia.vortex.protocol.messages.MensajeVortex,
	 *      net.gaia.vortex.lowlevel.api.ErroresDelMensaje)
	 */
	@Override
	public void onErrorDeMensaje(final MensajeVortex mensajeAEnviar, final ErroresDelMensaje errores) {
		LOG.error("Se produjo un error con el mensaje[" + mensajeAEnviar + "]: " + errores);
	}

	/**
	 * @see net.gaia.vortex.lowlevel.api.SesionVortex#poll()
	 */
	@Override
	public void poll() {
		// No hay recepciones para esta sesión
	}

	/**
	 * @see net.gaia.vortex.http.sessions.SesionConId#getCantidadSegundosSinActividadActuales()
	 */
	@Override
	public Long getCantidadSegundosSinActividadActuales() {
		return Long.valueOf(0);
	}

	/**
	 * @see net.gaia.vortex.http.sessions.SesionConId#cambiarCantidadSegundosSinActividad(java.lang.Long)
	 */
	@Override
	public void cambiarCantidadSegundosSinActividad(final Long segundosOtorgadosPorServer) {
		// No tenemos segundos de inactividad por que no es sesión
	}

	/**
	 * @see net.gaia.vortex.http.sessions.SesionConId#getPeriodoDePollingEnSegundos()
	 */
	@Override
	public Long getPeriodoDePollingEnSegundos() {
		// No hay polling si no hay sesion
		return null;
	}
}
