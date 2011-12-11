/**
 * 10/12/2011 11:31:24 Copyright (C) 2011 Darío L. García
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
import net.gaia.vortex.lowlevel.impl.ContextoDeEnvio;
import net.gaia.vortex.lowlevel.impl.ContextoDeRuteoDeMensaje;
import net.gaia.vortex.lowlevel.impl.IdentificadorDeEnvio;
import net.gaia.vortex.lowlevel.impl.MemoriaDeMensajes;
import net.gaia.vortex.lowlevel.impl.MensajesEnEspera;
import net.gaia.vortex.lowlevel.impl.ReceptorVortex;
import net.gaia.vortex.meta.Decision;
import net.gaia.vortex.protocol.IdVortex;
import net.gaia.vortex.protocol.confirmations.ConfirmacionRecepcion;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Esta clase representa la acción realizada por el nodo al recibir una confirmación de recepción.
 * En la que debe marcar el mensaje como no más pendiente de confirmación
 * 
 * @author D. García
 */
public class RecibirConfirmacionDeRecepcionWorkUnit implements WorkUnit {
	private static final Logger LOG = LoggerFactory.getLogger(RecibirConfirmacionDeRecepcionWorkUnit.class);

	private ContextoDeRuteoDeMensaje contexto;
	private ConfirmacionRecepcion confirmacion;

	/**
	 * @see net.gaia.taskprocessor.api.WorkUnit#doWork()
	 */
	public void doWork() throws InterruptedException {
		// Creamos el identificador del envío
		final IdVortex identificacionMensaje = confirmacion.getIdentificacionMensaje();
		final ReceptorVortex receptor = this.contexto.getEmisor();
		final IdentificadorDeEnvio identificadorDeEnvio = IdentificadorDeEnvio.create(identificacionMensaje, receptor);

		// Quitamos el mensaje de la lista de espera de confirmaciones
		final MemoriaDeMensajes memoriaDeMensajes = this.contexto.getMemoriaDeMensajes();
		final MensajesEnEspera esperandoConfirmacion = memoriaDeMensajes.getEsperandoConfirmacionDeRecepcion();
		final ContextoDeEnvio contextoDeEnvio = esperandoConfirmacion.quitar(identificadorDeEnvio);
		if (contextoDeEnvio == null) {
			// Ya lo quitaron. Quizás por confirmación o porque se acabó el tiempo de espera
			LOG.info("Llegó una confirmación de recepción[{}] para la que no existe contexto", confirmacion);
			// Nada que hacer
			return;
		}

		// Determinamos la proxima acción en base a los datos de la confirmación
		final WorkUnit proximaAccion = crearAccionDesde(confirmacion, contextoDeEnvio);
		contexto.getProcesador().process(proximaAccion);

	}

	/**
	 * Determina la próxima acción a partir de la confirmación recibida. Si es exitosa
	 * 
	 * @param contextoDeEnvio
	 * 
	 * @param confirmacion2
	 * @return
	 */
	@HasDependencyOn({ Decision.TODAVIA_NO_IMPLEMENTE_PRORROGA,
			Decision.TODAVIA_NO_IMPLEMENTE_REENVIO_DE_MENSAJE_PERDIDO })
	private WorkUnit crearAccionDesde(final ConfirmacionRecepcion confirmacion, final ContextoDeEnvio contextoDeEnvio) {
		// Vemos si es un error o es una confirmación exitosa
		if (confirmacion.getAceptado()) {
			final RegistrarMensajeRecibidoWorkUnit registrarRecibido = RegistrarMensajeRecibidoWorkUnit
					.create(contextoDeEnvio);
			return registrarRecibido;
		}
		// TODO: Si es mensaje perdido se debería reenviar

		// Lo registramos como rechazado
		final RegistrarMensajeRechazadoWorkUnit registrarRechazo = RegistrarMensajeRechazadoWorkUnit
				.create(contextoDeEnvio);
		return registrarRechazo;
	}

	public static RecibirConfirmacionDeRecepcionWorkUnit create(final ContextoDeRuteoDeMensaje contexto,
			final ConfirmacionRecepcion recepcion) {
		final RecibirConfirmacionDeRecepcionWorkUnit recibir = new RecibirConfirmacionDeRecepcionWorkUnit();
		recibir.contexto = contexto;
		recibir.confirmacion = recepcion;
		return recibir;
	}
}
