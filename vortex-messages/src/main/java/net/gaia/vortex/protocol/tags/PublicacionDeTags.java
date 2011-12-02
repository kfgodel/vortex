/**
 * 01/12/2011 23:06:52 Copyright (C) 2011 Darío L. García
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
 * Esta clase representa el metamensaje de la publicación de tags, donde un nodo le dice a otro los
 * tags que le interesa enviar y recibir.<br>
 * Este mensaje indica cada vez que es enviado todos los tags, reemplazando los anteriores
 * 
 * @author D. García
 */
public class PublicacionDeTags {

	private List<String> tagsEnviables;
	private List<String> tagsRecibibles;

	public List<String> getTagsEnviables() {
		return tagsEnviables;
	}

	public void setTagsEnviables(final List<String> tagsEnviables) {
		this.tagsEnviables = tagsEnviables;
	}

	public List<String> getTagsRecibibles() {
		return tagsRecibibles;
	}

	public void setTagsRecibibles(final List<String> tagsRecibibles) {
		this.tagsRecibibles = tagsRecibibles;
	}

}
