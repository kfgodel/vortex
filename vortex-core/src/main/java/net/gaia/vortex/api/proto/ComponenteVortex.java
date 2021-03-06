/**
 * 06/07/2012 00:10:42 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.api.proto;


/**
 * Esta interfaz define los métodos que tiene cualquier componente vortex.<br>
 * Las subclases de esta interfaz representan componentes básicos o nodos de vortex que pueden
 * usarse para armar una red de comunicación entre módulos, aplicaciones y sistemas
 * 
 * @author D. García
 */
public interface ComponenteVortex extends ShortStringable {

	/**
	 * Devuelve un número asignado a esta instancia para identificarlo con respecto a otros
	 * componentes en memoria
	 * 
	 * @return El número que permite discriminar esta instancia de otras
	 */
	public long getNumeroDeInstancia();

}
