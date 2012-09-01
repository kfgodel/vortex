/**
 * 16/06/2012 19:03:38 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.core.impl.mensaje;

import java.util.HashMap;
import java.util.Map;

import net.gaia.vortex.core.api.mensaje.ContenidoVortex;
import net.gaia.vortex.core.api.mensaje.MensajeVortex;
import net.gaia.vortex.core.api.mensaje.ids.IdDeMensaje;
import net.gaia.vortex.core.api.moleculas.ids.IdDeComponenteVortex;
import net.gaia.vortex.core.impl.ids.IdDeMensajeConNodoYSecuencia;
import net.gaia.vortex.core.impl.moleculas.ids.IdEstaticoDeComponente;
import ar.com.dgarcia.coding.exceptions.UnhandledConditionException;
import ar.com.dgarcia.lang.strings.ToString;

/**
 * Esta clase es la implementación de un mensaje vortex utilizando un mapa para conservar la
 * representación de estado de los datos transmitidos
 * 
 * @author D. García
 */
public class MensajeConContenido implements MensajeVortex {

	public static final String ID_DE_MENSAJE_KEY = "id_mensaje_vortex";
	public static final String SECUENCIA_DEL_ID_KEY = "numero_secuencia";
	public static final String EMISOR_DEL_ID_KEY = "id_del_emisor";

	private IdDeMensaje idDeMensaje;
	public static final String idDeMensaje_FIELD = "idDeMensaje";

	private ContenidoVortex contenido;
	public static final String contenido_FIELD = "contenido";

	/**
	 * @see net.gaia.vortex.core.api.mensaje.MensajeVortex#getContenido()
	 */
	@Override
	public ContenidoVortex getContenido() {
		return contenido;
	}

	/**
	 * Crea un mensaje vacío sin registro de nodos visitados
	 * 
	 * @return El mensaje creado
	 */
	@SuppressWarnings("unchecked")
	public static MensajeConContenido crearVacio() {
		final ContenidoVortex contenidoVacio = ContenidoMapa.create();
		return create(contenidoVacio);
	}

	/**
	 * Crea un mensaje vortex con el contenido indicado y el registro de nodos visitados pasado
	 * 
	 * @param contenidoIncial
	 *            El contenido a portar por este mensaje
	 * @param idsVisitados
	 *            Los ids tomados como visitados
	 * @return El mensaje creado
	 */
	public static MensajeConContenido create(final ContenidoVortex contenido) {
		final MensajeConContenido mensaje = new MensajeConContenido();
		mensaje.contenido = contenido;
		return mensaje;
	}

	/**
	 * Regenera un mensaje vortex desde el mapa de valores pasado. Tomando los Ids de nodos
	 * visitados
	 * 
	 * @param valoresExternos
	 *            El mapa de valores para regenerar el mensaje
	 * @return El mensaje recreado
	 */
	public static MensajeConContenido regenerarDesde(final Map<String, Object> valoresExternos) {
		final ContenidoMapa contenido = ContenidoMapa.create(valoresExternos);
		final MensajeConContenido mensajeReconstruido = MensajeConContenido.create(contenido);
		final IdDeMensaje idDelMensaje = MensajeConContenido.obtenerIdDesde(valoresExternos);
		mensajeReconstruido.asignarId(idDelMensaje);
		return mensajeReconstruido;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return ToString.de(this).add(idDeMensaje_FIELD, idDeMensaje).add(contenido_FIELD, contenido).toString();
	}

	/**
	 * Verifica que el mapa pasado sea tenga una colección de strings como datos de los nodos por
	 * los que pasó.<br>
	 * En caso contrario devuelve una colección vacía o produce una excepción si el atributo existe
	 * pero no es de los tipos esperados
	 * 
	 * @param contenidoRegenerado
	 *            El mapa a revisar por la lista de IDs
	 * 
	 * @return La colección de IDs recuperada del mensaje
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static IdDeMensaje obtenerIdDesde(final Map<String, Object> contenidoRegenerado) {
		final Object object = contenidoRegenerado.get(MensajeConContenido.ID_DE_MENSAJE_KEY);
		if (object == null) {
			throw new UnhandledConditionException("No econtramos el ID de mensaje en un mensaje recibido?: "
					+ contenidoRegenerado);
		}
		final Map<String, Object> idComoMapa;
		try {
			idComoMapa = (Map<String, Object>) object;
		} catch (final ClassCastException e) {
			throw new UnhandledConditionException("El mensaje tiene como atributo["
					+ MensajeConContenido.ID_DE_MENSAJE_KEY + "] un valor que no es un mapa de valores: " + object
					+ " mensaje[" + contenidoRegenerado + "]", e);
		}
		final Object emisorEnMensaje = idComoMapa.get(EMISOR_DEL_ID_KEY);
		if (emisorEnMensaje == null) {
			throw new UnhandledConditionException("El mensaje no tiene emisor en su parte ID: " + contenidoRegenerado);
		}
		String valorDelIdDelEmisor;
		try {
			valorDelIdDelEmisor = (String) emisorEnMensaje;
		} catch (final ClassCastException e) {
			throw new UnhandledConditionException("El mensaje tiene como parte del ID un emisor que no es String: "
					+ emisorEnMensaje + " mensaje[" + contenidoRegenerado + "]", e);
		}

		final Object secuenciaDeMensaje = idComoMapa.get(SECUENCIA_DEL_ID_KEY);
		if (secuenciaDeMensaje == null) {
			throw new UnhandledConditionException("El mensaje no tiene secuencia en su parte del ID"
					+ contenidoRegenerado);
		}
		Number numeroDeSecuencia;
		try {
			numeroDeSecuencia = (Number) secuenciaDeMensaje;
		} catch (final ClassCastException e) {
			throw new UnhandledConditionException("El mensaje tiene como parte del ID una secuencia que no es Number: "
					+ emisorEnMensaje + " mensaje[" + contenidoRegenerado + "]", e);
		}

		final IdDeComponenteVortex idDelEmisor = IdEstaticoDeComponente.create(valorDelIdDelEmisor);
		final IdDeMensaje idGenerado = IdDeMensajeConNodoYSecuencia.create(idDelEmisor, numeroDeSecuencia.longValue());
		return idGenerado;
	}

	/**
	 * @see net.gaia.vortex.core.api.mensaje.MensajeVortex#getIdDeMensaje()
	 */
	@Override
	public IdDeMensaje getIdDeMensaje() {
		return this.idDeMensaje;
	}

	/**
	 * @see net.gaia.vortex.core.api.mensaje.MensajeVortex#asignarId(net.gaia.vortex.core.api.mensaje.ids.IdDeMensaje)
	 */
	@Override
	public void asignarId(final IdDeMensaje idNuevo) {
		this.idDeMensaje = idNuevo;
		final Map<String, Object> mapaDelId = crearMapaDelId();
		getContenido().put(ID_DE_MENSAJE_KEY, mapaDelId);
	}

	/**
	 * Devuelve una versión mapa del ID
	 * 
	 * @return El mapa de valores que conforma el ID
	 */
	private Map<String, Object> crearMapaDelId() {
		final String idDelEmisor = this.idDeMensaje.getIdDelEmisor().getValorActual();
		final Long numeroDeSecuencia = this.idDeMensaje.getNumeroDeSecuencia();
		final Map<String, Object> mapa = new HashMap<String, Object>(4);
		mapa.put(EMISOR_DEL_ID_KEY, idDelEmisor);
		mapa.put(SECUENCIA_DEL_ID_KEY, numeroDeSecuencia);
		return mapa;
	}
}
