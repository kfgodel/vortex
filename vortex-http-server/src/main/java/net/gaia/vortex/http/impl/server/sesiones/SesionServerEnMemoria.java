/**
 * 25/07/2012 22:36:44 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.http.impl.server.sesiones;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;

import net.gaia.vortex.api.mensajes.MensajeVortex;
import net.gaia.vortex.http.external.json.VortexHttpTextualizer;
import net.gaia.vortex.http.impl.moleculas.NexoHttp;
import net.gaia.vortex.http.messages.PaqueteHttpVortex;
import net.gaia.vortex.http.sesiones.SesionVortexHttpEnServer;
import net.gaia.vortex.impl.mensajes.MensajeConContenido;
import ar.com.dgarcia.lang.strings.ToString;
import ar.com.dgarcia.lang.time.TimeMagnitude;
import ar.dgarcia.textualizer.api.CannotTextSerializeException;
import ar.dgarcia.textualizer.api.CannotTextUnserializeException;

/**
 * Esta clase representa una sesión vortex http mantenida en memoria
 * 
 * @author D. García
 */
public class SesionServerEnMemoria implements SesionVortexHttpEnServer {

	/**
	 * Espera inicial para dar como vieja la sesion
	 */
	private static final long ESPERA_MAXIMA_INICIAL = TimeMagnitude.of(30, TimeUnit.SECONDS).getMillis();
	private static final long ESPERA_MINIMA_INICIAL = 0;

	private String idDeSesion;
	public static final String idDeSesion_FIELD = "idDeSesion";

	private NexoHttp nexoAsociado;
	public static final String nexoAsociado_FIELD = "nexoAsociado";

	private static final long MAXIMA_ESPERA_OTORGABLE = TimeMagnitude.of(7, TimeUnit.DAYS).getMillis();

	private ConcurrentLinkedQueue<MensajeVortex> mensajesAcumulados;

	private VortexHttpTextualizer textualizer;

	private long momentoDeUltimaActividad;
	private long esperaMaxima;

	/**
	 * @see net.gaia.vortex.http.sesiones.SesionVortexHttpEnServer#recibirDesdeHttp(java.lang.String)
	 */
	
	public void recibirDesdeHttp(final String mensajesComoJson) throws CannotTextUnserializeException {
		registrarActividad();
		final PaqueteHttpVortex paquete = textualizer.convertFromString(mensajesComoJson);
		final List<Map<String, Object>> contenidosDeMensajes = paquete.getContenidos();
		for (final Map<String, Object> contenido : contenidosDeMensajes) {
			enviarAVortex(contenido);
		}
		negociarEsperaConCliente(paquete);
	}

	/**
	 * Actualiza la espera máxima que se le brinda al cliente en esta sesión
	 * 
	 * @param paquete
	 *            El paquete que define la espera máxima
	 */
	private void negociarEsperaConCliente(final PaqueteHttpVortex paquete) {
		Long proximaEspera = paquete.getProximaEsperaMaxima();
		if (proximaEspera == null) {
			// No modificamos la espera máxima
			return;
		}
		if (proximaEspera > MAXIMA_ESPERA_OTORGABLE) {
			proximaEspera = MAXIMA_ESPERA_OTORGABLE;
		}
		esperaMaxima = proximaEspera;
	}

	/**
	 * Envía el mapa recibido como contenido de un mensaje vortex
	 * 
	 * @param contenido
	 *            El mapa de contenidos
	 */
	private void enviarAVortex(final Map<String, Object> contenidoRegenerado) {
		final MensajeConContenido mensajeReconstruido = MensajeConContenido.regenerarDesde(contenidoRegenerado);
		getNexoAsociado().onMensajeDesdeHttp(mensajeReconstruido);
	}

	/**
	 * @see net.gaia.vortex.http.sesiones.SesionVortexHttpEnServer#obtenerParaHttp()
	 */
	
	public String obtenerParaHttp() throws CannotTextSerializeException {
		registrarActividad();
		final PaqueteHttpVortex paqueteDeSalida = PaqueteHttpVortex.create(0, esperaMaxima);
		final Iterator<MensajeVortex> iteradorDeMensajes = this.mensajesAcumulados.iterator();
		while (iteradorDeMensajes.hasNext()) {
			final MensajeVortex mensaje = iteradorDeMensajes.next();
			final Map<String, Object> contenidoTextualizable = mensaje.getContenido();
			paqueteDeSalida.agregarContenido(contenidoTextualizable);
			iteradorDeMensajes.remove();
		}
		final String jsonDelPaquete = textualizer.convertToString(paqueteDeSalida);
		return jsonDelPaquete;
	}

	/**
	 * @see net.gaia.vortex.http.sesiones.SesionVortexHttpEnServer#getIdDeSesion()
	 */
	
	public String getIdDeSesion() {
		return idDeSesion;
	}

	public static SesionServerEnMemoria create(final String idDeSesion, final VortexHttpTextualizer textualizer) {
		final SesionServerEnMemoria sesion = new SesionServerEnMemoria();
		sesion.idDeSesion = idDeSesion;
		sesion.mensajesAcumulados = new ConcurrentLinkedQueue<MensajeVortex>();
		sesion.textualizer = textualizer;
		sesion.registrarActividad();
		sesion.esperaMaxima = ESPERA_MAXIMA_INICIAL;
		return sesion;
	}

	/**
	 * Registra que esta sesioón tiene actividad, evitando que se considere vieja
	 */
	private void registrarActividad() {
		this.momentoDeUltimaActividad = getCurrentTime();
	}

	/**
	 * Devuelve el momento considerado actual para comparar sesiones viejas
	 * 
	 * @return
	 */
	private long getCurrentTime() {
		return System.currentTimeMillis();
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	
	public String toString() {
		return ToString.de(this).con(idDeSesion_FIELD, idDeSesion).con(nexoAsociado_FIELD, nexoAsociado).toString();
	}

	
	public NexoHttp getNexoAsociado() {
		return nexoAsociado;
	}

	
	public void setNexoAsociado(final NexoHttp nexoAsociado) {
		this.nexoAsociado = nexoAsociado;
	}

	/**
	 * @see net.gaia.vortex.http.sesiones.SesionVortexHttpEnServer#onMensajeDesdeVortex(net.gaia.vortex.api.mensajes.MensajeVortex)
	 */
	
	public void onMensajeDesdeVortex(final MensajeVortex mensaje) {
		this.mensajesAcumulados.add(mensaje);
	}

	/**
	 * @see net.gaia.vortex.http.sesiones.SesionVortexHttpEnServer#esVieja()
	 */
	
	public boolean esVieja() {
		final long now = getCurrentTime();
		final long transcurridos = now - momentoDeUltimaActividad;
		final boolean esVieja = transcurridos > esperaMaxima;
		return esVieja;
	}

	/**
	 * @see net.gaia.vortex.http.sesiones.SesionVortexHttpEnServer#tomarParametrosInicialesDe(java.lang.String)
	 */
	
	public void tomarParametrosInicialesDe(final String parametrosJson) throws CannotTextUnserializeException {
		PaqueteHttpVortex parametros = null;
		if (parametrosJson != null) {
			parametros = textualizer.convertFromString(parametrosJson);
		}
		if (parametros == null) {
			parametros = PaqueteHttpVortex.create(ESPERA_MINIMA_INICIAL, ESPERA_MAXIMA_INICIAL);
		}
		negociarEsperaConCliente(parametros);
	}
}
