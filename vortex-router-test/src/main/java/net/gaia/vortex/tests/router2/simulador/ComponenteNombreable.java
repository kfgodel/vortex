/**
 * 07/12/2012 18:38:55 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.tests.router2.simulador;

import ar.com.dgarcia.lang.strings.ToString;

/**
 * Esta clase es la implementación de un componente que es identificable por nombre
 * 
 * @author D. García
 */
public class ComponenteNombreable extends ComponenteSimulable implements Nombrable {

	private String nombre;
	public static final String nombre_FIELD = "nombre";

	public String getNombre() {
		return nombre;
	}

	protected void setNombre(final String nombre) {
		this.nombre = nombre;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return ToString.de(this).con(nombre_FIELD, nombre).toString();
	}

}
