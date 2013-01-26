/**
 * 26/01/2013 13:53:41 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.helpers;

import java.util.Iterator;
import java.util.Map;

import ar.com.dgarcia.colecciones.maps.impl.CaseInsensitiveHashMap;

/**
 * Esta clase representa el mapa utilizado en vortex para representar los datos serializados de los
 * objetos.<br>
 * Esta clase no discrimina mayúsculas de minúsculas en las claves y compara los valores por el
 * criterio de igualdad de {@link VortexEquals}
 * 
 * @author D. García
 */
public class VortexMap extends CaseInsensitiveHashMap<Object> {

	/**
	 * @see ar.com.dgarcia.colecciones.maps.impl.CaseInsensitiveHashMap#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(final Object obj) {
		if (obj == this) {
			return true;
		}

		if (!(obj instanceof Map)) {
			return false;
		}
		@SuppressWarnings("unchecked")
		final Map<String, Object> m = (Map<String, Object>) obj;
		if (m.size() != size()) {
			return false;
		}

		try {
			final Iterator<Entry<String, Object>> i = entrySet().iterator();
			while (i.hasNext()) {
				final Entry<String, Object> e = i.next();
				final String key = e.getKey();
				final Object thisValue = e.getValue();
				if (thisValue == null) {
					if (!(m.get(key) == null && m.containsKey(key))) {
						return false;
					}
				} else {
					final Object thatValue = m.get(key);
					if (!VortexEquals.areEquals(thisValue, thatValue)) {
						return false;
					}
				}
			}
		} catch (final ClassCastException unused) {
			return false;
		} catch (final NullPointerException unused) {
			return false;
		}

		return true;
	}

}
