/**
 * 15/01/2013 22:27:08 Copyright (C) 2011 Darío L. García
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

import java.util.List;

/**
 * Esta interfaz representa un tipo de datos definible en java que puede ser una clase, interfaz,
 * tipo primitivo o tipos complejos con argumentos de tipo usando generics
 * 
 * @author D. García
 */
public interface Tipo {

	/**
	 * Devuelve el linaje de este tipo que identifica a todos los supertipos que lo componen
	 * 
	 * @return El linaje concreto de este tipo
	 */
	Linaje getLinaje();

	/**
	 * Devuelve la lista de parámetros con el que este tipo está parametrizado. La lista puede ser
	 * vacía si este tipo no es parametrizable
	 * 
	 * @return La lista de parámetros
	 */
	List<Tipo> getParametrosGenerics();

	/**
	 * Devuelve la instancia de clase que este tipo representa
	 * 
	 * @return La clase representada por este tipo, o null si este tipo no representa a una clase
	 */
	Class<?> getClassInstance();

	/**
	 * Devuelve el tipo que este tipo especifuca como super
	 * 
	 * @return
	 */
	Tipo getSuperTipo();

	/**
	 * Devuelve el tipo del array que representa este tipo
	 * 
	 * @return null si este tipo no representa un array
	 */
	Tipo getTipoDelArray();

	/**
	 * Devuelve las variables que este tipo declara, indicando el tipo concreto de cada valor si es
	 * posible
	 * 
	 * @return La variables de este tipo resueltas si es posible
	 */
	List<VariableGenerics> getVariablesGenerics();

	/**
	 * Reemplaza las variables declaradas en este tipo con los valores de las variables pasadas. Si
	 * este tipo no tiene variables, o las variables no cambian el tipo se devuelve a sí mismo
	 * 
	 * @param variablesGenerics
	 *            La variables con sus valores de reemplazo
	 * @return El tipo obtenido reemplazando las variables
	 */
	Tipo reemplazando(List<VariableGenerics> variablesGenerics);

	/**
	 * Indica si este tipo representa a la clase pasada.<br>
	 * Este tipo representa a la clase pasada tanto si tiene parámetros generics definidos como si
	 * no.<br>
	 * Se devuelve false si este tipo no representa una clase
	 * 
	 * @param clase
	 *            La clase evaluada
	 * @return true si este tipo tiene como clase base a la clase pasada
	 */
	boolean representaA(Class<?> clase);

	/**
	 * Devuelve el nombre de variable si este tipo representa una variable de tipo
	 * 
	 * @return El nombre o null si este tipo no representa una variable
	 */
	String getNombreDeVariable();
}
