/**
 * 14/06/2012 23:32:32 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.core.api.atomos.condicional;

import net.gaia.vortex.core.api.Nodo;
import net.gaia.vortex.core.api.annotations.Atomo;
import net.gaia.vortex.core.api.atomos.Receptor;
import net.gaia.vortex.core.api.condiciones.Condicion;

/**
 * Esta interfaz representa un componente vortex que elige uno entre dos caminos para el mensaje
 * basándose en una condicion. Elige el camino según el valor de la condicion al evaluar el mensaje
 * 
 * @author D. García
 */
@Atomo
public interface Bifurcador extends Nodo {

	/**
	 * Devuelve la condición utilizada por este bifurcador para elegir el camino del mensaje
	 * 
	 * @return La condición que si devuelve true el mensaje es enviado al receptor por true
	 */
	public Condicion getCondicion();

	/**
	 * Establece la condición utilizada por este componente para elegir el camino del mensaje
	 * 
	 * @param condicion
	 *            La condición a evaluar para cada mensaje
	 */
	public void setCondicion(Condicion condicion);

	/**
	 * Establece el receptor utilizado para enviarle los mensajes cuando la condición indica true
	 * 
	 * @param receptorPorTrue
	 *            El receptor que recibe los mensajes true
	 */
	public void setReceptorPorTrue(Receptor receptorPorTrue);

	/**
	 * Devuelve el receptor que recibe los mensajes evaluados en true por la condición
	 * 
	 * @return El receptor de mensajes
	 */
	public Receptor getReceptorPorTrue();

	/**
	 * Establece el receptor al que se le envian los mensajes cuando la condición indica false
	 * 
	 * @param receptorPorFalse
	 *            El receptor que recibe los mensajes por false
	 */
	public void setReceptorPorFalse(Receptor receptorPorFalse);

	/**
	 * Devuelve el receptor utilizado para los mensajes evaluados a false
	 * 
	 * @return El receptor que receibirá los mensajes en false
	 */
	public Receptor getReceptorPorFalse();

}
