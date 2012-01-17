/**
 * 17/01/2012 01:01:24 Copyright (C) 2011 Darío L. García
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
import net.gaia.vortex.protocol.messages.meta.AgregarTags;
import net.gaia.vortex.protocol.messages.meta.MetamensajeVortex;

/**
 * Esta clase representa la operación realizada por el nodo al detectar que le interesan más tags
 * 
 * @author D. García
 */
public class NotificarTagsAgregadosWorkUnit implements WorkUnit {

	private NodoVortexConTasks nodo;
	private Set<String> nuevosTags;

	/**
	 * @see net.gaia.taskprocessor.api.WorkUnit#doWork()
	 */
	@Override
	public void doWork() throws InterruptedException {
		// Armamos el metamensaje
		final MetamensajeVortex agregadoDeTags = AgregarTags.create(nuevosTags);

		// Se lo enviamos a todos
		final ProcesarEnvioDeMetamensajeGlobalWorkUnit envioGlobal = ProcesarEnvioDeMetamensajeGlobalWorkUnit.create(
				nodo, agregadoDeTags);
		nodo.getProcesador().process(envioGlobal);
	}

	public static NotificarTagsAgregadosWorkUnit create(final NodoVortexConTasks nodo, final Set<String> nuevosTags) {
		final NotificarTagsAgregadosWorkUnit notificar = new NotificarTagsAgregadosWorkUnit();
		notificar.nodo = nodo;
		notificar.nuevosTags = nuevosTags;
		return notificar;
	}
}
