/**
 * 14/07/2012 19:17:58 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.comm.api.messages;

import net.gaia.vortex.comm.impl.messages.VortexChatSupport;

/**
 * Esta clase representa un mensaje de aviso a otros clientes conectados de que nos conectamos
 * 
 * @author D. García
 */
public class AvisoDePresencia extends VortexChatSupport {
	public static final String AVISO_DE_PRESENCIA = "AvisoDePresencia";

	public AvisoDePresencia() {
		super(AVISO_DE_PRESENCIA);
	}
}
