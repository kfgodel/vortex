/**
 * 27/06/2012 13:51:03 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.api.ids.componentes;

import net.gaia.vortex.api.proto.ShortStringable;

/**
 * Esta interfaz representa un identificador dentro de la red vortex para una molécula
 * 
 * @author D. García
 */
public interface IdDeComponenteVortex extends Comparable<IdDeComponenteVortex>, ShortStringable {

	/**
	 * Devuelve el valor actual del identificador para ser usado en la identificación de la ruta de
	 * los mensajes.<br>
	 * El identificador podría cambiar en el tiempo pero el algoritmo que haga el cambio debe tomar
	 * en cuenta los mensajes que ya tienen un valor asignado para un nodo
	 * 
	 * @return El identificador actual
	 */
	public String getValorActual();
}
