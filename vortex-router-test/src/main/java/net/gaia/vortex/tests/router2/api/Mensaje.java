/**
 * 13/10/2012 18:13:38 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.tests.router2.api;

/**
 * Esta interfaz representa un mensaje enviable por los nodos entre sí
 * 
 * @author D. García
 */
public interface Mensaje {

	/**
	 * Devuelve el ID de este mensaje
	 */
	public Long getIdDeMensaje();

	/**
	 * Devuelve un String que representa contenido de este mensaje procesable por los filtros
	 */
	public String getTag();

	/**
	 * Indica opcionalmente el ID de pata local al receptor del mensaje por el cual se envía este
	 * mensaje.<br>
	 * Para que este ID esté definido previamente los nodos deben conocer el ID de pata de su
	 * receptor.
	 * 
	 * @return El id que corresponde a la pata que recibe el mensaje en destino, o null si no se
	 *         conoce
	 */
	public Long getIdDePataLocalAlReceptor();

}
