/**
 * 07/06/2012 23:42:17 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.sets.api;

/**
 * Esta interfaz representa un filtro aplicado a los mensajes para determinar si deben ser
 * procesados por un {@link HandlerTipadoDeMensajes}.<br>
 * Los mensajes aceptados por este filtro deben ser type-compatibles con el tipo del handler
 * utilizado al registrarlo
 * 
 * @author D. García
 */
public interface FiltroDeMensajes {

	/**
	 * Evalúa si el mensaje recibido cumple con este filtro y por lo tanto puede ser delegado al
	 * {@link HandlerTipadoDeMensajes} utilizado para registrar este filtro
	 * 
	 * @param mensajeRecibido
	 *            El mensaje recibido
	 * @return false si no se debe invocar el handler con este mensaje, true si es correcto usar el
	 *         o los handlers registrados con este filtro
	 */
	public boolean aceptaA(Object mensajeRecibido);

}
