/**
 * 18/01/2012 22:47:26 Copyright (C) 2011 Darío L. García
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

import java.util.Set;

import net.gaia.taskprocessor.api.WorkUnit;
import net.gaia.vortex.lowlevel.impl.NodoVortexConTasks;
import net.gaia.vortex.lowlevel.impl.receptores.ReceptorVortex;
import net.gaia.vortex.protocol.messages.meta.QuitarTags;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Esta clase representa la operación realizada por el nodo para notificar a un receptor de tags
 * quitados
 * 
 * @author D. García
 */
public class NotificarAReceptorTagsQuitadosWorkUnit implements WorkUnit {
	private static final Logger LOG = LoggerFactory.getLogger(NotificarAReceptorTagsQuitadosWorkUnit.class);

	private NodoVortexConTasks nodo;
	private ReceptorVortex receptor;
	private Set<String> tagsQuitados;

	/**
	 * @see net.gaia.taskprocessor.api.WorkUnit#doWork()
	 */
	@Override
	public void doWork() throws InterruptedException {
		LOG.debug("Notificando al receptor[{}] tags quitados[{}]", receptor, tagsQuitados);
		// Mandamos el mensaje por los nuevos tags
		final QuitarTags quitarTags = QuitarTags.create(tagsQuitados);
		final ProcesarEnvioDeMetamensajeWorkUnit envioDeMensaje = ProcesarEnvioDeMetamensajeWorkUnit.create(nodo,
				receptor, quitarTags, null);
		nodo.getProcesador().process(envioDeMensaje);

		// Registramos que le avisamos
		receptor.quitarTagsNotificados(tagsQuitados);
	}

	public static NotificarAReceptorTagsQuitadosWorkUnit create(final NodoVortexConTasks nodo,
			final ReceptorVortex receptorANotificar, final Set<String> tagsQuitadosYNoNotificados) {
		final NotificarAReceptorTagsQuitadosWorkUnit notificar = new NotificarAReceptorTagsQuitadosWorkUnit();
		notificar.nodo = nodo;
		notificar.receptor = receptorANotificar;
		notificar.tagsQuitados = tagsQuitadosYNoNotificados;
		return notificar;
	}
}
