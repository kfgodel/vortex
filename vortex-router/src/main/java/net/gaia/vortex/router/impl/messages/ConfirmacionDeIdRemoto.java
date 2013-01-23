/**
 * 30/10/2012 11:38:19 Copyright (C) 2011 10Pines S.R.L.
 */
package net.gaia.vortex.router.impl.messages;

import java.util.Map;

import net.gaia.vortex.core.api.condiciones.Condicion;
import net.gaia.vortex.core.api.mensaje.MensajeVortex;
import net.gaia.vortex.sets.impl.condiciones.AndCompuesto;
import net.gaia.vortex.sets.impl.condiciones.AtributoPresente;
import net.gaia.vortex.sets.impl.condiciones.ValorEsperadoEn;
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
	public static final String NOMBRE_DE_TIPO = PREFIJO_METAMENSAJE + "IdRemoto.Confirmacion";

	private Long idLocalAlEmisor;
	public static final String idLocalAlEmisor_FIELD = "idLocalAlEmisor";

	private Map<String, Object> idRespuestaOriginal;
	public static final String idDeRespuesta_FIELD = "idDeRespuesta";

	public ConfirmacionDeIdRemoto() {
		super(NOMBRE_DE_TIPO);
	}

	public Map<String, Object> getIdRespuestaOriginal() {
		return idRespuestaOriginal;
	}

	public void setIdRespuestaOriginal(final Map<String, Object> idDeRespuesta) {
		this.idRespuestaOriginal = idDeRespuesta;
	}

	public static ConfirmacionDeIdRemoto create(final MensajeVortex respuesta, final Long idLocal) {
		final ConfirmacionDeIdRemoto confirmacion = new ConfirmacionDeIdRemoto();
		confirmacion.setIdLocalAlEmisor(idLocal);
		confirmacion.idRespuestaOriginal = respuesta.getIdDeMensaje().getAsMap();
		return confirmacion;
	}

	public Long getIdLocalAlEmisor() {
		return idLocalAlEmisor;
	}

	public void setIdLocalAlEmisor(final Long idDePataLocalAlEmisor) {
		this.idLocalAlEmisor = idDePataLocalAlEmisor;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return ToString.de(this).con(idLocalAlReceptor_FIELD, getIdLocalAlReceptor())
				.con(idLocalAlEmisor_FIELD, idLocalAlEmisor).con(idDeRespuesta_FIELD, idRespuestaOriginal).toString();
	}

	/**
	 * Devuelve el filtro que permite recibir este tipo de mensajes
	 * 
	 * @return El filtro que descarta otros mensajes y permite recibir este tipo
	 */
	public static Condicion getFiltroDelTipo() {
		final Condicion filtroDeConfirmacion = AndCompuesto.de( //
				ValorEsperadoEn.elAtributo(tipoDeMensaje_FIELD, NOMBRE_DE_TIPO),//
				AtributoPresente.conNombre(idLocalAlReceptor_FIELD),//
				AtributoPresente.conNombre(idLocalAlEmisor_FIELD),//
				AtributoPresente.conNombre(idDeRespuesta_FIELD)//
				);
		return filtroDeConfirmacion;
	}
}
