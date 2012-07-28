/**
 * 27/07/2012 23:07:44 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.http.external.json;

import net.gaia.vortex.http.sesiones.VortexHttpTextualizer;

/**
 * Esta clase es la implementación del textualizador de mensajes por http usando jackson
 * 
 * @author D. García
 */
public class JacksonHttpTextualizer implements VortexHttpTextualizer {

	public static JacksonHttpTextualizer create() {
		final JacksonHttpTextualizer textualizer = new JacksonHttpTextualizer();
		return textualizer;
	}
}
