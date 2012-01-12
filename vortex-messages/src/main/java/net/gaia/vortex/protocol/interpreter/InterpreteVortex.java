/**
 * 26/11/2011 15:40:17 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.protocol.interpreter;

import net.gaia.vortex.protocol.TipoMetamensaje;
import net.gaia.vortex.protocol.messages.MensajeVortex;

/**
 * Esta interfaz define métodos que permiten interpretar mensajes vortex en objetos y viceversa
 * 
 * @author D. García
 */
public interface InterpreteVortex {

	/**
	 * Interpreta del contenido recibido el tipo de objeto esperado verificando el tipo de contenido
	 * declarado
	 * 
	 * @param mensajeVortex
	 *            El mensaje recibido
	 * @param tipoContenidoEsperado
	 *            El tipo de contenido esperado
	 * @param tipoDeClaseEsperada
	 *            El tipo esperado para el objeto obtenido al interpretar el contenido del mensaje
	 * @return El objeto creado desde la interpretación del contenido
	 * @throws VortexInterpreterException
	 *             Si se produce un error al interpretar el mensaje (json, o no es del tipo
	 *             esperado)
	 */
	public <T> T fromContenidoDe(MensajeVortex mensajeVortex, String tipoContenidoEsperado, Class<T> tipoDeClaseEsperada)
			throws VortexInterpreterException;

	/**
	 * Interpreta del mensaje recibido el metamensaje indicado por tipo
	 * 
	 * @param mensajeVortex
	 *            El mensaje recibido cuyo contenido representa un metamensaje
	 * @param metamensajeEsperado
	 *            El tipo de metamensaje esperado
	 * @return El metamensaje creado al interpretar el contenido del mensaje
	 * @throws VortexInterpreterException
	 *             Si se produce un error al interpretar el mensaje (json, o no es del tipo
	 *             esperado)
	 */
	public <T> T fromContenidoDe(MensajeVortex mensajeVortex, TipoMetamensaje metamensajeEsperado)
			throws VortexInterpreterException;
}
