/**
 * 01/09/2012 12:55:21 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.core.api.memoria;

import net.gaia.vortex.core.api.mensaje.MensajeVortex;

/**
 * Esta interfaz es aplicable a componentes que tiene un registro interno de los mensajes que
 * recibieron y pueden indicar si es redundante que le enviemos un mensaje nuevamente
 * 
 * @author D. García
 */
public interface ComponenteConMemoria {

	/**
	 * Indica si este componente tiene registro previo del mensaje pasado y por lo tanto no debe
	 * enviársele.<br>
	 * Es posible que por limitaciones de memoria no exista registro de recepcion previa aunque ya
	 * haya ocurrido
	 * 
	 * @param mensaje
	 *            El mensaje a evaluar
	 * @return true si este componente ya recibió el mensaje y tiene registro
	 */
	boolean yaRecibio(MensajeVortex mensaje);

}
