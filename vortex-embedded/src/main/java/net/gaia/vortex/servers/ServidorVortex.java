/**
 * 12/11/2011 23:53:18 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.servers;

import java.util.List;

import net.gaia.vortex.protocol.messages.MensajeVortex;
import net.gaia.vortex.servers.embedded.ComunicanteVortex;

/**
 * Esta clase representa el contrato basico necesario por un servidor vortex
 * 
 * @author D. García
 */
public interface ServidorVortex {

	/**
	 * Envia los mensajes indicados al servidor
	 * 
	 * @param mensajes
	 */
	void enviar(List<MensajeVortex> mensajes);

	/**
	 * Crea un objeto que representa un extremo comunicante conectado al servidor
	 * 
	 * @return
	 */
	ComunicanteVortex crearComunicante();

}
