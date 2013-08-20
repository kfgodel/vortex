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

import net.gaia.vortex.api.basic.Nodo;
import net.gaia.vortex.api.proto.Conector;
import net.gaia.vortex.core.api.annotations.Atomo;
import net.gaia.vortex.core.api.condiciones.Condicion;

/**
 * Esta interfaz representa un atomo vortex que bifurca el camino del mensaje en función de una
 * condición aplicada sobre él.<br>
 * Si la condición es verdadera seguirá un camino, y si es falsa otro. Es descartado si la
 * evaluación de la condición falla
 * 
 * @author D. García
 */
@Atomo
public interface Bifurcador extends Nodo {

	/**
	 * Devuelve el conector utilizado utilizado en caso de true.<br>
	 * Los mensajes que evalúen a true serán entregados al receptor conectado a este conector.
	 * 
	 * @return El conector por casos true
	 */
	public Conector getConectorPorTrue();

	/**
	 * Devuelve el conector utilizado para el caso false.<br>
	 * Los mensajes evaluados a false serán entregados al receptor conectado a este conector.
	 * 
	 * @return El conector para casos false
	 */
	public Conector getConectorPorFalse();

	/**
	 * Devuelve la condición utilizada por este componente para evaluar los mensajes y decidir su
	 * conector destino
	 * 
	 * @return La condición evaluada en cada mensaje recibido
	 */
	public Condicion getCondicion();

	/**
	 * Establece la condición utilizada por este filtro para aceptar los mensajes y pasarlos al
	 * receptor que corresponda. Si la condición pasada devuelve true el mensaje es entregado al
	 * receptor del conector {@link #getConectorPorTrue()}, si es false, es entregada al conector de
	 * {@link #getConectorPorFalse()}. Si falla o es incierta, se descarta el mensaje enviandolo al
	 * receptor nulo
	 * 
	 * @param condicion
	 *            La condición a utilizar
	 */
	public void setCondicion(Condicion condicion);

}
