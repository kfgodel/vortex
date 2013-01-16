package ar.com.dgarcia.lang.reflection.types;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import ar.com.dgarcia.coding.exceptions.FaultyCodeException;

/**
 * Esta clase abstracta sirve para referenciar tipos complejos que usan generic en su definición. A
 * través de subclases de esta se pueden obtener referencias a las instancias genéricas que
 * representan esos tipos como List&lt;String&gt;.<br>
 * <br>
 * La manera de obtener un tipo parametrizado es crear una instancia de esta clase en forma anónima:<br>
 * - new TypeRef&lt;List&lt;String&gt;&gt;(){}.getType()<br>
 * <br>
 * Si un tipo es muy utilizado en el código se puede crear una clase con nombre a la que se le puede
 * pedir el tipo:<br>
 * - class ListaDeStrings extends TypeRef&lt;List&lt;String&gt;&gt; {...<br>
 * <br>
 * Tomado de Neal Gafter: http://gafter.blogspot.com.ar/search?q=super+type+token
 * 
 * @author D. García
 * @param <T>
 */
public abstract class TypeRef<T> {

	private final Type type;

	public TypeRef() {
		type = deducirParametroDeTipo();
	}

	/**
	 * Intenta deducir el tipo genérico con el que se parametrizó esta clase
	 * 
	 * @return El tipo que representa el parametro de la subclase
	 */
	private Type deducirParametroDeTipo() {
		final Class<?> concreteClass = getClass();
		final Class<?> expectedSuperclass = TypeRef.class;
		if (!expectedSuperclass.equals(concreteClass.getSuperclass())) {
			throw new FaultyCodeException("La clase[" + concreteClass + "] debe ser subclase directa de ["
					+ expectedSuperclass + "]. Si no, no es posible determinar el tipo del parámetro");
		}
		final Type genericSuperclass = concreteClass.getGenericSuperclass();
		final ParameterizedType parameterizedType = (ParameterizedType) genericSuperclass;
		return parameterizedType.getActualTypeArguments()[0];
	}

	public Type getType() {
		return type;
	}

}
