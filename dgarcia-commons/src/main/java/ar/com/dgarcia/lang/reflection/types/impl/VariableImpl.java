/**
 * 16/01/2013 15:25:43 Copyright (C) 2011 Darío L. García
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
package ar.com.dgarcia.lang.reflection.types.impl;

import ar.com.dgarcia.lang.reflection.types.Tipo;
import ar.com.dgarcia.lang.reflection.types.VariableGenerics;
import ar.com.dgarcia.lang.strings.ToString;

/**
 * Esta clase representa una variable de tipo
 * 
 * @author D. García
 */
public class VariableImpl implements VariableGenerics {

	private String nombre;
	public static final String nombre_FIELD = "nombre";

	private Tipo valor;
	public static final String valor_FIELD = "valor";

	/**
	 * @see ar.com.dgarcia.lang.reflection.types.VariableGenerics#getValor()
	 */
	
	public Tipo getValor() {
		return valor;
	}

	/**
	 * @see ar.com.dgarcia.lang.reflection.types.VariableGenerics#getNombre()
	 */
	
	public String getNombre() {
		return nombre;
	}

	public void setValor(final Tipo valor) {
		this.valor = valor;
	}

	public static VariableImpl create(final String nombre, final Tipo valor) {
		final VariableImpl variable = new VariableImpl();
		variable.nombre = nombre;
		variable.valor = valor;
		return variable;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	
	public String toString() {
		return ToString.de(this).con(nombre_FIELD, nombre).con(valor_FIELD, valor).toString();
	}

}
