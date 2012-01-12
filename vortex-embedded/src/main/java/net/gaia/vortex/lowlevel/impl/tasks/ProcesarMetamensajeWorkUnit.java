/**
 * 01/12/2011 22:59:08 Copyright (C) 2011 Darío L. García
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
import net.gaia.vortex.protocol.MensajeVortexEmbebido;
import net.gaia.vortex.protocol.confirmations.ConfirmacionConsumo;
import net.gaia.vortex.protocol.confirmations.ConfirmacionRecepcion;
import net.gaia.vortex.protocol.confirmations.SolicitudDeConfirmacionConsumo;
import net.gaia.vortex.protocol.confirmations.SolicitudDeConfirmacionRecepcion;

/**
 * Esta clase representa el trabajo que hace el nodo para procesar un metamensaje recibido
 * 
 * @author D. García
 */
public class ProcesarMetamensajeWorkUnit implements WorkUnit {

	private ContextoDeRuteoDeMensaje contexto;

	public static ProcesarMetamensajeWorkUnit create(final ContextoDeRuteoDeMensaje contexto) {
		final ProcesarMetamensajeWorkUnit procesado = new ProcesarMetamensajeWorkUnit();
		procesado.contexto = contexto;
		return procesado;
	}

	/**
	 * @see net.gaia.taskprocessor.api.WorkUnit#doWork()
	 */
	public void doWork() throws InterruptedException {
		// Tenemos que ver que tipo de metamensaje es, y en base a eso el tipo de acción a realizar
		final MensajeVortexEmbebido mensaje = this.contexto.getMensaje();
		final Object metamensaje = mensaje.getContenido();
		final WorkUnit tareaDelMensaje = crearTareaDesdeMetamensaje(metamensaje);
		if (tareaDelMensaje != null) {
			contexto.getProcesador().process(tareaDelMensaje);
		}
		// Deberíamos indicar si lo consumimos o no
	}

	/**
	 * Evalua el metamensaje para determinar el tipo de tarea que se debe realizar
	 * 
	 * @param mensaje
	 *            El mensaje recibido como metamensaje
	 * @return La tarea a realizar
	 */
	private WorkUnit crearTareaDesdeMetamensaje(final Object metamensaje) {
		if (metamensaje instanceof ConfirmacionRecepcion) {
			final ConfirmacionRecepcion confirmacion = (ConfirmacionRecepcion) metamensaje;
			final RecibirConfirmacionDeRecepcionWorkUnit recibirConfirmacion = RecibirConfirmacionDeRecepcionWorkUnit
					.create(contexto, confirmacion);
			return recibirConfirmacion;
		}
		if (metamensaje instanceof ConfirmacionConsumo) {
			final ConfirmacionConsumo confirmacion = (ConfirmacionConsumo) metamensaje;
			final RecibirConfirmacionDeConsumoWorkUnit recibirConfirmacion = RecibirConfirmacionDeConsumoWorkUnit
					.create(contexto, confirmacion);
			return recibirConfirmacion;
		}
		if (metamensaje instanceof SolicitudDeConfirmacionRecepcion) {
			final SolicitudDeConfirmacionRecepcion solicitud = (SolicitudDeConfirmacionRecepcion) metamensaje;
			final RecibirSolicitudDeRecepcionWorkUnit recibirSolicitud = RecibirSolicitudDeRecepcionWorkUnit.create(
					contexto, solicitud);
			return recibirSolicitud;
		}
		if (metamensaje instanceof SolicitudDeConfirmacionConsumo) {
			// Deberíamos enviar la confirmación nuevamente
			final SolicitudDeConfirmacionConsumo solicitud = (SolicitudDeConfirmacionConsumo) metamensaje;
			final RecibirSolicitudDeConsumoWorkUnit recibirSolicitud = RecibirSolicitudDeConsumoWorkUnit.create(
					contexto, solicitud);
			return recibirSolicitud;
		}
		return null;
	}
}
