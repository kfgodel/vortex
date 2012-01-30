/**
 * 27/11/2011 22:07:01 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.lowlevel.impl.mensajes;

import net.gaia.vortex.lowlevel.impl.envios.MensajesEnEspera;
import net.gaia.vortex.protocol.messages.MensajeVortex;

/**
 * Esta interfaz representa la memoria de un nodo para recordar mensajes recibidos. Esta memoria
 * permite detectar mensajes duplicados
 * 
 * @author D. García
 */
public interface MemoriaDeMensajes {

	/**
	 * Registra el mensaje pasado en esta memoria, solo si no existe previamente
	 * 
	 * @param mensaje
	 *            El mensaje a evaluar
	 * @return true si esta memoria tiene registro anterior del mensaje
	 */
	public abstract boolean registrarSiNoRecuerdaA(MensajeVortex mensaje);

	/**
	 * Devuelve los mensajes registrados que requieren una confirmación de consumo
	 * 
	 * @return Los mensajes que esperan confirmación de consumo
	 */
	MensajesEnEspera getEsperandoAcuseDeConsumo();

}
