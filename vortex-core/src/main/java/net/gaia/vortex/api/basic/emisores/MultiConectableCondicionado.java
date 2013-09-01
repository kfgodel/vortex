/**
 * Created on: Aug 31, 2013 11:40:00 PM by: Dario L. Garcia
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

import net.gaia.vortex.api.condiciones.Condicion;
import net.gaia.vortex.api.proto.Conector;

/**
 * Esta interfaz representa un emisor multiple al que se le pueden crear conectores con condiciones
 * asociadas para filtrar los mensajes transmitidos.<br>
 * A diferencia del {@link MultiConectable}, para crear un conector en esta instancia se debe
 * indicar una condición asociada
 * 
 * @author dgarcia
 */
public interface MultiConectableCondicionado extends MultiEmisor {
	/**
	 * Crea un nuevo conector para utilizar con un receptor al que se le enviarán los mensajes
	 * emitidos por esta instancia y filtrados por la condicion pasada
	 * 
	 * @param condicionFiltro
	 *            La condicion que determinara con true los mensajes que deben pasar
	 * @return El conector para la nueva conexión
	 */
	Conector crearConector(Condicion condicionFiltro);

	/**
	 * Elimina de este emisor el conector indicado.<br>
	 * Si el conector pasado no es parte de este emisor, es ignorado.
	 * 
	 * @param conector
	 *            El conector a eliminar
	 */
	void eliminarConector(Conector conector);

}
