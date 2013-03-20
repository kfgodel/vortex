/**
 * 16/01/2013 13:09:01 Copyright (C) 2011 Darío L. García
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

import java.util.List;

import ar.com.dgarcia.lang.reflection.types.Linaje;
import ar.com.dgarcia.lang.reflection.types.Tipo;
import ar.com.dgarcia.lang.strings.ToString;

/**
 * Esta clase representa el linaje de una clase con los tipos concretos
 * 
 * @author D. García
 */
public class LinajeConcreto implements Linaje {

	private List<Tipo> tipos;
	public static final String tipos_FIELD = "tipos";

	/**
	 * @see ar.com.dgarcia.lang.reflection.types.Linaje#getTipoDe(java.lang.Class)
	 */
	
	public Tipo getTipoDe(final Class<?> clase) {
		for (final Tipo tipo : tipos) {
			if (tipo.representaA(clase)) {
				return tipo;
			}
		}
		return null;
	}

	public static LinajeConcreto create(final List<Tipo> tiposDelLinaje) {
		final LinajeConcreto linaje = new LinajeConcreto();
		linaje.tipos = tiposDelLinaje;
		return linaje;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	
	public String toString() {
		return ToString.de(this).con(tipos_FIELD, tipos).toString();
	}

	/**
	 * @see ar.com.dgarcia.lang.reflection.types.Linaje#getTiposDesdeLaSubclase()
	 */
	
	public List<Tipo> getTiposDesdeLaSubclase() {
		return tipos;
	}

}
