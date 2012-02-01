/**
 * 01/02/2012 19:10:13 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.http;

import net.gaia.taskprocessor.api.TaskProcessor;
import net.gaia.taskprocessor.api.TaskProcessorConfiguration;
import net.gaia.taskprocessor.impl.ExecutorBasedTaskProcesor;
import net.gaia.vortex.lowlevel.api.MensajeVortexHandler;
import net.gaia.vortex.lowlevel.api.NodoVortex;
import net.gaia.vortex.lowlevel.api.SesionVortex;
import net.gaia.vortex.protocol.messages.MensajeVortex;

/**
 * Esta clase representa un nodo vortex remoto con
 * 
 * @author D. García
 */
public class NodoRemotoHttp implements NodoVortex {

	private TaskProcessor processor;

	/**
	 * @see net.gaia.vortex.lowlevel.api.NodoVortex#crearNuevaSesion(net.gaia.vortex.lowlevel.api.MensajeVortexHandler)
	 */
	@Override
	public SesionVortex crearNuevaSesion(final MensajeVortexHandler arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @see net.gaia.vortex.lowlevel.api.NodoVortex#detenerYDevolverRecursos()
	 */
	@Override
	public void detenerYDevolverRecursos() {
		// TODO Auto-generated method stub

	}

	/**
	 * @see net.gaia.vortex.lowlevel.api.NodoVortex#rutear(net.gaia.vortex.protocol.messages.MensajeVortex)
	 */
	@Override
	public void rutear(final MensajeVortex mensaje) {
		// TODO Auto-generated method stub

	}

	public static NodoRemotoHttp create(final String url) {
		final NodoRemotoHttp nodo = new NodoRemotoHttp();
		nodo.processor = ExecutorBasedTaskProcesor.create(TaskProcessorConfiguration.create());
		return nodo;
	}
}
