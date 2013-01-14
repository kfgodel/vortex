/**
 * 27/06/2012 14:03:35 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.core.api.ids.componentes;

/**
 * Esta interfaz representa un generador de identificadores vortex para las nuevas moléculas
 * 
 * @author D. García
 */
public interface GeneradorDeIdsComponentes {

	/**
	 * Genera un nuevo identificador para la molécula
	 * 
	 * @return El identificador generado
	 */
	public IdDeComponenteVortex generarId();

}
