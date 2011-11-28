/**
 * 27/11/2011 21:39:47 Copyright (C) 2011 Darío L. García
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

import net.gaia.taskprocessor.api.TaskProcessor;
import net.gaia.vortex.protocol.MensajeVortexEmbebido;

/**
 * Esta clase representa la inforamción de contexto necesaria para las tareas de ruteo de mensajes.
 * A través de esta instancia las tareas pueden pasar el contexto necesario para realizar el ruteo.<br>
 * Cada tarea toma de este contexto los objetos que necesite
 * 
 * @author D. García
 */
public class ContextoDeRuteoDeMensaje {

	private NodoVortexConTasks nodo;
	/**
	 * El receptor que es emisor del mensaje
	 */
	private ReceptorVortex emisor;

	/**
	 * El mensaje a rutear
	 */
	private MensajeVortexEmbebido mensaje;

	public TaskProcessor getProcesador() {
		return nodo.getProcesador();
	}

	public ReceptorVortex getEmisor() {
		return emisor;
	}

	public void setEmisor(final ReceptorVortex emisor) {
		this.emisor = emisor;
	}

	public MensajeVortexEmbebido getMensaje() {
		return mensaje;
	}

	public void setMensaje(final MensajeVortexEmbebido mensaje) {
		this.mensaje = mensaje;
	}

	public static ContextoDeRuteoDeMensaje create(final MensajeVortexEmbebido mensaje, final ReceptorVortex emisor,
			final NodoVortexConTasks nodo) {
		final ContextoDeRuteoDeMensaje contexto = new ContextoDeRuteoDeMensaje();
		contexto.emisor = emisor;
		contexto.mensaje = mensaje;
		contexto.nodo = nodo;
		return contexto;
	}

	public GeneradorMensajesDeNodo getGeneradorMensajes() {
		return nodo.getGeneradorMensajes();
	}

	public MemoriaDeMensajes getMemoriaDeMensajes() {
		return nodo.getMemoriaDeMensajes();
	}

	/**
	 * Devuelve el registro del nodo acerca de los receptores que posee
	 * 
	 * @return El registro de receptores del nodo
	 */
	public RegistroDeReceptores getRegistroDeReceptoresDelNodo() {
		return nodo.getRegistroReceptores();
	}

}
