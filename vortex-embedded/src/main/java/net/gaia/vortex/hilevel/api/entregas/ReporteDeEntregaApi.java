/**
 * 10/02/2012 20:17:00 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.hilevel.api.entregas;

import net.gaia.vortex.hilevel.api.MensajeVortexApi;

import com.google.common.base.Objects;
import com.google.common.base.Objects.ToStringHelper;

/**
 * Esta clase representa un reporte de entrega de un mensaje generado a partir de la información
 * disponible en los acuses recibidos.<br>
 * A través de esta clase el cliente vortex comunica a su código cliente qué pasó con el mensaje
 * 
 * @author D. García
 */
public class ReporteDeEntregaApi {

	private MensajeVortexApi mensajeEnviado;
	public static final String mensajeEnviado_FIELD = "mensajeEnviado";

	private StatusDeEntrega status;
	public static final String status_FIELD = "status";

	private String codigoError;
	public static final String codigoError_FIELD = "codigoError";
	private String descripcionError;
	public static final String descripcionError_FIELD = "descripcionError";

	private Long cantidadInteresados;
	public static final String cantidadInteresados_FIELD = "cantidadInteresados";
	private Long cantidadDuplicados;
	public static final String cantidadDuplicados_FIELD = "cantidadDuplicados";
	private Long cantidadFallados;
	public static final String cantidadFallados_FIELD = "cantidadFallados";
	private Long cantidadConsumidos;
	public static final String cantidadConsumidos_FIELD = "cantidadConsumidos";

	public String getCodigoError() {
		return codigoError;
	}

	public void setCodigoError(final String codigoError) {
		this.codigoError = codigoError;
	}

	public String getDescripcionError() {
		return descripcionError;
	}

	public void setDescripcionError(final String descripcionError) {
		this.descripcionError = descripcionError;
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

	public StatusDeEntrega getStatus() {
		return status;
	}

	public void setStatus(final StatusDeEntrega status) {
		this.status = status;
	}

	public MensajeVortexApi getMensajeEnviado() {
		return mensajeEnviado;
	}

	public void setMensajeEnviado(final MensajeVortexApi mensajeEnviado) {
		this.mensajeEnviado = mensajeEnviado;
	}

	public static ReporteDeEntregaApi create(final MensajeVortexApi mensaje, final StatusDeEntrega status) {
		final ReporteDeEntregaApi reporte = new ReporteDeEntregaApi();
		reporte.status = status;
		reporte.mensajeEnviado = mensaje;
		return reporte;
	}

	/**
	 * Indica si este reporte indica que el mensaje fue entregado a alguien en la red
	 * 
	 * @return false si el mensaje no puede considerarse como entregado a alguien
	 */
	public boolean fueExitoso() {
		return getStatus().correspondeAMensajeEntregado();
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		final ToStringHelper helper = Objects.toStringHelper(this).add(status_FIELD, status);
		if (status.esRechazo()) {
			helper.add(codigoError_FIELD, codigoError).add(descripcionError_FIELD, descripcionError);
		}
		if (status.fueRuteado()) {
			helper.add(cantidadInteresados_FIELD, cantidadInteresados)
					.add(cantidadConsumidos_FIELD, cantidadConsumidos).add(cantidadFallados_FIELD, cantidadFallados)
					.add(cantidadDuplicados_FIELD, cantidadDuplicados);
		}
		return helper.toString();
	}
}
