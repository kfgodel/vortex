/**
 * 14/02/2012 21:42:02 Copyright (C) 2011 Darío L. García
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

import net.gaia.vortex.dependencies.json.InterpreteJson;
import net.gaia.vortex.http.externals.http.ConectorHttp;

/**
 * Esta interfaz representa el contrato que tiene que respetar un obejto configuracion del nodo
 * remoto con http
 * 
 * @author D. García
 */
public interface ConfiguracionDeNodoRemotoHttp {

	/**
	 * Devuelve el conector que debe utilizar el nodo para enviar y recibir mensajes vortex
	 * 
	 * @param interprete
	 *            El interprete para convertir los objetos mensajes
	 * 
	 * @return El conector creado de acuerdo a esta configuración
	 */
	ConectorHttp getConectorHttp(InterpreteJson interprete);

	/**
	 * Devuelve la cantidad de segundos que una sesión puede permanecer sin actividad hasta ser
	 * eliminada por el nodo. Este valor es el inicial y puede negociarse por sesión
	 * 
	 * @return El valor inicial default para las sesiones creadas en el nodo
	 */
	public Long getMaximaCantidadDeSegundosSinActividad();

	/**
	 * Devuelve la cantidad de segundos que se debe esperar entre request de polling para solicitar
	 * nuevos mensajes al nodo http.<br>
	 * Si el valor es null indica que no se usa polling automático
	 * 
	 * @return El tiempo de período de polling o null
	 */
	public Long getPeriodoDePollingEnSegundos();
}
