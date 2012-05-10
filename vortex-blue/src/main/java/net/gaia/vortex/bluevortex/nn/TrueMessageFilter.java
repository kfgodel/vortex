/**
 * 10/05/2012 00:46:22 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.bluevortex.nn;

import net.gaia.vortex.bluevortex.api.FiltroDeMensajes;

/**
 * Esta clase representa el filtro de mensajes que acepta todos los mensajes
 * 
 * @author D. García
 */
public class TrueMessageFilter implements FiltroDeMensajes {

	/**
	 * Crea una nueva instancia de este filtro
	 * 
	 * @return El filtro creado
	 */
	public static TrueMessageFilter create() {
		final TrueMessageFilter filtro = new TrueMessageFilter();
		return filtro;
	}

}
