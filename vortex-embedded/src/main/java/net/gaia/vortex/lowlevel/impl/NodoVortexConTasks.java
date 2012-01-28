/**
 * 27/11/2011 19:37:50 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.lowlevel.impl;

import java.util.concurrent.atomic.AtomicInteger;

import net.gaia.annotations.HasDependencyOn;
import net.gaia.taskprocessor.api.TaskProcessor;
import net.gaia.vortex.lowlevel.api.MensajeVortexHandler;
import net.gaia.vortex.lowlevel.api.NodoVortexEmbebido;
import net.gaia.vortex.lowlevel.api.SesionVortex;
import net.gaia.vortex.lowlevel.impl.receptores.NullReceptorVortex;
import net.gaia.vortex.lowlevel.impl.receptores.ReceptorVortexConSesion;
import net.gaia.vortex.lowlevel.impl.receptores.RegistroDeReceptores;
import net.gaia.vortex.lowlevel.impl.ruteos.MemoriaDeMensajesImpl;
import net.gaia.vortex.lowlevel.impl.ruteos.MemoriaDeRuteosImpl;
import net.gaia.vortex.lowlevel.impl.tags.ReporteCambioDeTags;
import net.gaia.vortex.lowlevel.impl.tags.SummarizerDeReceptores;
import net.gaia.vortex.lowlevel.impl.tags.TagChangeListener;
import net.gaia.vortex.lowlevel.impl.tasks.ComenzarProcesoDeMensajeWorkUnit;
import net.gaia.vortex.lowlevel.impl.tasks.NotificarCambiosDeTagsEnNodoWorkUnit;
import net.gaia.vortex.lowlevel.impl.tasks.ValidacionDeMensajeWorkUnit;
import net.gaia.vortex.meta.Decision;
import net.gaia.vortex.meta.Loggers;
import net.gaia.vortex.protocol.messages.MensajeVortex;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Objects;

/**
 * Esta clase representa el nodo vortex implementado en memoria con un {@link TaskProcessor}
 * 
 * @author D. García
 */
public class NodoVortexConTasks implements NodoVortexEmbebido {
	private static final Logger LOG = LoggerFactory.getLogger(NodoVortexConTasks.class);

	private RegistroDeReceptores registroReceptores;

	/**
	 * Procesador de las tareas internas del nodo
	 */
	private TaskProcessor procesador;

	private GeneradorMensajesDeNodo generadorMensajes;

	private MemoriaDeMensajes memoriaDeMensajes;
	private MemoriaDeRuteos memoriaDeRuteos;

	private ConfiguracionDeNodo configuracion;

	/**
	 * Receptor a utilizar cuando no existe uno en un mensaje
	 */
	private NullReceptorVortex sinEmisorIdentificado;

	/**
	 * Nombre opcional para identificar la instancia
	 */
	private String nombre;
	public static final String nombre_FIELD = "nombre";

	private static final AtomicInteger nameSequencer = new AtomicInteger(0);

	public MemoriaDeMensajes getMemoriaDeMensajes() {
		return memoriaDeMensajes;
	}

	public GeneradorMensajesDeNodo getGeneradorMensajes() {
		return generadorMensajes;
	}

	public TaskProcessor getProcesador() {
		return procesador;
	}

	public void setProcesador(final TaskProcessor procesador) {
		this.procesador = procesador;
	}

	@HasDependencyOn({ Decision.TODAVIA_NO_IMPLEMENTE_EL_GENERADOR_DE_MENSAJES,
			Decision.TODAVIA_NO_IMPLEMENTE_LA_MEMORIA_DE_MENSAJES })
	public static NodoVortexConTasks create(final TaskProcessor processor, final String nombreOpcional) {
		final NodoVortexConTasks nodo = new NodoVortexConTasks();
		nodo.registroReceptores = SummarizerDeReceptores.create(new TagChangeListener() {
			@Override
			public void onTagChanges(final ReporteCambioDeTags reporte) {
				// Si hay cambios en los tags les avisamos a los clientes interesados
				final NotificarCambiosDeTagsEnNodoWorkUnit notificacionGlobal = NotificarCambiosDeTagsEnNodoWorkUnit
						.create(nodo, reporte);
				nodo.getProcesador().process(notificacionGlobal);
			}
		});
		nodo.procesador = processor;
		nodo.generadorMensajes = GeneradorDeMensajesImpl.create();
		nodo.memoriaDeMensajes = MemoriaDeMensajesImpl.create();
		nodo.memoriaDeRuteos = MemoriaDeRuteosImpl.create();
		nodo.configuracion = ConfiguracionDeNodo.create();
		nodo.sinEmisorIdentificado = NullReceptorVortex.create();
		nodo.nombre = getValidNameFrom(nombreOpcional);
		return nodo;
	}

	/**
	 * Devuelve un nombre nuevo si el pasado es nulo. Se asigna un número secuencial para cada
	 * instancia
	 * 
	 * @param nombreOpcional
	 *            El nombre del nodo
	 * @return Un identificador válido o el pasado
	 */
	private static String getValidNameFrom(final String nombreOpcional) {
		if (nombreOpcional != null) {
			return nombreOpcional;
		}
		final int availableSecuence = nameSequencer.getAndIncrement();
		return String.valueOf(availableSecuence);
	}

	/**
	 * @see net.gaia.vortex.lowlevel.api.NodoVortexEmbebido#rutear(net.gaia.vortex.protocol.MensajeVortex)
	 */
	@Override
	public void rutear(final MensajeVortex mensajeVortex) {
		LOG.debug("Mensaje[{}] recibido sin receptor en nodo[{}]", mensajeVortex, this);
		Loggers.RUTEO.info("RECIBIDO mensaje[{}] sin receptor en nodo[{}]. Contenido: [{}]", new Object[] {
				mensajeVortex, this, mensajeVortex.toPrettyPrint() });

		final ComenzarProcesoDeMensajeWorkUnit comienzoDeProceso = ComenzarProcesoDeMensajeWorkUnit.create(
				mensajeVortex, sinEmisorIdentificado, this);
		getProcesador().process(comienzoDeProceso);
	}

	/**
	 * Comienza el proceso de ruteo de un mensaje recibido
	 * 
	 * @param nuevoRuteo
	 *            El contexto del ruteo a realizar
	 */
	protected void comenzarRuteo(final ContextoDeRuteoDeMensaje nuevoRuteo) {
		// Comenzamos con el paso de validación
		final ValidacionDeMensajeWorkUnit validacion = ValidacionDeMensajeWorkUnit.create(nuevoRuteo);
		getProcesador().process(validacion);
	}

	/**
	 * @see net.gaia.vortex.lowlevel.api.NodoVortexEmbebido#crearNuevaSesion(net.gaia.vortex.lowlevel.api.MensajeVortexHandler)
	 */
	@Override
	public SesionVortex crearNuevaSesion(final MensajeVortexHandler handlerDeMensajes) {
		final SesionVortexImpl sesion = SesionVortexImpl.create(handlerDeMensajes, this);
		final ReceptorVortexConSesion nuevoReceptor = sesion.getReceptorEmisor();
		LOG.debug("Creando nueva sesión para el receptor[{}] en el nodo[{}]", nuevoReceptor, this);
		registroReceptores.agregar(nuevoReceptor);
		return sesion;
	}

	public RegistroDeReceptores getRegistroReceptores() {
		return registroReceptores;
	}

	public ConfiguracionDeNodo getConfiguracion() {
		return configuracion;
	}

	/**
	 * Devuelve la instancia de la memoria de ruteos que registra los activos en este nodo
	 * 
	 * @return La memoria de los ruteos activos
	 */
	public MemoriaDeRuteos getMemoriaDeRuteos() {
		return memoriaDeRuteos;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return Objects.toStringHelper(this).add(nombre_FIELD, nombre).toString();
	}

	/**
	 * @see net.gaia.vortex.lowlevel.api.NodoVortexEmbebido#detenerYDevolverRecursos()
	 */
	@Override
	public void detenerYDevolverRecursos() {
		procesador.detener();
	}

	/**
	 * @see java.lang.Object#finalize()
	 */
	@Override
	protected void finalize() throws Throwable {
		detenerYDevolverRecursos();
		super.finalize();
	}
}
