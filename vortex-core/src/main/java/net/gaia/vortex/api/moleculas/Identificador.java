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

import java.util.List;

import net.gaia.vortex.api.basic.Nodo;
import net.gaia.vortex.api.basic.Receptor;
import net.gaia.vortex.api.basic.emisores.Conectable;
import net.gaia.vortex.api.ids.componentes.IdDeComponenteVortex;
import net.gaia.vortex.api.mensajes.MensajeVortex;

/**
 * Esta interfaz representa el componente vortex que permite identificar los mensajes enviados y
 * descartar los duplicados recibidos.<br>
 * A través de este componente es posible evitar mensajes duplicados o propios en redes cuya
 * topología es desconocida, o donde existene bucles o rutas paralelas.
 * 
 * @author dgarcia
 */
public interface Identificador extends Nodo {

	/**
	 * Conecta este identificador al receptor indicado, el cual recibirá los mensajes con Id
	 * asignado en este componente
	 * 
	 * @see net.gaia.vortex.api.basic.emisores.Conectable#conectarCon(net.gaia.vortex.api.basic.Receptor)
	 */
	public void conectarCon(Receptor destino);

	/**
	 * Desconecta este identificador del receptor
	 * 
	 * @see net.gaia.vortex.api.basic.emisores.Conectable#desconectar()
	 */
	public void desconectar();

	/**
	 * Desconecta este identificador del receptor indicado
	 * 
	 * @see net.gaia.vortex.api.basic.emisores.Conectable#desconectarDe(net.gaia.vortex.api.basic.Receptor)
	 */
	public void desconectarDe(Receptor destino);

	/**
	 * Devuelve la lista con el receptor conectado a la salida de este identificador
	 * 
	 * @see net.gaia.vortex.api.basic.emisores.Conectable#getConectados()
	 */
	public List<Receptor> getConectados();

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
	Conectable getConectorParaRecibirSinDuplicados();

	/**
	 * Al recibir un mensaje este identificador lo filtra eliminando duplicados. Si el mensaje es
	 * nuevo y es externo, es entregado a través del {@link #getConectorParaRecibirSinDuplicados()}
	 * 
	 * @see net.gaia.vortex.api.basic.Receptor#recibir(net.gaia.vortex.api.mensajes.MensajeVortex)
	 */
	public void recibir(MensajeVortex mensaje);

}
