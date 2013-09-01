/**
 * Created on: Sep 1, 2013 1:38:28 AM by: Dario L. Garcia
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
package net.gaia.vortex.impl.moleculas;

import net.gaia.vortex.api.basic.Emisor;
import net.gaia.vortex.api.basic.Nodo;
import net.gaia.vortex.api.basic.Receptor;
import net.gaia.vortex.api.basic.emisores.ConectableIndirectamente;
import net.gaia.vortex.api.mensajes.MensajeVortex;
import net.gaia.vortex.impl.support.ReceptorSupport;

/**
 * Esta clase representa una molecula compuesta de atomos que definene su comportamiento al recibir
 * mensajes.<br>
 * Este tipo de moleculas no es conectable directamente si no a través de su salida
 * 
 * @author dgarcia
 * @param <E>
 */
public class MoleculaCompuesta<E extends Emisor> extends ReceptorSupport implements Nodo, ConectableIndirectamente<E> {

	private Receptor entrada;
	private E salida;

	/**
	 * @see net.gaia.vortex.api.basic.Receptor#recibir(net.gaia.vortex.api.mensajes.MensajeVortex)
	 */
	public void recibir(final MensajeVortex mensaje) {
		entrada.recibir(mensaje);
	}

	/**
	 * @see net.gaia.vortex.api.basic.emisores.ConectableIndirectamente#getSalida()
	 */
	public E getSalida() {
		return salida;
	}

	public Receptor getEntrada() {
		return entrada;
	}

	public void setEntrada(final Receptor entrada) {
		this.entrada = entrada;
	}

	public void setSalida(final E salida) {
		this.salida = salida;
	}

	/**
	 * Crea una molecula compuesta que derivará los mensajes entrantes a la entrada indicada, y las
	 * conexiones de salida a la salida indicada
	 * 
	 * @param entrada
	 *            La entrada que recibirá los mensajes
	 * @param salida
	 *            La salida que indica también el tipo de conectabilidad
	 * @return La molecula creada
	 */
	public static <E extends Emisor> MoleculaCompuesta<E> create(final Receptor entrada, final E salida) {
		final MoleculaCompuesta<E> molecula = new MoleculaCompuesta<E>();
		molecula.entrada = entrada;
		molecula.salida = salida;
		return molecula;
	}
}
