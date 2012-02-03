/**
 * 01/02/2012 20:24:19 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.http.tasks.contexts;

import net.gaia.taskprocessor.api.TaskProcessor;
import net.gaia.vortex.dependencies.json.InterpreteJson;
import net.gaia.vortex.http.NodoRemotoHttp;
import net.gaia.vortex.http.externals.http.ConectorHttp;
import net.gaia.vortex.http.sessions.SesionConId;

/**
 * Esta clase representa el contexto de una tarea del nodo remoto por http
 * 
 * @author D. García
 */
public class ContextoDeOperacionHttp {

	private NodoRemotoHttp nodo;
	private SesionConId sesion;

	public static ContextoDeOperacionHttp create(final NodoRemotoHttp nodo, final SesionConId sesion) {
		final ContextoDeOperacionHttp contexto = new ContextoDeOperacionHttp();
		return contexto;
	}

	/**
	 * Devuelve el conector http a utilizar en este contexto
	 * 
	 * @return El conector del nodo
	 */
	public ConectorHttp getConectorHttp() {
		return nodo.getConector();
	}

	/**
	 * Devuelve el {@link TaskProcessor} del nodo
	 */
	public TaskProcessor getProcessor() {
		return nodo.getProcessor();
	}

	/**
	 * Devuelve el intérprete de JSON del nodo
	 * 
	 * @return El intérprete del nodo de este contexto
	 */
	public InterpreteJson getInterpreteJson() {
		return nodo.getInterpreteJson();
	}

	/**
	 * Devuelve la sesión que origina el envío de los mensajes
	 * 
	 * @return La sesión que envia los mensajes al nodo
	 */
	public SesionConId getSesionInvolucrada() {
		return sesion;
	}

}
