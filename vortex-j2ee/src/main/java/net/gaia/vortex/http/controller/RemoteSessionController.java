/**
 * 28/01/2012 16:20:47 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.http.controller;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import net.gaia.taskprocessor.api.TaskProcessor;
import net.gaia.taskprocessor.api.TaskProcessorConfiguration;
import net.gaia.taskprocessor.impl.ExecutorBasedTaskProcesor;
import net.gaia.vortex.http.protocol.VortexWrapper;
import net.gaia.vortex.lowlevel.api.EncoladorDeMensajesHandler;
import net.gaia.vortex.lowlevel.api.NodoVortexEmbebido;
import net.gaia.vortex.lowlevel.api.SesionVortex;
import net.gaia.vortex.lowlevel.impl.NodoVortexConTasks;

import org.springframework.stereotype.Component;

/**
 * Esta clase representa un controlador de interacción de mensajes vortex en los que no es posible
 * mantener una sesión abierta, y por lo tanto se usan IDs como mediadores
 * 
 * @author D. García
 */
@Component
public class RemoteSessionController {

	private final ConcurrentHashMap<Long, RemoteSessionImpl> remoteSessions = new ConcurrentHashMap<Long, RemoteSessionImpl>();
	private final NodoVortexEmbebido nodoVortex;
	private final AtomicLong secuenciadorIds = new AtomicLong(1);
	private final NoRemoteSession sinSesionVortex;

	public RemoteSessionController() {
		final TaskProcessor taskProcessor = ExecutorBasedTaskProcesor.create(TaskProcessorConfiguration.create());
		nodoVortex = NodoVortexConTasks.create(taskProcessor, "nodo-http");
		sinSesionVortex = NoRemoteSession.create(nodoVortex);
	}

	/**
	 * Procesa el wrapper recibido como mensaje de uno de los clientes sin sesión. Utiliza el ID del
	 * wrapper para identificar a cual.<br>
	 * Responde con un wrapper para ser enviado al cliente
	 * 
	 * @param wrapper
	 *            El wrapper de input
	 * @return El wrapper de output
	 */
	public VortexWrapper process(final VortexWrapper wrapper) {
		final RemoteSession session = getOrCreateSessionFor(wrapper);

		// Primero enviamos los mensajes que envía el cliente
		session.enviarAlNodo(wrapper.getMensajes());

		// Después recolectamos los que tenemos que devolverle
		final VortexWrapper respuesta = VortexWrapper.createFrom(session);
		return respuesta;
	}

	/**
	 * Devuelve la sesión asociada al ID indicado en el wrapper
	 * 
	 * @param wrapper
	 *            El wrapper recibido
	 * @return La sesión referida por ID o una nueva creada si no tenía ID
	 */
	private RemoteSession getOrCreateSessionFor(final VortexWrapper wrapper) {
		RemoteSession remoteSession = null;
		final Long sessionId = wrapper.getSessionId();
		if (sessionId != null) {
			remoteSession = remoteSessions.get(sessionId);
		}
		// Puede que haya vencido o no tenga, le creamos una
		if (remoteSession == null) {
			if (!wrapper.requiereSesion()) {
				remoteSession = sinSesionVortex;
			} else {
				final RemoteSessionImpl sesionCreada = createRemoteSession();
				final Long idDeLaSesionCreada = sesionCreada.getSessionId();
				remoteSessions.put(idDeLaSesionCreada, sesionCreada);
				remoteSession = sesionCreada;
			}
		}
		return remoteSession;
	}

	/**
	 * Crea una nueva sesión remota para interactuar con el nodo vortex
	 * 
	 * @return La sesión creada para el cliente
	 */
	private RemoteSessionImpl createRemoteSession() {
		final EncoladorDeMensajesHandler encoladorDeLaSesion = EncoladorDeMensajesHandler.create();
		final SesionVortex sesionVortex = nodoVortex.crearNuevaSesion(encoladorDeLaSesion);
		final long proximoId = secuenciadorIds.getAndIncrement();
		final RemoteSessionImpl sesionRemota = RemoteSessionImpl.create(proximoId, sesionVortex, encoladorDeLaSesion);
		return sesionRemota;
	}
}