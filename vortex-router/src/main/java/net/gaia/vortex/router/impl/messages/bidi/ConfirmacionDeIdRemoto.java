/**
 * 30/10/2012 11:38:19 Copyright (C) 2011 10Pines S.R.L.
 */
package net.gaia.vortex.router.impl.messages.bidi;

import net.gaia.vortex.core.api.condiciones.Condicion;
import net.gaia.vortex.router.impl.messages.meta.MensajeConIdDePataReceptora;
import net.gaia.vortex.router.impl.messages.meta.MetamensajeSupport;
import net.gaia.vortex.sets.impl.condiciones.AndCompuesto;
import net.gaia.vortex.sets.impl.condiciones.AtributoPresente;
import net.gaia.vortex.sets.impl.condiciones.ValorEsperadoEn;
import ar.com.dgarcia.lang.strings.ToString;

/**
 * Esta clase representa el último mensaje del handshake para cerrar la comunicación bidireccional
 * entre patas de nodos distintos.<br>
 * 
 * @author D. García
 */
public class ConfirmacionDeIdRemoto extends MetamensajeSupport implements MensajeConIdDePataReceptora {

	/**
	 * Identificador para este tipo de mensajes bidi
	 */
	public static final String NOMBRE_DE_TIPO = PREFIJO_METAMENSAJE + "IdRemoto.Confirmacion";

	private Long idLocalAlEmisor;
	public static final String idLocalAlEmisor_FIELD = "idLocalAlEmisor";

	private Long idLocalAlReceptor;

	public ConfirmacionDeIdRemoto() {
		super(NOMBRE_DE_TIPO);
	}

	@Override
	public Long getIdLocalAlReceptor() {
		return idLocalAlReceptor;
	}

	public void setIdLocalAlReceptor(final Long idLocalAlReceptor) {
		this.idLocalAlReceptor = idLocalAlReceptor;
	}

	public static ConfirmacionDeIdRemoto create(final Long idRemoto, final Long idLocal) {
		final ConfirmacionDeIdRemoto confirmacion = new ConfirmacionDeIdRemoto();
		confirmacion.setIdLocalAlEmisor(idLocal);
		confirmacion.setIdLocalAlReceptor(idRemoto);
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
		return ToString.de(this).con(idLocalAlReceptor_FIELD, idLocalAlReceptor)
				.con(idLocalAlEmisor_FIELD, idLocalAlEmisor).toString();
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
				AtributoPresente.conNombre(idLocalAlEmisor_FIELD)//
				);
		return filtroDeConfirmacion;
	}
}
