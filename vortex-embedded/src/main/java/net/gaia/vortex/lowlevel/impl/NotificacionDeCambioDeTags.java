/**
 * 18/01/2012 22:00:35 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.lowlevel.impl;

import java.util.HashSet;
import java.util.Set;

/**
 * Esta clase representa la notificación de cambios en los tags del nodo que se deben avisar a un
 * cliente en particular
 * 
 * @author D. García
 */
public class NotificacionDeCambioDeTags {

	private ReceptorVortex receptorANotificar;
	private Set<String> tagsAgregados;
	private Set<String> tagsQuitados;

	/**
	 * Devuelve el receptor que debe ser notificado de los cambios de tags
	 * 
	 * @return
	 */
	public ReceptorVortex getReceptor() {
		return receptorANotificar;
	}

	/**
	 * Indica si esta notificación refiere a tags agregados
	 * 
	 * @return
	 */
	public boolean tieneTagsAgregados() {
		return tagsAgregados != null && !tagsAgregados.isEmpty();
	}

	/**
	 * Devuelve los tags agregados en esta notificación
	 * 
	 * @return
	 */
	public Set<String> getTagsAgregados() {
		if (tagsAgregados == null) {
			tagsAgregados = new HashSet<String>();

		}
		return tagsAgregados;
	}

	/**
	 * Indica si esta notificación registra tags quitados
	 * 
	 * @return
	 */
	public boolean tieneTagsQuitados() {
		return tagsQuitados != null && !tagsQuitados.isEmpty();
	}

	/**
	 * Devuelve los tags quitados del nodo
	 * 
	 * @return
	 */
	public Set<String> getTagsQuitados() {
		if (tagsQuitados == null) {
			tagsQuitados = new HashSet<String>();
		}
		return tagsQuitados;
	}

	public static NotificacionDeCambioDeTags create(final ReceptorVortex receptor) {
		final NotificacionDeCambioDeTags notificacion = new NotificacionDeCambioDeTags();
		notificacion.receptorANotificar = receptor;
		return notificacion;
	}

	/**
	 * Agrega el tag agregado como parte de esta notificación
	 * 
	 * @param tagAgregado
	 *            El tag que se agregó al nodo
	 */
	public void agregarTag(final String tagAgregado) {
		getTagsAgregados().add(tagAgregado);
	}

	/**
	 * Agrega el tag quitado como parte de esta notificación
	 * 
	 * @param tagQuitado
	 *            El tag quitado
	 */
	public void quitarTag(final String tagQuitado) {
		getTagsQuitados().add(tagQuitado);
	}

}
