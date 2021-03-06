/**
 * Created on: Sep 8, 2013 10:56:37 AM by: Dario L. Garcia
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
import net.gaia.vortex.api.basic.emisores.ConectableIndirectamente;
import net.gaia.vortex.api.basic.emisores.MonoEmisor;
import net.gaia.vortex.api.mensajes.MensajeVortex;
import net.gaia.vortex.impl.nulos.ReceptorNulo;

/**
 * Esta interfaz representa un punto de acceso a una red de componentes abstrayendo al red. Con la
 * terminal se pueden enviar y recibir mensajes.<br>
 * Los mensajes recibidos por este terminal son enviados a la red, y el receptor conectado desde
 * esta terminal recibe los mensajes de la red
 * 
 * @author dgarcia
 */
public interface Terminal extends Nodo, MonoEmisor, ConectableIndirectamente<Conectable> {

	/**
	 * Al recibir un mensaje este terminal lo envia a los otros terminales con los que comparte,
	 * recibiendo en los conectores de salida de todos los terminales con los que comparte los
	 * mensajes
	 * 
	 * @see net.gaia.vortex.api.basic.Receptor#recibir(net.gaia.vortex.api.mensajes.MensajeVortex)
	 */
	public void recibir(MensajeVortex mensaje);

	/**
	 * Conecta el receptor indicado a la salida de este componente.<br>
	 * Este método es equivalente a llamar {@link #getSalida()}.{@link #conectarCon(Receptor)}
	 * 
	 * @see net.gaia.vortex.api.basic.emisores.Conectable#conectarCon(net.gaia.vortex.api.basic.Receptor)
	 */
	public void conectarCon(Receptor destino);

	/**
	 * Desconecta la salida de este componente de sus receptores conectados.<br>
	 * Este método es equivalente a {@link #getSalida()}.{@link #desconectar()}
	 * 
	 * @see net.gaia.vortex.api.basic.emisores.Conectable#desconectar()
	 */
	public void desconectar();

	/**
	 * Devuelve la lista con el receptor conectado a la salida de este terminal
	 * 
	 * @see net.gaia.vortex.api.basic.emisores.Conectable#getConectados()
	 */
	public List<Receptor> getConectados();

	/**
	 * Devuelve el unico receptor conectado a la salida de esta terminal.<br>
	 * Si no hay un receptor conectado, se devuelve el {@link ReceptorNulo}
	 * 
	 * @see net.gaia.vortex.api.basic.emisores.MonoEmisor#getConectado()
	 */
	public Receptor getConectado();

	/**
	 * Desconecta de la salida de este componente el receptor indicado<br>
	 * Este método es equivalente a {@link #getSalida()}.{@link #desconectarDe(Receptor)}
	 * 
	 * @see net.gaia.vortex.api.basic.emisores.Conectable#desconectarDe(net.gaia.vortex.api.basic.Receptor)
	 */
	public void desconectarDe(Receptor destino);

	/**
	 * Devuelve el conector que se debe utilizar para recibir los mensajes compartidos desde otras
	 * terminales.<br>
	 * Este conector debe conectarse con {@link Conectable#conectarCon(Receptor)} al receptor que
	 * quiere recibir los mensajes de este terminal
	 * 
	 * @see net.gaia.vortex.api.basic.emisores.ConectableIndirectamente#getSalida()
	 */
	public Conectable getSalida();

	/**
	 * Devuelve el receptor utilizado para recibir los mensajes compartidos desde otras terminales,
	 * y que terminará saliendo por la salida de esta terminal en {@link #getSalida()}
	 * 
	 * @return El receptor para los mensajes de otras terminales
	 */
	public Receptor getReceptorParaTerminales();

	/**
	 * Quita la terminal pasada como receptora de los mensajes de esta terminal.<br>
	 * La terminal indicada dejará de recibir los mensajes recibidos por esta terminal
	 * 
	 * @param otraTerminal
	 *            La terminala excluir
	 */
	void descompartirMensajesA(final Terminal otraTerminal);

	/**
	 * Agrega la terminal pasada como receptora de los mensajes recibidos por esta terminal<br>
	 * La terminal indicada recibirá también los mensajes recibidos por esta terminal
	 * 
	 * @param otraTerminal
	 *            La terminal a agregar
	 */
	void compartirMensajesCon(final Terminal otraTerminal);

}
