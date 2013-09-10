/**
 * 25/01/2013 16:03:06 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.sets.impl.condiciones;

import net.gaia.vortex.api.condiciones.Condicion;

/**
 * Esta interfaz es aplicable a las condiciones que pueden simplificar su expresión eliminando
 * miembros innecesarios.<br>
 * 
 * @author D. García
 */
public interface Simplificable {

	/**
	 * Devuelve una versión simplificada de esta condición (si es posible) eliminando términos
	 * internos innecesarios
	 * 
	 * @return Una versión simplificada o sí misma si no es posible
	 */
	Condicion simplificar();
}
