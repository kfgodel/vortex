/**
 * 15/01/2012 13:31:27 Copyright (C) 2011 Darío L. García
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
import net.gaia.vortex.protocol.messages.IdVortex;
import net.gaia.vortex.protocol.messages.routing.AcuseDuplicado;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Esta clase representa la operación que realiza el nodo al recibir un acuse de duplicado de un
 * receptor
 * 
 * @author D. García
 */
public class RecibirAcuseDuplicadoWorkUnit implements WorkUnit {
	private static final Logger LOG = LoggerFactory.getLogger(RecibirAcuseDuplicadoWorkUnit.class);

	private ContextoDeRuteoDeMensaje contexto;
	private AcuseDuplicado acuse;

	/**
	 * @see net.gaia.taskprocessor.api.WorkUnit#doWork()
	 */
	@Override
	public void doWork() throws InterruptedException {
		// Armamos el ID del envio que realizamos
		final ReceptorVortex receptorDelMensaje = contexto.getEmisor();
		final IdVortex idMensajeDuplicado = acuse.getIdMensajeDuplicado();
		final IdentificadorDeEnvio idDeEnvio = IdentificadorDeEnvio.create(idMensajeDuplicado, receptorDelMensaje);

		// Deberíamos tener el acuse del envio pendiente
		final MemoriaDeMensajes memoriaDeMensajes = contexto.getMemoriaDeMensajes();
		final MensajesEnEspera esperandoAcuseDeConsumo = memoriaDeMensajes.getEsperandoAcuseDeConsumo();
		final ContextoDeEnvio contextoDeEnvio = esperandoAcuseDeConsumo.quitar(idDeEnvio);
		if (contextoDeEnvio == null) {
			// Si no tenemos el contexto lo más probable es que el acuse que recibimos sea inválido
			LOG.warn("Recibimos un acuse de duplicado para un envio que no tenemos registro: {}. Ignorando", acuse);
			return;
		}

		// Registramos el mensaje como duplicado
		final RegistrarMensajeDuplicadoWorkUnit registrarDuplicado = RegistrarMensajeDuplicadoWorkUnit
				.create(contextoDeEnvio);
		contexto.getProcesador().process(registrarDuplicado);
	}

	public static RecibirAcuseDuplicadoWorkUnit create(final ContextoDeRuteoDeMensaje contexto,
			final AcuseDuplicado acuseDuplicado) {
		final RecibirAcuseDuplicadoWorkUnit recibir = new RecibirAcuseDuplicadoWorkUnit();
		recibir.contexto = contexto;
		recibir.acuse = acuseDuplicado;
		return recibir;
	}
}