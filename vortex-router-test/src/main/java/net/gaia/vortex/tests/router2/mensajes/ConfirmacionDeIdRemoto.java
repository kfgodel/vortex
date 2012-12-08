/**
 * 30/10/2012 11:38:19 Copyright (C) 2011 10Pines S.R.L.
 */
package net.gaia.vortex.tests.router2.mensajes;

import net.gaia.vortex.tests.router2.impl.filtros.FiltroPorStrings;
import ar.com.dgarcia.lang.strings.ToString;

/**
 * Esta clase representa el ultimo mensaje del handshake para cerrar la comunicacion bidireccional
 * entre patas de nodos distintos.<br>
 * El id de pata conductora corresponde al ID local
 * 
 * @author D. García
 */
public class ConfirmacionDeIdRemoto extends MensajeSupport {

	private Long idDePataLocalAlEmisor;
	public static final String idDePataLocalAlEmisor_FIELD = "idDePataLocalAlEmisor";

	private RespuestaDeIdRemoto respuesta;

	public RespuestaDeIdRemoto getRespuesta() {
		return respuesta;
	}

	public void setRespuesta(final RespuestaDeIdRemoto respuesta) {
		this.respuesta = respuesta;
	}

	public static ConfirmacionDeIdRemoto create(final RespuestaDeIdRemoto respuesta, final Long idLocal) {
		final ConfirmacionDeIdRemoto confirmacion = new ConfirmacionDeIdRemoto();
		confirmacion.respuesta = respuesta;
		confirmacion.setIdDePataLocalAlEmisor(idLocal);
		return confirmacion;
	}

	/**
	 * @see net.gaia.vortex.tests.router2.api.Mensaje#getTag()
	 */
	@Override
	public String getTag() {
		return FiltroPorStrings.META_MENSAJE;
	}

	public Long getIdDePataLocalAlEmisor() {
		return idDePataLocalAlEmisor;
	}

	public void setIdDePataLocalAlEmisor(final Long idDePataLocalAlEmisor) {
		this.idDePataLocalAlEmisor = idDePataLocalAlEmisor;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return ToString.de(this).toString();
	}

}
