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
import java.util.Arrays;
import java.util.Iterator;
import java.util.Queue;
import java.util.TreeMap;
import java.util.concurrent.LinkedBlockingQueue;

import sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl;
import ar.com.dgarcia.coding.anno.MayBeNull;
import ar.com.dgarcia.lang.closures.Condition;
import ar.com.dgarcia.lang.closures.Expression;
import ar.com.dgarcia.lang.iterators.basic.ConditionalIterator;
import ar.com.dgarcia.lang.reflection.iterators.ClassMemberIterator;
import ar.com.dgarcia.lang.reflection.iterators.SuperClassIterator;

/**
 * Esta clase reune metodos utiles para trabajar sobre los objetos sus clases y otras hierbas
 * reflexivas
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
			
			public Constructor<?>[] evaluateOn(final Class<?> element) {
				return element.getDeclaredConstructors();
			}
		},
		/**
		 * Identifica los atributos de una clase
		 */
		FIELD {
			
			public Field[] evaluateOn(final Class<?> element) {
				return element.getDeclaredFields();
			}
		},
		/**
		 * Identifica al tipo de los metodos
		 */
		METHOD {
			
			public Method[] evaluateOn(final Class<?> element) {
				return element.getDeclaredMethods();
			}
		};

		/**
		 * @param <T>
		 *            Tipo de la clase y constructores
		 * @return Devuelve la expresion que permite extraer los metodos de una clase
		 */
		@SuppressWarnings("unchecked")
		public static <T> Expression<Class<T>, Constructor<T>[]> getConstructorExtractor() {
			return (Expression) CONSTRUCTOR;
		}

		/**
		 * @return Devuelve la expresion que permite extraer los metodos de una clase
		 */
		@SuppressWarnings("unchecked")
		public static Expression<Class<?>, Field[]> getFieldExtractor() {
			return (Expression) FIELD;
		}

		/**
		 * @return Devuelve la expresion que permite extraer los metodos de una clase
		 */
		@SuppressWarnings("unchecked")
		public static Expression<Class<?>, Method[]> getMethodExtractor() {
			return (Expression) METHOD;
		}
	};

	/**
	 * Crea una nueva instancia de la clase pasada utilizando el constructor niladico
	 * 
	 * @param <T>
	 *            Tipo del objeto a crear
	 * @param clase
	 *            Clase de la instancia creada
	 * @return La nueva instancia
	 */
	public static <T> T createInstance(final Class<T> clase) {
		try {
			final T instancia = clase.newInstance();
			return instancia;
		} catch (final InstantiationException e) {
			throw new RuntimeException("Existen permisos que impiden esta accion?", e);
		} catch (final IllegalAccessException e) {
			throw new RuntimeException("Se produjo un error o no existe constructor niladico", e);
		}
	}

	/**
	 * Devuelve la clase que representa el tipo basico del tipo pasado sin los parametros genericos
	 * 
	 * @param genericType
	 *            Tipo generificado
	 * @return La instancia de clase que representa el tipo pasado o null si no se pudo obtener un
	 *         tipo concreto del tipo pasado
	 */
	public static Class<?> degenerify(final Type genericType) {
		if (genericType instanceof Class) {
			return (Class<?>) genericType;
		}
		if (genericType instanceof ParameterizedType) {
			final ParameterizedType parameterized = (ParameterizedType) genericType;
			return (Class<?>) parameterized.getRawType();
		}
		if (genericType instanceof TypeVariable) {
			final TypeVariable<?> typeVariable = (TypeVariable<?>) genericType;
			return (Class<?>) typeVariable.getBounds()[0];
		}
		if (genericType instanceof WildcardType) {
			final WildcardType wildcard = (WildcardType) genericType;
			final Type[] upperBounds = wildcard.getUpperBounds();
			if (upperBounds.length > 0) {
				return degenerify(upperBounds[0]);
			}
			final Type[] lowerBounds = wildcard.getLowerBounds();
			if (lowerBounds.length > 0) {
				return degenerify(lowerBounds[0]);
			}
		}
		return null;
	}

	/**
	 * Genera y devuelve un iterador que permite recorrer todos los constructores de la clase
	 * pasada, incluidos los heredados
	 * 
	 * @param <T>
	 *            Tipo de la clase y constructores
	 * @param clase
	 *            Clase a partir de la cual se obtendran todos los constructores
	 * @return Un iterador con todos los constructores
	 */
	@SuppressWarnings("unchecked")
	public static <T> Iterator<Constructor<T>> getAllConstructorsOf(final Class<T> clase) {
		final Expression<Class<T>, Constructor<T>[]> constructorExtractor = MemberType.getConstructorExtractor();
		final ClassMemberIterator<Constructor<T>> constructorIterator = ClassMemberIterator.create(clase,
				(Expression) constructorExtractor);
		return constructorIterator;
	}

	/**
	 * Genera y devuelve un iterador que permite recorrer todos los atributos de la clase pasada,
	 * incluidos los heredados
	 * 
	 * @param clase
	 *            Clase a partir de la cual se obtendran todos los atributos
	 * @return Un iterador con todos los atributos
	 */
	public static Iterator<Field> getAllFieldsOf(final Class<?> clase) {
		final Expression<Class<?>, Field[]> fieldExtractor = MemberType.getFieldExtractor();
		final ClassMemberIterator<Field> fieldIterator = ClassMemberIterator.create(clase, fieldExtractor);
		return fieldIterator;
	}

	/**
	 * Genera y devuelve un iterador que permite recorrer todos los metodos de la clase pasada,
	 * incluidos los heredados
	 * 
	 * @param clase
	 *            Clase a partir de la cual se obtendran todos los metodos
	 * @return Un iterador con todos los métodos
	 */
	public static Iterator<Method> getAllMethodsOf(final Class<?> clase) {
		final Expression<Class<?>, Method[]> methodExtractor = MemberType.getMethodExtractor();
		final ClassMemberIterator<Method> methodIterator = ClassMemberIterator.create(clase, methodExtractor);
		return methodIterator;
	}

	/**
	 * Busca en la clase pasada los constructores que cumplen la condicion dada
	 * 
	 * @param condition
	 *            Condicion que deben cumplir los constructores
	 * @param clazz
	 *            Clase de la que obtendran los constructores
	 * @return Una lista con todos los constructores (tanto privados como heredados tambien) de la
	 *         clase que cumplan la condicion pasada
	 */
	public static Iterator<Constructor<?>> getConstructorsThatMeet(final Condition<? super Constructor<?>> condition,
			final Class<?> clazz) {
		return ReflectionUtils.getMembersThatMeet(condition, MemberType.CONSTRUCTOR, clazz);
	}

	/**
	 * Devuelve el tipo que corresponde al primer parametro del tipo pasado.<br>
	 * 
	 * @param genericType
	 *            Un tipo parametrizado del que se obtendra el primer parametro
	 * @return El tipo correspondiente al primer tipo o null si no existe ninguno, el tipo no es
	 *         parametrizado
	 */
	public static Type getElementTypeParameterFrom(final Type genericType) {
		final Class<?> concreteClass = degenerify(genericType);
		if (concreteClass == null) {
			return null;
		}
		if (concreteClass.isArray()) {
			return concreteClass.getComponentType();
		}
		if (!(genericType instanceof ParameterizedType)) {
			return null;
		}
		final ParameterizedType parameterizedType = (ParameterizedType) genericType;
		final Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
		if (actualTypeArguments.length < 1) {
			return null;
		}
		final Type elementType = actualTypeArguments[0];
		return elementType;

	}

	/**
	 * Busca en la clase pasada los atributos que cumplen la condicion dada
	 * 
	 * @param condition
	 *            Condicion que deben cumplir los atributos
	 * @param clazz
	 *            Clase de la que obtendran los atributos
	 * @return Una lista con todos los atributos (tanto privados como heredados tambien) de la clase
	 *         que cumplan la condicion pasada
	 */
	public static Iterator<Field> getFieldsThatMeet(final Condition<? super Field> condition, final Class<?> clazz) {
		return ReflectionUtils.getMembersThatMeet(condition, MemberType.FIELD, clazz);
	}

	/**
	 * Busca en la clase pasada los miembros que cumplen la condicion dada buscando hacia arriba en
	 * la jerarquia de clases
	 * 
	 * @param <T>
	 *            Tipo de miembro buscado
	 * @param condition
	 *            Condicion que deben cumplir los miembros
	 * @param memberType
	 *            Tipo de miembro buscado
	 * @param clazz
	 *            Clase de la que obtendran los miembros
	 * @return Una lista con todos los miembros (tanto privados como heredados tambien) de la clase
	 *         que cumplan la condicion pasada
	 */
	@SuppressWarnings("unchecked")
	public static <T extends Member> Iterator<T> getMembersThatMeet(final Condition<? super T> condition,
			final MemberType memberType, final Class clazz) {
		final Iterator<T> memberIterator = ClassMemberIterator.create(clazz, memberType);
		final ConditionalIterator<T> matchedMembersIterator = ConditionalIterator.createFrom(condition, memberIterator);
		return matchedMembersIterator;
	}

	/**
	 * Busca en la clase pasada los metodos que cumplen la condicion dada
	 * 
	 * @param condition
	 *            Condicion que deben cumplir los metodos
	 * @param clazz
	 *            Clase de la que obtendran los metodos
	 * @return Una lista con todos los metodos (tanto privados como heredados tambien) de la clase
	 *         que cumplan la condicion pasada
	 */
	public static Iterator<Method> getMethodsThatMeet(final Condition<? super Method> condition, final Class<?> clazz) {
		return ReflectionUtils.getMembersThatMeet(condition, MemberType.METHOD, clazz);
	}

	/**
	 * Crea una instancia de tipo parametrizado que representa una clase con generics
	 * 
	 * @param rawClass
	 *            Clase base que esta parametrizada
	 * @param typeParameters
	 *            Valores de los argumentos con los que esta parametrizada
	 * @return La instancia de tipo que representa la clase parametrizada
	 */
	public static ParameterizedType getParametricType(final Class<?> rawClass, final Type... typeParameters) {
		final ParameterizedTypeImpl parameterizedTypeImpl = ParameterizedTypeImpl.make(rawClass, typeParameters, null);
		return parameterizedTypeImpl;
	}

	/**
	 * Devuelve la clase que representa el tipo de elementos que tiene una coleccion. Ej:
	 * List<Integer> -> Integer Mediante este metodo se puede saber la clase que corresponde al
	 * primer parametro generico de la declaracion de tipo pasada
	 * 
	 * @param type
	 *            Declaracion de un tipo
	 * @return La clase que corresponde o null si no existe ninguna
	 */
	@Deprecated
	public static Class<?> getTypeParameterOfCollectionDeclaration(final Type type) {
		if (!(type instanceof ParameterizedType)) {
			throw new RuntimeException("El tipo pasado no esta parametrizado");
		}
		final ParameterizedType parameterizedType = (ParameterizedType) type;
		final Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
		if (actualTypeArguments.length != 1) {
			throw new RuntimeException("El tipo pasado no esta parametrizado con 1 tipo");
		}
		final Type elementType = actualTypeArguments[0];
		return (Class<?>) elementType;
	}

	/**
	 * Busca un atributo por su nombre, partiendo desde la clase pasada y subiendo a la superclase
	 * si no lo encuentra
	 * 
	 * @param atributo
	 *            Nombre del atributo buscado
	 * @param clase
	 *            Clase a partir de la que se buscara
	 * @return El primer atributo encontrado o null si no hay ninguno con ese nombre
	 */
	public static Field lookupField(final String atributo, final Class<? extends Object> clase) {
		@SuppressWarnings("unchecked")
		final Iterator<Class<?>> classes = (Iterator) SuperClassIterator.createFrom(clase);
		while (classes.hasNext()) {
			final Class<?> currentClass = classes.next();
			Field declaredField;
			try {
				declaredField = currentClass.getDeclaredField(atributo);
				return declaredField;
			} catch (final SecurityException e) {
				throw new RuntimeException("Existe un permiso de seguridad?", e);
			} catch (final NoSuchFieldException e) {
				// Se continua con el bucle
			}
		}
		return null;
	}

	/**
	 * Busca un metodo por su nombre, partiendo desde la clase pasada y subiendo a la superclase si
	 * no lo encuentra
	 * 
	 * @param methodName
	 *            Nombre del atributo buscado
	 * @param clase
	 *            Clase a partir de la que se buscará
	 * @return El primer metodo encontrado o null si no hay ninguno con ese nombre
	 */
	public static Method lookupMethod(final String methodName, final Class<? extends Object> clase) {
		final Condition<Method> hasSameName = new Condition<Method>() {
			
			public boolean isMetBy(final Method metodo) {
				return metodo.getName().equals(methodName);
			}
		};
		final Iterator<Method> methodsThatMeet = getMethodsThatMeet(hasSameName, clase);
		while (methodsThatMeet.hasNext()) {
			return methodsThatMeet.next();
		}
		return null;
	}

	/**
	 * Busca de la clase pasada como parametrizable, el primer valor concreto con el cual está
	 * parametrizada una subclase pasada como segundo parámetro.<br>
	 * Si la segunda clase no es subclase de la primera, o la primera no es parametrizable no se
	 * encontrará el valor del parámetro y por lo tanto se devuelve null
	 */
	@MayBeNull
	public static Class<?> getConcreteTypeForFirstParameterOf(final Class<?> parametrizableType, final Class<?> subclass) {
		final String parametrizableClassname = parametrizableType.getName();

		final Queue<Type> possibleTypes = new LinkedBlockingQueue<Type>();
		possibleTypes.add(subclass);

		while (!possibleTypes.isEmpty()) {
			final Type currentType = possibleTypes.poll();
			final Class<?> currentClass = ReflectionUtils.degenerify(currentType);
			final String currentClassname = currentClass.getName();
			if (currentClassname.equals(parametrizableClassname)) {
				// Es la clase que estabamos buscando
				final Class<?> firstParameterClass = getFirstTypeParameterAsClass(currentType);
				return firstParameterClass;
			}

			// Seguimos buscando en la jerarquía
			final Type genericSuperclass = currentClass.getGenericSuperclass();
			if (genericSuperclass != null) {
				possibleTypes.add(genericSuperclass);
			}
			final Type[] genericInterfaces = currentClass.getGenericInterfaces();
			possibleTypes.addAll(Arrays.asList(genericInterfaces));
		}

		return null;
	}

	/**
	 * Devuelve el primer parámetro de tipo del {@link Type} pasado e interpretado como
	 * {@link ParameterizedType}.<br>
	 * Devuelve <b>null</b> en caso de que el tipo sea parametrizable pero no tenga ningún parámetro
	 * asociado<br>
	 * En caso de que el tipo no sea parametrizable se lanza una excepción.
	 * 
	 * @param type
	 *            Tipo del cual se desea extraer el parámetro
	 * @return Primer parámetro del tipo. null en caso de que sea parametrizable pero no tenga
	 *         ningún parámetro asociado
	 * @throws IllegalArgumentException
	 *             En caso de que el tipo no sea parametrizable
	 */
	@MayBeNull
	private static Class<?> getFirstTypeParameterAsClass(final Type type) throws IllegalArgumentException {
		if (!(type instanceof ParameterizedType)) {
			throw new IllegalArgumentException("El tipo [" + type
					+ "] no es parametrizado por lo tanto no es posible devolver el primer parámetro");
		}
		final Type[] parameters = ((ParameterizedType) type).getActualTypeArguments();
		if (parameters.length < 1) {
			// El tipo es parametrizable, pero no tiene ningún parámetro
			return null;
		}
		final Type parameterType = parameters[0];
		final Class<?> returnClass = ReflectionUtils.degenerify(parameterType);
		return returnClass;
	}

	/**
	 * Calcula y devuelve el hash para un par de objetos.<br>
	 * Este método es una facilidad basado en la implementacion de
	 * {@link TreeMap.Entry#equals(Object)}
	 * 
	 * @param primero
	 *            El primero obejto (puede sr null)
	 * @param segundo
	 *            El segundo (puede ser null)
	 * @return El valor de hash para representar los objetos pasados como una sola cosa
	 */
	public static int hashDeDosValores(final Object primero, final Object segundo) {
		final int keyHash = (primero == null ? 0 : primero.hashCode());
		final int valueHash = (segundo == null ? 0 : segundo.hashCode());
		final int valorDeHash = keyHash ^ valueHash;
		return valorDeHash;
	}
}
