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
import net.gaia.vortex.lowlevel.impl.ContextoDeRuteoDeMensaje;
import net.gaia.vortex.lowlevel.impl.MemoriaDeMensajes;
import net.gaia.vortex.lowlevel.impl.MensajesEnEspera;
import net.gaia.vortex.lowlevel.impl.ReceptorVortex;
import net.gaia.vortex.protocol.MensajeVortexEmbebido;

/**
 * Esta clase representa la tarea de enviar el mensaje y esperar confirmación de entrega
 * 
 * @author D. García
 */
public class EnviarMensajeWorkUnit implements WorkUnit {

	private ContextoDeRuteoDeMensaje contexto;
	private ReceptorVortex receptorVortex;

	public static EnviarMensajeWorkUnit create(final ContextoDeRuteoDeMensaje contexto,
			final ReceptorVortex receptorDelMensaje) {
		final EnviarMensajeWorkUnit envio = new EnviarMensajeWorkUnit();
		envio.contexto = contexto;
		envio.receptorVortex = receptorDelMensaje;
		return envio;
	}

	/**
	 * @see net.gaia.taskprocessor.api.WorkUnit#doWork()
	 */
	public void doWork() throws InterruptedException {
		final MensajeVortexEmbebido mensajeAEnviar = contexto.getMensaje();

		// Primero lo registramos como esperando confirmación por si llega antes
		final MemoriaDeMensajes memoriaDeMensajes = this.contexto.getMemoriaDeMensajes();
		final MensajesEnEspera esperandoConfirmacion = memoriaDeMensajes.getEsperandoConfirmacionDeRecepcion();
		esperandoConfirmacion.agregar(mensajeAEnviar, contexto);

		// Enviamos el mensaje a su destinatarios
		receptorVortex.recibir(mensajeAEnviar);

		// Agregamos la espera por timeout por si la confirmación no llega
		final EsperarRecepcionOTimeoutWorkUnit esperaWorkUnit = EsperarRecepcionOTimeoutWorkUnit.create(contexto);
		this.contexto.getProcesador().processDelayed(EsperarRecepcionOTimeoutWorkUnit.DEFAULT_TIMEOUT, esperaWorkUnit);
	}
}