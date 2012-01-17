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
import java.util.Set;
import java.util.concurrent.locks.ReentrantLock;

import net.gaia.annotations.HasDependencyOn;
import net.gaia.vortex.lowlevel.impl.ReceptorVortex;
import net.gaia.vortex.meta.Decision;
import ar.com.dgarcia.coding.caching.Instantiator;
import ar.com.dgarcia.coding.caching.SimpleCacheConcurrentMap;

import com.google.common.collect.Sets;

/**
 * Esta clase representa una implementación del {@link TagSummarizer}
 * 
 * @author D. García
 */
public class TagSummarizerImpl implements TagSummarizer {

	private SimpleCacheConcurrentMap<String, Set<ReceptorVortex>> receptoresPorTag;
	private SimpleCacheConcurrentMap<ReceptorVortex, Set<String>> tagsPorReceptor;

	private ReentrantLock modificationLock;

	private TagChangeListener listener;

	public static TagSummarizerImpl create(final TagChangeListener listener) {
		final TagSummarizerImpl summarizer = new TagSummarizerImpl();
		summarizer.modificationLock = new ReentrantLock();

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
						@HasDependencyOn(Decision.LA_DIFERENCIA_DE_SETS_DE_TAGS_REQUIERE_LA_MISMA_IMPL_DE_SETS)
						final HashSet<String> tagsDelReceptor = new HashSet<String>();
						return tagsDelReceptor;
					}
				});
		summarizer.listener = listener;
		return summarizer;
	}

	/**
	 * Modifica el estado de este summarizer asegurándose que nadie más modifique el estado
	 * 
	 * @param code
	 *            Código a ejecutar para modificar el estado
	 */
	private void doModification(final Runnable code) {
		modificationLock.lock();
		try {
			code.run();
		} finally {
			modificationLock.unlock();
		}
	}

	/**
	 * @see net.gaia.vortex.lowlevel.impl.tags.TagSummarizer#agregarTagsPara(net.gaia.vortex.lowlevel.impl.ReceptorVortex,
	 *      Set)
	 */
	@Override
	public void agregarTagsPara(final ReceptorVortex receptorAModificar, final Set<String> nuevosTags) {
		final Set<String> tagGlobalmenteAgregados = internalAddTags(receptorAModificar, nuevosTags);
		if (!tagGlobalmenteAgregados.isEmpty()) {
			// Hay nuevos tags agregados para notificar
			listener.onTagsAgregadosGlobalmente(tagGlobalmenteAgregados, receptorAModificar);
		}
	}

	/**
	 * Agrega los tags indicados al receptor, actualizando la estructura interna de este summarizer,
	 * sin invocar al listener
	 * 
	 * @param receptorAModificar
	 *            El receptor al que se le agregan tags
	 * @param nuevosTags
	 *            Los tags agregados
	 * @return El conjunto de tags agregados globalmente
	 */
	private Set<String> internalAddTags(final ReceptorVortex receptorAModificar, final Set<String> nuevosTags) {
		if (nuevosTags.isEmpty()) {
			// Optimización para conjuntos vacíos
			return nuevosTags;
		}
		final HashSet<String> tagGlobalmenteAgregados = new HashSet<String>();
		doModification(new Runnable() {
			@Override
			public void run() {
				final Set<String> tagsDelReceptor = getTagsRegistradosDe(receptorAModificar);
				for (final String tagAgregado : nuevosTags) {
					final boolean reallyAdded = tagsDelReceptor.add(tagAgregado);
					if (!reallyAdded) {
						// Ya estaba declarado este tag
						continue;
					}
					final Set<ReceptorVortex> receptoresDelTag = getReceptoresRegistradosDe(tagAgregado);
					if (receptoresDelTag.isEmpty()) {
						// Es un tag nuevo globalmente
						tagGlobalmenteAgregados.add(tagAgregado);
					}
					receptoresDelTag.add(receptorAModificar);
				}
			}

		});
		return tagGlobalmenteAgregados;
	}

	/**
	 * Devuelve los tags previamente conocidos del receptor, o una lista vacía si no se conocía
	 * ninguno
	 */
	protected Set<String> getTagsRegistradosDe(final ReceptorVortex receptorAModificar) {
		return tagsPorReceptor.getOrCreateIfNullFor(receptorAModificar);
	}

	/**
	 * @see net.gaia.vortex.lowlevel.impl.tags.TagSummarizer#quitarTagsPara(net.gaia.vortex.lowlevel.impl.ReceptorVortex,
	 *      Set)
	 */
	@Override
	public void quitarTagsPara(final ReceptorVortex receptorAModificar, final Set<String> tagsQuitados) {
		final Set<String> tagGlobalmenteQuitados = internalRemoveTags(receptorAModificar, tagsQuitados);
		if (!tagGlobalmenteQuitados.isEmpty()) {
			// Hay nuevos tags agregados para notificar
			listener.onTagsQuitadosGlobalmente(tagGlobalmenteQuitados, receptorAModificar);
		}
	}

	/**
	 * Elimina los tags indicados del receptor actualizando la estructura interna de este summarizer
	 * atómicamente, sin invocar al listener
	 * 
	 * @param receptorAModificar
	 *            El receptor para el que se modificarán los datos
	 * @param tagsQuitados
	 *            Los tags quitados
	 * @return Los tags quitados globalmente
	 */
	private Set<String> internalRemoveTags(final ReceptorVortex receptorAModificar, final Set<String> tagsQuitados) {
		if (tagsQuitados.isEmpty()) {
			// Optimización para conjuntos vacíos
			return tagsQuitados;
		}
		final HashSet<String> tagGlobalmenteQuitados = new HashSet<String>();
		doModification(new Runnable() {
			@Override
			public void run() {
				final Set<String> tagsDelReceptor = getTagsRegistradosDe(receptorAModificar);
				for (final String tagQuitado : tagsQuitados) {
					final boolean reallyRemoved = tagsDelReceptor.remove(tagQuitado);
					if (!reallyRemoved) {
						// No estaba registrado como tag del receptor
						continue;
					}
					final Set<ReceptorVortex> receptoresDelTag = getReceptoresRegistradosDe(tagQuitado);
					receptoresDelTag.remove(receptorAModificar);
					if (receptoresDelTag.isEmpty()) {
						// Limpiamos la entrada del tag si no quedan receptores
						receptoresPorTag.remove(tagQuitado);
						// Era el último receptor de ese tag
						tagGlobalmenteQuitados.add(tagQuitado);
					}
				}
				// Limpiamos la entrada si no quedan tags para el receptor
				if (tagsDelReceptor.isEmpty()) {
					tagsPorReceptor.remove(receptorAModificar);
				}
			}
		});
		return tagGlobalmenteQuitados;
	}

	/**
	 * @see net.gaia.vortex.lowlevel.impl.tags.TagSummarizer#reemplazarTagsPara(net.gaia.vortex.lowlevel.impl.ReceptorVortex,
	 *      Set) La implementación actual hace una diferencia de los nuevos tags y los viejos e
	 *      invoca a agregar y quitar tags con las diferencias de cada uno
	 */
	@Override
	public void reemplazarTagsPara(final ReceptorVortex receptorAModificar, final Set<String> nuevosTags) {
		doModification(new Runnable() {
			@Override
			public void run() {
				final Set<String> tagsPrevios = getTagsRegistradosDe(receptorAModificar);

				@HasDependencyOn(Decision.LA_DIFERENCIA_DE_SETS_DE_TAGS_REQUIERE_LA_MISMA_IMPL_DE_SETS)
				final HashSet<String> setDeTagsNuevos = new HashSet<String>(nuevosTags);

				// Primero calculamos las diferencias que se producirían
				final Set<String> tagsQuitados = Sets.difference(tagsPrevios, setDeTagsNuevos).copyInto(
						new HashSet<String>());
				final Set<String> tagsAgregados = Sets.difference(setDeTagsNuevos, tagsPrevios).copyInto(
						new HashSet<String>());

				// Ejecutamos los dos cambios
				quitarTagsPara(receptorAModificar, tagsQuitados);
				agregarTagsPara(receptorAModificar, tagsAgregados);
			}
		});
	}

	/**
	 * @see net.gaia.vortex.lowlevel.impl.tags.TagSummarizer#limpiarTagsPara(net.gaia.vortex.lowlevel.impl.ReceptorVortex)
	 *      Esta implementación toma los tags actuales y los quita del receptor
	 */
	@Override
	public void limpiarTagsPara(final ReceptorVortex receptorAModificar) {
		doModification(new Runnable() {
			@Override
			public void run() {
				final Set<String> tagsDelReceptor = new HashSet<String>(getTagsRegistradosDe(receptorAModificar));
				quitarTagsPara(receptorAModificar, tagsDelReceptor);
			}
		});
	}

	/**
	 * Devuelve los receptores registrados del tag indicado, o una lista vacía si no se registró
	 * ninguno previamente
	 */
	private Set<ReceptorVortex> getReceptoresRegistradosDe(final String tagAgregado) {
		return receptoresPorTag.getOrCreateIfNullFor(tagAgregado);
	}

	/**
	 * @see net.gaia.vortex.lowlevel.impl.tags.TagSummarizer#getGlobalTags()
	 */
	@Override
	public Set<String> getGlobalTags() {
		final Set<String> allTags = receptoresPorTag.keySet();
		return new HashSet<String>(allTags);
	}
}
