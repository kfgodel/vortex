/**
 * 16/01/2013 14:57:36 Copyright (C) 2011 Darío L. García
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

/**
 * Esta interfaz representa una variable utilizada en un tipo para poder parametrizar su
 * comportamiento
 * 
 * @author D. García
 */
public interface VariableGenerics {

	/**
	 * Devuelve el tipo que esta variable tiene asignada como valor
	 * 
	 * @return El tipo que representa el valor de esta variable
	 */
	Tipo getValor();

	/**
	 * Devuelve el nombre conque se identifica a esta variable
	 * 
	 * @return El nombre de esta variable
	 */
	String getNombre();

}
