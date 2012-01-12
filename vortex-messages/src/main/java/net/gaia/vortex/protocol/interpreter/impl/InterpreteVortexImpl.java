/**
 * 26/11/2011 16:16:44 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.protocol.interpreter.impl;

import net.gaia.vortex.dependencies.json.InterpreteJson;
import net.gaia.vortex.dependencies.json.JsonConversionException;
import net.gaia.vortex.protocol.TipoMetamensaje;
import net.gaia.vortex.protocol.interpreter.InterpreteVortex;
import net.gaia.vortex.protocol.interpreter.VortexInterpreterException;
import net.gaia.vortex.protocol.messages.ContenidoVortex;
import net.gaia.vortex.protocol.messages.MensajeVortex;

/**
 * Esta clase permite interpretar los mensajes vortex como objetos de la aplicación
 * 
 * @author D. García
 */
public class InterpreteVortexImpl implements InterpreteVortex {

	private InterpreteJson interpreteJson;

	/**
	 * @see net.gaia.vortex.protocol.interpreter.InterpreteVortex#fromContenidoDe(net.gaia.vortex.protocol.messages.MensajeVortex,
	 *      java.lang.String, java.lang.Class)
	 */
	@Override
	public <T> T fromContenidoDe(final MensajeVortex mensajeVortex, final String tipoContenidoEsperado,
			final Class<T> tipoDeClaseEsperada) throws VortexInterpreterException {
		final ContenidoVortex contenido = mensajeVortex.getContenido();
		final String tipoContenido = contenido.getTipoContenido();
		if (!tipoContenidoEsperado.equals(tipoContenido)) {
			throw new VortexInterpreterException("El tipo de contenido recibido[" + tipoContenido
					+ "] no es el esperado[" + tipoContenidoEsperado + "]");
		}
		final String jsonValue = (String) contenido.getValor();
		T objeto;
		try {
			objeto = interpreteJson.fromJson(jsonValue, tipoDeClaseEsperada);
		} catch (final JsonConversionException e) {
			throw new VortexInterpreterException("No fue posible interpretar el json del contenido como objeto", e);
		}
		return objeto;
	}

	/**
	 * @see net.gaia.vortex.protocol.interpreter.InterpreteVortex#fromContenidoDe(net.gaia.vortex.protocol.messages.MensajeVortex,
	 *      net.gaia.vortex.protocol.TipoMetamensaje)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <T> T fromContenidoDe(final MensajeVortex mensajeVortex, final TipoMetamensaje metamensajeEsperado)
			throws VortexInterpreterException {
		final Class<?> clase = metamensajeEsperado.getClase();
		final String tipoContenido = metamensajeEsperado.getTipoContenido();
		final Object interpretado = fromContenidoDe(mensajeVortex, tipoContenido, clase);
		return (T) interpretado;
	}

}
