/**
 * 11/05/2012 01:12:54 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.bluevortex.api;

import net.gaia.vortex.bluevortex.api.async.AsyncValue;

/**
 * Esta interfaz representa un reporte de entrega de un mensaje enviado a partir del cual puede
 * conocerse si el mensaje fue entregado y a cuantos
 * 
 * @author D. García
 */
public interface ReporteDeEntrega {

	/**
	 * Devuelve la cantidad de mensajes entregados a interesados como tarea a ser resuelta a futuro.<br>
	 * El future devolverá un valor cuando el proceso de ruteo termine y el número sea conocido
	 * (entregado a todos los posibles conectados interesados)
	 * 
	 * @return El future que devolverá el valor cuando se conozca y mientras permite esperarlo
	 */
	AsyncValue<Long> getCantidadDeEntregados();

}
