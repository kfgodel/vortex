/**
 * 27/11/2011 22:55:41 Copyright (C) 2011 Darío L. García
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
import net.gaia.vortex.lowlevel.impl.ContextoDeEnvio;
import net.gaia.vortex.lowlevel.impl.IdentificadorDeEnvio;
import net.gaia.vortex.lowlevel.impl.MemoriaDeMensajes;
import net.gaia.vortex.lowlevel.impl.MensajesEnEspera;
import net.gaia.vortex.lowlevel.impl.ReceptorVortex;
import net.gaia.vortex.protocol.messages.MensajeVortex;

/**
 * Esta clase representa la tarea de enviar el mensaje y esperar confirmación de entrega
 * 
 * @author D. García
 */
public class ProcesarEnvioDeMensajeWorkUnit implements WorkUnit {

	private ContextoDeEnvio contexto;

	public static ProcesarEnvioDeMensajeWorkUnit create(final ContextoDeEnvio contexto) {
		final ProcesarEnvioDeMensajeWorkUnit envio = new ProcesarEnvioDeMensajeWorkUnit();
		envio.contexto = contexto;
		return envio;
	}

	/**
	 * @see net.gaia.taskprocessor.api.WorkUnit#doWork()
	 */
	@Override
	public void doWork() throws InterruptedException {

		// Primero lo registramos en espera de consumo por si llega rápido el acuse de consumo
		final MemoriaDeMensajes memoriaDeMensajes = this.contexto.getMemoriaDeMensajes();
		final MensajesEnEspera esperandoConsumo = memoriaDeMensajes.getEsperandoAcuseDeConsumo();
		final IdentificadorDeEnvio identificacionDeEnvio = contexto.getIdDeEnvio();
		esperandoConsumo.agregar(identificacionDeEnvio, contexto);

		// Enviamos el mensaje a su destinatario
		final ReceptorVortex destino = contexto.getReceptorInteresado();
		final MensajeVortex mensaje = contexto.getMensaje();
		final EntregarMensajeWorkUnit entregaMensaje = EntregarMensajeWorkUnit.create(destino, mensaje);
		contexto.getProcesador().process(entregaMensaje);

		// Esperamos el acuse de consumo
		final EsperarAcuseConsumoWorkUnit esperaConsumo = EsperarAcuseConsumoWorkUnit.create(contexto);
		contexto.getProcesador().process(esperaConsumo);
	}
}
