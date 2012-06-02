/**
 * 22/10/2005 18:59:35
 */
package ar.com.dgarcia.lang.reflection;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.Iterator;

import sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl;
import ar.com.dgarcia.lang.closures.Condition;
import ar.com.dgarcia.lang.closures.Expression;
import ar.com.dgarcia.lang.iterators.basic.ConditionalIterator;
import ar.com.dgarcia.lang.reflection.iterators.ClassMemberIterator;
import ar.com.dgarcia.lang.reflection.iterators.SuperClassIterator;

/**
 * Esta clase reune metodos utiles para trabajar sobre los objetos sus clases y
 * otras hierbas reflexivas
 *
 * @version 1.0
 * @since 18/01/2007
 * @author D. Garcia
 */
public class ReflectionUtils {

	/**
	 * Este enum permite definir el tipo de miembro sobre el que se operara
	 *
	 * @author D. Garcia
	 */
	public enum MemberType implements Expression<Class<?>, Member[]> {
		/**
		 * Identifica los constructores de una clase
		 */
		CONSTRUCTOR {
			public Constructor<?>[] evaluateOn(Class<?> element) {
				return element.getDeclaredConstructors();
			}
		},
		/**
		 * Identifica los atributos de una clase
		 */
		FIELD {
			public Field[] evaluateOn(Class<?> element) {
				return element.getDeclaredFields();
			}
		},
		/**
		 * Identifica al tipo de los metodos
		 */
		METHOD {
			public Method[] evaluateOn(Class<?> element) {
				return element.getDeclaredMethods();
			}
		};

		/**
		 * @param <T>
		 *            Tipo de la clase y constructores
		 * @return Devuelve la expresion que permite extraer los metodos de una
		 *         clase
		 */
		@SuppressWarnings("unchecked")
		public static <T> Expression<Class<T>, Constructor<T>[]> getConstructorExtractor() {
			return (Expression) CONSTRUCTOR;
		}

		/**
		 * @return Devuelve la expresion que permite extraer los metodos de una
		 *         clase
		 */
		@SuppressWarnings("unchecked")
		public static Expression<Class<?>, Field[]> getFieldExtractor() {
			return (Expression) FIELD;
		}

		/**
		 * @return Devuelve la expresion que permite extraer los metodos de una
		 *         clase
		 */
		@SuppressWarnings("unchecked")
		public static Expression<Class<?>, Method[]> getMethodExtractor() {
			return (Expression) METHOD;
		}
	};

	/**
	 * Crea una nueva instancia de la clase pasada utilizando el constructor
	 * niladico
	 *
	 * @param <T>
	 *            Tipo del objeto a crear
	 * @param clase
	 *            Clase de la instancia creada
	 * @return La nueva instancia
	 */
	public static <T> T createInstance(Class<T> clase) {
		try {
			T instancia = clase.newInstance();
			return instancia;
		} catch (InstantiationException e) {
			throw new RuntimeException("Existen permisos que impiden esta accion?", e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException("Se produjo un error o no existe constructor niladico", e);
		}
	}

	/**
	 * Devuelve la clase que representa el tipo basico del tipo pasado
	 * sin los parametros genericos
	 * @param genericType Tipo generificado
	 * @return La instancia de clase que representa el tipo pasado o
	 * null si no se pudo obtener un tipo concreto del tipo pasado
	 */
	public static Class<?> degenerify(Type genericType) {
		if(genericType instanceof Class){
			return (Class<?>) genericType;
		}
		if (genericType instanceof ParameterizedType) {
			ParameterizedType parameterized = (ParameterizedType) genericType;
			return (Class<?>) parameterized.getRawType();
		}
		if (genericType instanceof TypeVariable) {
			TypeVariable<?> typeVariable = (TypeVariable<?>) genericType;
			return (Class<?>) typeVariable.getBounds()[0];
		}
		if (genericType instanceof WildcardType) {
			WildcardType wildcard = (WildcardType) genericType;
			Type[] upperBounds = wildcard.getUpperBounds();
			if(upperBounds.length > 0){
				return degenerify(upperBounds[0]);
			}
			Type[] lowerBounds = wildcard.getLowerBounds();
			if(lowerBounds.length > 0){
				return degenerify(lowerBounds[0]);
			}
		}
		return null;
	}

	/**
	 * Genera y devuelve un iterador que permite recorrer todos los
	 * constructores de la clase pasada, incluidos los heredados
	 *
	 * @param <T>
	 *            Tipo de la clase y constructores
	 * @param clase
	 *            Clase a partir de la cual se obtendran todos los constructores
	 * @return Un iterador con todos los constructores
	 */
	@SuppressWarnings("unchecked")
	public static <T> Iterator<Constructor<T>> getAllConstructorsOf(Class<T> clase) {
		Expression<Class<T>, Constructor<T>[]> constructorExtractor = MemberType
				.getConstructorExtractor();
		ClassMemberIterator<Constructor<T>> constructorIterator = ClassMemberIterator.create(clase, (Expression)constructorExtractor);
		return constructorIterator;
	}

	/**
	 * Genera y devuelve un iterador que permite recorrer todos los atributos de
	 * la clase pasada, incluidos los heredados
	 *
	 * @param clase
	 *            Clase a partir de la cual se obtendran todos los atributos
	 * @return Un iterador con todos los atributos
	 */
	public static Iterator<Field> getAllFieldsOf(Class<?> clase) {
		Expression<Class<?>, Field[]> fieldExtractor = MemberType.getFieldExtractor();
		ClassMemberIterator<Field> fieldIterator = ClassMemberIterator.create(clase, fieldExtractor);
		return fieldIterator;
	}

	/**
	 * Genera y devuelve un iterador que permite recorrer todos los metodos de
	 * la clase pasada, incluidos los heredados
	 *
	 * @param clase
	 *            Clase a partir de la cual se obtendran todos los metodos
	 * @return Un iterador con todos los métodos
	 */
	public static Iterator<Method> getAllMethodsOf(Class<?> clase) {
		Expression<Class<?>, Method[]> methodExtractor = MemberType.getMethodExtractor();
		ClassMemberIterator<Method> methodIterator = ClassMemberIterator.create(clase, methodExtractor);
		return methodIterator;
	}

	/**
	 * Busca en la clase pasada los constructores que cumplen la condicion dada
	 *
	 * @param condition
	 *            Condicion que deben cumplir los constructores
	 * @param clazz
	 *            Clase de la que obtendran los constructores
	 * @return Una lista con todos los constructores (tanto privados como
	 *         heredados tambien) de la clase que cumplan la condicion pasada
	 */
	public static Iterator<Constructor<?>> getConstructorsThatMeet(Condition<? super Constructor<?>> condition, Class<?> clazz) {
		return ReflectionUtils.getMembersThatMeet(condition, MemberType.CONSTRUCTOR, clazz);
	}

	/**
	 * Devuelve el tipo que corresponde al primer parametro del tipo pasado.<br>
	 * @param genericType Un tipo parametrizado del que se obtendra el
	 * primer parametro
	 * @return El tipo correspondiente al primer tipo o null si no existe ninguno,
	 * el tipo no es parametrizado
	 */
	public static Type getElementTypeParameterFrom(Type genericType) {
		Class<?> concreteClass = degenerify(genericType);
		if(concreteClass == null){
			return null;
		}
		if(concreteClass.isArray()){
			return concreteClass.getComponentType();
		}
		if (!(genericType instanceof ParameterizedType)) {
			return null;
		}
		ParameterizedType parameterizedType = (ParameterizedType) genericType;
		Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
		if (actualTypeArguments.length < 1) {
			return null;
		}
		Type elementType = actualTypeArguments[0];
		return elementType;

	}

	/**
	 * Busca en la clase pasada los atributos que cumplen la condicion dada
	 *
	 * @param condition
	 *            Condicion que deben cumplir los atributos
	 * @param clazz
	 *            Clase de la que obtendran los atributos
	 * @return Una lista con todos los atributos (tanto privados como heredados
	 *         tambien) de la clase que cumplan la condicion pasada
	 */
	public static Iterator<Field> getFieldsThatMeet(Condition<? super Field> condition, Class<?> clazz) {
		return ReflectionUtils.getMembersThatMeet(condition, MemberType.FIELD, clazz);
	}

	/**
	 * Busca en la clase pasada los miembros que cumplen la condicion dada
	 * buscando hacia arriba en la jerarquia de clases
	 *
	 * @param <T>
	 *            Tipo de miembro buscado
	 * @param condition
	 *            Condicion que deben cumplir los miembros
	 * @param memberType
	 *            Tipo de miembro buscado
	 * @param clazz
	 *            Clase de la que obtendran los miembros
	 * @return Una lista con todos los miembros (tanto privados como heredados
	 *         tambien) de la clase que cumplan la condicion pasada
	 */
	@SuppressWarnings("unchecked")
	public static <T extends Member> Iterator<T> getMembersThatMeet(Condition<? super T> condition,
			MemberType memberType, Class clazz) {
		Iterator<T> memberIterator = ClassMemberIterator.create(clazz, memberType);
		ConditionalIterator<T> matchedMembersIterator = ConditionalIterator.createFrom(condition, memberIterator);
		return matchedMembersIterator;
	}

	/**
	 * Busca en la clase pasada los metodos que cumplen la condicion dada
	 *
	 * @param condition
	 *            Condicion que deben cumplir los metodos
	 * @param clazz
	 *            Clase de la que obtendran los metodos
	 * @return Una lista con todos los metodos (tanto privados como heredados
	 *         tambien) de la clase que cumplan la condicion pasada
	 */
	public static Iterator<Method> getMethodsThatMeet(Condition<? super Method> condition, Class<?> clazz) {
		return ReflectionUtils.getMembersThatMeet(condition, MemberType.METHOD, clazz);
	}

	/**
	 * Crea una instancia de tipo parametrizado que representa una clase con
	 * generics
	 * @param rawClass Clase base que esta parametrizada
	 * @param typeParameters Valores de los argumentos con los que esta
	 * parametrizada
	 * @return La instancia de tipo que representa la clase parametrizada
	 */
	public static ParameterizedType getParametricType(Class<?> rawClass, Type... typeParameters){
		ParameterizedTypeImpl parameterizedTypeImpl = ParameterizedTypeImpl.make(rawClass, typeParameters, null);
		return parameterizedTypeImpl;
	}


	/**
	 * Devuelve la clase que representa el tipo de elementos que tiene una
	 * coleccion. Ej: List<Integer> -> Integer Mediante este metodo se puede
	 * saber la clase que corresponde al primer parametro generico de la
	 * declaracion de tipo pasada
	 *
	 * @param type
	 *            Declaracion de un tipo
	 * @return La clase que corresponde o null si no existe ninguna
	 */
	@Deprecated
	public static Class<?> getTypeParameterOfCollectionDeclaration(Type type) {
		if (!(type instanceof ParameterizedType)) {
			throw new RuntimeException("El tipo pasado no esta parametrizado");
		}
		ParameterizedType parameterizedType = (ParameterizedType) type;
		Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
		if (actualTypeArguments.length != 1) {
			throw new RuntimeException("El tipo pasado no esta parametrizado con 1 tipo");
		}
		Type elementType = actualTypeArguments[0];
		return (Class<?>) elementType;
	}

	/**
	 * Busca un atributo por su nombre, partiendo desde la clase pasada y
	 * subiendo a la superclase si no lo encuentra
	 *
	 * @param atributo
	 *            Nombre del atributo buscado
	 * @param clase
	 *            Clase a partir de la que se buscara
	 * @return El primer atributo encontrado o null si no hay ninguno con ese
	 *         nombre
	 */
	public static Field lookupField(String atributo, Class<? extends Object> clase) {
		@SuppressWarnings("unchecked")
		Iterator<Class<?>> classes = (Iterator)SuperClassIterator.createFrom(clase);
		while (classes.hasNext()) {
			Class<?> currentClass = classes.next();
			Field declaredField;
			try {
				declaredField = currentClass.getDeclaredField(atributo);
				return declaredField;
			} catch (SecurityException e) {
				throw new RuntimeException("Existe un permiso de seguridad?", e);
			} catch (NoSuchFieldException e) {
				// Se continua con el bucle
			}
		}
		return null;
	}

	/**
	 * Busca un metodo por su nombre, partiendo desde la clase pasada y subiendo
	 * a la superclase si no lo encuentra
	 *
	 * @param methodName
	 *            Nombre del atributo buscado
	 * @param clase
	 *            Clase a partir de la que se buscará
	 * @return El primer metodo encontrado o null si no hay ninguno con ese
	 *         nombre
	 */
	public static Method lookupMethod(final String methodName, Class<? extends Object> clase) {
		Condition<Method> hasSameName = new Condition<Method>() {
			public boolean isMetBy(Method metodo) {
				return metodo.getName().equals(methodName);
			}
		};
		Iterator<Method> methodsThatMeet = getMethodsThatMeet(hasSameName, clase);
		while(methodsThatMeet.hasNext()){
			return methodsThatMeet.next();
		}
		return null;
	}
}
