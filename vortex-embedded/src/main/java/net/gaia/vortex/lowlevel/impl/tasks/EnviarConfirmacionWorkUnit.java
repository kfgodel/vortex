/**
 * 27/11/2011 22:32:07 Copyright (C) 2011 Darío L. García
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

import net.gaia.annotations.HasDependencyOn;
import net.gaia.taskprocessor.api.WorkUnit;
import net.gaia.vortex.lowlevel.impl.ContextoDeRuteoDeMensaje;
import net.gaia.vortex.lowlevel.impl.ReceptorVortex;
import net.gaia.vortex.meta.Decision;
import net.gaia.vortex.protocol.MensajeVortexEmbebido;
import net.gaia.vortex.protocol.confirmations.MensajeDeConfirmacion;
import net.gaia.vortex.protocol.messages.IdVortex;

/**
 * Esta clase representa la tarea de enviar una confirmación al emisor de un mensaje. A diferencia
 * de otros envíos esta tarea no requiere confirmación del envío
 * 
 * @author D. García
 */
public class EnviarConfirmacionWorkUnit implements WorkUnit {

	private ContextoDeRuteoDeMensaje contexto;

	private MensajeDeConfirmacion confirmacion;

	public static EnviarConfirmacionWorkUnit create(final ContextoDeRuteoDeMensaje contexto,
			final MensajeDeConfirmacion confirmacion) {
		final EnviarConfirmacionWorkUnit envio = new EnviarConfirmacionWorkUnit();
		envio.contexto = contexto;
		envio.confirmacion = confirmacion;
		return envio;
	}

	/**
	 * @see net.gaia.taskprocessor.api.WorkUnit#doWork()
	 */
	@HasDependencyOn({ Decision.LA_TAREA_DE_ENVIO_DE_CONFIRMACION_REQUIERE_EMISOR_REAL,
			Decision.LA_TAREA_DE_ENVIO_DE_CONFIRMACION_ASIGNA_EL_ID_DEL_MENSAJE })
	public void doWork() throws InterruptedException {
		// Establecemos el ID del mensaje para el que se envía la confirmación
		final MensajeVortexEmbebido mensaje = contexto.getMensaje();
		final IdVortex identificacion = mensaje.getIdentificacion();
		this.confirmacion.setIdentificacionMensaje(identificacion);

		// Hacemos que el emisor reciba la confirmación
		final MensajeVortexEmbebido mensajeConfirmacion = contexto.getGeneradorMensajes().generarMetaMensajePara(
				confirmacion);
		final ReceptorVortex emisor = contexto.getEmisor();
		emisor.recibir(mensajeConfirmacion);
	}

}
