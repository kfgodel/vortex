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

import net.gaia.annotations.HasDependencyOn;
import net.gaia.taskprocessor.api.TaskProcessor;
import net.gaia.vortex.lowlevel.api.MensajeVortexHandler;
import net.gaia.vortex.lowlevel.api.NodoVortexEmbebido;
import net.gaia.vortex.lowlevel.api.SesionVortex;
import net.gaia.vortex.meta.Decision;
import net.gaia.vortex.protocol.MensajeVortexEmbebido;

/**
 * Esta clase representa el nodo vortex implementado en memoria con un {@link TaskProcessor}
 * 
 * @author D. García
 */
public class NodoVortexConTasks implements NodoVortexEmbebido {

	private RegistroDeReceptores registroReceptores;

	/**
	 * Procesador de las tareas internas del nodo
	 */
	private TaskProcessor procesador;

	private GeneradorMensajesDeNodo generadorMensajes;

	private MemoriaDeMensajes memoriaDeMensajes;

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
	public static NodoVortexConTasks create(final TaskProcessor processor) {
		final NodoVortexConTasks nodo = new NodoVortexConTasks();
		nodo.registroReceptores = RegistroDeReceptores.create();
		nodo.procesador = processor;
		nodo.generadorMensajes = null;
		nodo.memoriaDeMensajes = null;
		return nodo;
	}

	/**
	 * @see net.gaia.vortex.lowlevel.api.NodoVortexEmbebido#rutear(net.gaia.vortex.protocol.MensajeVortexEmbebido)
	 */
	public void rutear(final MensajeVortexEmbebido mensajeVortex) {
		// TODO Auto-generated method stub

	}

	/**
	 * @see net.gaia.vortex.lowlevel.api.NodoVortexEmbebido#crearNuevaSesion(net.gaia.vortex.lowlevel.api.MensajeVortexHandler)
	 */
	public SesionVortex crearNuevaSesion(final MensajeVortexHandler handlerDeMensajes) {
		final ReceptorVortex nuevoReceptor = ReceptorVortex.create(handlerDeMensajes);
		final SesionVortexImpl sesion = SesionVortexImpl.create(nuevoReceptor);
		registroReceptores.agregar(nuevoReceptor);
		return sesion;
	}

	public RegistroDeReceptores getRegistroReceptores() {
		return registroReceptores;
	}

}
