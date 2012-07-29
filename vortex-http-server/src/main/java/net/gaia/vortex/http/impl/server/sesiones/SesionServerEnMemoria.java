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

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;

import net.gaia.vortex.core.api.mensaje.MensajeVortex;
import net.gaia.vortex.core.impl.mensaje.ContenidoMapa;
import net.gaia.vortex.core.impl.mensaje.MensajeConContenido;
import net.gaia.vortex.http.external.json.VortexHttpTextualizer;
import net.gaia.vortex.http.impl.moleculas.NexoHttp;
import net.gaia.vortex.http.messages.PaqueteHttpVortex;
import net.gaia.vortex.http.sesiones.SesionVortexHttp;
import ar.com.dgarcia.coding.exceptions.UnhandledConditionException;
import ar.com.dgarcia.lang.strings.ToString;
import ar.com.dgarcia.lang.time.TimeMagnitude;

/**
 * Esta clase representa una sesión vortex http mantenida en memoria
 * 
 * @author D. García
 */
public class SesionServerEnMemoria implements SesionVortexHttp {

	/**
	 * Espera inicial para dar como vieja la sesion
	 */
	private static final long ESPERA_MAXIMA_INICIAL = TimeMagnitude.of(30, TimeUnit.SECONDS).getMillis();

	private String idDeSesion;
	public static final String idDeSesion_FIELD = "idDeSesion";

	private NexoHttp nexoAsociado;
	public static final String nexoAsociado_FIELD = "nexoAsociado";

	private static final long MAXIMA_ESPERA_OTORGABLE = TimeMagnitude.of(7, TimeUnit.DAYS).getMillis();

	private ConcurrentLinkedQueue<MensajeVortex> mensajesAcumulados;

	private VortexHttpTextualizer textualizer;

	private long momentoDeUltimaActidad;
	private long esperaMaxima;

	/**
	 * @see net.gaia.vortex.http.sesiones.SesionVortexHttp#recibirDelCliente(java.lang.String)
	 */
	@Override
	public void recibirDelCliente(final String mensajesComoJson) {
		registrarActividad();
		final PaqueteHttpVortex paquete = textualizer.convertFromString(mensajesComoJson);
		negociarEsperaConCliente(paquete);
		final List<Map<String, Object>> contenidosDeMensajes = paquete.getContenidos();
		for (final Map<String, Object> contenido : contenidosDeMensajes) {
			enviarAVortex(contenido);
		}
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
		final ContenidoMapa contenido = ContenidoMapa.create(contenidoRegenerado);
		final Collection<String> idsVisitados = castearYVerificarContenidoDeVisitados(contenidoRegenerado);
		final MensajeConContenido mensajeReconstruido = MensajeConContenido.create(contenido, idsVisitados);
		getNexoAsociado().onObjectReceived(mensajeReconstruido, this);
	}

	/**
	 * Verifica que el mapa pasado sea tenga una colección de strings como datos de los nodos por
	 * los que pasó.<br>
	 * En caso contrario devuelve una colección vacía o produce una excepción si no es del tipo
	 * esperado
	 * 
	 * @param contenidoRegenerado
	 *            El mapa a revisar por la lista de IDs
	 * 
	 * @return La colección de IDs recuperada del mensaje
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private Collection<String> castearYVerificarContenidoDeVisitados(final Map<String, Object> contenidoRegenerado) {
		final Object object = contenidoRegenerado.get(MensajeConContenido.TRAZA_IDENTIFICADORES_VORTEX_KEY);
		if (object == null) {
			return Collections.emptySet();
		}
		Collection coleccionDeIds;
		try {
			coleccionDeIds = (Collection) object;
		} catch (final ClassCastException e) {
			throw new UnhandledConditionException("El mensaje tiene como atributo["
					+ MensajeConContenido.TRAZA_IDENTIFICADORES_VORTEX_KEY
					+ "] un valor que no es una coleccion de ids: " + object, e);
		}
		for (final Object posibleId : coleccionDeIds) {
			if (posibleId instanceof String) {
				continue;
			}
			throw new UnhandledConditionException("El atributo[" + MensajeConContenido.TRAZA_IDENTIFICADORES_VORTEX_KEY
					+ "] tiene en la coleccion un ID que no es string: " + posibleId);
		}
		return coleccionDeIds;
	}

	/**
	 * @see net.gaia.vortex.http.sesiones.SesionVortexHttp#obtenerParaElCliente()
	 */
	@Override
	public String obtenerParaElCliente() {
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
	 * @see net.gaia.vortex.http.sesiones.SesionVortexHttp#getIdDeSesion()
	 */
	@Override
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
		this.momentoDeUltimaActidad = getCurrentTime();
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
	@Override
	public String toString() {
		return ToString.de(this).con(idDeSesion_FIELD, idDeSesion).con(nexoAsociado_FIELD, nexoAsociado).toString();
	}

	@Override
	public NexoHttp getNexoAsociado() {
		return nexoAsociado;
	}

	@Override
	public void setNexoAsociado(final NexoHttp nexoAsociado) {
		this.nexoAsociado = nexoAsociado;
	}

	/**
	 * @see net.gaia.vortex.http.sesiones.SesionVortexHttp#acumularParaCliente(net.gaia.vortex.core.api.mensaje.MensajeVortex)
	 */
	@Override
	public void acumularParaCliente(final MensajeVortex mensaje) {
		this.mensajesAcumulados.add(mensaje);
	}

	/**
	 * @see net.gaia.vortex.http.sesiones.SesionVortexHttp#esVieja()
	 */
	@Override
	public boolean esVieja() {
		final long now = getCurrentTime();
		final long transcurridos = now - momentoDeUltimaActidad;
		final boolean esVieja = transcurridos > esperaMaxima;
		return esVieja;
	}
}
