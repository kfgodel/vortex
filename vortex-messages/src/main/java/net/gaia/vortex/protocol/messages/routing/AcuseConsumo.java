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
import net.sf.oval.constraint.NotNegative;
import net.sf.oval.constraint.NotNull;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import com.google.common.base.Objects;

/**
 * Esta clase representa el metamensaje de notificación de consumo realizado de un mensaje por parte
 * de un ruteador
 * 
 * @author D. García
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class AcuseConsumo implements Acuse {

	@NotNull
	private IdVortex idMensajeConsumido;
	public static final String idMensajeConsumido_FIELD = "idMensajeConsumido";

	/**
	 * Cantidad de clientes del nodo (otros nodos) interesados en el mensaje
	 */
	@NotNull
	@NotNegative
	private Long cantidadInteresados;
	public static final String cantidadInteresados_FIELD = "cantidadInteresados";
	/**
	 * Cantidad de ruteos que indicaron duplicado
	 */
	@NotNull
	@NotNegative
	private Long cantidadDuplicados;
	public static final String cantidadDuplicados_FIELD = "cantidadDuplicados";
	/**
	 * Cantidad de ruteos que tuvieron fallas
	 */
	@NotNull
	@NotNegative
	private Long cantidadFallados;
	public static final String cantidadFallados_FIELD = "cantidadFallados";
	/**
	 * Cantidad de clientes que indicaron consumo del mensaje
	 */
	@NotNull
	@NotNegative
	private Long cantidadConsumidos;
	public static final String cantidadConsumidos_FIELD = "cantidadConsumidos";

	public IdVortex getIdMensajeConsumido() {
		return idMensajeConsumido;
	}

	public void setIdMensajeConsumido(final IdVortex idMensajeConsumido) {
		this.idMensajeConsumido = idMensajeConsumido;
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

	/**
	 * @see net.gaia.vortex.protocol.messages.routing.Acuse#setIdMensajeInvolucrado(net.gaia.vortex.protocol.messages.IdVortex)
	 */
	@Override
	public void setIdMensajeInvolucrado(final IdVortex idMensaje) {
		this.setIdMensajeConsumido(idMensaje);
	}

	public static AcuseConsumo create() {
		final AcuseConsumo acuse = new AcuseConsumo();
		acuse.cantidadInteresados = 0L;
		acuse.cantidadDuplicados = 0L;
		acuse.cantidadFallados = 0L;
		acuse.cantidadConsumidos = 0L;
		return acuse;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return Objects.toStringHelper(this).add(idMensajeConsumido_FIELD, idMensajeConsumido)
				.add(cantidadInteresados_FIELD, cantidadInteresados).add(cantidadConsumidos_FIELD, cantidadConsumidos)
				.add(cantidadFallados_FIELD, cantidadFallados).add(cantidadDuplicados_FIELD, cantidadDuplicados)
				.toString();
	}
}
