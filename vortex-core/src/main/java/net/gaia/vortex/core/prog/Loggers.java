/**
 * 26/01/2012 01:12:26 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.core.prog;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Esta clase reúne métodos útiles para los logs del nodo vortex
 * 
 * @author D. García
 */
public class Loggers {
	/**
	 * Logger utilizado para registrar las decisiones de ruteo de los mensajes
	 */
	public static final Logger RUTEO = LoggerFactory.getLogger("net.gaia.vortex.meta.Loggers.RUTEO");

	/**
	 * Logger utilizado para registrar las decisiones de ruteo de los mensajes a nivel de átomos.<br>
	 * Detalle super fino
	 */
	public static final Logger ATOMOS = LoggerFactory.getLogger("net.gaia.vortex.meta.Loggers.ATOMOS");

	/**
	 * Logger utilizado para registrar las recepciones y envíos de mensajes en los nodos bidi
	 */
	public static final Logger BIDI_MSG = LoggerFactory.getLogger("net.gaia.vortex.meta.Loggers.MENSAJERIA_BIDI");

}
