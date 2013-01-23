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
package net.gaia.vortex.router.impl.messages;

import java.util.Map;

import net.gaia.vortex.core.api.condiciones.Condicion;
import net.gaia.vortex.core.api.mensaje.MensajeVortex;
import net.gaia.vortex.sets.impl.condiciones.AndCompuesto;
import net.gaia.vortex.sets.impl.condiciones.AtributoPresente;
import net.gaia.vortex.sets.impl.condiciones.ValorEsperadoEn;
import ar.com.dgarcia.lang.strings.ToString;

/**
 * Esta clase representa la reconfirmación enviada por una pata para indicar que ya es bidireccional
 * 
 * @author D. García
 */
public class ReconfirmacionDeIdRemoto extends MensajeBidiSupport {

	/**
	 * Identificador para este tipo de mensajes bidi
	 */
	public static final String NOMBRE_DE_TIPO = PREFIJO_METAMENSAJE + "IdRemoto.ReConfirmacion";

	private Long idLocalAlEmisor;
	public static final String idLocalAlEmisor_FIELD = "idLocalAlEmisor";

	private Map<String, Object> idConfirmacionOriginal;
	public static final String idDeRespuesta_FIELD = "idDeRespuesta";

	public ReconfirmacionDeIdRemoto() {
		super(NOMBRE_DE_TIPO);
	}

	public Map<String, Object> getIdConfirmacionOriginal() {
		return idConfirmacionOriginal;
	}

	public void setIdConfirmacionOriginal(final Map<String, Object> idDeRespuesta) {
		this.idConfirmacionOriginal = idDeRespuesta;
	}

	public static ReconfirmacionDeIdRemoto create(final MensajeVortex respuesta, final Long idLocal) {
		final ReconfirmacionDeIdRemoto confirmacion = new ReconfirmacionDeIdRemoto();
		confirmacion.setIdLocalAlEmisor(idLocal);
		confirmacion.idConfirmacionOriginal = respuesta.getIdDeMensaje().getAsMap();
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
				.con(idLocalAlEmisor_FIELD, idLocalAlEmisor).con(idDeRespuesta_FIELD, idConfirmacionOriginal)
				.toString();
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
