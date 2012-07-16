/**
 * 15/07/2012 18:18:36 Copyright (C) 2011 Darío L. García
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

import net.gaia.vortex.core.api.condiciones.Condicion;
import net.gaia.vortex.core.api.mensaje.ContenidoVortex;
import net.gaia.vortex.core.api.mensaje.MensajeVortex;
import ar.com.dgarcia.lang.strings.ToString;

/**
 * Esta clase representa una condición que evalua un mensaje vortex según el valor de un atributo.<br>
 * Esta condición es verdadera si el atributo existe y su valor es igual al valor indicado
 * 
 * @author D. García
 */
public class AtributoIgual implements Condicion {

	private String nombreDelAtributo;
	public static final String nombreDelAtributo_FIELD = "nombreDelAtributo";
	private Object valorEsperado;
	public static final String valorEsperado_FIELD = "valorEsperado";

	/**
	 * @see net.gaia.vortex.core.api.condiciones.Condicion#esCumplidaPor(net.gaia.vortex.core.api.mensaje.MensajeVortex)
	 */
	@Override
	public boolean esCumplidaPor(MensajeVortex mensaje) {
		ContenidoVortex contenidoDelMensaje = mensaje.getContenido();
		boolean contieneAtributo = contenidoDelMensaje.containsKey(nombreDelAtributo);
		if (!contieneAtributo) {
			return false;
		}
		Object valorEnElMensaje = contenidoDelMensaje.get(nombreDelAtributo);
		if (valorEsperado == null) {
			boolean esElValorEsperado = valorEnElMensaje == null;
			return esElValorEsperado;
		}
		boolean sonIguales = valorEsperado.equals(valorEnElMensaje);
		return sonIguales;
	}

	public static AtributoIgual create(String nombreDelAtributo, Object valorEsperado) {
		AtributoIgual condicion = new AtributoIgual();
		condicion.nombreDelAtributo = nombreDelAtributo;
		condicion.valorEsperado = valorEsperado;
		return condicion;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return ToString.de(this).con(nombreDelAtributo_FIELD, nombreDelAtributo)
				.con(valorEsperado_FIELD, valorEsperado).toString();
	}
}
