/**
 * 25/07/2012 22:31:52 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.http.sesiones;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import net.gaia.vortex.http.external.json.JacksonHttpTextualizer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Esta clase implementa el administrador de sesiones manteniendo el estado en memoria
 * 
 * @author D. García
 */
public class AdministradorEnMemoria implements AdministradorDeSesiones {
	private static final Logger LOG = LoggerFactory.getLogger(AdministradorEnMemoria.class);

	private ConcurrentHashMap<String, SesionVortexHttp> sesionesPorId;
	private AtomicLong proximoId;
	private ListenerDeSesionesHttp listener;
	private VortexHttpTextualizer textualizer;

	/**
	 * @see net.gaia.vortex.http.sesiones.AdministradorDeSesiones#getSesion(java.lang.String)
	 */
	@Override
	public SesionVortexHttp getSesion(final String sessionId) {
		final SesionVortexHttp sesion = sesionesPorId.get(sessionId);
		return sesion;
	}

	/**
	 * @see net.gaia.vortex.http.sesiones.AdministradorDeSesiones#crearNuevaSesion()
	 */
	@Override
	public SesionVortexHttp crearNuevaSesion() {
		final long nuevoId = proximoId.getAndIncrement();
		final String nuevoIdDeSesion = String.format("%1$04d", nuevoId);
		final SesionEnMemoria sesion = SesionEnMemoria.create(nuevoIdDeSesion, textualizer);
		sesionesPorId.put(nuevoIdDeSesion, sesion);
		listener.onSesionCreada(sesion);
		return sesion;
	}

	/**
	 * @see net.gaia.vortex.http.sesiones.AdministradorDeSesiones#eliminarSesion(net.gaia.vortex.http.sesiones.SesionVortexHttp)
	 */
	@Override
	public void eliminarSesion(final SesionVortexHttp sesion) {
		listener.onSesionDestruida(sesion);
		final String idDeSesion = sesion.getIdDeSesion();
		final SesionVortexHttp eliminada = sesionesPorId.remove(idDeSesion);
		if (eliminada == null) {
			LOG.error("Se eliminó una sesion[{}] que no esta registrada por ID", sesion);
			return;
		}
	}

	public static AdministradorEnMemoria create(final ListenerDeSesionesHttp listener) {
		final AdministradorEnMemoria administrador = new AdministradorEnMemoria();
		administrador.sesionesPorId = new ConcurrentHashMap<String, SesionVortexHttp>();
		administrador.proximoId = new AtomicLong(1);
		administrador.listener = listener;
		administrador.textualizer = JacksonHttpTextualizer.create();
		return administrador;
	}
}
