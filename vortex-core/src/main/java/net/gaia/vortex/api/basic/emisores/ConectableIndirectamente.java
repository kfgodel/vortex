/**
 * Created on: Sep 1, 2013 1:43:26 AM by: Dario L. Garcia
 * 
 * <a rel="license" href="http://creativecommons.org/licenses/by/2.5/ar/"><img
 * alt="Creative Commons License" style="border-width:0"
 * src="http://i.creativecommons.org/l/by/2.5/ar/88x31.png" /></a><br />
 * <span xmlns:dc="http://purl.org/dc/elements/1.1/"
 * href="http://purl.org/dc/dcmitype/InteractiveResource" property="dc:title"
 * rel="dc:type">Bean2Bean</span> by <a xmlns:cc="http://creativecommons.org/ns#"
 * href="https://sourceforge.net/projects/bean2bean/" property="cc:attributionName"
 * rel="cc:attributionURL">Dar&#237;o Garc&#237;a</a> is licensed under a <a rel="license"
 * href="http://creativecommons.org/licenses/by/2.5/ar/">Creative Commons Attribution 2.5 Argentina
 * License</a>.<br />
 * Based on a work at <a xmlns:dc="http://purl.org/dc/elements/1.1/"
 * href="https://bean2bean.svn.sourceforge.net/svnroot/bean2bean"
 * rel="dc:source">bean2bean.svn.sourceforge.net</a>
 */
package net.gaia.vortex.api.basic.emisores;

import net.gaia.vortex.api.basic.Emisor;

/**
 * Esta interfaz define el contrato esperado en los nodos cuya conexión se hace a traves de otro
 * emisor.<br>
 * Los emisores indirectamente conectables utilizan otros emisores como salida.<br>
 * <br>
 * Las subclases de este tipo son emisores porque emiten mensajes, pero a través de otras instancias
 * 
 * @author dgarcia
 */
public interface ConectableIndirectamente<E extends Emisor> extends Emisor {

	/**
	 * El emisor del cual se pueden obtener los conectores para obtener los mensajes de esta
	 * instancia
	 * 
	 * @return El emisor que sirve de salida de esta instancia
	 */
	E getSalida();
}
