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
package net.gaia.vortex.http.impl.server.sesiones;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import net.gaia.taskprocessor.api.SubmittedTask;
import net.gaia.taskprocessor.api.WorkUnit;
import net.gaia.taskprocessor.api.processor.TaskProcessor;
import net.gaia.vortex.http.external.json.JacksonHttpTextualizer;
import net.gaia.vortex.http.external.json.VortexHttpTextualizer;
import net.gaia.vortex.http.sesiones.ListenerDeSesionesHttp;
import net.gaia.vortex.http.sesiones.SesionVortexHttpEnServer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ar.com.dgarcia.lang.time.TimeMagnitude;

/**
 * Esta clase implementa el administrador de sesiones manteniendo el estado en memoria
 * 
 * @author D. García
 */
public class AdministradorServerEnMemoria implements AdministradorDeSesionesServer {
	/**
	 * El proceso de limpieza corre cada un minuto
	 */
	public static final TimeMagnitude ESPERA_ENTRE_LIMPIEZAS = TimeMagnitude.of(15, TimeUnit.SECONDS);

	private static final Logger LOG = LoggerFactory.getLogger(AdministradorServerEnMemoria.class);

	private ConcurrentHashMap<String, SesionVortexHttpEnServer> sesionesPorId;
	private AtomicLong proximoId;
	private ListenerDeSesionesHttp listener;
	private VortexHttpTextualizer textualizer;
	private volatile boolean detenido = false;

	private TaskProcessor processor;
	private final WorkUnit tareaDeLimpiezaDeSesiones = new WorkUnit() {

		@Override
		public WorkUnit doWork() throws InterruptedException {
			if (detenido) {
				// No hacemos nada si está detenido el administrador
				return null;
			}
			onLimpiezaDeSesiones();
			return null;
		}
	};

	private SubmittedTask controlDeTareaDeLimpieza;

	/**
	 * @see net.gaia.vortex.http.impl.server.sesiones.AdministradorDeSesionesServer#getSesion(java.lang.String)
	 */

	@Override
	public SesionVortexHttpEnServer getSesion(final String sessionId) {
		final SesionVortexHttpEnServer sesion = sesionesPorId.get(sessionId);
		return sesion;
	}

	/**
	 * @see net.gaia.vortex.http.impl.server.sesiones.AdministradorDeSesionesServer#crearNuevaSesion()
	 */

	@Override
	public SesionVortexHttpEnServer crearNuevaSesion() {
		final long nuevoId = proximoId.getAndIncrement();
		final String nuevoIdDeSesion = String.format("%1$04d", nuevoId);
		final SesionServerEnMemoria sesion = SesionServerEnMemoria.create(nuevoIdDeSesion, textualizer);
		sesionesPorId.put(nuevoIdDeSesion, sesion);
		listener.onSesionCreada(sesion);
		return sesion;
	}

	/**
	 * @see net.gaia.vortex.http.impl.server.sesiones.AdministradorDeSesionesServer#eliminarSesion(net.gaia.vortex.http.sesiones.SesionVortexHttpEnServer)
	 */

	@Override
	public void eliminarSesion(final SesionVortexHttpEnServer sesion) {
		listener.onSesionDestruida(sesion);
		final String idDeSesion = sesion.getIdDeSesion();
		final SesionVortexHttpEnServer eliminada = sesionesPorId.remove(idDeSesion);
		if (eliminada == null) {
			LOG.error("Se eliminó una sesion[{}] que no esta registrada por ID", sesion);
			return;
		}
	}

	public static AdministradorServerEnMemoria create(final ListenerDeSesionesHttp listener,
			final TaskProcessor processor) {
		final AdministradorServerEnMemoria administrador = new AdministradorServerEnMemoria();
		administrador.sesionesPorId = new ConcurrentHashMap<String, SesionVortexHttpEnServer>();
		administrador.proximoId = new AtomicLong(1);
		administrador.listener = listener;
		administrador.textualizer = JacksonHttpTextualizer.create();
		administrador.processor = processor;
		administrador.planificarProximaLimpieza();
		return administrador;
	}

	/**
	 * Planifica a futuro la limpieza de sesiones viejas
	 */
	private void planificarProximaLimpieza() {
		controlDeTareaDeLimpieza = this.processor.processDelayed(ESPERA_ENTRE_LIMPIEZAS, tareaDeLimpiezaDeSesiones);
	}

	/**
	 * Invocado periódicamente para limpiar las sesiones viejas en este administrador
	 */
	protected void onLimpiezaDeSesiones() {
		limpiarSesionesViejas();
		planificarProximaLimpieza();
	}

	/**
	 * Elimina de este administrador las sesiones que superaron el tiempo de espera máximo y no han
	 * tenido actividad
	 */
	private void limpiarSesionesViejas() {
		final Collection<SesionVortexHttpEnServer> allSesiones = sesionesPorId.values();
		for (final SesionVortexHttpEnServer sesionVortexHttp : allSesiones) {
			if (sesionVortexHttp.esVieja()) {
				LOG.info("Eliminando por antiguedad la sesión[{}]", sesionVortexHttp);
				// Es seguro eliminar mientras se itera porque la colección es concurrente
				this.eliminarSesion(sesionVortexHttp);
			}
		}
	}

	/**
	 * @see net.gaia.vortex.http.impl.server.sesiones.AdministradorDeSesionesServer#cerrarYLiberarRecursos()
	 */

	@Override
	public void cerrarYLiberarRecursos() {
		detenido = true;
		// Detiene la tarea de limpieza de sesiones
		controlDeTareaDeLimpieza.cancelExecution(true);
	}
}
