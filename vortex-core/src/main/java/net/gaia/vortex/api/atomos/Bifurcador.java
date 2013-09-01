/**
 * 19/08/2013 20:07:09 Copyright (C) 2013 Darío L. García
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
package net.gaia.vortex.api.atomos;

import net.gaia.vortex.api.annotations.clases.Atomo;
import net.gaia.vortex.api.condiciones.Condicion;
import net.gaia.vortex.api.proto.Conector;

/**
 * Esta interfaz representa un atomo vortex que bifurca el camino del mensaje en función de una
 * {@link Condicion} aplicada sobre él.<br>
 * Si la condición es verdadera seguirá un camino, y si es falsa otro. Es descartado si la
 * evaluación de la condición falla
 * 
 * @author D. García
 */
@Atomo
public interface Bifurcador extends Filtro {

	/**
	 * Devuelve el conector utilizado para el caso false.<br>
	 * Los mensajes evaluados a false serán entregados al receptor conectado a este conector.
	 * 
	 * @return El conector para casos false
	 */
	public Conector getConectorPorFalse();

}
