/**
 * 14/06/2012 23:29:50 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.core3.api.atomos.condicional;

import net.gaia.vortex.core3.api.annon.Atomo;
import net.gaia.vortex.core3.api.atomos.forward.Nexo;

/**
 * Esta interfaz representa un componente vortex que permite filtrar los mensajes pasados a otro
 * componente utilizando una condición.<br>
 * 
 * 
 * @author D. García
 */
@Atomo
public interface Filtro extends Nexo {

	/**
	 * Devuelve la condición utilizada por este componente para evaluar y descartar los mensajes
	 * 
	 * @return La condición que si aplica permite el pasaje del mensaje
	 */
	public Condicion getCondicion();

	/**
	 * Establece la condición utilizada por este filtro para aceptar los mensajes y pasarlos al
	 * proximo receptor. Si la condición pasada devuelve true el mensaje circula
	 * 
	 * @param condicion
	 *            La condición a utilizar
	 */
	public void setCondicion(Condicion condicion);

}
