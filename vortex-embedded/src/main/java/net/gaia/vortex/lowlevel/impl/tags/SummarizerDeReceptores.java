/**
 * 18/01/2012 21:21:05 Copyright (C) 2011 Darío L. García
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
import java.util.concurrent.locks.ReentrantLock;

import net.gaia.annotations.HasDependencyOn;
import net.gaia.vortex.lowlevel.impl.ReportePerformanceRuteo;
import net.gaia.vortex.lowlevel.impl.SeleccionDeReceptores;
import net.gaia.vortex.lowlevel.impl.receptores.ReceptorVortex;
import net.gaia.vortex.lowlevel.impl.receptores.RegistroDeReceptores;
import net.gaia.vortex.meta.Decision;
import ar.com.dgarcia.coding.caching.Instantiator;
import ar.com.dgarcia.coding.caching.SimpleCacheConcurrentMap;

import com.google.common.collect.Sets;

/**
 * Esta clase representa el sumarizador de tags utilizado por el nodo para notificar a los clientes
 * de los tags que le interesan como reflejo de los tags que le interesan a los otros clientes
 * 
 * @author D. García
 */
public class SummarizerDeReceptores implements RegistroDeReceptores, TagSummarizer {

	private ReentrantLock modificationLock;
	private SimpleCacheConcurrentMap<String, Set<ReceptorVortex>> receptoresPorTag;
	private SimpleCacheConcurrentMap<ReceptorVortex, Set<String>> tagsPorReceptor;
	private TagChangeListener listener;

	public static SummarizerDeReceptores create(final TagChangeListener listener) {
		final SummarizerDeReceptores summarizer = new SummarizerDeReceptores();
		summarizer.modificationLock = new ReentrantLock();
		summarizer.tagsPorReceptor = new SimpleCacheConcurrentMap<ReceptorVortex, Set<String>>(
				new Instantiator<ReceptorVortex, Set<String>>() {
					@Override
					public Set<String> instantiate(final ReceptorVortex input) {
						return new HashSet<String>();
					}
				});
		summarizer.receptoresPorTag = new SimpleCacheConcurrentMap<String, Set<ReceptorVortex>>(
				new Instantiator<String, Set<ReceptorVortex>>() {
					@Override
					public Set<ReceptorVortex> instantiate(final String input) {
						return new HashSet<ReceptorVortex>();
					}
				});
		summarizer.listener = listener;
		return summarizer;
	}

	/**
	 * @see net.gaia.vortex.lowlevel.impl.tags.TagSummarizer#agregarTagsPara(net.gaia.vortex.lowlevel.impl.receptores.ReceptorVortex,
	 *      java.util.Set)
	 */
	@Override
	public void agregarTagsPara(final ReceptorVortex receptorAModificar, final Set<String> tagsDeclarados) {
		makeTagOperationAndReport(new AtomicTagOperation() {
			@Override
			public void execute(final ReporteCambioDeTags reporte) {
				// Limpiamos la lista de tags por si el cliente mandó tags ya conocidos
				final Set<String> tagsRealmenteAgregados = conservarTagsNoRegistradosDe(receptorAModificar,
						tagsDeclarados);
				// Agregamos los tags nuevos
				internalAgregarTags(tagsRealmenteAgregados, receptorAModificar, reporte);
			}
		});
	}

	/**
	 * Por cada uno de los tags lo agrega a este registro. Esta operación debe ser atómica
	 * 
	 * @param tagsRealmenteAgregados
	 *            Tags a agregar
	 * @param receptorAModificar
	 *            El receptor del que se agregan
	 * @param reporte
	 *            El reporte para cargar las notificaciones
	 */
	protected void internalAgregarTags(final Set<String> tagsRealmenteAgregados,
			final ReceptorVortex receptorAModificar, final ReporteCambioDeTags reporte) {
		// Impactamos los cambios tag por tag registrando en el reporte
		for (final String tagAgregado : tagsRealmenteAgregados) {
			agregarTagPara(receptorAModificar, tagAgregado, reporte);
		}
	}

	/**
	 * @see net.gaia.vortex.lowlevel.impl.tags.TagSummarizer#reemplazarTagsPara(net.gaia.vortex.lowlevel.impl.receptores.ReceptorVortex,
	 *      java.util.Set)
	 */
	@Override
	public void reemplazarTagsPara(final ReceptorVortex receptorAModificar, final Set<String> nuevosTags) {
		makeTagOperationAndReport(new AtomicTagOperation() {
			@Override
			public void execute(final ReporteCambioDeTags reporte) {
				final Set<String> tagsPrevios = getTagsRegistradosDe(receptorAModificar);

				@HasDependencyOn(Decision.LA_DIFERENCIA_DE_SETS_DE_TAGS_REQUIERE_LA_MISMA_IMPL_DE_SETS)
				final HashSet<String> tagsDeclarados = new HashSet<String>(nuevosTags);

				// Antes de modificar, calculamos los tags que ya no están más
				final Set<String> tagsQuitados = Sets.difference(tagsPrevios, tagsDeclarados).copyInto(
						new HashSet<String>());
				// Y calculamos los que no estaban y ahora sí
				final Set<String> tagsAgregados = Sets.difference(tagsDeclarados, tagsPrevios).copyInto(
						new HashSet<String>());

				// Ahora sí impactamos las modificaciones
				if (!tagsQuitados.isEmpty()) {
					internalQuitarTags(tagsQuitados, receptorAModificar, reporte);
				}
				if (!tagsAgregados.isEmpty()) {
					internalAgregarTags(tagsAgregados, receptorAModificar, reporte);
				}
			}
		});
	}

	/**
	 * Ejecuta una operación sobre los tags registrados en forma atómica, generando un reporte de de
	 * notificaciones como consecuencia de los cambios que es pasado al listener
	 * 
	 * @param operation
	 *            Operación a realizar
	 */
	private void makeTagOperationAndReport(final AtomicTagOperation operation) {
		// Creamos el reporte para recopilar las notificaciones
		final ReporteCambioDeTags reporte = ReporteCambioDeTags.create();

		// Ejecutamos la operación atómicamente
		doAtomicOperation(new Runnable() {
			@Override
			public void run() {
				operation.execute(reporte);
			}
		});

		// Si hay notificaciones las compartimos
		if (reporte.tieneNotificaciones()) {
			listener.onTagChanges(reporte);
		}
	}

	/**
	 * @see net.gaia.vortex.lowlevel.impl.tags.TagSummarizer#quitarTagsPara(net.gaia.vortex.lowlevel.impl.receptores.ReceptorVortex,
	 *      java.util.Set)
	 */
	@Override
	public void quitarTagsPara(final ReceptorVortex receptorAModificar, final Set<String> tagsDeclarados) {
		makeTagOperationAndReport(new AtomicTagOperation() {
			@Override
			public void execute(final ReporteCambioDeTags reporte) {
				// Limpiamos la lista de tags por si el cliente mandó tags ya conocidos
				final Set<String> tagsRealmenteQuitados = conservarTagsRegistradosDe(receptorAModificar, tagsDeclarados);

				// Quitamos los tags
				internalQuitarTags(tagsRealmenteQuitados, receptorAModificar, reporte);
			}
		});
	}

	/**
	 * Quita los tags indicados del receptor. Esta operación debe ser atómica
	 * 
	 * @param tagsRealmenteQuitados
	 *            Los tags a eliminar
	 * @param receptorAModificar
	 *            El receptor del que se quitarán
	 * @param reporte
	 *            El reporte donde cargar las notificaciones
	 */
	protected void internalQuitarTags(final Set<String> tagsRealmenteQuitados, final ReceptorVortex receptorAModificar,
			final ReporteCambioDeTags reporte) {
		// Impactamos los cambios tag por tag registrando en el reporte
		for (final String tagQuitado : tagsRealmenteQuitados) {
			quitarTagPara(receptorAModificar, tagQuitado, reporte);
		}
	}

	/**
	 * Agrega el tag indicado al receptor pasado, indicando en el reporte pasado las notificaciones
	 * que surjan de la operación
	 * 
	 * @param receptorAModificar
	 *            El receptor al que se le agregarán los tags
	 * @param tagAgregado
	 *            El tag a agregar
	 * @param reporte
	 *            El reporte en el que se registrarán las notificaciones
	 */
	protected void agregarTagPara(final ReceptorVortex receptorAModificar, final String tagAgregado,
			final ReporteCambioDeTags reporte) {
		// En base al cambio de tamaño del conjunto de receptores se deducen las notificaciones
		final Set<ReceptorVortex> receptoresDelTag = getReceptoresDelTag(tagAgregado);
		final int tamanioPrevio = receptoresDelTag.size();

		// Agregamos el receptor como interesado en el tag
		receptoresDelTag.add(receptorAModificar);

		// Y el tag como interés del receptor
		final Set<String> tagsDelReceptor = getTagsRegistradosDe(receptorAModificar);
		tagsDelReceptor.add(tagAgregado);

		// Verificamos que tipo de notificación corresponde (deducida del cambio de cantidades)
		final int tamanioPosterior = receptoresDelTag.size();
		if (tamanioPrevio == 0 && tamanioPosterior == 1) {
			// Es el primero interesado, el resto debe saberlo para enviar mensajes al nodo
			final Set<ReceptorVortex> allReceptores = getAllReceptores();
			notificarAgregadoATodosMenosA(receptorAModificar, allReceptores, tagAgregado, reporte);
		}
		if (tamanioPrevio == 1 && tamanioPosterior == 2) {
			// Es el segundo, el único no notificado es el que estaba antes
			notificarAgregadoATodosMenosA(receptorAModificar, receptoresDelTag, tagAgregado, reporte);
		}
		// No hace falta avisar a nadie, es un tag ya notificado a otros
	}

	/**
	 * Agrega el tag indicado al receptor pasado, indicando en el reporte pasado las notificaciones
	 * que surjan de la operación
	 * 
	 * @param receptorAModificar
	 *            El receptor al que se le agregarán los tags
	 * @param tagQuitado
	 *            El tag a agregar
	 * @param reporte
	 *            El reporte en el que se registrarán las notificaciones
	 */
	@HasDependencyOn(Decision.AL_QUITAR_TAG_SE_ELIMINA_SU_ENTRADA_DEL_MAPA)
	protected void quitarTagPara(final ReceptorVortex receptorAModificar, final String tagQuitado,
			final ReporteCambioDeTags reporte) {
		// En base al cambio de tamaño del conjunto de receptores se deducen las notificaciones
		final Set<ReceptorVortex> receptoresDelTag = getReceptoresDelTag(tagQuitado);
		final int tamanioPrevio = receptoresDelTag.size();

		// Quitamos el receptor como interesado en el tag
		receptoresDelTag.remove(receptorAModificar);

		// Y el tag como interés del receptor
		final Set<String> tagsDelReceptor = getTagsRegistradosDe(receptorAModificar);
		tagsDelReceptor.remove(tagQuitado);

		final int tamanioPosterior = receptoresDelTag.size();
		if (tamanioPosterior == 0) {
			// Quitamos el tag del registro si no le interesa a nadie
			this.receptoresPorTag.remove(tagQuitado);
		}

		// Verificamos que tipo de notificación corresponde (deducida del cambio de cantidades)
		if (tamanioPrevio == 1 && tamanioPosterior == 0) {
			// Ya no hay nadie interesado, notificamos al resto para que no mande más mensajes
			final Set<ReceptorVortex> allReceptores = getAllReceptores();
			notificarQuitadoATodosMenosA(receptorAModificar, allReceptores, tagQuitado, reporte);
		}
		if (tamanioPrevio == 2 && tamanioPosterior == 1) {
			// Quedó uno sólo, tenemos que avisarle para que no mande mensajes si no hay otro
			notificarQuitadoATodosMenosA(receptorAModificar, receptoresDelTag, tagQuitado, reporte);
		}
		// No hace falta avisar a nadie, es un tag ya notificado a otros
	}

	/**
	 * Genera una notificación por tag agregado para cada uno de los receptores en el conjunto,
	 * excluyendo al indicado
	 * 
	 * @param receptorExcluido
	 *            El receptor al que no se debe notificar
	 * @param receptoresANotificar
	 *            Los receptores que deben ser notificados
	 * @param tagAgregado
	 *            Tag a agregar
	 * @param reporte
	 *            El reporte en el cual registrar la notificación
	 */
	private void notificarAgregadoATodosMenosA(final ReceptorVortex receptorExcluido,
			final Set<ReceptorVortex> receptoresANotificar, final String tagAgregado, final ReporteCambioDeTags reporte) {
		// Generamos la notificación para cada uno
		for (final ReceptorVortex receptorANotificar : receptoresANotificar) {
			// Omitimos el excluído
			if (receptorANotificar.equals(receptorExcluido)) {
				continue;
			}
			// Generamos la notificación por el tag agregado
			reporte.notificarTagIncorporadoAlNodoA(receptorANotificar, tagAgregado);
		}
	}

	/**
	 * Genera una notificación por tag quitado para cada uno de los receptores en el conjunto,
	 * excluyendo al indicado
	 * 
	 * @param receptorExcluido
	 *            El receptor al que no se debe notificar
	 * @param receptoresANotificar
	 *            Los receptores que deben ser notificados
	 * @param tagQuitado
	 *            Tag a agregar
	 * @param reporte
	 *            El reporte en el cual registrar la notificación
	 */
	private void notificarQuitadoATodosMenosA(final ReceptorVortex receptorExcluido,
			final Set<ReceptorVortex> receptoresANotificar, final String tagQuitado, final ReporteCambioDeTags reporte) {
		// Generamos la notificación para cada uno
		for (final ReceptorVortex receptorANotificar : receptoresANotificar) {
			// Omitimos el excluído
			if (receptoresANotificar.equals(receptorExcluido)) {
				continue;
			}
			// Generamos la notificación por el tag quitado
			reporte.notificarTagEliminadoDelNodoA(receptorANotificar, tagQuitado);
		}
	}

	/**
	 * Devuelve los receptores interesados en el tag pasado
	 * 
	 * @param tag
	 *            El tag del que se obtendrán los receptores
	 * @return El conjunto de receptores interesados
	 */
	private Set<ReceptorVortex> getReceptoresDelTag(final String tag) {
		return this.receptoresPorTag.getOrCreateIfNullFor(tag);
	}

	/**
	 * Elimina los tags conocidos del conjunto pasado. Sólo quedarán los tags realmente nuevos
	 * 
	 * @param receptorAModificar
	 *            Receptor que reporta los tags
	 * 
	 * @param nuevosTags
	 *            Los tags a revisar
	 * @return Un nuevo conjunto con los tags realmente nuevos para el receptor indicado
	 */
	protected Set<String> conservarTagsNoRegistradosDe(final ReceptorVortex receptorAModificar,
			final Set<String> nuevosTags) {
		final Set<String> tagsRegistrados = getTagsRegistradosDe(receptorAModificar);

		// Optimización para la primera vez que no tiene tags previamente registrados
		if (tagsRegistrados.isEmpty()) {
			// Todos son nuevos
			return new HashSet<String>(nuevosTags);
		}
		// Los que ya están registrados los excluimos
		final Set<String> tagsRealmenteNuevos = new HashSet<String>();
		for (final String nuevoTag : nuevosTags) {
			if (tagsRegistrados.contains(nuevoTag)) {
				continue;
			}
			tagsRealmenteNuevos.add(nuevoTag);
		}
		return tagsRealmenteNuevos;
	}

	/**
	 * Elimina los tags no registrados del conjunto pasado. Sólo quedarán los tags que realmente se
	 * quitarían al receptor
	 * 
	 * @param receptorAModificar
	 *            El receptor cuyos tags están registrados
	 * @param tagsDeclarados
	 *            Los tags que se eliminarán
	 * @return Un nuevo conjunto con los tags que estaban registrados
	 */
	protected Set<String> conservarTagsRegistradosDe(final ReceptorVortex receptorAModificar,
			final Set<String> tagsDeclarados) {
		final Set<String> tagsRegistrados = getTagsRegistradosDe(receptorAModificar);
		final HashSet<String> tagsRealmenteQuitados = new HashSet<String>();
		for (final String tagQuitado : tagsDeclarados) {
			if (tagsRegistrados.contains(tagQuitado)) {
				tagsRealmenteQuitados.add(tagQuitado);
			}
		}
		return tagsRealmenteQuitados;
	}

	/**
	 * @see net.gaia.vortex.lowlevel.impl.tags.TagSummarizer#limpiarTagsPara(net.gaia.vortex.lowlevel.impl.receptores.ReceptorVortex)
	 */
	@Override
	public void limpiarTagsPara(final ReceptorVortex receptorAModificar) {
		makeTagOperationAndReport(new AtomicTagOperation() {
			@Override
			public void execute(final ReporteCambioDeTags reporte) {
				final Set<String> tagsDelReceptor = new HashSet<String>(getTagsRegistradosDe(receptorAModificar));
				internalQuitarTags(tagsDelReceptor, receptorAModificar, reporte);
			}
		});
	}

	/**
	 * Modifica el estado de este summarizer asegurándose que nadie más modifique el estado
	 * 
	 * @param code
	 *            Código a ejecutar para modificar el estado
	 */
	private void doAtomicOperation(final Runnable code) {
		modificationLock.lock();
		try {
			code.run();
		} finally {
			modificationLock.unlock();
		}
	}

	/**
	 * @see net.gaia.vortex.lowlevel.impl.receptores.RegistroDeReceptores#agregar(net.gaia.vortex.lowlevel.impl.ReceptorVortexConSesion)
	 */
	@Override
	@HasDependencyOn(Decision.AL_QUITAR_TAG_SE_ELIMINA_SU_ENTRADA_DEL_MAPA)
	public void agregar(final ReceptorVortex nuevoReceptor) {
		makeTagOperationAndReport(new AtomicTagOperation() {
			@Override
			public void execute(final ReporteCambioDeTags reporte) {
				// Al nuevo le decimos que nos interesa todo lo que le interesa a los demás
				final Set<String> allTags = receptoresPorTag.keySet();
				for (final String tagRegistrado : allTags) {
					reporte.notificarTagIncorporadoAlNodoA(nuevoReceptor, tagRegistrado);
				}
			}
		});
	}

	/**
	 * @see net.gaia.vortex.lowlevel.impl.receptores.RegistroDeReceptores#getReceptoresInteresadosMenosA(net.gaia.vortex.lowlevel.impl.receptores.ReceptorVortex,
	 *      java.util.List)
	 */
	@Override
	@HasDependencyOn(Decision.TODAVIA_NO_IMPLEMENTE_EL_AJUSTE_DE_PESOS)
	public SeleccionDeReceptores getReceptoresInteresadosMenosA(final ReceptorVortex emisorExcluido,
			final List<String> tagsDelMensaje) {
		// Creamos la selección que excluirá al emisor
		final SeleccionDeReceptores interesadosEnElMensaje = SeleccionDeReceptores.create(emisorExcluido);
		doAtomicOperation(new Runnable() {
			@Override
			public void run() {
				// Por cada tag recolectamos los receptores interesados
				for (final String tagIncluidoEnMensaje : tagsDelMensaje) {
					final Set<ReceptorVortex> interesadosEnElTag = getReceptoresDelTag(tagIncluidoEnMensaje);
					interesadosEnElMensaje.incluirTodos(interesadosEnElTag);
				}
			}
		});
		return interesadosEnElMensaje;
	}

	/**
	 * @see net.gaia.vortex.lowlevel.impl.receptores.RegistroDeReceptores#ajustarPesosDeAcuerdoA(net.gaia.vortex.lowlevel.impl.ReportePerformanceRuteo)
	 */
	@Override
	@HasDependencyOn(Decision.TODAVIA_NO_IMPLEMENTE_EL_AJUSTE_DE_PESOS)
	public void ajustarPesosDeAcuerdoA(final ReportePerformanceRuteo reportePerformance) {
		// TODO Por ahora no hacemos nada
	}

	/**
	 * @see net.gaia.vortex.lowlevel.impl.receptores.RegistroDeReceptores#getAllReceptores()
	 */
	@Override
	public Set<ReceptorVortex> getAllReceptores() {
		final HashSet<ReceptorVortex> allReceptores = new HashSet<ReceptorVortex>(this.tagsPorReceptor.keySet());
		return allReceptores;
	}

	/**
	 * @see net.gaia.vortex.lowlevel.impl.receptores.RegistroDeReceptores#getTagSummarizer()
	 */
	@Override
	public TagSummarizer getTagSummarizer() {
		return this;
	}

	/**
	 * Devuelve los tags previamente conocidos del receptor, o una lista vacía si no se conocía
	 * ninguno
	 */
	protected Set<String> getTagsRegistradosDe(final ReceptorVortex receptorAModificar) {
		return tagsPorReceptor.getOrCreateIfNullFor(receptorAModificar);
	}

	/**
	 * Devuelve el conjunto de todos los tags registrados por receptores interesados en ellos
	 * 
	 * @return El conjunto de todos los tags
	 */
	public Set<String> getAllTags() {
		final HashSet<String> allTags = new HashSet<String>(this.receptoresPorTag.keySet());
		return allTags;
	}

	/**
	 * @see net.gaia.vortex.lowlevel.impl.receptores.RegistroDeReceptores#quitar(net.gaia.vortex.lowlevel.impl.receptores.ReceptorVortex)
	 */
	@Override
	public void quitar(final ReceptorVortex receptorQuitado) {
		doAtomicOperation(new Runnable() {
			@Override
			public void run() {
				limpiarTagsPara(receptorQuitado);
				tagsPorReceptor.remove(receptorQuitado);
			}
		});
	}

}
