/**
 * 31/10/2011 23:54:40 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.model.messages.meta;

import java.util.List;
import java.util.Set;

import net.gaia.vortex.model.messages.TipoMetaMensaje;
import net.gaia.vortex.persistibles.ReceptorHttp;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.google.common.collect.Lists;

/**
 * Esta clase representa el mensaje recibido para quitar los tags asociados a un receptor
 * 
 * @author D. García
 */
public class QuitarTagsMetaMensaje implements MetamensajeSobreTags {
	private List<String> tags;

	public List<String> getTags() {
		return tags;
	}

	public void setTags(final List<String> tags) {
		this.tags = tags;
	}

	/**
	 * @see net.gaia.vortex.model.messages.meta.MetamensajeSobreTags#modificarTagsInteresantesPara(net.gaia.vortex.persistibles.ReceptorHttp)
	 */
	@Override
	public void modificarTagsInteresantesPara(final ReceptorHttp receptorHttp) {
		for (final String tag : tags) {
			receptorHttp.quitarTag(tag);
		}
	}

	/**
	 * @see net.gaia.vortex.model.messages.meta.MetamensajeSobreTags#getTipoDeMetamensaje()
	 */
	@Override
	@JsonIgnore
	public TipoMetaMensaje getTipoDeMetamensaje() {
		return TipoMetaMensaje.QUITAR_TAGS;
	}

	public static QuitarTagsMetaMensaje create(final Set<String> tagsDelServerExcluyendoAlReceptor) {
		final QuitarTagsMetaMensaje agregar = new QuitarTagsMetaMensaje();
		agregar.tags = Lists.newArrayList(tagsDelServerExcluyendoAlReceptor);
		return agregar;
	}

	/**
	 * @see net.gaia.vortex.model.messages.meta.MetamensajeSobreTags#modificarTagsNotificadosA(net.gaia.vortex.persistibles.ReceptorHttp)
	 */
	@Override
	public void modificarTagsNotificadosA(final ReceptorHttp receptorHttp) {
		for (final String tagQuitado : tags) {
			receptorHttp.quitarNotificado(tagQuitado);
		}

	}
}
