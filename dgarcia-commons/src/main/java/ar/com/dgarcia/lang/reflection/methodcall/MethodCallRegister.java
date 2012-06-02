/**
 * 14/12/2005 19:36:10
 */
package ar.com.dgarcia.lang.reflection.methodcall;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Esta clase registra la llamada a metodos que se produce
 * sobre una interfaz, manteniendo la llamada como un objeto en s�
 * misma. 
 * Permite convertir la llamada a metodos en objetos y llevar un 
 * historial de las llamadas(sin ejecutarlas)
 * 
 * Ejemplo de uso:
 * 
 * 		interface Imprimible{ imprimir(String texto);}		
 * 
 * 		{@link MethodCallRegister} recorder = new {@link MethodCallRegister}();
 * 		Imprimible instancia = recorder.{@link #recordType}(Imprimible.class);
 * 		instancia.imprimir("Hola");
 * 		MethodCall lastMethodCall = recorder.{@link #getNextMethodCall()};
 * 		System.out.println(lastMethodCall);
 * 
 * Las llamadas se van acumulando en una cola, que los conserva
 * hasta que se llame al metodo {@link #getNextMethodCall()}
 * 
 * @version 1.0
 * @since 18/01/2007
 * @author D. Garcia 
 */
public class MethodCallRegister implements InvocationHandler {

	/**
	 * Registro de las llamadas a metodos 
	 */
	private Queue<MethodCall> methodCalls = new LinkedList<MethodCall>();

	/**
	 * Captura la llamada a un metodo registrandola en un objeto 
	 * @see java.lang.reflect.InvocationHandler#invoke(java.lang.Object, java.lang.reflect.Method, java.lang.Object[])
	 */
	public Object invoke(@SuppressWarnings("unused")
	Object proxy, Method method, Object[] args)
			throws Throwable {
		MethodCall methodCall = new MethodCall(method,args);
		this.methodCalls.offer(methodCall);
		return null;
	}

	/**
	 * @return Obtiene la ultima llamada a un metodo como objeto
	 * o null si no habia ninguna
	 */
	public MethodCall getNextMethodCall() {
		return this.methodCalls.poll();
	}

	/**
	 * Crea una instancia del tipo T para registrar las llamadas
	 * a metodos producidas sobre esa instancia. 
	 * Despu�s de cada llamada a un metodo sobre la instancia 
	 * devuelta se puede obtener el objeto {@link MethodCall} que
	 * representa la llamada hecha.
	 * Un {@link MethodCallRegister} esta dedicado a una unica
	 * instancia, por lo que si se crea mas de una instancia, 
	 * cada una puede pisar los metodos de la otra.
	 * @param <T> Tipo de la instancia que se quiere obtener
	 * @param interfaz Interfaz que define el tipo de la 
	 * instancia y los metodos que se desean registrar
	 * @return Una nueva instancia que registrara las llamadas
	 * a metodos que se hagan sobre ella
	 */
	@SuppressWarnings("unchecked")
	public<T> T recordType(Class<T> interfaz) {
		ClassLoader classLoader = interfaz.getClassLoader();
		Class<?>[] interfaces = new Class[]{interfaz};
		InvocationHandler handler = this;
		T instancia = (T) Proxy.newProxyInstance(classLoader,interfaces,handler);
		return instancia;
	}

}
