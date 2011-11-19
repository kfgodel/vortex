/**
 * 12/11/2011 23:55:49 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.servers.embedded;

import net.gaia.vortex.servers.ServidorVortex;

/**
 * Esta clase representa un servidor vortex embebido que funciona en memoria
 * 
 * @author D. García
 */
public class ServidorVortexEmbebido implements ServidorVortex {

	public static ServidorVortexEmbebido create() {
		final ServidorVortexEmbebido embebido = new ServidorVortexEmbebido();
		return embebido;
	}
}
