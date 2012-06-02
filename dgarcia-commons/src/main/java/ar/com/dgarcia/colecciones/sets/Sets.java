/**
 * 12/11/2011 17:46:06 Copyright (C) 2011 Darío L. García
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
package ar.com.dgarcia.colecciones.sets;

import java.util.LinkedHashSet;

/**
 * Esta clase define algunos métodos para operaciones comunes con los sets
 * 
 * @author D. García
 */
public class Sets {

	/**
	 * Crea un nuevo set con los objetos pasados agregados
	 * 
	 * @param elements
	 *            El conjunto de elementos a agregar
	 * @return El conjunto de elementos creado
	 */
	public static <E> LinkedHashSet<E> newLinkedHashSet(final E... elements) {
		final LinkedHashSet<E> createdSet = new LinkedHashSet<E>(elements.length);
		for (final E e : elements) {
			createdSet.add(e);
		}
		return createdSet;
	}

}
