/**
 * 18/01/2012 22:09:45 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.lowlevel.impl.tasks;

import java.util.HashSet;
import java.util.Set;

import net.gaia.taskprocessor.api.WorkUnit;
import net.gaia.vortex.lowlevel.impl.NodoVortexConTasks;
import net.gaia.vortex.lowlevel.impl.receptores.ReceptorVortex;
import net.gaia.vortex.lowlevel.impl.tags.NotificacionDeCambioDeTags;

/**
 * Esta clase representa la operación realizada por el nodo para notificar a un cliente que
 * cambiaron los tags del nodo
 * 
 * @author D. García
 */
public class NotificarAReceptorCambiosDeTagsWorkUnit implements WorkUnit {

	private NotificacionDeCambioDeTags notificacion;
	private NodoVortexConTasks nodo;

	public static NotificarAReceptorCambiosDeTagsWorkUnit create(final NodoVortexConTasks nodo,
			final NotificacionDeCambioDeTags notificacion) {
		final NotificarAReceptorCambiosDeTagsWorkUnit notificar = new NotificarAReceptorCambiosDeTagsWorkUnit();
		notificar.notificacion = notificacion;
		notificar.nodo = nodo;
		return notificar;
	}

	/**
	 * @see net.gaia.taskprocessor.api.WorkUnit#doWork()
	 */
	@Override
	public void doWork() throws InterruptedException {
		// Comparamos con los tags que ya notificamos para evitar notificar varias veces
		final ReceptorVortex receptorANotificar = notificacion.getReceptor();
		final Set<String> tagsNotificadosPreviamente = receptorANotificar.getTagsNotificados();

		// Comprobamos los tags agregados
		if (notificacion.tieneTagsAgregados()) {
			final Set<String> tagsAgregadosYNoNotificados = new HashSet<String>();
			final Set<String> tagsAgregados = notificacion.getTagsAgregados();
			for (final String tagAgregado : tagsAgregados) {
				if (!tagsNotificadosPreviamente.contains(tagAgregado)) {
					tagsAgregadosYNoNotificados.add(tagAgregado);
				}
			}
			if (!tagsAgregadosYNoNotificados.isEmpty()) {
				final NotificarAReceptorTagsAgregadosWorkUnit notificarAgregados = NotificarAReceptorTagsAgregadosWorkUnit
						.create(nodo, receptorANotificar, tagsAgregadosYNoNotificados);
				nodo.getProcesador().process(notificarAgregados);
			}
		}

		// Comprobamos los tags quitados
		if (notificacion.tieneTagsQuitados()) {
			final Set<String> tagsQuitadosYNoNotificados = new HashSet<String>();
			final Set<String> tagsQuitados = notificacion.getTagsQuitados();
			for (final String tagQuitado : tagsQuitados) {
				if (tagsNotificadosPreviamente.contains(tagQuitado)) {
					tagsQuitadosYNoNotificados.add(tagQuitado);
				}
			}
			if (!tagsQuitadosYNoNotificados.isEmpty()) {
				final NotificarAReceptorTagsQuitadosWorkUnit notificarAgregados = NotificarAReceptorTagsQuitadosWorkUnit
						.create(nodo, receptorANotificar, tagsQuitadosYNoNotificados);
				nodo.getProcesador().process(notificarAgregados);
			}
		}
	}
}
