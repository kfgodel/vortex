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
import net.gaia.vortex.lowlevel.impl.envios.ContextoDeEnvio;
import net.gaia.vortex.lowlevel.impl.envios.ControlDeConsumoDeEnvio;
import net.gaia.vortex.lowlevel.impl.envios.EsperaDeAccion;
import net.gaia.vortex.lowlevel.impl.envios.IdentificadorDeEnvio;
import net.gaia.vortex.lowlevel.impl.envios.MensajesEnEspera;
import net.gaia.vortex.lowlevel.impl.mensajes.MemoriaDeMensajes;
import net.gaia.vortex.lowlevel.impl.nodo.ConfiguracionDeNodo;
import net.gaia.vortex.lowlevel.impl.receptores.ReceptorVortex;
import net.gaia.vortex.lowlevel.impl.ruteo.ContextoDeRuteoDeMensaje;
import net.gaia.vortex.meta.Loggers;
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
		LOG.debug("Recibiendo solicitud de espera para del mensaje[{}]", contexto.getMensaje());
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
			LOG.debug("Recibimos una solicitud de espera para un envio que ya está cerrado: " + idEnvioRealizado);
			Loggers.RUTEO.info("SOLICITUD ESPERA para envio[{}] no tiene contexto registrado. Ignorando. FIN",
					idEnvioRealizado);
			final TerminarProcesoDeMensajeWorkUnit terminarProceso = TerminarProcesoDeMensajeWorkUnit.create(receptor,
					contexto.getNodo());
			contexto.getProcesador().process(terminarProceso);
			return;
		}

		// Extendemos el plazo de espera del acuse de consumo;
		final EsperaDeAccion esperaDeAcuse = contextoDeEnvio.getEsperaDeAcuseConsumo();
		final ConfiguracionDeNodo config = contexto.getConfig();
		final TimeMagnitude prorroga = config.getTimeoutDeAcuseDeConsumo();
		esperaDeAcuse.iniciarEsperaDe(prorroga);
		LOG.debug("Extendiendo espera[{}] para el envio[{}]", prorroga, idEnvioRealizado);
		Loggers.RUTEO.info("SOLICITUD ESPERA confirmada para envio[{}]. Extendiendo espera [{}]. FIN",
				idEnvioRealizado, prorroga);

		// Registramos que recibimos una solicitud de espera
		final ControlDeConsumoDeEnvio controlDeConsumo = contextoDeEnvio.getControlDeConsumo();
		controlDeConsumo.registrarRecepcionDeSolicitudDeEspera();

		final TerminarProcesoDeMensajeWorkUnit terminarProceso = TerminarProcesoDeMensajeWorkUnit.create(receptor,
				contexto.getNodo());
		contexto.getProcesador().process(terminarProceso);
	}

	public static RecibirSolicitudDeEsperaAcuseConsumoWorkUnit create(final ContextoDeRuteoDeMensaje contexto,
			final SolicitudEsperaAcuseConsumo solicitud) {
		final RecibirSolicitudDeEsperaAcuseConsumoWorkUnit recibir = new RecibirSolicitudDeEsperaAcuseConsumoWorkUnit();
		recibir.contexto = contexto;
		recibir.solicitud = solicitud;
		return recibir;
	}
}
