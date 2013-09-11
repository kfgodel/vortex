/**
 * 19/08/2013 21:12:39 Copyright (C) 2013 Darío L. García
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
package net.gaia.vortex.api.flujos;

import net.gaia.vortex.api.basic.Receptor;
import net.gaia.vortex.api.basic.emisores.Conectable;
import net.gaia.vortex.impl.nulos.ConectorNulo;
import net.gaia.vortex.impl.nulos.ReceptorNulo;

/**
 * Esta interfaz representa una secuencia de componentes vortex interconectados para generar un ruta
 * de mensajes posibles.<br>
 * A través de esta interfaz se abstrae la complejidad y el tipo de componentes utilizados
 * 
 * @author D. García
 */
public interface FlujoVortex {

	/**
	 * Devuelve el componente que sirve como punto de entrada de los mensajes en este flujo.<br>
	 * 
	 * @return El receptor a utilizar como punto de entrada. Un flujo no inicializado puede devolver
	 *         el {@link ReceptorNulo}
	 */
	Receptor getEntrada();

	/**
	 * Devuelve el componente que sirve como punto de salida de los mensajes en este flujo
	 * 
	 * @return El conector a utilizar para los mensajes de salida. Puede ser el {@link ConectorNulo}
	 *         si el flujo no está inicializado
	 */
	Conectable getSalida();

}
