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
import net.gaia.vortex.protocol.messages.meta.ReemplazarTags;

import com.google.common.collect.Lists;

/**
 * 
 * @author D. García
 */
public class GeneradorPost {

	public static void main(final String[] args) {
		final InterpreteJackson interpreteJackson = new InterpreteJackson();

		final ReemplazarTags publicacionDeTags = ReemplazarTags.create(Lists.newArrayList("TAG1"));
		final String contenidoJson = interpreteJackson.toJson(publicacionDeTags);

		final ContenidoVortex contenido = ContenidoVortex.create(publicacionDeTags.getClass().getName(), contenidoJson);
		final IdVortex identificacion = IdVortex.create("1", "1", 1L, 0L);
		final List<String> tags = Lists.newArrayList(MensajeVortex.TAG_INTERCAMBIO_VECINO);
		final MensajeVortex mensajeVortex = MensajeVortex.create(contenido, identificacion, tags);
		final VortexWrapper wrapper = new VortexWrapper();
		wrapper.setMensajes(Lists.newArrayList(mensajeVortex));

		System.out.println(interpreteJackson.toJson(wrapper));
	}
}
