/**
 * 06/07/2012 00:15:28 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.impl.helpers;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Esta clase permite obtener un número de instancia único para cada componente creado
 * 
 * @author D. García
 */
public class SecuenciadorDeInstancias {

	private static final AtomicLong proximoDisponible = new AtomicLong(0);

	/**
	 * Devuelve un número secuencial para asignar a una instancia con el próximo número disponible
	 * 
	 * @return El número a asignar en la instancia creada
	 */
	public static long getProximoNumero() {
		return proximoDisponible.getAndIncrement();
	}
}
