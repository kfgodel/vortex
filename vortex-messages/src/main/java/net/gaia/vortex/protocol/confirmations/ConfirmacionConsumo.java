/**
 * 27/11/2011 13:51:46 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.protocol.confirmations;

import net.gaia.vortex.protocol.IdVortex;

/**
 * Esta clase representa la informacion de consumo del mensaje recibido por el nodo, que es devuelto
 * como feedback al emisor para que determine que tan bueno es el camino
 * 
 * @author D. García
 */
public class ConfirmacionConsumo {

	/**
	 * identificacion del mensaje original
	 */
	private IdVortex identificacionMensaje;
	public static final String identificacionMensaje_FIELD = "identificacionMensaje";

	/**
	 * Mensajes que fueron entregados y consumidos por un endpoint
	 */
	private Integer consumidos;
	/**
	 * Mensajes que fueron rechazados en el camino
	 */
	private Integer rechazados;
	/**
	 * Mensajes de los que no se tiene confirmación ni de rechazo ni de consumición (posiblemente
	 * enlaces que se cortaron, o timeouts demasiado chicos)
	 */
	private Integer perdidos;

	/**
	 * Cantidad de receptores interesados en el mensaje
	 */
	private Integer interesados;

	/**
	 * Cantidad de receptores a los que no les interesaba el mensaje
	 */
	private Integer noInteresados;

	/**
	 * Cantidad de nodos a los que se les intento enviar el mensaje
	 */
	private Integer seleccionados;

	public Integer getConsumidos() {
		return consumidos;
	}

	public void setConsumidos(final Integer consumidos) {
		this.consumidos = consumidos;
	}

	public Integer getRechazados() {
		return rechazados;
	}

	public void setRechazados(final Integer rechazados) {
		this.rechazados = rechazados;
	}

	public Integer getPerdidos() {
		return perdidos;
	}

	public void setPerdidos(final Integer perdidos) {
		this.perdidos = perdidos;
	}

	public IdVortex getIdentificacionMensaje() {
		return identificacionMensaje;
	}

	public void setIdentificacionMensaje(final IdVortex identificacionMensaje) {
		this.identificacionMensaje = identificacionMensaje;
	}

	public Integer getInteresados() {
		return interesados;
	}

	public void setInteresados(final Integer interesados) {
		this.interesados = interesados;
	}

	public Integer getNoInteresados() {
		return noInteresados;
	}

	public void setNoInteresados(final Integer noInteresados) {
		this.noInteresados = noInteresados;
	}

	public Integer getSeleccionados() {
		return seleccionados;
	}

	public void setSeleccionados(final Integer seleccionados) {
		this.seleccionados = seleccionados;
	}

	public static ConfirmacionConsumo create(final IdVortex identificacionDelMensaje) {
		final ConfirmacionConsumo confirmacion = new ConfirmacionConsumo();
		confirmacion.identificacionMensaje = identificacionDelMensaje;
		confirmacion.interesados = 0;
		confirmacion.noInteresados = 0;
		confirmacion.perdidos = 0;
		confirmacion.consumidos = 0;
		confirmacion.rechazados = 0;
		confirmacion.seleccionados = 0;
		return confirmacion;
	}
}
