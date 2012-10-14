/**
 * 13/10/2012 22:10:21 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.tests.router.impl.patas.filtros;

import java.util.Collection;

import net.gaia.vortex.tests.router.Mensaje;

/**
 * Esta interfaz representa un filtro para los mensajes
 * 
 * @author D. García
 */
public interface Filtro {

	/**
	 * Indica si este filtro acepta el mensaje pasado
	 * 
	 * @param mensaje
	 *            El mensaje a evaluar
	 * @return true si este filtro deja pasar el mensaje
	 */
	public boolean aceptaA(Mensaje mensaje);

	/**
	 * Indica si este filtro contiene los valores pasados
	 * 
	 * @param filtros
	 *            Valores esperados
	 * @return true si este filtro contiene a todos los valores esperados
	 */
	public boolean usaA(Collection<String> filtros);
}
