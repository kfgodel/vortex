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
import net.gaia.vortex.lowlevel.impl.receptores.ReceptorVortex;
import net.gaia.vortex.lowlevel.impl.ruteos.MensajesEnEspera;
import net.gaia.vortex.protocol.messages.IdVortex;
import net.gaia.vortex.protocol.messages.routing.AcuseConsumo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Esta clase representa la acción del nodo al recibir una confirmación de consumo de un receptor
 * 
 * @author D. García
 */
public class RecibirAcuseConsumoWorkUnit implements WorkUnit {
	private static final Logger LOG = LoggerFactory.getLogger(RecibirAcuseConsumoWorkUnit.class);

	private ContextoDeRuteoDeMensaje contexto;
	private AcuseConsumo acuse;

	public static RecibirAcuseConsumoWorkUnit create(final ContextoDeRuteoDeMensaje contexto, final AcuseConsumo acuse) {
		final RecibirAcuseConsumoWorkUnit recibir = new RecibirAcuseConsumoWorkUnit();
		recibir.contexto = contexto;
		recibir.acuse = acuse;
		return recibir;
	}

	/**
	 * @see net.gaia.taskprocessor.api.WorkUnit#doWork()
	 */
	@Override
	public void doWork() throws InterruptedException {
		LOG.debug("Recibiendo acuse de consumo del mensaje[{}]", contexto.getMensaje());
		// Armamos el identificador del envío que realizamos
		final IdVortex identificacionMensaje = acuse.getIdMensajeConsumido();
		final ReceptorVortex receptor = this.contexto.getEmisor();
		final IdentificadorDeEnvio identificadorDeEnvio = IdentificadorDeEnvio.create(identificacionMensaje, receptor);

		// Quitamos el mensaje de la lista de espera de acuses de consumo
		final MemoriaDeMensajes memoriaDeMensajes = this.contexto.getMemoriaDeMensajes();
		final MensajesEnEspera esperandoConfirmacion = memoriaDeMensajes.getEsperandoAcuseDeConsumo();
		final ContextoDeEnvio contextoDeEnvio = esperandoConfirmacion.quitar(identificadorDeEnvio);
		if (contextoDeEnvio == null) {
			// Ya lo quitaron. O recibimos un acuse previo, o ya lo dimos por perdido
			LOG.debug("Llegó un acuse de consumo[{}] para el que no existe contexto previo en el mensaje[{}]", acuse,
					contexto.getMensaje());
			return;
		}

		// Si lo pudimos quitar registramos que recibimos confirmación
		final RegistrarMensajeConsumidoWorkUnit registroConsumo = RegistrarMensajeConsumidoWorkUnit.create(
				contextoDeEnvio, acuse);
		contexto.getProcesador().process(registroConsumo);
	}
}
