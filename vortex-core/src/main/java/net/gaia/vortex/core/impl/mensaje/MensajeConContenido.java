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

import java.util.Map;

import net.gaia.vortex.api.ids.mensajes.IdDeMensaje;
import net.gaia.vortex.core.api.mensaje.ContenidoVortex;
import net.gaia.vortex.core.api.mensaje.MensajeVortex;
import ar.com.dgarcia.lang.strings.ToString;

/**
 * Esta clase es la implementación de un mensaje vortex utilizando un mapa para conservar la
 * representación de estado de los datos transmitidos
 * 
 * @author D. García
 */
public class MensajeConContenido implements MensajeVortex {

	private IdDeMensaje idDeMensaje;
	public static final String idDeMensaje_FIELD = "idDeMensaje";

	private ContenidoVortex contenido;
	public static final String contenido_FIELD = "contenido";

	/**
	 * @see net.gaia.vortex.core.api.mensaje.MensajeVortex#getContenido()
	 */
	
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
		mensaje.idDeMensaje = contenido.getIdDeMensaje();
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
		return mensajeReconstruido;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	
	public String toString() {
		return ToString.de(this).add(idDeMensaje_FIELD, idDeMensaje).add(contenido_FIELD, contenido).toString();
	}

	/**
	 * @see net.gaia.vortex.core.api.mensaje.MensajeVortex#getIdDeMensaje()
	 */
	
	public IdDeMensaje getIdDeMensaje() {
		return this.idDeMensaje;
	}

	/**
	 * @see net.gaia.vortex.core.api.mensaje.MensajeVortex#asignarId(net.gaia.vortex.api.ids.mensajes.IdDeMensaje)
	 */
	
	public void asignarId(final IdDeMensaje idNuevo) {
		this.idDeMensaje = idNuevo;
		getContenido().setIdDeMensaje(idNuevo);
	}

	/**
	 * @see net.gaia.vortex.api.proto.ShortStringable#toShortString()
	 */
	
	public String toShortString() {
		return idDeMensaje.toShortString();
	}

}
