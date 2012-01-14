/**
 * 10/12/2011 14:22:01 Copyright (C) 2011 Darío L. García
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
import net.gaia.vortex.lowlevel.impl.ContextoDeRuteoDeMensaje;
import net.gaia.vortex.lowlevel.impl.IdentificadorDeEnvio;
import net.gaia.vortex.lowlevel.impl.MemoriaDeMensajes;
import net.gaia.vortex.lowlevel.impl.MensajesEnEspera;
import net.gaia.vortex.lowlevel.impl.ReceptorVortex;
import net.gaia.vortex.protocol.confirmations.ConfirmacionConsumo;
import net.gaia.vortex.protocol.messages.IdVortex;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Esta clase representa la acción del nodo al recibir una confirmación de consumo de un receptor
 * 
 * @author D. García
 */
public class RecibirConfirmacionDeConsumoWorkUnit implements WorkUnit {
	private static final Logger LOG = LoggerFactory.getLogger(RecibirConfirmacionDeConsumoWorkUnit.class);

	private ContextoDeRuteoDeMensaje contexto;
	private ConfirmacionConsumo confirmacion;

	public static RecibirConfirmacionDeConsumoWorkUnit create(final ContextoDeRuteoDeMensaje contexto,
			final ConfirmacionConsumo confirmacion) {
		final RecibirConfirmacionDeConsumoWorkUnit recibir = new RecibirConfirmacionDeConsumoWorkUnit();
		recibir.contexto = contexto;
		recibir.confirmacion = confirmacion;
		return recibir;
	}

	/**
	 * @see net.gaia.taskprocessor.api.WorkUnit#doWork()
	 */
	public void doWork() throws InterruptedException {
		// Creamos el identificador del envío
		final IdVortex identificacionMensaje = confirmacion.getIdentificacionMensaje();
		final ReceptorVortex receptor = this.contexto.getEmisor();
		final IdentificadorDeEnvio identificadorDeEnvio = IdentificadorDeEnvio.create(identificacionMensaje, receptor);

		// Quitamos el mensaje de la lista de espera de confirmaciones de consumo
		final MemoriaDeMensajes memoriaDeMensajes = this.contexto.getMemoriaDeMensajes();
		final MensajesEnEspera esperandoConfirmacion = memoriaDeMensajes.getEsperandoAcuseDeConsumo();
		final ContextoDeEnvio contextoDeEnvio = esperandoConfirmacion.quitar(identificadorDeEnvio);
		if (contextoDeEnvio == null) {
			// Ya lo quitaron. Quizás por confirmación o porque se acabó el tiempo de espera
			LOG.info("Llegó una confirmación de consumo[{}] para la que no existe contexto", confirmacion);
			// Nada que hacer
			return;
		}

		// Si lo pudimos quitar registramos que recibimos confirmación
		final RegistrarMensajeConsumidoWorkUnit registroConsumo = RegistrarMensajeConsumidoWorkUnit
				.create(contextoDeEnvio);
		contexto.getProcesador().process(registroConsumo);
	}
}
