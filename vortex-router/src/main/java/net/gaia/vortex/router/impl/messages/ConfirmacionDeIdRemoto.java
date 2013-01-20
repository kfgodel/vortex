/**
 * 30/10/2012 11:38:19 Copyright (C) 2011 10Pines S.R.L.
 */
package net.gaia.vortex.router.impl.messages;

import java.util.Map;

import net.gaia.vortex.core.api.condiciones.Condicion;
import net.gaia.vortex.core.api.mensaje.MensajeVortex;
import net.gaia.vortex.sets.impl.AndCompuesto;
import net.gaia.vortex.sets.impl.AtributoPresente;
import net.gaia.vortex.sets.impl.ValorEsperadoEn;
import ar.com.dgarcia.lang.strings.ToString;

/**
 * Esta clase representa el ultimo mensaje del handshake para cerrar la comunicacion bidireccional
 * entre patas de nodos distintos.<br>
 * El id de pata conductora corresponde al ID local
 * 
 * @author D. García
 */
public class ConfirmacionDeIdRemoto extends MensajeBidiSupport {

	/**
	 * Identificador para este tipo de mensajes bidi
	 */
	public static final String NOMBRE_DE_TIPO = "vortex.id.confirmacion";

	private Long idDePataLocalAlEmisor;
	public static final String idDePataLocalAlEmisor_FIELD = "idDePataLocalAlEmisor";

	private Map<String, Object> idDeRespuesta;
	public static final String idDeRespuesta_FIELD = "idDeRespuesta";

	public ConfirmacionDeIdRemoto() {
		super(NOMBRE_DE_TIPO);
	}

	public Map<String, Object> getIdDeRespuesta() {
		return idDeRespuesta;
	}

	public void setIdDeRespuesta(final Map<String, Object> idDeRespuesta) {
		this.idDeRespuesta = idDeRespuesta;
	}

	public static ConfirmacionDeIdRemoto create(final MensajeVortex respuesta, final Long idLocal) {
		final ConfirmacionDeIdRemoto confirmacion = new ConfirmacionDeIdRemoto();
		confirmacion.setIdDePataLocalAlEmisor(idLocal);
		confirmacion.idDeRespuesta = respuesta.getIdDeMensaje().getAsMap();
		return confirmacion;
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
		return ToString.de(this).con(idDePataLocalAlReceptor_FIELD, getIdDePataLocalAlReceptor())
				.con(idDePataLocalAlEmisor_FIELD, idDePataLocalAlEmisor).con(idDeRespuesta_FIELD, idDeRespuesta)
				.toString();
	}

	/**
	 * Devuelve el filtro que permite recibir este tipo de mensajes
	 * 
	 * @return El filtro que descarta otros mensajes y permite recibir este tipo
	 */
	public static Condicion getFiltroDelTipo() {
		final Condicion filtroDeConfirmacion = AndCompuesto.de( //
				ValorEsperadoEn.elAtributo(nombreDeTipo_FIELD, NOMBRE_DE_TIPO),//
				AtributoPresente.conNombre(idDePataLocalAlReceptor_FIELD),//
				AtributoPresente.conNombre(idDePataLocalAlEmisor_FIELD),//
				AtributoPresente.conNombre(idDeRespuesta_FIELD)//
				);
		return filtroDeConfirmacion;
	}
}
