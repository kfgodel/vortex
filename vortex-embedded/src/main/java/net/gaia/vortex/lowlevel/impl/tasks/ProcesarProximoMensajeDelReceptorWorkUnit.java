/**
 * 24/01/2012 07:47:53 Copyright (C) 2011 Darío L. García
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

import net.gaia.vortex.lowlevel.impl.nodo.NodoVortexConTasks;
import net.gaia.vortex.lowlevel.impl.receptores.ColaDeMensajesVortex;
import net.gaia.vortex.lowlevel.impl.receptores.ReceptorVortex;
import net.gaia.vortex.protocol.messages.MensajeVortex;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Esta clase representa la operación realizada por el nodo para procesar el proximo mensaje en cola
 * de un receptor
 * 
 * @author D. García
 */
public class ProcesarProximoMensajeDelReceptorWorkUnit implements TareaParaReceptor {
	private static final Logger LOG = LoggerFactory.getLogger(ProcesarProximoMensajeDelReceptorWorkUnit.class);

	private NodoVortexConTasks nodo;
	private ReceptorVortex receptor;

	/**
	 * @see net.gaia.vortex.lowlevel.impl.tasks.TareaParaReceptor#esPara(net.gaia.vortex.lowlevel.impl.receptores.ReceptorVortex)
	 */
	@Override
	public boolean esPara(final ReceptorVortex receptor) {
		return this.receptor == receptor;
	}

	/**
	 * @see net.gaia.taskprocessor.api.WorkUnit#doWork()
	 */
	@Override
	public void doWork() throws InterruptedException {
		LOG.debug("Tomando proximo mensaje disponible del receptor[{}]", receptor);
		// Verificamos si podemos seguir procesando o ya hay otro mensaje en proceso
		final ColaDeMensajesVortex colaDeMensajes = receptor.getColaDeMensajes();
		final MensajeVortex proximoMensaje = colaDeMensajes.tomarProximoMensaje();
		if (proximoMensaje == null) {
			LOG.debug("No quedan mensajes recibidos pendientes para el receptor[{}]. Descansando", receptor);
			return;
		}

		// Comenzamos con el procesamiento
		final ComenzarProcesoDeMensajeWorkUnit comienzoDeProceso = ComenzarProcesoDeMensajeWorkUnit.create(
				proximoMensaje, receptor, nodo);
		nodo.getProcesador().process(comienzoDeProceso);
	}

	public static ProcesarProximoMensajeDelReceptorWorkUnit create(final ReceptorVortex receptorEmisor,
			final NodoVortexConTasks nodo) {
		final ProcesarProximoMensajeDelReceptorWorkUnit name = new ProcesarProximoMensajeDelReceptorWorkUnit();
		name.nodo = nodo;
		name.receptor = receptorEmisor;
		return name;
	}

}
