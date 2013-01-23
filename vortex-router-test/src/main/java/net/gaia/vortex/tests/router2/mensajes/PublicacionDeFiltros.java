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
package net.gaia.vortex.tests.router2.mensajes;

import net.gaia.vortex.tests.router2.impl.filtros.Filtro;
import net.gaia.vortex.tests.router2.impl.filtros.FiltroPorStrings;

/**
 * Esta clase representa el mensaje para publicar filtros de un nodo a otro
 * 
 * @author D. García
 */
public class PublicacionDeFiltros extends MensajeBidiSuppor {

	private Filtro filtro;

	public Filtro getFiltro() {
		return filtro;
	}

	public void setFiltro(final Filtro filtro) {
		this.filtro = filtro;
	}

	public static PublicacionDeFiltros create(final Filtro nuevoFiltro, final Long idDePataRemota) {
		final PublicacionDeFiltros publicacion = new PublicacionDeFiltros();
		publicacion.filtro = nuevoFiltro;
		publicacion.setIdLocalAlReceptor(idDePataRemota);
		return publicacion;
	}

	/**
	 * @see net.gaia.vortex.tests.router2.api.Mensaje#getTag()
	 */
	@Override
	public String getTag() {
		return FiltroPorStrings.META_MENSAJE;
	}

}
