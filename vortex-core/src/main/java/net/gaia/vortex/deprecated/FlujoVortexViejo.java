/**
 * 13/01/2013 19:36:18 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.deprecated;

import net.gaia.vortex.api.basic.Receptor;

/**
 * Esta interfaz representa una secuencia de componentes que procesa los mensajes que recibe en una
 * entrada, y los envía en una salida. El flujo es utilizado por las moléculas para definir su red
 * interna de atomos.<br>
 * <br>
 * A través de los flujos se pueden crear procesos de transformación de mensajes o definir
 * comportamiento como composición de componentes vortex
 * 
 * @author D. García
 */
@Deprecated
public interface FlujoVortexViejo {

	/**
	 * Devuelve el componente que sirve como punto de entrada de los mensajes en este flujo.<br>
	 * 
	 * @return El receptor a utilizar como punto de entrada.Un flujo no inicializado puede devolver
	 *         el receptor nulo
	 */
	Receptor getEntrada();

	/**
	 * Devuelve el componente que sirve como punto de salida de los mensajes en este flujo
	 * 
	 * @return El emisor para los mensajes de salidad. Puede ser el emisor nulo si el flujo no está
	 *         inicializado
	 */
	EmisorViejo getSalida();
}
