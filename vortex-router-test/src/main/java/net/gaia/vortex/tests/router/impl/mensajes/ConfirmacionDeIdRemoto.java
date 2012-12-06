/**
 * 30/10/2012 11:38:19 Copyright (C) 2011 10Pines S.R.L.
 */
package net.gaia.vortex.tests.router.impl.mensajes;

import net.gaia.vortex.tests.router.impl.patas.filtros.FiltroPorStrings;

/**
 * Esta clase representa el ultimo mensaje del handshake para cerrar la comunicacion bidireccional
 * entre patas de nodos distintos
 * 
 * @author D. García
 */
public class ConfirmacionDeIdRemoto extends MensajeSupport {

	private RespuestaDeIdRemoto respuesta;

	private Long idAsignado;

	public Long getIdAsignado() {
		return idAsignado;
	}

	public void setIdAsignado(final Long idAsignado) {
		this.idAsignado = idAsignado;
	}

	public RespuestaDeIdRemoto getRespuesta() {
		return respuesta;
	}

	public void setRespuesta(final RespuestaDeIdRemoto respuesta) {
		this.respuesta = respuesta;
	}

	public static ConfirmacionDeIdRemoto create(final RespuestaDeIdRemoto respuesta, final Long idLocal) {
		final ConfirmacionDeIdRemoto name = new ConfirmacionDeIdRemoto();
		name.respuesta = respuesta;
		name.idAsignado = idLocal;
		return name;
	}

	/**
	 * @see net.gaia.vortex.tests.router2.api.Mensaje#getTag()
	 */
	@Override
	public String getTag() {
		return FiltroPorStrings.META_MENSAJE;
	}

}
