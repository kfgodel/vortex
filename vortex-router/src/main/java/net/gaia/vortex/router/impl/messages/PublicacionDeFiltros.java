/**
 * 13/10/2012 18:19:04 Copyright (C) 2011 Darío L. García
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
import net.gaia.vortex.router.impl.messages.meta.MetamensajeSupport;
import net.gaia.vortex.sets.impl.condiciones.AndCompuesto;
import net.gaia.vortex.sets.impl.condiciones.AtributoPresente;
import net.gaia.vortex.sets.impl.condiciones.ValorEsperadoEn;
import ar.com.dgarcia.lang.strings.ToString;

/**
 * Esta clase representa el mensaje para publicar filtros de un nodo a otro
 * 
 * @author D. García
 */
public class PublicacionDeFiltros extends MetamensajeSupport {

	public static final String NOMBRE_DE_TIPO = "vortex.filtro.publicacion";

	private Long idLocalAlReceptor;
	public static final String idLocalAlReceptor_FIELD = "idLocalAlReceptor";

	private Map<String, Object> filtro;
	public static final String filtro_FIELD = "filtro";

	public PublicacionDeFiltros() {
		super(NOMBRE_DE_TIPO);
	}

	public Map<String, Object> getFiltro() {
		return filtro;
	}

	public void setFiltro(final Map<String, Object> filtro) {
		this.filtro = filtro;
	}

	public static PublicacionDeFiltros create(final Map<String, Object> nuevoFiltro, final Long idDePataRemota) {
		final PublicacionDeFiltros publicacion = new PublicacionDeFiltros();
		publicacion.filtro = nuevoFiltro;
		publicacion.idLocalAlReceptor = idDePataRemota;
		return publicacion;
	}

	public Long getIdLocalAlReceptor() {
		return idLocalAlReceptor;
	}

	public void setIdLocalAlReceptor(final Long idLocalAlReceptor) {
		this.idLocalAlReceptor = idLocalAlReceptor;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return ToString.de(this).con(idLocalAlReceptor_FIELD, idLocalAlReceptor).con(filtro_FIELD, filtro).toString();
	}

	/**
	 * Devuelve el filtro que permite recibir este tipo de mensajes
	 * 
	 * @return El filtro que descarta otros mensajes y permite recibir este tipo
	 */
	public static Condicion getFiltroDelTipo() {
		final Condicion filtroDePublicaciones = AndCompuesto.de( //
				ValorEsperadoEn.elAtributo(tipoDeMensaje_FIELD, NOMBRE_DE_TIPO),//
				AtributoPresente.conNombre(idLocalAlReceptor_FIELD),//
				AtributoPresente.conNombre(filtro_FIELD)//
				);
		return filtroDePublicaciones;
	}
}
