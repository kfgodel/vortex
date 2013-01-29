/**
 * 24/12/2012 15:17:47 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.router.impl.filtros;

import net.gaia.vortex.core.api.condiciones.Condicion;

/**
 * Esta interfaz representa un listener que es invocado cuando se producen cambios en el conjunto de
 * condiciones
 * 
 * @author D. García
 */
public interface ListenerDeConjuntoDeCondiciones {

	/**
	 * Invocado cuando se produce un cambio en alguna de las partes del conjunto que genera un
	 * cambio en la condición global que representa el conjunto
	 * 
	 * @param conjunto
	 *            El conjunto modificado
	 * @param nuevaCondicion
	 *            La nueva condición resultante de los cambios
	 */
	void onCambioDeCondicionEn(ConjuntoDeCondiciones conjunto, Condicion nuevaCondicion);

}
