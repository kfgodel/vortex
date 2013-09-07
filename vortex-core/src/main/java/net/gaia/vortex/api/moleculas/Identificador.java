/**
 * Created on: Sep 6, 2013 7:58:32 PM by: Dario L. Garcia
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
package net.gaia.vortex.api.moleculas;

import net.gaia.vortex.api.basic.Nodo;
import net.gaia.vortex.api.basic.Receptor;
import net.gaia.vortex.api.basic.emisores.MonoConectable;
import net.gaia.vortex.api.ids.componentes.IdDeComponenteVortex;
import net.gaia.vortex.api.proto.Conector;

/**
 * Esta interfaz representa el componente vortex que permite identificar los mensajes enviados y
 * descartar los duplicados recibidos.<br>
 * 
 * @author dgarcia
 */
public interface Identificador extends Nodo, MonoConectable {

	/**
	 * Devuelve el identificador de este componente que será utilizado para generar los ids de los
	 * mensajes enviados<br>
	 * Este identificador es utilizado para discriminar los mensajes propios
	 * 
	 * @return El identificador de este componente como entidad identificadora de mensajes
	 */
	IdDeComponenteVortex getIdPropio();

	/**
	 * Devuelve el componente de este identificador que permite a otro componente enviar mensajes
	 * con un id.<br>
	 * Se utilizarán ids generados por este identificador que serán asignados a los mensajes
	 * salientes por el {@link #getConectorDeSalida()}
	 * 
	 * @return El receptor al que enviar los mensajes para que se les asigne ID
	 */
	Receptor getConectorParaEnviarConId();

	/**
	 * Devuelve el conector de los mensajes entrantes que filtra los duplicados. A través de este
	 * conector otro componente puede recibir mensajes evitando los mensajes propios y los
	 * duplicados producidos por redes con bucles
	 * 
	 * @return El conector para los mensajes entrantes
	 */
	Conector getConectorParaRecibirSinDuplicados();

}
