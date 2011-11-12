/**
 * 31/10/2011 23:54:32 Copyright (C) 2011 Darío L. García
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

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.gaia.vortex.model.messages.TipoMetaMensaje;
import net.gaia.vortex.persistibles.ReceptorHttp;
import net.gaia.vortex.persistibles.TagPersistible;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

/**
 * Essta clase representa el mensaje recibido para agregarle tags a un receptor
 * 
 * @author D. García
 */
public class AgregarTagsMetaMensaje implements MetamensajeSobreTags {
	private static final Logger LOG = LoggerFactory.getLogger(AgregarTagsMetaMensaje.class);

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
		for (final String tagNuevo : tags) {
			final TagPersistible preexistente = receptorHttp.getTag(tagNuevo);
			if (preexistente != null) {
				// Ya tiene el tag
				LOG.warn("El receptor[{}] esta declarando un tag repetido[{}]", receptorHttp.getId(), tagNuevo);
				continue;
			}
			receptorHttp.agregarTag(tagNuevo);
		}
	}

	public static AgregarTagsMetaMensaje create(final Set<String> tagsDelServerExcluyendoAlReceptor) {
		final AgregarTagsMetaMensaje agregar = new AgregarTagsMetaMensaje();
		agregar.tags = Lists.newArrayList(tagsDelServerExcluyendoAlReceptor);
		return agregar;
	}

	/**
	 * @param tags2
	 * @return
	 */
	public static AgregarTagsMetaMensaje create(final String[] tags2) {
		final HashSet<String> tags = Sets.newHashSet(tags2);
		return create(tags);
	}

	/**
	 * @see net.gaia.vortex.model.messages.meta.MetamensajeSobreTags#getTipoDeMetamensaje()
	 */
	@Override
	@JsonIgnore
	public TipoMetaMensaje getTipoDeMetamensaje() {
		return TipoMetaMensaje.AGREGAR_TAGS;
	}

	/**
	 * @see net.gaia.vortex.model.messages.meta.MetamensajeSobreTags#modificarTagsNotificadosA(net.gaia.vortex.persistibles.ReceptorHttp)
	 */
	@Override
	public void modificarTagsNotificadosA(final ReceptorHttp receptorHttp) {
		for (final String tagAgregado : tags) {
			receptorHttp.agregarNotificado(tagAgregado);
		}
	}

}
