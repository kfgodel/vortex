/**
 * 15/07/2012 18:42:34 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.comm.impl;

import net.gaia.vortex.core.api.mensaje.MensajeVortex;
import net.gaia.vortex.core.api.transformaciones.Transformacion;
import net.gaia.vortex.core.impl.mensaje.ContenidoPrimitiva;
import ar.com.dgarcia.lang.strings.ToString;

/**
 * Esta clase representa una transformación que asigna el valor indicado como atributo de un mensaje
 * vortex
 * 
 * @author D. García
 */
public class AsignarAtributo implements Transformacion {

	private String nombreDelAtributo;
	public static final String nombreDelAtributo_FIELD = "nombreDelAtributo";
	private Object nuevoValor;
	public static final String nuevoValor_FIELD = "nuevoValor";

	/**
	 * @see net.gaia.vortex.core.api.transformaciones.Transformacion#transformar(net.gaia.vortex.core.api.mensaje.MensajeVortex)
	 */
	@Override
	public MensajeVortex transformar(MensajeVortex mensaje) {
		mensaje.getContenido().put(nombreDelAtributo, nuevoValor);
		return mensaje;
	}

	public static AsignarAtributo create(String atributo, Object valor) {
		if (!ContenidoPrimitiva.esPrimitivaVortex(valor)) {
			throw new IllegalArgumentException("El valor[" + valor + "] no puede asignarse por no ser primitiva vortex");
		}
		AsignarAtributo asignar = new AsignarAtributo();
		asignar.nombreDelAtributo = atributo;
		asignar.nuevoValor = valor;
		return asignar;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return ToString.de(this).con(nombreDelAtributo_FIELD, nombreDelAtributo).con(nuevoValor_FIELD, nuevoValor)
				.toString();
	}
}
