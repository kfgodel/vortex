/**
 * 20/08/2012 18:25:48 Copyright (C) 2011 Darío L. García
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

import java.util.Map;

import ar.com.dgarcia.lang.strings.ToString;

/**
 * Esta clase representa un accessor de los valores en un mapa directamente modificando la key
 * 
 * @author D. García
 */
public class PropertyAccessor extends ValueAccessorSupport {

	private String propertyName;
	public static final String propertyName_FIELD = "propertyName";

	/**
	 * @see net.gaia.vortex.sets.reflection.ValueAccessor#setValueInto(java.util.Map,
	 *      java.lang.Object)
	 */
	
	public void setValueInto(final Map<String, Object> contenido, final Object value) {
		contenido.put(propertyName, value);
	}

	/**
	 * @see net.gaia.vortex.sets.reflection.ValueAccessor#getValueFrom(java.util.Map)
	 */
	
	public Object getValueFrom(final Map<String, Object> contenido) {
		final Object value = contenido.get(propertyName);
		return value;
	}

	/**
	 * @see net.gaia.vortex.sets.reflection.ValueAccessor#removeValueFrom(java.util.Map)
	 */
	
	public void removeValueFrom(final Map<String, Object> contenido) {
		contenido.remove(propertyName);
	}

	/**
	 * @see net.gaia.vortex.sets.reflection.ValueAccessor#hasValueIn(java.util.Map)
	 */
	
	public boolean hasValueIn(final Map<String, Object> contenido) {
		final boolean containsKey = contenido.containsKey(propertyName);
		return containsKey;
	}

	public static PropertyAccessor create(final String property) {
		final PropertyAccessor accessor = new PropertyAccessor();
		accessor.propertyName = property;
		return accessor;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	
	public String toString() {
		return ToString.de(this).con(propertyName_FIELD, propertyName).toString();
	}

	/**
	 * @see net.gaia.vortex.sets.reflection.ValueAccessor#getPropertyPath()
	 */
	
	public String getPropertyPath() {
		return propertyName;
	}
}
