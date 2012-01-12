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
import net.gaia.vortex.lowlevel.impl.GeneradorMensajesDeNodo;
import net.gaia.vortex.lowlevel.impl.IdentificadorDeEnvio;
import net.gaia.vortex.lowlevel.impl.ReceptorVortex;
import net.gaia.vortex.protocol.MensajeVortexEmbebido;
import net.gaia.vortex.protocol.confirmations.SolicitudDeConfirmacionConsumo;
import net.gaia.vortex.protocol.messages.IdVortex;

/**
 * Esta clase representa la acción del nodo para solicitar a un receptor que envíe la confirmación
 * de consumo
 * 
 * @author D. García
 */
public class EnviarSolicitudDeConfirmacionConsumoWorkUnit implements WorkUnit {

	private ContextoDeEnvio contexto;

	public static EnviarSolicitudDeConfirmacionConsumoWorkUnit create(final ContextoDeEnvio contexto) {
		final EnviarSolicitudDeConfirmacionConsumoWorkUnit solicitud = new EnviarSolicitudDeConfirmacionConsumoWorkUnit();
		solicitud.contexto = contexto;
		return solicitud;
	}

	/**
	 * @see net.gaia.taskprocessor.api.WorkUnit#doWork()
	 */
	public void doWork() throws InterruptedException {
		// Creamos la solicitud a enviar
		final IdentificadorDeEnvio idDeEnvio = this.contexto.getIdDeEnvio();
		final IdVortex idDeMensajeEnviado = idDeEnvio.getIdDeMensajeEnviado();
		final SolicitudDeConfirmacionConsumo solicitud = SolicitudDeConfirmacionConsumo.create(idDeMensajeEnviado);

		// La metemos en un mensaje vortex
		final GeneradorMensajesDeNodo generadorMensajes = this.contexto.getGeneradorDeMensajes();
		final MensajeVortexEmbebido mensajeDeSolicitud = generadorMensajes.generarMetaMensajePara(solicitud);
		// Se la enviamos al receptor que no respondió todavía
		final ReceptorVortex receptorASolicitar = idDeEnvio.getReceptorDestino();
		receptorASolicitar.recibir(mensajeDeSolicitud);
	}

}
