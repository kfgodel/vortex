/**
 * Created on: Sep 1, 2013 12:07:17 PM by: Dario L. Garcia
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
import net.gaia.vortex.api.basic.emisores.Conectable;
import net.gaia.vortex.api.basic.emisores.ConectableIndirectamente;

/**
 * Esta interfaz representa un componente vortex compuesto de otros que forman una red interna. Esta
 * red definirá el comportamiento real de esta instancia.<br>
 * A traves de un componente que hace de entrada y otro que hace de salida, esta instancia puede
 * procesar los mensajes que recibe, y darle unidad a toda la red de componentes como punto unico de
 * entrada salida
 * 
 * @author dgarcia
 */
public interface Compuesto<C extends Conectable> extends Nodo, ConectableIndirectamente<C> {

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
	 * Desconecta de la salida de este componente el receptor indicado<br>
	 * Este método es equivalente a {@link #getSalida()}.{@link #desconectarDe(Receptor)}
	 * 
	 * @see net.gaia.vortex.api.basic.emisores.Conectable#desconectarDe(net.gaia.vortex.api.basic.Receptor)
	 */
	public void desconectarDe(Receptor destino);
}
