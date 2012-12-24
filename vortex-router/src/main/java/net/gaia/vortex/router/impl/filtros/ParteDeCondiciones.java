/**
 * 24/12/2012 14:16:38 Copyright (C) 2011 Darío L. García
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
 * Esta interfaz representa una vista parcial del conjunto de condiciones en la que sólo se ve una
 * condición
 * 
 * @author D. García
 */
public interface ParteDeCondiciones {

	/**
	 * Establece la nueva condición que aplicará en esta parte del conjunto afectando al todo.<br>
	 * Al cambiar la condición puede producirse un cambio global en la condición formada por esta
	 * parte
	 * 
	 * @param nuevaCondicion
	 *            La nueva instancia de condición que definirá los mensajes a filtrar
	 */
	void cambiarA(Condicion nuevaCondicion);

}
