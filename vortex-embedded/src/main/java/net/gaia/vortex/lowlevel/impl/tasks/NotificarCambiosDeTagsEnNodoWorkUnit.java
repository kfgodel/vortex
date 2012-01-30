/**
 * 18/01/2012 21:56:10 Copyright (C) 2011 Darío L. García
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

import java.util.List;

import net.gaia.taskprocessor.api.WorkUnit;
import net.gaia.vortex.lowlevel.impl.nodo.NodoVortexConTasks;
import net.gaia.vortex.lowlevel.impl.tags.NotificacionDeCambioDeTags;
import net.gaia.vortex.lowlevel.impl.tags.ReporteCambioDeTags;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Esta clase representa la operación realizada por el nodo para notificar a los clientes de cambios
 * en los tags del nodo
 * 
 * @author D. García
 */
public class NotificarCambiosDeTagsEnNodoWorkUnit implements WorkUnit {
	private static final Logger LOG = LoggerFactory.getLogger(NotificarCambiosDeTagsEnNodoWorkUnit.class);

	private ReporteCambioDeTags reporte;
	private NodoVortexConTasks nodo;

	/**
	 * @see net.gaia.taskprocessor.api.WorkUnit#doWork()
	 */
	@Override
	public void doWork() throws InterruptedException {
		LOG.debug("Cambios de tags en el nodo detectado. Notificando a afectados");
		final List<NotificacionDeCambioDeTags> notificaciones = reporte.getNotificaciones();
		for (final NotificacionDeCambioDeTags notificacionDeCambioDeTags : notificaciones) {
			final NotificarAReceptorCambiosDeTagsWorkUnit notificarAReceptor = NotificarAReceptorCambiosDeTagsWorkUnit
					.create(nodo, notificacionDeCambioDeTags);
			nodo.getProcesador().process(notificarAReceptor);
		}

	}

	public static NotificarCambiosDeTagsEnNodoWorkUnit create(final NodoVortexConTasks nodo,
			final ReporteCambioDeTags reporte) {
		final NotificarCambiosDeTagsEnNodoWorkUnit notificar = new NotificarCambiosDeTagsEnNodoWorkUnit();
		notificar.nodo = nodo;
		notificar.reporte = reporte;
		return notificar;
	}
}
