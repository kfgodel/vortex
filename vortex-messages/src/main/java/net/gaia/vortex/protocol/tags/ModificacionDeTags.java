/**
 * 01/12/2011 23:08:52 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.protocol.tags;

import java.util.List;

/**
 * Esta clase representa el metamensaje de modificación de los tags de un nodo publicados a otro
 * 
 * @author D. García
 */
public class ModificacionDeTags {

	private List<String> tagsRecibiblesAgregados;
	private List<String> tagsRecibiblesQuitados;
	private List<String> tagsEnviablesAgregados;
	private List<String> tagsEnviablesQuitados;

	public List<String> getTagsRecibiblesAgregados() {
		return tagsRecibiblesAgregados;
	}

	public void setTagsRecibiblesAgregados(final List<String> tagsRecibiblesAgregados) {
		this.tagsRecibiblesAgregados = tagsRecibiblesAgregados;
	}

	public List<String> getTagsRecibiblesQuitados() {
		return tagsRecibiblesQuitados;
	}

	public void setTagsRecibiblesQuitados(final List<String> tagsRecibiblesQuitados) {
		this.tagsRecibiblesQuitados = tagsRecibiblesQuitados;
	}

	public List<String> getTagsEnviablesAgregados() {
		return tagsEnviablesAgregados;
	}

	public void setTagsEnviablesAgregados(final List<String> tagsEnviablesAgregados) {
		this.tagsEnviablesAgregados = tagsEnviablesAgregados;
	}

	public List<String> getTagsEnviablesQuitados() {
		return tagsEnviablesQuitados;
	}

	public void setTagsEnviablesQuitados(final List<String> tagsEnviablesQuitados) {
		this.tagsEnviablesQuitados = tagsEnviablesQuitados;
	}

}
