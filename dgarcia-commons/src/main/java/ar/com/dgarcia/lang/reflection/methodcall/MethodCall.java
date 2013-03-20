/**
 * 14/12/2005 19:36:31
 */
package ar.com.dgarcia.lang.reflection.methodcall;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

import ar.com.dgarcia.usercomm.msgs.BadException;
import ar.com.dgarcia.usercomm.msgs.ErroneousExecution;
import ar.com.dgarcia.usercomm.msgs.ErrorType;

/**
 * Esta clase representa la llamada a un m�todo con par�metros
 * determinados. Esta llamada puede utilizarse para separar
 * la llamada a un m�todo de su verdadera ejecuci�n.
 * A diferencia del objeto {@link java.lang.reflect.Method}
 * este objeto incluye los parametros con que fue lalmado el metodo 
 * 
 * @version 1.0
 * @since 18/01/2007
 * @author D. Garcia 
 */
public class MethodCall {

	/**
	 * Metodo a invocar en esta llamada
	 */
	private final Method method;
	/**
	 * Argumentos para usar en esta llamada
	 */
	private final Object[] arguments;

	/**
	 * Constructor de una llamada a un metodo
	 * @param method Metodo que se invoca
	 * @param arguments Argumentos con los que se invoca
	 */
	public MethodCall(Method method, Object[] arguments) {
		Class<?>[] parameterTypes = method.getParameterTypes();
		if(parameterTypes.length != arguments.length){
			ErroneousExecution.hasHappened("La cantidad de argumentos debiera ser igual", ErrorType.CONTRACT_VIOLATION);
		}
		for (int i = 0; i < parameterTypes.length; i++) {
			Class<?> parameterType = parameterTypes[i];
			Object argument = arguments[i];
			Class<?> argumentType = argument.getClass();
			if(!parameterType.isAssignableFrom(argumentType)){
				ErroneousExecution.hasHappened("El parametro ("+argument+")["+argumentType+"] no concide con el tipo esperado: " + parameterType +" en " + method , ErrorType.CONTRACT_VIOLATION);
			}
		}
		this.method = method;
		this.arguments = arguments;
	}
	
	/**
	 * Realiza esta llamada sobre el objeto pasado
	 * devolviendo el resultado de la ejecucion del metodo
	 * @param receiver Objeto sobre el que se ejecutara el
	 * metodo indicado por esta llamada
	 * @return El resultado de ejecutar el metodo contenido
	 * en esta llamada
	 */
	public Object invokeOn(Object receiver){
		try {
			return this.method.invoke(receiver,this.arguments);
		} catch (IllegalArgumentException e) {
			BadException.hasHappened("Se verifico que los argumentos coincidieran con la firma ["+method+"]:"+ Arrays.toString(arguments), e, ErrorType.CONTRADICTORY_EXECUTION);
		} catch (IllegalAccessException e) {
			BadException.hasHappened("No se permite acceder a "+method , e, ErrorType.BAD_CONFIGURATION);
		} catch (InvocationTargetException e) {
			BadException.hasHappened("Se produjo un error en el metodo llamado", e, ErrorType.UNHANDLED_SITUATION);
		}
		throw null;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	
	public String toString() {
		return this.method.getName() + "(" + Arrays.toString(arguments) + ")";
	}
}
