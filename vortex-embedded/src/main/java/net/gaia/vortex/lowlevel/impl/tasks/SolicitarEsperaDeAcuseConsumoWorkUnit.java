/**
 * 14/01/2012 18:35:02 Copyright (C) 2011 Darío L. García
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
import net.gaia.vortex.lowlevel.impl.ContextoDeRuteoDeMensaje;
import net.gaia.vortex.lowlevel.impl.ReceptorVortex;
import net.gaia.vortex.protocol.messages.IdVortex;
import net.gaia.vortex.protocol.messages.routing.SolicitudEsperaAcuseConsumo;

/**
 * Esta clase representa la operación de solicitud de espera de consumo.<br>
 * Esta operación es necesaria cuando el acuse de consumo está en progreso todavía
 * 
 * @author D. García
 */
public class SolicitarEsperaDeAcuseConsumoWorkUnit implements WorkUnit {

	private IdVortex idMensajeSolicitado;
	private ContextoDeRuteoDeMensaje contexto;

	/**
	 * @see net.gaia.taskprocessor.api.WorkUnit#doWork()
	 */
	@Override
	public void doWork() throws InterruptedException {
		// Armamos la solicitud y la mandamos
		final SolicitudEsperaAcuseConsumo solicitudDeEspera = SolicitudEsperaAcuseConsumo.create(idMensajeSolicitado);
		final ReceptorVortex emisor = contexto.getEmisor();
		final ProcesarEnvioDeMetamensajeWorkUnit envioMetamensaje = ProcesarEnvioDeMetamensajeWorkUnit.create(contexto,
				emisor, solicitudDeEspera);
		contexto.getProcesador().process(envioMetamensaje);
	}

	public static SolicitarEsperaDeAcuseConsumoWorkUnit create(final ContextoDeRuteoDeMensaje contexto,
			final IdVortex idMensajeSolicitado) {
		final SolicitarEsperaDeAcuseConsumoWorkUnit solicitar = new SolicitarEsperaDeAcuseConsumoWorkUnit();
		solicitar.contexto = contexto;
		solicitar.idMensajeSolicitado = idMensajeSolicitado;
		return solicitar;
	}
}