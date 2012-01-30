/**
 * 18/01/2012 22:39:43 Copyright (C) 2011 Darío L. García
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
import net.gaia.vortex.lowlevel.impl.nodo.NodoVortexConTasks;
import net.gaia.vortex.lowlevel.impl.receptores.ReceptorVortex;
import net.gaia.vortex.meta.Loggers;
import net.gaia.vortex.protocol.messages.meta.AgregarTags;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Esta clase representa la operación que realiza el nodo para notificar de tags agregados a un
 * cliente
 * 
 * @author D. García
 */
public class NotificarAReceptorTagsAgregadosWorkUnit implements WorkUnit {
	private static final Logger LOG = LoggerFactory.getLogger(NotificarAReceptorTagsAgregadosWorkUnit.class);

	private NodoVortexConTasks nodo;
	private ReceptorVortex receptor;
	private Set<String> tagsAgregados;

	/**
	 * @see net.gaia.taskprocessor.api.WorkUnit#doWork()
	 */
	@Override
	public void doWork() throws InterruptedException {
		LOG.debug("Notificando a receptor[{}] por tags agregados[{}]", receptor, tagsAgregados);
		Loggers.RUTEO.info("ENVIO TAGS AGREGADOS en nodo[{}] al receptor [{}]: {}", new Object[] { nodo, receptor,
				tagsAgregados });
		// Mandamos el mensaje por los nuevos tags
		final AgregarTags agregarTags = AgregarTags.create(tagsAgregados);
		final ProcesarEnvioDeMetamensajeWorkUnit envioDeMensaje = ProcesarEnvioDeMetamensajeWorkUnit.create(nodo,
				receptor, agregarTags, null);
		nodo.getProcesador().process(envioDeMensaje);

		// Registramos que le avisamos
		receptor.agregarTagsNotificados(tagsAgregados);
	}

	public static NotificarAReceptorTagsAgregadosWorkUnit create(final NodoVortexConTasks nodo,
			final ReceptorVortex receptorANotificar, final Set<String> tagsAgregadosYNoNotificados) {
		final NotificarAReceptorTagsAgregadosWorkUnit notificar = new NotificarAReceptorTagsAgregadosWorkUnit();
		notificar.nodo = nodo;
		notificar.receptor = receptorANotificar;
		notificar.tagsAgregados = tagsAgregadosYNoNotificados;
		return notificar;
	}
}
