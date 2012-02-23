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

import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import net.gaia.vortex.lowlevel.api.NodoVortex;
import net.gaia.vortex.lowlevel.api.SesionVortex;
import net.gaia.vortex.lowlevel.impl.mensajes.EncoladorDeMensajesHandler;
import net.gaia.vortex.lowlevel.impl.nodo.ConfiguracionDeNodo;
import net.gaia.vortex.lowlevel.impl.nodo.NodoVortexConTasks;
import net.gaia.vortex.prog.Decision;
import net.gaia.vortex.protocol.http.VortexWrapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.tenpines.commons.annotations.HasDependencyOn;

/**
 * Esta clase representa un controlador de interacción de mensajes vortex en los que no es posible
 * mantener una sesión abierta, y por lo tanto se usan IDs como mediadores
 * 
 * @author D. García
 */
@Component
@Scope("singleton")
public class RemoteSessionController {
	/**
	 * Duración default de una sesión sin actividad
	 */
	private static final long DURACION_DEFAULT_DE_SESION_EN_SEGUNDOS = TimeUnit.SECONDS.convert(2, TimeUnit.HOURS);
	/**
	 * Duración mínima que podemos aceptar como vida de una sesión sin actividad
	 */
	private static final long DURACION_MINIMA_DE_SESION_EN_SEGUNDOS = TimeUnit.SECONDS.convert(5, TimeUnit.MINUTES);
	/**
	 * Duración máxima de una sesión en segundos que podemos aceptar sin actividad
	 */
	private static final long DURACION_MAXIMA_DE_SESION_EN_SEGUNDOS = TimeUnit.SECONDS.convert(7, TimeUnit.DAYS);

	private static final Logger LOG = LoggerFactory.getLogger(RemoteSessionController.class);

	private final ConcurrentHashMap<Long, RemoteSessionImpl> remoteSessions = new ConcurrentHashMap<Long, RemoteSessionImpl>();
	private final NodoVortex nodoVortex;
	private final AtomicLong secuenciadorIds = new AtomicLong(1);
	private final NoRemoteSession sinSesionVortex;

	public RemoteSessionController() {
		final ConfiguracionDeNodo configuracionHttp = ConfiguracionDeNodo.createEnHttp();
		nodoVortex = NodoVortexConTasks.create(configuracionHttp, "nodo-http");
		sinSesionVortex = NoRemoteSession.create(nodoVortex);
	}

	/**
	 * Procesa el wrapper recibido como mensaje de uno de los clientes sin sesión. Utiliza el ID del
	 * wrapper para identificar a cual.<br>
	 * Responde con un wrapper para ser enviado al cliente
	 * 
	 * @param wrapperRecibido
	 *            El wrapper de input
	 * @return El wrapper de output
	 */
	public VortexWrapper process(final VortexWrapper wrapperRecibido) {
		final RemoteSession session = getOrCreateSessionFor(wrapperRecibido);

		// Primero enviamos los mensajes que envía el cliente
		session.enviarAlNodo(wrapperRecibido.getMensajes());

		// Después recolectamos los que tenemos que devolverle
		final VortexWrapper wrapperEnviado = session.recibirDelNodo();

		// Extendemos la vida de la sesión
		extenderSesion(session, wrapperRecibido, wrapperEnviado);
		return wrapperEnviado;
	}

	/**
	 * Extiende la duración de la sesión tomando en cuenta el tiempo pedido en el wrapper
	 * 
	 * @param session
	 *            La sesión a extender
	 * @param wrapperRecibido
	 *            El wrapper con la extensión solicitada
	 * @param wrapperEnviado
	 *            El wrapper al que se debe indicar la extensión otorgada
	 */
	private void extenderSesion(final RemoteSession session, final VortexWrapper wrapperRecibido,
			final VortexWrapper wrapperEnviado) {
		Long extensionEnSegundos = wrapperRecibido.getExtensionDeSesion();
		if (extensionEnSegundos == null) {
			extensionEnSegundos = DURACION_DEFAULT_DE_SESION_EN_SEGUNDOS;
		} else if (extensionEnSegundos < DURACION_MINIMA_DE_SESION_EN_SEGUNDOS) {
			extensionEnSegundos = DURACION_MINIMA_DE_SESION_EN_SEGUNDOS;
		} else if (extensionEnSegundos > DURACION_MAXIMA_DE_SESION_EN_SEGUNDOS) {
			extensionEnSegundos = DURACION_MAXIMA_DE_SESION_EN_SEGUNDOS;
		}
		session.extenderVencimiento(extensionEnSegundos);
		wrapperEnviado.setExtensionDeSesion(extensionEnSegundos);
	}

	/**
	 * Devuelve la sesión asociada al ID indicado en el wrapper
	 * 
	 * @param wrapper
	 *            El wrapper recibido
	 * @return La sesión referida por ID o una nueva creada si no tenía ID
	 */
	@HasDependencyOn(Decision.SE_REQUIERE_SESION_SI_SE_USA_ID_O_SI_SE_USAN_METAMENSAJES)
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
				remoteSession = createSession();
				LOG.debug("Sesión remota[{}] creada para el wrapper:[{}]", remoteSession.getSessionId(), wrapper);
			}
		}
		return remoteSession;
	}

	/**
	 * Crea una nueva sesión registrándola en las sesiones de este controlador
	 * 
	 * @return La nueva sesión creada
	 */
	public RemoteSessionImpl createSession() {
		final RemoteSessionImpl sesionCreada = createRemoteSession();
		final Long idDeLaSesionCreada = sesionCreada.getSessionId();
		remoteSessions.put(idDeLaSesionCreada, sesionCreada);
		return sesionCreada;
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

	/**
	 * Elimina las sesiones remotas que no tuvieron actividad en el último período dentro de un
	 * lapso razonable
	 */
	public void removeOldRemoteSessions() {
		LOG.debug("Iniciando limpieza de sesiones viejas");
		final Collection<RemoteSessionImpl> allRemoteSessions = remoteSessions.values();
		final Iterator<RemoteSessionImpl> iterator = allRemoteSessions.iterator();

		while (iterator.hasNext()) {
			final RemoteSessionImpl remoteSession = iterator.next();
			if (remoteSession.estaVencida()) {
				LOG.info("Eliminando sesión vieja: {}", remoteSession);
				iterator.remove();
				remoteSession.cerrar();
			}
		}
	}
}
