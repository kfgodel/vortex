/**
 * 14/01/2012 16:12:22 Copyright (C) 2011 Darío L. García
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
import net.gaia.vortex.lowlevel.impl.ids.GeneradorMensajesDeNodo;
import net.gaia.vortex.lowlevel.impl.nodo.NodoVortexConTasks;
import net.gaia.vortex.lowlevel.impl.receptores.ReceptorVortex;
import net.gaia.vortex.protocol.messages.MensajeVortex;
import net.gaia.vortex.protocol.messages.meta.MetamensajeVortex;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Esta clase entrega el metamensaje indicado al receptor encapsulándolo en un mensaje vortex
 * 
 * @author D. García
 */
public class ProcesarEnvioDeMetamensajeWorkUnit implements WorkUnit {
	private static final Logger LOG = LoggerFactory.getLogger(ProcesarEnvioDeMetamensajeWorkUnit.class);

	private MetamensajeVortex metamensaje;
	private ReceptorVortex receptor;
	private NodoVortexConTasks nodo;
	private Runnable whenDone;

	/**
	 * @see net.gaia.taskprocessor.api.WorkUnit#doWork()
	 */
	@Override
	public void doWork() throws InterruptedException {
		LOG.debug("Generando metamensaje[{}] para el receptor[{}]", metamensaje, receptor);

		final GeneradorMensajesDeNodo generadorMensajes = nodo.getGeneradorMensajes();
		// Generamos el mensaje para el metamensaje
		final MensajeVortex mensaje = generadorMensajes.generarMetaMensajePara(metamensaje);

		// Lo entregamos al receptor
		final EntregarMensajeWorkUnit entregarMensaje = EntregarMensajeWorkUnit.create(receptor, mensaje);
		nodo.getProcesador().process(entregarMensaje);

		if (whenDone != null) {
			whenDone.run();
		}
	}

	public static ProcesarEnvioDeMetamensajeWorkUnit create(final NodoVortexConTasks nodo,
			final ReceptorVortex destino, final MetamensajeVortex contenido, final Runnable whenDone) {
		final ProcesarEnvioDeMetamensajeWorkUnit entrega = new ProcesarEnvioDeMetamensajeWorkUnit();
		entrega.metamensaje = contenido;
		entrega.receptor = destino;
		entrega.nodo = nodo;
		entrega.whenDone = whenDone;
		return entrega;
	}
}
