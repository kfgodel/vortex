/**
 * 16/01/2013 13:17:56 Copyright (C) 2011 Darío L. García
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

import java.util.ArrayList;

import ar.com.dgarcia.lang.reflection.types.Linaje;
import ar.com.dgarcia.lang.reflection.types.Tipo;

/**
 * Esta clase permite armar el linaje de una clase reconstruyendo la información de generics con los
 * tipos concretos
 * 
 * @author D. García
 */
public class ArmadorDeLinaje {

	private Tipo tipoBase;

	/**
	 * Regenera el linaje de la indicada como base reemplazando las variables generics con tipos
	 * concretos
	 * 
	 * @return El linaje reconstruido
	 */
	public Linaje armarLinaje() {
		Tipo tipoActual = tipoBase;
		final ArrayList<Tipo> tiposDelLinaje = new ArrayList<Tipo>();
		while (tipoActual != null) {
			tiposDelLinaje.add(tipoActual);
			tipoActual = tipoActual.getSuperTipo();
		}

		final LinajeConcreto linaje = LinajeConcreto.create(tiposDelLinaje);
		return linaje;
	}

	public static ArmadorDeLinaje create(final Tipo tipoBase) {
		final ArmadorDeLinaje armador = new ArmadorDeLinaje();
		armador.tipoBase = tipoBase;
		return armador;
	}
}
