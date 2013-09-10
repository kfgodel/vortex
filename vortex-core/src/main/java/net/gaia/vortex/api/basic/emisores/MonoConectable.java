/**
 * Created on: Aug 31, 2013 11:03:32 PM by: Dario L. Garcia
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

import net.gaia.vortex.api.proto.Conector;

/**
 * Esta interfaz representa el {@link MonoEmisor} que ofrece un conector siempre disponible al cual
 * conectarse.<br>
 * A través de ese conector se reciben los mensajes de esta instancia
 * 
 * @author dgarcia
 */
public interface MonoConectable extends MonoEmisor {
	/**
	 * Permite obtener el único conector de salida de este emisor, el cual puede utilizarse para
	 * conectar al receptor que recibirá los mensajes salientes de esta instancia
	 * 
	 * @return El conector que sirve de contacto con el receptor
	 */
	public Conector getConectorDeSalida();

}
