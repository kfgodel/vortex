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

import java.util.concurrent.ConcurrentLinkedQueue;

import net.gaia.vortex.core.api.mensaje.MensajeVortex;
import net.gaia.vortex.http.external.json.VortexHttpTextualizer;
import net.gaia.vortex.http.impl.moleculas.NexoHttp;

/**
 * Esta clase representa un sesión http del lado del cliente
 * 
 * @author D. García
 */
public class SesionClienteEnMemoria implements SesionVortexHttp {

	private ConcurrentLinkedQueue<MensajeVortex> mensajesAcumulados;
	private long esperaMinima;
	private long momentoDeComienzoDeEspera;
	private NexoHttp nexoAsociado;
	private VortexHttpTextualizer textualizer;
	private String idDeSesion;

	/**
	 * Devuelve el tiempo considerado actual en milis para las mediciones de espera
	 * 
	 * @return El tiempo en milis
	 */
	public long getCurrentTime() {
		return System.currentTimeMillis();
	}

	/**
	 * @see net.gaia.vortex.http.sesiones.SesionVortexHttp#recibirDelCliente(java.lang.String)
	 */
	@Override
	public void recibirDelCliente(final String mensajesComoJson) {
		// TODO Auto-generated method stub

	}

	/**
	 * @see net.gaia.vortex.http.sesiones.SesionVortexHttp#obtenerParaElCliente()
	 */
	@Override
	public String obtenerParaElCliente() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @see net.gaia.vortex.http.sesiones.SesionVortexHttp#getIdDeSesion()
	 */
	@Override
	public String getIdDeSesion() {
		return idDeSesion;
	}

	/**
	 * @see net.gaia.vortex.http.sesiones.SesionVortexHttp#acumularParaCliente(net.gaia.vortex.core.api.mensaje.MensajeVortex)
	 */
	@Override
	public void acumularParaCliente(final MensajeVortex mensaje) {
		this.mensajesAcumulados.add(mensaje);
		enviarMensajesAlServer();
	}

	/**
	 * Intenta enviar los mensajes acumulados al server si es que se superó la espera mínima
	 */
	private void enviarMensajesAlServer() {
		if (esperaMinimaSuperada()) {
			return;
		}
	}

	/**
	 * Indica si esta sesión ya esperó lo suficiente para enviar otro request desde el momento que
	 * el server indico la espera mínima
	 * 
	 * @return true si ya se esperó lo sufuciente como para enviar otro request
	 */
	private boolean esperaMinimaSuperada() {
		final long now = getCurrentTime();
		final long esperado = now - momentoDeComienzoDeEspera;
		final boolean esperaMinimaSuperada = esperado >= esperaMinima;
		return esperaMinimaSuperada;
	}

	/**
	 * @see net.gaia.vortex.http.sesiones.SesionVortexHttp#getNexoAsociado()
	 */
	@Override
	public NexoHttp getNexoAsociado() {
		return nexoAsociado;
	}

	/**
	 * @see net.gaia.vortex.http.sesiones.SesionVortexHttp#setNexoAsociado(net.gaia.vortex.http.impl.moleculas.NexoHttp)
	 */
	@Override
	public void setNexoAsociado(final NexoHttp nexoAsociado) {
		this.nexoAsociado = nexoAsociado;
	}

	/**
	 * @see net.gaia.vortex.http.sesiones.SesionVortexHttp#esVieja()
	 */
	@Override
	public boolean esVieja() {
		return false;
	}

	public static SesionClienteEnMemoria create(final String idDeSesion) {
		final SesionClienteEnMemoria sesion = new SesionClienteEnMemoria();
		sesion.idDeSesion = idDeSesion;
		sesion.esperaMinima = 0;
		sesion.mensajesAcumulados = new ConcurrentLinkedQueue<MensajeVortex>();
		sesion.momentoDeComienzoDeEspera = sesion.getCurrentTime();
		return sesion;
	}
}
