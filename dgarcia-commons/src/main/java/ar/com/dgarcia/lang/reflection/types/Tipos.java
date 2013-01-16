/**
 * 15/01/2013 22:31:54 Copyright (C) 2011 Darío L. García
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
package ar.com.dgarcia.lang.reflection.types;

import java.lang.reflect.Field;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;

import ar.com.dgarcia.coding.exceptions.UnhandledConditionException;
import ar.com.dgarcia.lang.reflection.types.impl.TipoComun;

/**
 * Esta clase es un punto de acceso a los {@link Tipo}s
 * 
 * @author D. García
 */
public class Tipos {

	/**
	 * Devuelve el tipo que representa el tipo java pasado.<br>
	 * 
	 * @param javaType
	 *            El tipo java del que queremos obtener su representación
	 * @return El tipo que representa el valor pasado
	 */
	public static Tipo desdeType(final Type javaType) {
		Class<?> claseConcreta = null;
		if (javaType instanceof Class) {
			claseConcreta = (Class<?>) javaType;
		} else if (javaType instanceof ParameterizedType) {
			final ParameterizedType parametricType = (ParameterizedType) javaType;
			final Type rawType = parametricType.getRawType();
			if (!(rawType instanceof Class)) {
				throw new UnhandledConditionException("El tipo raw de un parameterized type[" + parametricType
						+ "] no es una clase?: " + rawType);
			}
			claseConcreta = (Class<?>) rawType;
		} else if (javaType instanceof GenericArrayType) {
			claseConcreta = null;
		} else if (javaType instanceof TypeVariable) {
			claseConcreta = null;
		} else if (javaType instanceof WildcardType) {
			claseConcreta = null;
		} else {
			throw new UnhandledConditionException("Hay un tipo de java que no es ninguno de los que conozco?: "
					+ javaType);
		}
		return TipoComun.create(javaType, claseConcreta);
	}

	/**
	 * Devuelve el tipo que representa el tipo del field.<br>
	 * Debido a como se manejan los arrays con reflection, la clase del array no conoce información
	 * de generics de su delcaración ni su declaración a la clase concreta. Este método es necesario
	 * para obtener el tipo que contiene a ambas
	 * 
	 * @param field
	 *            El atributo que declara un tipo (con generics o no)
	 * @return El tipo definido en el atributo con información de clase y de generics
	 */
	public static Tipo desdeField(final Field field) {
		final Class<?> tipoSinGenerics = field.getType();
		final Type tipoConGenerics = field.getGenericType();
		return TipoComun.create(tipoConGenerics, tipoSinGenerics);
	}
}
