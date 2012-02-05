/**
 * 11/01/2012 23:55:12 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.protocol.messages.tags;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import net.gaia.vortex.protocol.messages.MetamensajeVortex;

import com.google.common.base.Objects;

/**
 * Esta clase representa el metamensaje para quitar tags asociados a un cliente vortex
 * 
 * @author D. García
 */
public class QuitarTags implements MetamensajeVortex {

	private List<String> tags;
	public static final String tags_FIELD = "tags";

	public List<String> getTags() {
		return tags;
	}

	public void setTags(final List<String> tags) {
		this.tags = tags;
	}

	public static QuitarTags create(final Set<String> quitados) {
		final QuitarTags quitar = new QuitarTags();
		quitar.tags = new ArrayList<String>(quitados);
		return quitar;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return Objects.toStringHelper(this).add(tags_FIELD, tags).toString();
	}

}
