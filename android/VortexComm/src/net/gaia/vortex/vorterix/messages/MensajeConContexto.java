/**
 * 24/06/2012 16:45:38 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.vorterix.messages;

/**
 * Esta interfaz representa un contrato general para los mensajes que circulan en vortex e indican
 * en que contexto fueron concebidos, de manera poder discriminarlos por contexto
 * 
 * @author D. García
 */
public interface MensajeConContexto {

	/**
	 * Devuelve el contexto de este mensaje
	 * 
	 * @return El contexto que es compartido por los mensajes del mismo dominio/aplicacion/contexto
	 */
	public String getContexto();

}
