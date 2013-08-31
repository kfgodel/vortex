/**
 * 23/01/2013 11:54:25 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.router.impl.messages.bidi;

import net.gaia.vortex.api.condiciones.Condicion;
import net.gaia.vortex.router.impl.messages.meta.MensajeConIdDePataReceptora;
import net.gaia.vortex.router.impl.messages.meta.MetamensajeSupport;
import net.gaia.vortex.sets.impl.condiciones.AndCompuesto;
import net.gaia.vortex.sets.impl.condiciones.AtributoPresente;
import net.gaia.vortex.sets.impl.condiciones.ValorEsperadoEn;
import ar.com.dgarcia.lang.strings.ToString;

/**
 * Esta clase representa la reconfirmación enviada por una pata para indicar que ya es bidireccional
 * 
 * @author D. García
 */
public class ReconfirmacionDeIdRemoto extends MetamensajeSupport implements MensajeConIdDePataReceptora {

	/**
	 * Identificador para este tipo de mensajes bidi
	 */
	public static final String NOMBRE_DE_TIPO = PREFIJO_METAMENSAJE + "IdRemoto.ReConfirmacion";

	private Long idLocalAlEmisor;
	public static final String idLocalAlEmisor_FIELD = "idLocalAlEmisor";

	private Long idLocalAlReceptor;

	public ReconfirmacionDeIdRemoto() {
		super(NOMBRE_DE_TIPO);
	}

	
	public Long getIdLocalAlReceptor() {
		return idLocalAlReceptor;
	}

	public void setIdLocalAlReceptor(final Long idLocalAlReceptor) {
		this.idLocalAlReceptor = idLocalAlReceptor;
	}

	public static ReconfirmacionDeIdRemoto create(final Long idRemoto, final Long idLocal) {
		final ReconfirmacionDeIdRemoto confirmacion = new ReconfirmacionDeIdRemoto();
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
