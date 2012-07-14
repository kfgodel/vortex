/**
 * 11/07/2012 19:42:51 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.android.service;

import net.gaia.taskprocessor.api.TaskProcessor;
import net.gaia.vortex.core.api.Nodo;
import net.gaia.vortex.core.impl.atomos.ids.MultiplexorIdentificador;

/**
 * Esta interfaz define los puntos de entrada y de uso principales de vortex desde una aplicación
 * android.<br>
 * La implementación de esta interfaz ofrecerá métodos para que la aplicación android acceda a los
 * conectores de vortex que funcionan en background
 * 
 * @author D. García
 */
public interface VortexAndroidAccess {

	/**
	 * Devuelve el nodo central administrado por el servicio en background, al cual se le pueden
	 * conectar más nodos para agregar funcionalidad.<br>
	 * El nodo es una instancia de {@link MultiplexorIdentificador} que identifica los mensajes
	 * recibidos evitando duplicados
	 * 
	 * @return El nodo central de vortex
	 */
	public Nodo getNodoCentral();

	/**
	 * El procesador compartido por los componentes vortex interconectados a este punto de acceso
	 * 
	 * @return El procesador a utilizar para los componentes
	 */
	public TaskProcessor getProcesadorCentral();
}
