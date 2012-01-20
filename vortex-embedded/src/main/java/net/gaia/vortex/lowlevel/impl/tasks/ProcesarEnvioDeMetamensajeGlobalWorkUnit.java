/**
 * 17/01/2012 01:16:53 Copyright (C) 2011 Darío L. García
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
import net.gaia.vortex.lowlevel.impl.ReceptorVortex;
import net.gaia.vortex.lowlevel.impl.RegistroDeReceptores;
import net.gaia.vortex.protocol.messages.meta.MetamensajeVortex;

/**
 * Esta clase representa la operación realizada por el nodo para enviar un metamensaje a todos sus
 * receptores
 * 
 * @author D. García
 */
public class ProcesarEnvioDeMetamensajeGlobalWorkUnit implements WorkUnit {

	private NodoVortexConTasks nodo;
	private MetamensajeVortex metamensaje;

	/**
	 * @see net.gaia.taskprocessor.api.WorkUnit#doWork()
	 */
	@Override
	public void doWork() throws InterruptedException {
		// Buscamos todos los receptores
		final RegistroDeReceptores registroReceptores = nodo.getRegistroReceptores();
		final Set<ReceptorVortex> allReceptores = registroReceptores.getAllReceptores();

		// Les enviamos el metamensaje a cada uno
		for (final ReceptorVortex destino : allReceptores) {
			final ProcesarEnvioDeMetamensajeWorkUnit notificarMetamensaje = ProcesarEnvioDeMetamensajeWorkUnit.create(
					nodo, destino, metamensaje);
			nodo.getProcesador().process(notificarMetamensaje);
		}

	}

	public static ProcesarEnvioDeMetamensajeGlobalWorkUnit create(final NodoVortexConTasks nodo,
			final MetamensajeVortex metamensaje) {
		final ProcesarEnvioDeMetamensajeGlobalWorkUnit enviar = new ProcesarEnvioDeMetamensajeGlobalWorkUnit();
		enviar.nodo = nodo;
		enviar.metamensaje = metamensaje;
		return enviar;
	}
}
