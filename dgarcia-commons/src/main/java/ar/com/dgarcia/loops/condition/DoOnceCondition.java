/**
 * Created on 14/01/2007 23:41:37
 * Copyright (C) 2007  Dario L. Garcia
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA 
 */
package ar.com.dgarcia.loops.condition;

import ar.com.dgarcia.lang.closures.Closure;
import ar.com.dgarcia.lang.closures.Condition;

/**
 * Esta clase representa una condicion que se cumple al realizarse
 * una accion. Esta clase tiene una forma dual, funciona como accion que
 * encapsula a la accion real, y como condicion que se verifica al realizarse
 * la accion.
 * 
 * @version 1.0
 * @since 14/01/2007
 * @author D. Garcia 
 * @param <T> Tipo de los elementos en los que se aplicara la accion
 */
public class DoOnceCondition<T> implements Condition<Object>, Closure<T>{
	/**
	 * Flag que indica que ya se realizo la action
	 */
	private boolean actionAlreadyDone;
	
	/**
	 * Accion real sobre la que esta condicion lleva control
	 */
	private Closure<? super T> baseAction;

	/**
	 * @see ar.com.dgarcia.lang.closures.Condition#isMetBy(java.lang.Object)
	 */
	public boolean isMetBy(@SuppressWarnings("unused")
			Object element) {
		return actionAlreadyDone;
	}
	
	/**
	 * Crea una condicion que se cumple cuando una accion es realizada.
	 * La accion debera modificar el estado de esta condicion para que
	 * se cumpla la condicion
	 * @param action 
	 * @param <T> Tipo de los elementos en los que se aplicara esta
	 * instancia como accion
	 * @return La condicion que verifica la flag para cumplirse
	 */
	public static<T> DoOnceCondition<T> create(Closure<? super T> action){
		DoOnceCondition<T> condition = new DoOnceCondition<T>();
		condition.baseAction = action;
		condition.actionAlreadyDone = false;
		return condition;
	}

	/**
	 * @see ar.com.dgarcia.lang.closures.Closure#evaluateOn(java.lang.Object)
	 */
	public void evaluateOn(T element) {
		this.baseAction.evaluateOn(element);
		this.actionAlreadyDone = true;
	}
}
