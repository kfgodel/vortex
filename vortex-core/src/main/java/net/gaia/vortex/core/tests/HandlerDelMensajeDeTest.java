/**
 * 30/06/2012 21:22:43 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.core.tests;


/**
 * Esta interfaz está definida solo para testear la recepción del mensaje de test
 * 
 * @author D. García
 */
public interface HandlerDelMensajeDeTest {

	/**
	 * Invocado al recibir el mensaje en el receptor
	 * 
	 * @param mensaje
	 *            El mensaje recibido
	 */
	public void recibir(MensajeModeloParaTests mensaje);

}
