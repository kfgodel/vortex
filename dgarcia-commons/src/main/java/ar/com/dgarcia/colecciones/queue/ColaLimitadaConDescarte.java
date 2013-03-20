/**
 * 31/08/2012 22:33:11 Copyright (C) 2011 Darío L. García
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
package ar.com.dgarcia.colecciones.queue;

import java.util.concurrent.ConcurrentLinkedQueue;

import ar.com.dgarcia.coding.exceptions.UnhandledConditionException;

/**
 * Esta clase representa una cola con capacidad limitada que descarta los elementos más viejos si
 * alcanza su capacidad.<br>
 * Esta implementación es concurrente pudiendo crecer más allá del limite durante la inserción pero
 * decrece acorde al límite al terminar la inserción.
 * 
 * @author D. García
 */
public class ColaLimitadaConDescarte<E> extends ConcurrentLinkedQueue<E> {
	private static final long serialVersionUID = 7158768640486247548L;

	/**
	 * Indica la cantidad máxima de elementos
	 */
	private final int cantidadMaxima;

	/**
	 * Crea una nueva instancia limitada a la cantidad indicada.<br>
	 * La cantidad indicada sirve como límite. La cola descartará los elementos insertados primeros
	 * cuando el size() > cantidadMaxima
	 * 
	 * @param cantidadMaxima
	 *            La cantidad máxima de elementos
	 */
	public ColaLimitadaConDescarte(final int cantidadMaxima) {
		super();
		this.cantidadMaxima = cantidadMaxima;
	}

	/**
	 * @see java.util.concurrent.ConcurrentLinkedQueue#offer(java.lang.Object)
	 */
	
	public boolean offer(final E e) {
		final boolean added = super.offer(e);
		if (!added) {
			throw new UnhandledConditionException(
					"Por alguna razón inesperada no pudimos agregar el elemento a la cola");
		}
		if (size() > cantidadMaxima) {
			// Quitamos el más viejo
			poll();
		}
		return true;
	}
}
