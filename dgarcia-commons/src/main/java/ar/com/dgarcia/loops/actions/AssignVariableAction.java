package ar.com.dgarcia.loops.actions;

import ar.com.dgarcia.java.lang.SharedVariable;
import ar.com.dgarcia.lang.closures.Closure;

/**
 * Esta clase representa la accion de asignar a una variable
 * el valor encontrado
 * 
 * @param <T> Tipo del elemento a asignar
 * 
 * @version 1.0
 * @since 15/01/2007
 * @author D. Garcia 
 */
public class AssignVariableAction<T> implements Closure<T> {
	/**
	 * Variable sobre la que se hara la asignacion
	 */
	private SharedVariable<T> variable;

	/**
	 * Crea una nueva accion que asignara en la variable indicada cuando
	 * sea ejecutada
	 * @param <T> Tipo de elemento a asignar
	 * @param variable Variable sobre la que se hara la asignacion
	 * @return La nueva accion
	 */
	public static<T> AssignVariableAction<T> createFor(SharedVariable<T> variable) {
		AssignVariableAction<T> action = new AssignVariableAction<T>();
		action.variable = variable;
		return action;
	}

	/**
	 * Asigna el valor pasado sobre la variable
	 * @see ar.com.dgarcia.lang.closures.Closure#evaluateOn(java.lang.Object)
	 */
	public void evaluateOn(T value) {
		variable.set(value);
	}
}