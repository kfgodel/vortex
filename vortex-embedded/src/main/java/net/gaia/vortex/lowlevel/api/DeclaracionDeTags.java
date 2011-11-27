/**
 * 27/11/2011 14:39:18 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.lowlevel.api;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Esta clase representa una declaración de tags enviables y recibibles para ser procesada por un
 * nodo
 * 
 * @author D. García
 */
public class DeclaracionDeTags {

	private List<String> tagsRecibibles;
	private List<String> tagsEnviables;

	public List<String> getTagsRecibibles() {
		return tagsRecibibles;
	}

	public void setTagsRecibibles(final List<String> tagsRecibibles) {
		this.tagsRecibibles = tagsRecibibles;
	}

	public List<String> getTagsEnviables() {
		return tagsEnviables;
	}

	public void setTagsEnviables(final List<String> tagsEnviables) {
		this.tagsEnviables = tagsEnviables;
	}

	public static DeclaracionDeTags create(final Set<String> recibibles, final Set<String> enviables) {
		final DeclaracionDeTags declaracion = new DeclaracionDeTags();
		declaracion.tagsEnviables = new ArrayList<String>(enviables);
		declaracion.tagsRecibibles = new ArrayList<String>(recibibles);
		return declaracion;
	}
}
