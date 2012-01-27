/**
 * 27/01/2012 00:17:50 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.hilevel.api.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ar.com.fdvs.dgarcia.colecciones.sets.ConcurrentHashSet;

/**
 * Esta clase representa la información de tags declarada por el nodo
 * 
 * @author D. García
 */
public class TagsDelNodo {

	private ConcurrentHashSet<String> tagsDelNodo;

	public static TagsDelNodo create() {
		final TagsDelNodo tags = new TagsDelNodo();
		tags.tagsDelNodo = new ConcurrentHashSet<String>();
		return tags;
	}

	/**
	 * Agrega los tags indicados como declarados por el nodo
	 * 
	 * @param tags
	 *            Los tags a agregar a los declarados
	 */
	public void agregar(final List<String> tags) {
		tagsDelNodo.addAll(tags);
	}

	/**
	 * Quita los tags indicados de los declarados del nodo
	 * 
	 * @param tags
	 *            Los tags a quitar
	 */
	public void quitar(final List<String> tags) {
		tagsDelNodo.removeAll(tags);
	}

	/**
	 * Reemplaza los tags declarados del nodo por los pasados
	 * 
	 * @param tags
	 */
	public void reemplazar(final List<String> tags) {
		tagsDelNodo.clear();
		tagsDelNodo.addAll(tags);
	}

	/**
	 * Elimina los tags declarados del nodo
	 */
	public void limpiar() {
		tagsDelNodo.clear();
	}

	/**
	 * Devuelve todos los tags declarados por el nodo
	 * 
	 * @return
	 */
	public Set<String> getAllTags() {
		return new HashSet<String>(tagsDelNodo);
	}
}
