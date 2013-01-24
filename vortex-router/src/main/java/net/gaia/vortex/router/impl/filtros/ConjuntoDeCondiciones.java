/**
 * 24/12/2012 14:12:12 Copyright (C) 2011 Darío L. García
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
 * Esta interfaz representa un conjunto de condiciones que es modificable por partes (cada condición
 * por separado), pero que a la vez puede verse como una sola gran condición.<br>
 * Cada parte del conjunto define una condición que puede modificar y haciéndolo modifica la
 * condición representada por este conjunto como un todo.
 * 
 * 
 * @author D. García
 */
public interface ConjuntoDeCondiciones {

	/**
	 * Crea una nueva parte de este conjunto de condiciones que permitirá modificar sólo una
	 * condición, posiblemente alterando el estado global de este con
	 * 
	 * @param condicionInicial
	 *            Condición con la que se creará la parte
	 * 
	 * @return La parte creada
	 */
	ParteDeCondiciones crearNuevaParte(Condicion condicionInicial);

	/**
	 * Elimina una de las partes de este conjunto, posiblemente alterando el estado global de este
	 * conjunto
	 * 
	 * @param parteDeCondicion
	 *            La parte a eliminar
	 */
	void eliminarParte(ParteDeCondiciones parteDeCondicion);

	/**
	 * Devuelve la condicion que representa al conjunto menos la parte indicada
	 * 
	 * @param parteExceptuada
	 *            La parte que será exluida de la condicion global
	 * 
	 * @return La condicion que representa al resto de las partes. Si no hay otras partes se
	 *         devolverá la condicion FALSE, que representa el conjunto vacio
	 */
	Condicion getCondicionDelConjuntoMenos(ParteDeCondiciones parteExceptuada);

}
