/**
 * 29/07/2012 15:48:06 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.http.impl.cliente.sesiones;

import java.util.concurrent.ConcurrentLinkedQueue;

import net.gaia.taskprocessor.api.TaskProcessor;
import net.gaia.taskprocessor.api.WorkUnit;
import net.gaia.vortex.http.external.json.JacksonHttpTextualizer;
import net.gaia.vortex.http.sesiones.ListenerDeSesionesHttp;
import net.gaia.vortex.http.sesiones.SesionClienteEnMemoria;
import net.gaia.vortex.http.sesiones.SesionVortexHttp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Esta clase representa el administrador de sesiones del cliente en memoria
 * 
 * @author D. García
 */
public class AdministradorClienteEnMemoria implements AdministradorDeSesionesCliente {
	private static final Logger LOG = LoggerFactory.getLogger(AdministradorClienteEnMemoria.class);

	private ListenerDeSesionesHttp listener;
	private JacksonHttpTextualizer textualizer;
	private TaskProcessor processor;
	private ConcurrentLinkedQueue<SesionVortexHttp> sesiones;

	private final boolean detenido = false;
	private final WorkUnit tareaDeActualizacionDeSesiones = new WorkUnit() {
		@Override
		public WorkUnit doWork() throws InterruptedException {
			if (detenido) {
				// No hacemos nada si está detenido el administrador
				return null;
			}
			onActualizacionDeSesiones();
			return null;
		}
	};

	/**
	 * @see net.gaia.vortex.http.impl.cliente.sesiones.AdministradorDeSesionesCliente#crearSesion(java.lang.String)
	 */
	@Override
	public SesionVortexHttp crearSesion(final String idDeSesionCreada) {
		final SesionClienteEnMemoria nuevaSesion = SesionClienteEnMemoria.create(idDeSesionCreada);
		sesiones.add(nuevaSesion);
		listener.onSesionCreada(nuevaSesion);
		return nuevaSesion;
	}

	/**
	 * 
	 */
	protected void onActualizacionDeSesiones() {
		// TODO Auto-generated method stub

	}

	/**
	 * @see net.gaia.vortex.http.impl.cliente.sesiones.AdministradorDeSesionesCliente#cerrarSesion(net.gaia.vortex.http.sesiones.SesionVortexHttp)
	 */
	@Override
	public void cerrarSesion(final SesionVortexHttp sesionCerrada) {
		listener.onSesionDestruida(sesionCerrada);
		final boolean removed = sesiones.remove(sesionCerrada);
		if (!removed) {
			LOG.error("Se intentó quitar la sesion[{}] y no existe en este administrador[{}]", sesionCerrada, this);
			return;
		}
	}

	public static AdministradorClienteEnMemoria create(final ListenerDeSesionesHttp listener,
			final TaskProcessor processor) {
		final AdministradorClienteEnMemoria administrador = new AdministradorClienteEnMemoria();
		administrador.sesiones = new ConcurrentLinkedQueue<SesionVortexHttp>();
		administrador.listener = listener;
		administrador.textualizer = JacksonHttpTextualizer.create();
		administrador.processor = processor;
		administrador.planificarProximaActualizacion();
		return administrador;
	}

	/**
	 * Planifica la ejecución de la próxima actualización de sesiones
	 */
	private void planificarProximaActualizacion() {
		this.processor.processDelayed(ESPERA_ENTRE_LIMPIEZAS, tareaDeActualizacionDeSesiones);
	}
}
