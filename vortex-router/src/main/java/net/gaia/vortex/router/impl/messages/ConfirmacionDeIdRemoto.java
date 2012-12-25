/**
 * 30/10/2012 11:38:19 Copyright (C) 2011 10Pines S.R.L.
 */
package net.gaia.vortex.router.impl.messages;


/**
 * Esta clase representa el ultimo mensaje del handshake para cerrar la comunicacion bidireccional
 * entre patas de nodos distintos.<br>
 * El id de pata conductora corresponde al ID local
 * 
 * @author D. García
 */
public class ConfirmacionDeIdRemoto extends MensajeBidiSupport {

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

	public Long getIdDePataLocalAlEmisor() {
		return idDePataLocalAlEmisor;
	}

	public void setIdDePataLocalAlEmisor(final Long idDePataLocalAlEmisor) {
		this.idDePataLocalAlEmisor = idDePataLocalAlEmisor;
	}

}
