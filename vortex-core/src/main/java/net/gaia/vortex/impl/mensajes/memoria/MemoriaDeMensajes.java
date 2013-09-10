/**
 * 01/09/2012 01:29:11 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.impl.mensajes.memoria;

import net.gaia.vortex.api.mensajes.MensajeVortex;

/**
 * Esta interfaz representa un registro de mensajes que permite indicar si un mensaje es duplicado
 * respecto de un conjunto de referencia.<br>
 * Según la implementación cómo se maneja y determina ese conjunto de referencia
 * 
 * @author D. García
 */
public interface MemoriaDeMensajes {

	/**
	 * Registra el mensaje pasado como un nuevo mensaje sólo si no es encontrado en el grupo de
	 * referencia previo. Si el mensaje es reconocido como mensaje "viejo" no se agrega
	 * 
	 * @param mensaje
	 *            El mensaje a recordar
	 * @return true si el mensaje fue efectivamente agregado, false si ya existía en esta memoria
	 */
	boolean registrarNuevo(MensajeVortex mensaje);

	/**
	 * Indica si esta memoria tiene registro del mensaje recibido
	 * 
	 * @param mensaje
	 *            El mensje a evaluar
	 * @return true si el mensaje ya está registrado en esta memoria
	 */
	boolean tieneRegistroDe(MensajeVortex mensaje);
}
