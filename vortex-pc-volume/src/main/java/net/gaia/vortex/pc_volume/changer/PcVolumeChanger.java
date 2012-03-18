/**
 * 17/03/2012 23:52:14 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.pc_volume.changer;

/**
 * Esta interfaz define los métodos útiles para cambiar el volumen de la pc
 * 
 * @author D. García
 */
public interface PcVolumeChanger {

	/**
	 * Cambia el volumen de la PC al nivel indicado como un entero porcentaje. 0 - 100
	 * 
	 * @param volumenLevel
	 *            El nivel deseado
	 */
	void changeTo(int volumenLevel);

}
