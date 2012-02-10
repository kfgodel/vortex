/**
 * 10/02/2012 18:29:53 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.hilevel.api;

import java.util.Set;

/**
 * Esta interfaz representa el listener de los cambios de tags en el nodo.<br>
 * A través de los métodos de esta interfaz se reciben las notificaciones por cambios en los tags
 * 
 * @author D. García
 */
public interface ListenerDeTagsDelNodo {

	/**
	 * Invocado cuando el nodo informa sobre cambios en sus tags publicados
	 * 
	 * @param tagsDelNodo
	 *            El estado actual de los tags que le interesan al nodo, según el último mensaje de
	 *            actualización recibido
	 */
	public void onCambiosDeTags(Set<String> tagsDelNodo);
}
