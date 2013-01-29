/**
 * 15/01/2013 22:38:01 Copyright (C) 2011 Darío L. García
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
 * Esta interfaz representa el conjunto de tipos que una clase implementa o define como jerarquía.<br>
 * A diferencias de las clases o tipos sueltos, un linaje especifica los parámetros genéricos con
 * tipos concretos
 * 
 * @author D. García
 */
public interface Linaje {

	/**
	 * Devuelve el tipo dentro de este linaje correspondiente a la clase indicada.<br>
	 * El tipo devuelto corresponderá con uno de los que están en la cadena de herencia
	 * 
	 * @param clase
	 *            La clase que sirve de referencia para acceder al tipo
	 * @return El tipo correspondiente a la clase o null si no hay ningun tipo de esa clase en este
	 *         linaje
	 */
	Tipo getTipoDe(Class<?> clase);

	/**
	 * Devuelve la lista de tipos ordenando primero la subclase del linaje
	 * 
	 * @return La lista de tipos
	 */
	List<Tipo> getTiposDesdeLaSubclase();
}
