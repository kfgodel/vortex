/**
 * 10/12/2011 14:53:37 Copyright (C) 2011 Darío L. García
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
import net.gaia.vortex.protocol.confirmations.ConfirmacionRecepcion;
import net.gaia.vortex.protocol.confirmations.SolicitudDeConfirmacionRecepcion;
import net.gaia.vortex.protocol.messages.IdVortex;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Esta clase representa la acción realizada por el nodo al recibir una solicitud de recepción
 * 
 * @author D. García
 */
public class RecibirSolicitudDeRecepcionWorkUnit implements WorkUnit {
	private static final Logger LOG = LoggerFactory.getLogger(RecibirSolicitudDeRecepcionWorkUnit.class);

	private ContextoDeRuteoDeMensaje contexto;
	private SolicitudDeConfirmacionRecepcion solicitud;

	/**
	 * @see net.gaia.taskprocessor.api.WorkUnit#doWork()
	 */
	@HasDependencyOn(Decision.TODAVIA_NO_IMPLEMENTE_PRORROGA)
	public void doWork() throws InterruptedException {
		// Vemos si recordamos el mensaje y por lo tanto lo recibimos
		final ReceptorVortex emisor = contexto.getEmisor();
		final IdVortex idDeMensajeAConfirmar = solicitud.getIdDeMensajeAConfirmar();
		DevolverAcuseFallaRecepcionWorkUnit confirmacionDevuelta;
		final boolean recibimosElMensaje = emisor.envioPreviamente(idDeMensajeAConfirmar);
		if (recibimosElMensaje) {
			// Le devolvemos una confirmación exitosa porque recibimos el mensaje en algún momento
			confirmacionDevuelta = DevolverAcuseFallaRecepcionWorkUnit.create(contexto, null);
		} else {
			LOG.error("Nos pidieron confirmación de un mensaje[{}] que no recibimos", idDeMensajeAConfirmar);
			// Le devolvemos una confirmación pero por error
			confirmacionDevuelta = DevolverAcuseFallaRecepcionWorkUnit.create(contexto,
					ConfirmacionRecepcion.MENSAJE_IS_MISSING_ERROR);
		}
		// TODO: Pedimos tiempo de prórroga?
		contexto.getProcesador().process(confirmacionDevuelta);
	}

	public static RecibirSolicitudDeRecepcionWorkUnit create(final ContextoDeRuteoDeMensaje contexto,
			final SolicitudDeConfirmacionRecepcion solicitud) {
		final RecibirSolicitudDeRecepcionWorkUnit recibir = new RecibirSolicitudDeRecepcionWorkUnit();
		recibir.contexto = contexto;
		recibir.solicitud = solicitud;
		return recibir;
	}

}
