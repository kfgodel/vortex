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
import net.gaia.vortex.lowlevel.impl.envios.IdentificadorDeEnvio;
import net.gaia.vortex.lowlevel.impl.receptores.ReceptorVortex;
import net.gaia.vortex.lowlevel.impl.ruteo.ContextoDeRuteoDeMensaje;
import net.gaia.vortex.lowlevel.impl.ruteo.MemoriaDeRuteos;
import net.gaia.vortex.meta.Decision;
import net.gaia.vortex.meta.Loggers;
import net.gaia.vortex.protocol.messages.IdVortex;
import net.gaia.vortex.protocol.messages.routing.SolicitudAcuseConsumo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Esta clase representa la acción realizada por el nodo al recibir una solicitud de recepción
 * 
 * @author D. García
 */
public class RecibirSolicitudDeAcuseConsumoWorkUnit implements WorkUnit {
	private static final Logger LOG = LoggerFactory.getLogger(RecibirSolicitudDeAcuseConsumoWorkUnit.class);

	private ContextoDeRuteoDeMensaje contexto;
	private SolicitudAcuseConsumo solicitud;

	/**
	 * @see net.gaia.taskprocessor.api.WorkUnit#doWork()
	 */
	@Override
	@HasDependencyOn(Decision.TODAVIA_NO_IMPLEMENTE_PRORROGA)
	public void doWork() throws InterruptedException {
		LOG.debug("Recibiendo solicitud de acuse de consumo para mensaje[{}]", contexto.getMensaje());

		// Armamos el identificador del envío realizado
		final ReceptorVortex receptorDelEnvio = contexto.getEmisor();
		final IdVortex idMensajeSolicitado = solicitud.getIdMensajeEnviado();
		final IdentificadorDeEnvio idEnvioRealizado = IdentificadorDeEnvio
				.create(idMensajeSolicitado, receptorDelEnvio);

		// Verificamos que el acuse está inconcluso todavía
		final MemoriaDeRuteos memoriaDeMensajes = contexto.getMemoriaDeRuteos();
		if (!memoriaDeMensajes.tieneRuteoActivoPara(idEnvioRealizado)) {
			// No estamos armando el acuse. O ya enviamos el acuse, o nunca enviamos el mensaje
			LOG.info(
					"Solicitaron un acuse para el ID de mensaje[{}] del que no tenemos envio. Asumiendo que ya lo enviamos",
					idMensajeSolicitado);
			Loggers.RUTEO
					.info("SOLICITUD ACUSE para envio[{}] no tiene ruteo activo. Ignorando. FIN", idEnvioRealizado);
			final TerminarProcesoDeMensajeWorkUnit terminarProceso = TerminarProcesoDeMensajeWorkUnit.create(
					receptorDelEnvio, contexto.getNodo());
			contexto.getProcesador().process(terminarProceso);
			return;
		}

		Loggers.RUTEO.info("SOLICITUD ACUSE confirmada para envio[{}]", idEnvioRealizado);
		// Todavía estamos armando el acuse, solicitamos más tiempo
		final SolicitarEsperaDeAcuseConsumoWorkUnit solicitarEspera = SolicitarEsperaDeAcuseConsumoWorkUnit.create(
				contexto, idMensajeSolicitado);
		contexto.getProcesador().process(solicitarEspera);
	}

	public static RecibirSolicitudDeAcuseConsumoWorkUnit create(final ContextoDeRuteoDeMensaje contexto,
			final SolicitudAcuseConsumo solicitud) {
		final RecibirSolicitudDeAcuseConsumoWorkUnit recibir = new RecibirSolicitudDeAcuseConsumoWorkUnit();
		recibir.contexto = contexto;
		recibir.solicitud = solicitud;
		return recibir;
	}

}
