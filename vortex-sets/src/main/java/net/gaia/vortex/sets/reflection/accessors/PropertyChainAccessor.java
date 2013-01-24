/**
 * 20/08/2012 18:33:07 Copyright (C) 2011 Darío L. García
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

import java.util.Arrays;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.gaia.vortex.sets.reflection.MessageReflectionException;
import net.gaia.vortex.sets.reflection.MissingPropertyException;
import net.gaia.vortex.sets.reflection.ValueAccessor;
import net.gaia.vortex.sets.reflection.ValueNotMapException;
import ar.com.dgarcia.java.lang.ParOrdenado;
import ar.com.dgarcia.lang.strings.ToString;

/**
 * Esta clase representa el accessor para una cadena de propiedades anidadas
 * 
 * @author D. García
 */
public class PropertyChainAccessor extends ValueAccessorSupport {

	/**
	 * Caracter utilizado para delimitar las propiedades de la cadena
	 */
	public static final String PROPERTY_DELIMITER = ".";

	public static final Pattern propertyChainPattern = Pattern
			.compile("^(?:([\\p{Alpha}|_|$][\\p{Alnum}|_|$]*)(?:\\.([\\p{Alpha}|_|$][\\p{Alnum}|_|$]*))*)$");

	private String[] propertyNames;
	public static final String propertyNames_FIELD = "propertyNames";

	private ValueAccessor[] cachedAttributes;

	/**
	 * @see net.gaia.vortex.sets.reflection.ValueAccessor#setValueInto(java.util.Map,
	 *      java.lang.Object)
	 */
	@Override
	public void setValueInto(final Map<String, Object> contenido, final Object value) throws MessageReflectionException {
		final ParOrdenado<Map<String, Object>, ValueAccessor> lastChainLink = getLastValueAccessorFrom(contenido);
		final Map<String, Object> currentObject = lastChainLink.getPrimero();
		final ValueAccessor accessor = lastChainLink.getSegundo();
		accessor.setValueInto(currentObject, value);
	}

	/**
	 * Devuelve el ultimo objeto y su value accessor de la cadena representada por esta instancia
	 * siguiendo el objeto pasado
	 * 
	 * @param contenido
	 *            El contenido a recorrer con esta cadena
	 * @return El par de ultimo objeto y accessor de esta cadena
	 */
	@SuppressWarnings("unchecked")
	private ParOrdenado<Map<String, Object>, ValueAccessor> getLastValueAccessorFrom(final Map<String, Object> contenido)
			throws MissingPropertyException, ValueNotMapException {
		Map<String, Object> currentObject = contenido;
		ValueAccessor attribute = null;
		for (int i = 0; i < propertyNames.length; i++) {
			attribute = getCachedAttributes()[i];
			if (attribute == null) {
				final String propertyName = propertyNames[i];
				attribute = PropertyAccessor.create(propertyName);
				getCachedAttributes()[i] = attribute;
			}
			if (i == propertyNames.length - 1) {
				// Es la propiedad que estabamos buscando!
				break;
			}
			final Object nextObject = attribute.getValueFrom(currentObject);
			if (nextObject == null) {
				throw new MissingPropertyException("La " + (i + 1) + "º propiedad[" + propertyNames[i]
						+ "] es null al seguir la cadena: " + Arrays.toString(propertyNames)
						+ " desde el contenido raiz[" + contenido + "]");
			}
			if (!(nextObject instanceof Map)) {
				throw new ValueNotMapException("El valor[" + nextObject + "] de la " + (i + 1) + "º propiedad["
						+ propertyNames[i] + "] no es un mapa para seguir la cadena: " + Arrays.toString(propertyNames)
						+ " desde el contenido raiz[" + contenido + "]");
			}
			currentObject = (Map<String, Object>) nextObject;
		}
		final ParOrdenado<Map<String, Object>, ValueAccessor> lastChainLink = new ParOrdenado<Map<String, Object>, ValueAccessor>(
				currentObject, attribute);
		return lastChainLink;
	}

	/**
	 * @see net.gaia.vortex.sets.reflection.ValueAccessor#getValueFrom(java.util.Map)
	 */
	@Override
	public Object getValueFrom(final Map<String, Object> contenido) throws MessageReflectionException {
		final ParOrdenado<Map<String, Object>, ValueAccessor> lastChainLink = getLastValueAccessorFrom(contenido);
		final Map<String, Object> currentObject = lastChainLink.getPrimero();
		final ValueAccessor accessor = lastChainLink.getSegundo();
		final Object value = accessor.getValueFrom(currentObject);
		return value;
	}

	/**
	 * @see net.gaia.vortex.sets.reflection.ValueAccessor#removeValueFrom(java.util.Map)
	 */
	@Override
	public void removeValueFrom(final Map<String, Object> contenido) throws MessageReflectionException {
		final ParOrdenado<Map<String, Object>, ValueAccessor> lastChainLink = getLastValueAccessorFrom(contenido);
		final Map<String, Object> currentObject = lastChainLink.getPrimero();
		final ValueAccessor accessor = lastChainLink.getSegundo();
		accessor.removeValueFrom(currentObject);
	}

	/**
	 * @see net.gaia.vortex.sets.reflection.ValueAccessor#hasValueIn(java.util.Map)
	 */
	@Override
	public boolean hasValueIn(final Map<String, Object> contenido) throws MessageReflectionException {
		ParOrdenado<Map<String, Object>, ValueAccessor> lastChainLink;
		try {
			lastChainLink = getLastValueAccessorFrom(contenido);
		} catch (final ValueNotMapException e) {
			// Algun valor en el medio no es un mapa para seguir la secuencia
			return false;
		} catch (final MissingPropertyException e) {
			// Alguno en el medio es null para seguir la secuencia
			return false;
		}
		final Map<String, Object> currentObject = lastChainLink.getPrimero();
		final ValueAccessor accessor = lastChainLink.getSegundo();
		final boolean hasValue = accessor.hasValueIn(currentObject);
		return hasValue;
	}

	public static PropertyChainAccessor create(final String propertyChainExpression) {
		if (!isPropertyChain(propertyChainExpression)) {
			throw new IllegalArgumentException("La expresión [" + propertyChainExpression
					+ "] no representa una secuencia de propiedades");
		}
		final PropertyChainAccessor expression = new PropertyChainAccessor();
		expression.propertyNames = propertyChainExpression.split("\\" + PROPERTY_DELIMITER);
		return expression;
	}

	/**
	 * Crea el accessor menor que se requiere para la cadena de propiedades pasadas.<br>
	 * Si la cadena no involucra varias propiedades se utiliza el {@link PropertyAccessor}
	 * 
	 * @param propertyChain
	 *            La cadena de propiedades
	 * @return El accesor de la cadena o propiedad pasada
	 */
	public static ValueAccessor createAccessor(final String propertyChain) {
		if (propertyChain.contains(PropertyChainAccessor.PROPERTY_DELIMITER)) {
			return PropertyChainAccessor.create(propertyChain);
		}
		return PropertyAccessor.create(propertyChain);
	}

	/**
	 * Indica si la expresión pasada como cadena representa una cadena de propiedades separadas por
	 * puntos
	 * 
	 * @param propertyChain
	 * @return Devuelve true si la cadena pasada puede ser tomada como cadena de propiedades
	 *         separadas por puntos
	 */
	public static boolean isPropertyChain(final String propertyChain) {
		if (propertyChain == null) {
			return false;
		}
		final Matcher matcher = propertyChainPattern.matcher(propertyChain);
		return matcher.matches();
	}

	private ValueAccessor[] getCachedAttributes() {
		if (cachedAttributes == null) {
			cachedAttributes = new ValueAccessor[propertyNames.length];
		}
		return cachedAttributes;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return ToString.de(this).con(propertyNames_FIELD, Arrays.toString(propertyNames)).toString();
	}

	/**
	 * @see net.gaia.vortex.sets.reflection.ValueAccessor#getPropertyPath()
	 */
	@Override
	public String getPropertyPath() {
		final StringBuilder propertyPath = new StringBuilder();
		for (int i = 0; i < propertyNames.length; i++) {
			final String propertyName = propertyNames[i];
			if (i > 0) {
				propertyPath.append(PROPERTY_DELIMITER);
			}
			propertyPath.append(propertyName);
		}
		return propertyPath.toString();
	}

}
