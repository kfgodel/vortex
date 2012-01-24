/**
 * 10/12/2011 14:12:38 Copyright (C) 2011 Darío L. García
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
import net.gaia.vortex.lowlevel.impl.NodoVortexConTasks;
import net.gaia.vortex.lowlevel.impl.receptores.ReceptorVortex;
import net.gaia.vortex.protocol.messages.IdVortex;
import net.gaia.vortex.protocol.messages.routing.SolicitudAcuseConsumo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Esta clase representa la acción del nodo para solicitar a un receptor que envíe la confirmación
 * de consumo
 * 
 * @author D. García
 */
public class EnviarSolicitudDeAcuseDeConsumoWorkUnit implements WorkUnit {
	private static final Logger LOG = LoggerFactory.getLogger(EnviarSolicitudDeAcuseDeConsumoWorkUnit.class);

	private ContextoDeEnvio contexto;

	public static EnviarSolicitudDeAcuseDeConsumoWorkUnit create(final ContextoDeEnvio contexto) {
		final EnviarSolicitudDeAcuseDeConsumoWorkUnit solicitud = new EnviarSolicitudDeAcuseDeConsumoWorkUnit();
		solicitud.contexto = contexto;
		return solicitud;
	}

	/**
	 * @see net.gaia.taskprocessor.api.WorkUnit#doWork()
	 */
	@Override
	public void doWork() throws InterruptedException {
		LOG.debug("Generando solicitud para el acuse de consumo para el mensaje[{}]", contexto.getMensaje());
		// Creamos la solicitud a enviar
		final IdentificadorDeEnvio idDeEnvio = this.contexto.getIdDeEnvio();
		final IdVortex idDeMensajeEnviado = idDeEnvio.getIdDeMensajeEnviado();
		final SolicitudAcuseConsumo solicitudAEnviar = SolicitudAcuseConsumo.create(idDeMensajeEnviado);

		// Se la enviamos al receptor que le mandamos el mensaje
		final ContextoDeRuteoDeMensaje contextoRuteo = contexto.getContextoDeRuteo();
		final ReceptorVortex destino = idDeEnvio.getReceptorDestino();
		final NodoVortexConTasks nodo = contextoRuteo.getNodo();
		final ProcesarEnvioDeMetamensajeWorkUnit envioMetamensaje = ProcesarEnvioDeMetamensajeWorkUnit.create(nodo,
				destino, solicitudAEnviar);
		contextoRuteo.getProcesador().process(envioMetamensaje);

		LOG.debug("Registrando solicitud de consumo enviada para mensaje[{}]", contexto.getMensaje());
		// Registramos que enviamos la solicitud por si viene una espera
		contexto.getControlDeConsumo().registrarEnvioDeSolicitud();
	}

}
