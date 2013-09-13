/**
 * Created on: Sep 11, 2013 10:59:42 PM by: Dario L. Garcia
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
package net.gaia.vortex.impl.support;

import java.util.List;

import net.gaia.vortex.api.basic.Receptor;
import net.gaia.vortex.api.basic.emisores.Conectable;
import net.gaia.vortex.api.basic.emisores.ConectableIndirectamente;

/**
 * Esta clase sirve de base para los tipos de componente coenctables indirectamente
 * 
 * @author dgarcia
 * @param <E>
 */
public abstract class ConectableIndirectamenteSupport<E extends Conectable> extends ReceptorSupport implements
		ConectableIndirectamente<E>, Conectable {

	/**
	 * Conecta el receptor indicado a la salida de este componente.<br>
	 * Este método es equivalente a llamar {@link #getSalida()}.{@link #conectarCon(Receptor)}
	 * 
	 * @see net.gaia.vortex.api.moleculas.Compuesto#conectarCon(net.gaia.vortex.api.basic.Receptor)
	 */
	public void conectarCon(final Receptor destino) {
		getSalida().conectarCon(destino);
	}

	/**
	 * Desconecta la salida de este componente de sus receptores conectados.<br>
	 * Este método es equivalente a {@link #getSalida()}.{@link #desconectar()}
	 * 
	 * @see net.gaia.vortex.api.moleculas.Compuesto#desconectar()
	 */
	public void desconectar() {
		getSalida().desconectar();
	}

	/**
	 * Desconecta de la salida de este componente el receptor indicado<br>
	 * Este método es equivalente a {@link #getSalida()}.{@link #desconectarDe(Receptor)}
	 * 
	 * @see net.gaia.vortex.api.moleculas.Compuesto#desconectarDe(net.gaia.vortex.api.basic.Receptor)
	 */
	public void desconectarDe(final Receptor destino) {
		getSalida().desconectarDe(destino);
	}

	/**
	 * Devuelve la lista de los conectados a la salida de este componente<br>
	 * Este método es equivalente a {@link #getSalida()}.{@link #getConectados()}
	 * 
	 * @see net.gaia.vortex.api.basic.emisores.Conectable#getConectados()
	 */
	public List<Receptor> getConectados() {
		return getSalida().getConectados();
	}
}