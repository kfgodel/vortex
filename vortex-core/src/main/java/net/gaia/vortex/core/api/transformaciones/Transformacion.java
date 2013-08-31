/**
 * 13/06/2012 01:35:24 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.core.api.transformaciones;

import net.gaia.vortex.api.mensajes.MensajeVortex;

/**
 * Esta interfaz representa una transformación realizada a un mensaje vortex en la red.<br>
 * Normalmente las transformaciones deben ser thread-safe lo que permite la ejecución de multiples
 * thread en paralelo con el mismo objeto
 * 
 * @author D. García
 */
public interface Transformacion {

	/**
	 * Invocado por un componente para modificar el mensaje de acuerdo a la transformación
	 * representada por esta instancia.<br>
	 * El mensaje resultante será reemplazante del pasado
	 * 
	 * @param mensaje
	 *            El mensaje a modificar por esta transformación
	 * @return El mensaje obtenido después de la transformación (puede ser la misma instancia)
	 */
	public MensajeVortex transformar(MensajeVortex mensaje);

}
