/**
 * 10/12/2011 21:05:51 Copyright (C) 2011 Darío L. García
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

import net.gaia.taskprocessor.api.TimeMagnitude;
import net.gaia.taskprocessor.api.WorkUnit;
import net.gaia.vortex.lowlevel.impl.ConfiguracionDeNodo;
import net.gaia.vortex.lowlevel.impl.ContextoDeEnvio;
import net.gaia.vortex.lowlevel.impl.ContextoDeRuteoDeMensaje;
import net.gaia.vortex.lowlevel.impl.ControlDeConsumoDeEnvio;
import net.gaia.vortex.lowlevel.impl.EsperaDeAccion;
import net.gaia.vortex.lowlevel.impl.IdentificadorDeEnvio;
import net.gaia.vortex.lowlevel.impl.MemoriaDeMensajes;
import net.gaia.vortex.lowlevel.impl.MensajesEnEspera;
import net.gaia.vortex.lowlevel.impl.ReceptorVortex;
import net.gaia.vortex.protocol.messages.IdVortex;
import net.gaia.vortex.protocol.messages.routing.SolicitudEsperaAcuseConsumo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Esta clase representa la acción realizada por el nodo al recibir la solicitud de consumo
 * 
 * @author D. García
 */
public class RecibirSolicitudDeEsperaAcuseConsumoWorkUnit implements WorkUnit {
	private static final Logger LOG = LoggerFactory.getLogger(RecibirSolicitudDeEsperaAcuseConsumoWorkUnit.class);

	private ContextoDeRuteoDeMensaje contexto;
	private SolicitudEsperaAcuseConsumo solicitud;

	/**
	 * @see net.gaia.taskprocessor.api.WorkUnit#doWork()
	 */
	@Override
	public void doWork() throws InterruptedException {
		// Armamos el ID del envío que realizamos
		final IdVortex idMensajeEnviado = solicitud.getIdMensajeRecibido();
		final ReceptorVortex receptor = contexto.getEmisor();
		final IdentificadorDeEnvio idEnvioRealizado = IdentificadorDeEnvio.create(idMensajeEnviado, receptor);

		// Obtenemos el contexto del envío original
		final MemoriaDeMensajes memoriaDeMensajes = contexto.getMemoriaDeMensajes();
		final MensajesEnEspera esperandoAcuseDeConsumo = memoriaDeMensajes.getEsperandoAcuseDeConsumo();
		final ContextoDeEnvio contextoDeEnvio = esperandoAcuseDeConsumo.getContextoDe(idEnvioRealizado);

		if (contextoDeEnvio == null) {
			// O recibimos el acuse, o se acabo el tiempo, por lo que la espera no tiene sentido
			LOG.debug("Recibimos una solicitud para un envio que ya está cerrado: " + idEnvioRealizado);
			return;
		}

		// Extendemos el plazo de espera del acuse de consumo;
		final EsperaDeAccion esperaDeAcuse = contextoDeEnvio.getEsperaDeAcuseConsumo();
		final ConfiguracionDeNodo config = contexto.getConfig();
		final TimeMagnitude prorroga = config.getTimeoutDeAcuseDeConsumo();
		esperaDeAcuse.iniciarEsperaDe(prorroga);

		// Registramos que recibimos una solicitud de espera
		final ControlDeConsumoDeEnvio controlDeConsumo = contextoDeEnvio.getControlDeConsumo();
		controlDeConsumo.registrarRecepcionDeSolicitudDeEspera();
	}

	public static RecibirSolicitudDeEsperaAcuseConsumoWorkUnit create(final ContextoDeRuteoDeMensaje contexto,
			final SolicitudEsperaAcuseConsumo solicitud) {
		final RecibirSolicitudDeEsperaAcuseConsumoWorkUnit recibir = new RecibirSolicitudDeEsperaAcuseConsumoWorkUnit();
		recibir.contexto = contexto;
		recibir.solicitud = solicitud;
		return recibir;
	}
}
