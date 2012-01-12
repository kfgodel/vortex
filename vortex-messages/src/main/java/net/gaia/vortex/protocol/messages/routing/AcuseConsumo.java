/**
 * 12/01/2012 00:01:15 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.protocol.messages.routing;

import net.gaia.vortex.protocol.messages.IdVortex;

/**
 * Esta clase representa el metamensaje de notificación de consumo realizado de un mensaje por parte
 * de un ruteador
 * 
 * @author D. García
 */
public class AcuseConsumo {
	private IdVortex idMensajeConsumido;
	private Integer valorConsumo;
	private Long cantidadInteresados;
	private Long cantidadDuplicados;
	private Long cantidadFallados;
	private Long cantidadConsumidos;

	public IdVortex getIdMensajeConsumido() {
		return idMensajeConsumido;
	}

	public void setIdMensajeConsumido(final IdVortex idMensajeConsumido) {
		this.idMensajeConsumido = idMensajeConsumido;
	}

	public Integer getValorConsumo() {
		return valorConsumo;
	}

	public void setValorConsumo(final Integer valorConsumo) {
		this.valorConsumo = valorConsumo;
	}

	public Long getCantidadInteresados() {
		return cantidadInteresados;
	}

	public void setCantidadInteresados(final Long cantidadInteresados) {
		this.cantidadInteresados = cantidadInteresados;
	}

	public Long getCantidadDuplicados() {
		return cantidadDuplicados;
	}

	public void setCantidadDuplicados(final Long cantidadDuplicados) {
		this.cantidadDuplicados = cantidadDuplicados;
	}

	public Long getCantidadFallados() {
		return cantidadFallados;
	}

	public void setCantidadFallados(final Long cantidadFallados) {
		this.cantidadFallados = cantidadFallados;
	}

	public Long getCantidadConsumidos() {
		return cantidadConsumidos;
	}

	public void setCantidadConsumidos(final Long cantidadConsumidos) {
		this.cantidadConsumidos = cantidadConsumidos;
	}

}
