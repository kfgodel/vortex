/**
 * 01/02/2012 20:32:56 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.http.tasks;

import java.util.List;

import net.gaia.taskprocessor.api.WorkUnit;
import net.gaia.vortex.http.sessions.SesionConId;
import net.gaia.vortex.http.tasks.contexts.ContextoDeOperacionHttp;
import net.gaia.vortex.protocol.http.VortexWrapper;
import net.gaia.vortex.protocol.messages.MensajeVortex;

/**
 * Esta clase representa la operación de recepción de mensajes del nodo remoto
 * 
 * @author D. García
 */
public class RecibirMensajesWorkUnit implements WorkUnit {

	private ContextoDeOperacionHttp contexto;
	private VortexWrapper recibido;

	/**
	 * @see net.gaia.taskprocessor.api.WorkUnit#doWork()
	 */
	@Override
	public void doWork() throws InterruptedException {
		// Tomamos el id que nos asigna el nodo
		final Long nuevoId = recibido.getSessionId();
		final SesionConId sesionReceptora = contexto.getSesionInvolucrada();
		sesionReceptora.cambiarId(nuevoId);

		// Quitamos los mensajes del wrapper y pasamos a desconvertir los metamensajes
		final List<MensajeVortex> mensajesRecibidos = recibido.getMensajes();
		final DesconvertirMetamensajesDeStringsWorkUnit desconversion = DesconvertirMetamensajesDeStringsWorkUnit
				.create(contexto, mensajesRecibidos);
		contexto.getProcessor().process(desconversion);
	}

	public static RecibirMensajesWorkUnit create(final ContextoDeOperacionHttp contexto, final VortexWrapper recibido) {
		final RecibirMensajesWorkUnit recepcion = new RecibirMensajesWorkUnit();
		recepcion.contexto = contexto;
		recepcion.recibido = recibido;
		return recepcion;
	}
}
