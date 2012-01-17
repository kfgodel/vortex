/**
 * 16/01/2012 19:48:06 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.lowlevel.impl.tags;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.common.collect.Sets;

import net.gaia.vortex.lowlevel.impl.ReceptorVortex;
import ar.com.dgarcia.coding.caching.Instantiator;
import ar.com.dgarcia.coding.caching.SimpleCacheConcurrentMap;

/**
 * Esta clase representa una implementación del {@link TagSummarizer}
 * 
 * @author D. García
 */
public class TagSummarizerImpl implements TagSummarizer {

	private SimpleCacheConcurrentMap<String, Set<ReceptorVortex>> receptoresPorTag;
	private SimpleCacheConcurrentMap<ReceptorVortex, Set<String>> tagsPorReceptor;

	private TagChangeListener listener;

	public static TagSummarizerImpl create(final TagChangeListener listener) {
		final TagSummarizerImpl summarizer = new TagSummarizerImpl();
		// Definimos el instanciador junto con la cache
		summarizer.receptoresPorTag = new SimpleCacheConcurrentMap<String, Set<ReceptorVortex>>(
				new Instantiator<String, Set<ReceptorVortex>>() {
					@Override
					public Set<ReceptorVortex> instantiate(final String input) {
						return new HashSet<ReceptorVortex>();
					}
				});
		summarizer.tagsPorReceptor = new SimpleCacheConcurrentMap<ReceptorVortex, Set<String>>(
				new Instantiator<ReceptorVortex, Set<String>>() {
					@Override
					public Set<String> instantiate(final ReceptorVortex input) {
						return new HashSet<String>();
					}
				});
		summarizer.listener = listener;
		return summarizer;
	}

	/**
	 * @see net.gaia.vortex.lowlevel.impl.tags.TagSummarizer#agregarTagsPara(net.gaia.vortex.lowlevel.impl.ReceptorVortex,
	 *      java.util.List)
	 */
	@Override
	public void agregarTagsPara(final ReceptorVortex receptorAModificar, final List<String> nuevosTags) {
		final HashSet<String> reallyNewTags = new HashSet<String>();
		final Set<String> tagsPrevios = tagsPorReceptor.getOrCreateIfNullFor(receptorAModificar);
		synchronized (tagsPrevios) {
			for (final String nuevoTag : tagsPrevios) {
				final boolean reallyAdded = tagsPrevios.add(nuevoTag);
				if (reallyAdded) {
					reallyNewTags.add(nuevoTag);
				}
			}
		}

		final Set<String> tagAgregadosGlobalmente = new HashSet<String>();
		for (final String nuevoTag : nuevosTags) {
			final Set<ReceptorVortex> receptoresDelTag = receptoresPorTag.getOrCreateIfNullFor(nuevoTag);
			// Nos aseguramos que sólo nosotros modifiquemos este set
			synchronized (receptoresDelTag) {
				if (receptoresDelTag.isEmpty()) {
					// Si no había antes, hay que avisar que este tag es nuevo
					tagAgregadosGlobalmente.add(nuevoTag);
				}
				receptoresDelTag.add(receptorAModificar);
			}
		}
		if (!tagAgregadosGlobalmente.isEmpty()) {
			// Hay nuevos tags agregados para notificar
			listener.onTagsAgregadosGlobalmente(tagAgregadosGlobalmente, receptorAModificar);
		}
	}

	/**
	 * @see net.gaia.vortex.lowlevel.impl.tags.TagSummarizer#quitarTagsPara(net.gaia.vortex.lowlevel.impl.ReceptorVortex,
	 *      java.util.List)
	 */
	@Override
	public void quitarTagsPara(final ReceptorVortex receptorAModificar, final List<String> nuevosTags) {
		final Set<String> tagQuitadosGlobalmente = new HashSet<String>();
		for (final String nuevoTag : nuevosTags) {
			final Set<ReceptorVortex> receptoresDelTag = receptoresPorTag.getOrCreateIfNullFor(nuevoTag);
			// Nos aseguramos que sólo nosotros modifiquemos este set
			synchronized (receptoresDelTag) {
				receptoresDelTag.remove(receptoresDelTag);
				if (receptoresDelTag.isEmpty()) {
					// Si no quedó nadie después, es un tag a eliminar globalmente
					tagQuitadosGlobalmente.add(nuevoTag);
				}
			}
		}
		if (!tagQuitadosGlobalmente.isEmpty()) {
			// Hay nuevos tags agregados para notificar
			listener.onTagsQuitadosGlobalmente(tagQuitadosGlobalmente, receptorAModificar);
		}
	}

	/**
	 * @see net.gaia.vortex.lowlevel.impl.tags.TagSummarizer#reemplazarTagsPara(net.gaia.vortex.lowlevel.impl.ReceptorVortex,
	 *      java.util.List)
	 */
	@Override
	public void reemplazarTagsPara(final ReceptorVortex receptorAModificar, final List<String> nuevosTags) {
		final Set<String> tagsPrevios = tagsPorReceptor.get(receptorAModificar);
		//Tratamos de ser los unicos mo
		synchronized(tagsPrevios){
			
		}
		Sets.difference(null, tagsPrevios)
	}

	/**
	 * @see net.gaia.vortex.lowlevel.impl.tags.TagSummarizer#limpiarTagsPara(net.gaia.vortex.lowlevel.impl.ReceptorVortex)
	 */
	@Override
	public void limpiarTagsPara(final ReceptorVortex receptorAModificar) {
		// TODO Auto-generated method stub

	}
}
