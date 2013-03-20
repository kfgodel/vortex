/**
 * 24/01/2013 15:59:26 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.sets.reflection.accessors;

import net.gaia.vortex.sets.reflection.ValueAccessor;

/**
 * Esta clase define el comportamiento base para facilitar la implementación de
 * {@link ValueAccessor}
 * 
 * @author D. García
 */
public abstract class ValueAccessorSupport implements ValueAccessor {

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	
	public boolean equals(final Object obj) {
		if (!(obj instanceof ValueAccessor)) {
			return false;
		}
		final ValueAccessor that = (ValueAccessor) obj;
		final boolean mismoPropertyPath = this.getPropertyPath().equals(that.getPropertyPath());
		return mismoPropertyPath;
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	
	public int hashCode() {
		return getPropertyPath().hashCode();
	}

}