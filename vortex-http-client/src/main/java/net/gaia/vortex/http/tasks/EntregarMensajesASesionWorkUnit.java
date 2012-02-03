/**
 * 02/02/2012 23:21:44 Copyright (C) 2011 Darío L. García
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
import net.gaia.vortex.protocol.messages.MensajeVortex;

/**
 * Esta clase representa a la operación que realiza el nodo http para entregar los mensajes
 * recibidos a una de sus sesiones
 * 
 * @author D. García
 */
public class EntregarMensajesASesionWorkUnit implements WorkUnit {

	private ContextoDeOperacionHttp contexto;
	private List<MensajeVortex> mensajesRecibidos;

	/**
	 * @see net.gaia.taskprocessor.api.WorkUnit#doWork()
	 */
	@Override
	public void doWork() throws InterruptedException {
		// Obtenemos el handler designado para la sesión
		final SesionConId sesionReceptora = contexto.getSesionInvolucrada();

		// Hacemos que reciba cada mensaje
		for (final MensajeVortex mensajeRecibido : mensajesRecibidos) {
			sesionReceptora.recibirDelNodo(mensajeRecibido);
		}
	}

	public static EntregarMensajesASesionWorkUnit create(final ContextoDeOperacionHttp contexto,
			final List<MensajeVortex> mensajesRecibidos) {
		final EntregarMensajesASesionWorkUnit entrega = new EntregarMensajesASesionWorkUnit();
		entrega.contexto = contexto;
		entrega.mensajesRecibidos = mensajesRecibidos;
		return entrega;
	}
}
