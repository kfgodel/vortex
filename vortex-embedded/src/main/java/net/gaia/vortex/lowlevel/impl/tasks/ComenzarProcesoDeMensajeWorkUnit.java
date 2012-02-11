/**
 * 24/01/2012 08:01:18 Copyright (C) 2011 Darío L. García
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
import net.gaia.vortex.lowlevel.impl.receptores.ReceptorVortex;
import net.gaia.vortex.lowlevel.impl.ruteo.ContextoDeRuteoDeMensaje;
import net.gaia.vortex.protocol.messages.MensajeVortex;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Esta clase representa la operación realizada por el nodo vortex para procesar un nuevo mensaje.<br>
 * Existe más que nada como punto común de inicio del proceso
 * 
 * @author D. García
 */
public class ComenzarProcesoDeMensajeWorkUnit implements TareaParaReceptor {
	private static final Logger LOG = LoggerFactory.getLogger(ComenzarProcesoDeMensajeWorkUnit.class);

	private MensajeVortex mensaje;
	private ReceptorVortex receptor;
	private NodoVortexConTasks nodo;

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
		LOG.debug("Comenzando a procesar mensaje[{}] recibido de receptor[{}]", mensaje, receptor);

		// Creamos el contexto para el ruteo del mensaje del emisor
		final ContextoDeRuteoDeMensaje nuevoRuteo = ContextoDeRuteoDeMensaje.create(mensaje, receptor, this.nodo);

		// Comenzamos con la validación del mensaje
		final ValidacionDeMensajeWorkUnit validacion = ValidacionDeMensajeWorkUnit.create(nuevoRuteo);
		nodo.getProcesador().process(validacion);
	}

	public static ComenzarProcesoDeMensajeWorkUnit create(final MensajeVortex mensajeRecibido,
			final ReceptorVortex receptorEmisor, final NodoVortexConTasks nodo) {
		final ComenzarProcesoDeMensajeWorkUnit name = new ComenzarProcesoDeMensajeWorkUnit();
		name.mensaje = mensajeRecibido;
		name.receptor = receptorEmisor;
		name.nodo = nodo;
		return name;
	}
}
