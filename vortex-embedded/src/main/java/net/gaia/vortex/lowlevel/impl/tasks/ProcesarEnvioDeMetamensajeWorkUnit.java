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
import net.gaia.vortex.lowlevel.impl.ContextoDeRuteoDeMensaje;
import net.gaia.vortex.lowlevel.impl.GeneradorMensajesDeNodo;
import net.gaia.vortex.lowlevel.impl.ReceptorVortex;
import net.gaia.vortex.protocol.messages.MensajeVortex;
import net.gaia.vortex.protocol.messages.meta.MetamensajeVortex;

/**
 * Esta clase entrega el metamensaje indicado al receptor encapsulándolo en un mensaje vortex
 * 
 * @author D. García
 */
public class ProcesarEnvioDeMetamensajeWorkUnit implements WorkUnit {

	private ContextoDeRuteoDeMensaje contexto;
	private MetamensajeVortex metamensaje;
	private ReceptorVortex receptor;

	/**
	 * @see net.gaia.taskprocessor.api.WorkUnit#doWork()
	 */
	@Override
	public void doWork() throws InterruptedException {
		// Generamos el mensaje para el metamensaje
		final GeneradorMensajesDeNodo generadorMensajes = contexto.getGeneradorMensajes();
		final MensajeVortex mensaje = generadorMensajes.generarMetaMensajePara(metamensaje);

		// Lo entregamos al receptor
		final EntregarMensajeWorkUnit entregarMensaje = EntregarMensajeWorkUnit.create(receptor, mensaje);
		contexto.getProcesador().process(entregarMensaje);
	}

	public static ProcesarEnvioDeMetamensajeWorkUnit create(final ContextoDeRuteoDeMensaje contexto,
			final ReceptorVortex destino, final MetamensajeVortex contenido) {
		final ProcesarEnvioDeMetamensajeWorkUnit entrega = new ProcesarEnvioDeMetamensajeWorkUnit();
		entrega.contexto = contexto;
		entrega.metamensaje = contenido;
		entrega.receptor = destino;
		return entrega;
	}
}
