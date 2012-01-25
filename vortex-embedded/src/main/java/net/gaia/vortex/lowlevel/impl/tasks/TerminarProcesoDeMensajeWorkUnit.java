/**
 * 24/01/2012 08:21:33 Copyright (C) 2011 Darío L. García
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
import net.gaia.vortex.lowlevel.impl.NodoVortexConTasks;
import net.gaia.vortex.lowlevel.impl.receptores.ColaDeMensajesDelReceptor;
import net.gaia.vortex.lowlevel.impl.receptores.ReceptorVortex;
import net.gaia.vortex.protocol.messages.MensajeVortex;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Esta clase representa la operación realizada por el nodo para terminar el proceso de tratamiento
 * de un mensaje recibido
 * 
 * @author D. García
 */
public class TerminarProcesoDeMensajeWorkUnit implements WorkUnit {
	private static final Logger LOG = LoggerFactory.getLogger(TerminarProcesoDeMensajeWorkUnit.class);

	private ReceptorVortex receptor;
	private NodoVortexConTasks nodo;

	/**
	 * @see net.gaia.taskprocessor.api.WorkUnit#doWork()
	 */
	@Override
	public void doWork() throws InterruptedException {
		// Limpiamos el mensaje actual
		final ColaDeMensajesDelReceptor colaDeMensajes = receptor.getColaDeMensajes();
		final MensajeVortex mensajeProcesado = colaDeMensajes.terminarMensajeActual();
		LOG.debug("Mensaje[{}] procesado completamente", mensajeProcesado);

		// Seguimos con el próximo si hay
		final ProcesarProximoMensajeDelReceptorWorkUnit procesarSiguiente = ProcesarProximoMensajeDelReceptorWorkUnit
				.create(receptor, nodo);
		nodo.getProcesador().process(procesarSiguiente);
	}

	public static TerminarProcesoDeMensajeWorkUnit create(final ReceptorVortex receptor, final NodoVortexConTasks nodo) {
		final TerminarProcesoDeMensajeWorkUnit terminar = new TerminarProcesoDeMensajeWorkUnit();
		terminar.nodo = nodo;
		terminar.receptor = receptor;
		return terminar;
	}
}
