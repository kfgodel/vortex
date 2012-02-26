/**
 * 26/02/2012 00:45:46 Copyright (C) 2011 10Pines S.R.L.
 */
package net.gaia.vortex.http.tasks;

import net.gaia.taskprocessor.api.WorkUnit;
import net.gaia.vortex.http.sessions.SesionConId;
import net.gaia.vortex.http.tasks.contexts.ContextoDeOperacionHttp;

/**
 * Esta clase representa la operación que realiza el nodo remoto para pollear los mensajes de una
 * sesión
 * 
 * @author D. García
 */
public class RealizarPollingDeMensajesWorkUnit implements WorkUnit {

	private ContextoDeOperacionHttp contexto;

	/**
	 * @see net.gaia.taskprocessor.api.WorkUnit#doWork()
	 */
	@Override
	public void doWork() throws InterruptedException {
		final SesionConId sesionInvolucrada = contexto.getSesionInvolucrada();
		sesionInvolucrada.poll();
	}

	public static RealizarPollingDeMensajesWorkUnit create(final ContextoDeOperacionHttp contexto) {
		final RealizarPollingDeMensajesWorkUnit name = new RealizarPollingDeMensajesWorkUnit();
		name.contexto = contexto;
		return name;
	}
}
