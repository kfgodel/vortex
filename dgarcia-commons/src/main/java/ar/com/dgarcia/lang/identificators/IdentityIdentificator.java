/**
 * 10/10/2005 14:30:21
 * Copyright (C) 2006  Dario L. Garcia
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */
package ar.com.dgarcia.lang.identificators;

import ar.com.dgarcia.lang_identificators.AbstractIdentificator;


/**
 * Esta clase toma como dominio conceptual la identidad de los
 * objetos, discriminando y ordenando por su identidad.
 * Para ello se utiliza el metodo nativo {@link java.lang.Object#hashCode()}
 * sin tomar en cuenta las redefiniciones de ese metodo hechas
 * por su subclases.
 *
 * @version 1.0
 * @since 2006-03-23
 * @author D. Garcia
 */
public class IdentityIdentificator extends AbstractIdentificator<Object> {

	/**
	 * Singleton
	 */
	@SuppressWarnings("hiding")
	public static final IdentityIdentificator instance = new IdentityIdentificator();


	/**
	 * Determina si las dos referencias pasadas son la misma
	 * @param first Primer objeto
	 * @param second  Segundo
	 * @return True si ambas referencias corresponden al mismo
	 * objeto
	 * @see ar.com.dgarcia.lang_identificators.AbstractIdentificator#areEquals(Object, Object)
	 */
	
	public boolean areEquals(Object first, Object second) {
		return first == second;
	}

	/**
	 * @param object El objeto de cual obtener el hashcode
	 * @return El discriminante que identifica la instancia
	 * @see ar.com.dgarcia.lang_identificators.AbstractIdentificator#discriminator(Object)
	 */
	
	public long discriminator(Object object) {
		return System.identityHashCode(object);
	}
}
