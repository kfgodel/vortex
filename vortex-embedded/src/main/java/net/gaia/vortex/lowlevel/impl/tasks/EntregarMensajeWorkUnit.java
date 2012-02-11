/**
 * 14/01/2012 15:44:55 Copyright (C) 2011 Darío L. García
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

import net.gaia.vortex.lowlevel.impl.receptores.ReceptorVortex;
import net.gaia.vortex.protocol.messages.MensajeVortex;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Esta clase representa la operación de entrega del mensaje a un receptor
 * 
 * @author D. García
 */
public class EntregarMensajeWorkUnit implements TareaParaReceptor {
	private static final Logger LOG = LoggerFactory.getLogger(EntregarMensajeWorkUnit.class);

	private ReceptorVortex receptor;
	private MensajeVortex mensaje;

	/**
	 * @see net.gaia.taskprocessor.api.WorkUnit#doWork()
	 */
	@Override
	public void doWork() throws InterruptedException {
		LOG.debug("Entregando mensaje[{}] a receptor[{}]", mensaje, receptor);
		// Le damos el mensaje al receptor destinado
		receptor.recibir(mensaje);
	}

	/**
	 * @see net.gaia.vortex.lowlevel.impl.tasks.TareaParaReceptor#esPara(net.gaia.vortex.lowlevel.impl.receptores.ReceptorVortex)
	 */
	@Override
	public boolean esPara(final ReceptorVortex receptor) {
		return this.receptor == receptor;
	}

	public static EntregarMensajeWorkUnit create(final ReceptorVortex destino, final MensajeVortex mensajeAEnviar) {
		final EntregarMensajeWorkUnit entrega = new EntregarMensajeWorkUnit();
		entrega.mensaje = mensajeAEnviar;
		entrega.receptor = destino;
		return entrega;
	}
}
