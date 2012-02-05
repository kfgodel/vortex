/**
 * 05/02/2012 13:59:53 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.lowlevel.api;

/**
 * Esta clase representa un resumen de los errores en un mensaje. Permite conocer el motivo de la
 * falla
 * 
 * @author D. García
 */
public class ErroresDelMensaje {

	private String descripcion;

	private Throwable excepcion;

	public Throwable getExcepcion() {
		return excepcion;
	}

	public void setExcepcion(final Throwable excepcion) {
		this.excepcion = excepcion;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(final String descripcion) {
		this.descripcion = descripcion;
	}

	public static ErroresDelMensaje create(final String descripcion) {
		final ErroresDelMensaje name = new ErroresDelMensaje();
		name.descripcion = descripcion;
		return name;
	}
}
