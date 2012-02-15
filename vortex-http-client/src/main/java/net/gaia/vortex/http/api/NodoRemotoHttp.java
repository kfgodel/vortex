/**
 * 01/02/2012 19:10:13 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.http.api;

import net.gaia.taskprocessor.api.TaskProcessor;
import net.gaia.taskprocessor.api.TaskProcessorConfiguration;
import net.gaia.taskprocessor.impl.ExecutorBasedTaskProcesor;
import net.gaia.vortex.dependencies.json.InterpreteJson;
import net.gaia.vortex.dependencies.json.impl.InterpreteJackson;
import net.gaia.vortex.http.externals.http.ConectorHttp;
import net.gaia.vortex.http.sessions.SesionRemotaHttp;
import net.gaia.vortex.http.sessions.SinSesionRemotaHttp;
import net.gaia.vortex.http.tasks.ValidarMensajesPrevioEnvioWorkUnit;
import net.gaia.vortex.http.tasks.contexts.ContextoDeOperacionHttp;
import net.gaia.vortex.lowlevel.api.MensajeVortexHandler;
import net.gaia.vortex.lowlevel.api.NodoVortex;
import net.gaia.vortex.lowlevel.api.SesionVortex;
import net.gaia.vortex.lowlevel.impl.nodo.NodoVortexConTasks;
import net.gaia.vortex.protocol.messages.MensajeVortex;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Objects;

/**
 * Esta clase representa un nodo vortex remoto con
 * 
 * @author D. García
 */
public class NodoRemotoHttp implements NodoVortex {
	private static final Logger LOG = LoggerFactory.getLogger(NodoRemotoHttp.class);

	private TaskProcessor processor;
	private SinSesionRemotaHttp sinSesion;
	private InterpreteJson interprete;

	private String nombre;
	public static final String nombre_FIELD = "nombre";

	private ConectorHttp conector;
	public static final String conector_FIELD = "conector";

	/**
	 * @see net.gaia.vortex.lowlevel.api.NodoVortex#crearNuevaSesion(net.gaia.vortex.lowlevel.api.MensajeVortexHandler)
	 */
	@Override
	public SesionVortex crearNuevaSesion(final MensajeVortexHandler handlerDeMensajes) {
		final SesionRemotaHttp sesion = SesionRemotaHttp.create(this, handlerDeMensajes);
		LOG.debug("Nueva Sesión creada[{}] en nodo[{}]", sesion, this);
		return sesion;
	}

	/**
	 * @see net.gaia.vortex.lowlevel.api.NodoVortex#detenerYDevolverRecursos()
	 */
	@Override
	public void detenerYDevolverRecursos() {
		processor.detener();
	}

	/**
	 * @see net.gaia.vortex.lowlevel.api.NodoVortex#rutear(net.gaia.vortex.protocol.messages.MensajeVortex)
	 */
	@Override
	public void rutear(final MensajeVortex mensaje) {
		// Usamos la sesión que no tiene sesión real cuando se pide ruteo directo
		final ContextoDeOperacionHttp contexto = ContextoDeOperacionHttp.create(this, sinSesion);
		final ValidarMensajesPrevioEnvioWorkUnit validacion = ValidarMensajesPrevioEnvioWorkUnit.create(contexto,
				mensaje);
		getProcessor().process(validacion);
	}

	/**
	 * Crea un pseudo nodo vortex que se conecta a un nodo real por HTTP usando la URL indicada
	 * 
	 * @param url
	 *            La url a la que enviar requests con HTTP por los mensajes
	 * @param nombreOpcional
	 *            El nombre opcional para identificar a este nodo
	 * @return El nodo creado
	 */
	public static NodoRemotoHttp create(final ConfiguracionDeNodoRemotoHttp config, final String nombreOpcional) {
		final NodoRemotoHttp nodo = new NodoRemotoHttp();
		nodo.processor = ExecutorBasedTaskProcesor.create(TaskProcessorConfiguration.create());
		nodo.interprete = InterpreteJackson.create();
		nodo.nombre = NodoVortexConTasks.getValidNameFrom(nombreOpcional);
		nodo.conector = config.getConectorHttp(nodo.interprete);
		nodo.sinSesion = SinSesionRemotaHttp.create();
		return nodo;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return Objects.toStringHelper(this).add(nombre_FIELD, nombre).add(conector_FIELD, conector).toString();
	}

	public TaskProcessor getProcessor() {
		return processor;
	}

	/**
	 * Devuelve el intérprete Json usado en este nodo
	 * 
	 * @return El intérprete para conversiones de json
	 */
	public InterpreteJson getInterpreteJson() {
		return interprete;
	}

	/**
	 * Devuelve el conector HTTP de este nodo
	 * 
	 * @return El conector para utilizar en los envíos de requests
	 */
	public ConectorHttp getConector() {
		return conector;
	}

}
