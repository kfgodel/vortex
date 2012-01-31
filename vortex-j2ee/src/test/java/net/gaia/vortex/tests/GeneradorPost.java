/**
 * 28/01/2012 18:26:06 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.tests;

import java.util.List;

import net.gaia.vortex.dependencies.json.impl.InterpreteJackson;
import net.gaia.vortex.http.protocol.VortexWrapper;
import net.gaia.vortex.protocol.messages.ContenidoVortex;
import net.gaia.vortex.protocol.messages.IdVortex;
import net.gaia.vortex.protocol.messages.MensajeVortex;
import net.gaia.vortex.protocol.messages.tags.ReemplazarTags;

import com.google.common.collect.Lists;

/**
 * 
 * @author D. García
 */
public class GeneradorPost {

	static final InterpreteJackson interpreteJackson = new InterpreteJackson();

	public static void main(final String[] args) {
		final MensajeVortex mensajeVortex = crearReemplazoDeTags();
		final VortexWrapper wrapper = new VortexWrapper();
		wrapper.setMensajes(Lists.newArrayList(mensajeVortex));

		System.out.println(interpreteJackson.toJson(wrapper));
	}

	/**
	 * @return
	 */
	private static MensajeVortex crearHolaMundo() {
		final MensajeVortex mensajeVortex = crearMensaje("texto", "Hola mundo", "TagEjemplo");
		return mensajeVortex;
	}

	private static MensajeVortex crearReemplazoDeTags() {
		final ReemplazarTags publicacionDeTags = ReemplazarTags.create(Lists.newArrayList("TAG1"));
		final String contenidoJson = interpreteJackson.toJson(publicacionDeTags);

		final String tipoDeContenido = publicacionDeTags.getClass().getName();
		final MensajeVortex mensajeVortex = crearMensaje(contenidoJson, tipoDeContenido,
				MensajeVortex.TAG_INTERCAMBIO_VECINO);
		return mensajeVortex;
	}

	private static MensajeVortex crearMensaje(final String contenidoJson, final String tipoDeContenido,
			final String... tagsArray) {
		final ContenidoVortex contenido = ContenidoVortex.create(tipoDeContenido, contenidoJson);
		final IdVortex identificacion = IdVortex.create("1", "1", 1L, 0L);
		final List<String> tags = Lists.newArrayList(tagsArray);
		final MensajeVortex mensajeVortex = MensajeVortex.create(contenido, identificacion, tags);
		return mensajeVortex;
	}
}
