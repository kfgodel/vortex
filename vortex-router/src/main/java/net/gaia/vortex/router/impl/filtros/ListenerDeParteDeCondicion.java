/**
 * 24/12/2012 15:08:47 Copyright (C) 2011 Darío L. García
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

import net.gaia.vortex.api.condiciones.Condicion;

/**
 * esta interfaz representa el listener que será notificado por cambios en las condiciones de una
 * parte
 * 
 * @author D. García
 */
public interface ListenerDeParteDeCondicion {

	/**
	 * Invocado cuando la parte cambia su condición por una nueva
	 * 
	 * @param nuevaCondicion
	 *            La nueva condición para la parte
	 * @param parte
	 *            La parte modificada
	 */
	void onCambioDeCondicion(Condicion nuevaCondicion, ParteDeCondiciones parte);

}
