/**
 * 24/01/2012 03:25:12 Copyright (C) 2011 Darío L. García
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

import net.gaia.taskprocessor.api.WorkUnit;
import net.gaia.vortex.lowlevel.impl.ContextoDeRuteoDeMensaje;
import net.gaia.vortex.lowlevel.impl.receptores.ReceptorVortex;

/**
 * Esta clase representa la operación que realiza el nodo al receibir un mensaje de un receptor para
 * procesarlos en orden
 * 
 * @author D. García
 */
public class AgregarMensajeAColaDeReceptorWorkUnit implements WorkUnit {

	private ContextoDeRuteoDeMensaje contexto;

	/**
	 * @see net.gaia.taskprocessor.api.WorkUnit#doWork()
	 */
	@Override
	public void doWork() throws InterruptedException {
		final ReceptorVortex emisor = contexto.getEmisor();
		contexto.getMensaje();
	}

	public static AgregarMensajeAColaDeReceptorWorkUnit create(final ContextoDeRuteoDeMensaje contexto) {
		final AgregarMensajeAColaDeReceptorWorkUnit agregado = new AgregarMensajeAColaDeReceptorWorkUnit();
		agregado.contexto = contexto;
		return agregado;
	}
}
