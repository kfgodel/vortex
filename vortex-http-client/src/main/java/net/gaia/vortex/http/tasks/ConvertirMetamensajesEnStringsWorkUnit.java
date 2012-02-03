/**
 * 01/02/2012 20:40:00 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.http.tasks;

import java.util.List;

import net.gaia.taskprocessor.api.WorkUnit;
import net.gaia.vortex.dependencies.json.InterpreteJson;
import net.gaia.vortex.http.tasks.contexts.ContextoDeOperacionHttp;
import net.gaia.vortex.protocol.messages.ContenidoVortex;
import net.gaia.vortex.protocol.messages.MensajeVortex;

/**
 * Esta clase representa la operación que realiza el nodo http para convertir en strings sus
 * metamensajes de manera de poder transmitirlos por http
 * 
 * @author D. García
 */
public class ConvertirMetamensajesEnStringsWorkUnit implements WorkUnit {

	private ContextoDeOperacionHttp contexto;
	private List<MensajeVortex> mensajes;
	private InterpreteJson interprete;

	/**
	 * @see net.gaia.taskprocessor.api.WorkUnit#doWork()
	 */
	@Override
	public void doWork() throws InterruptedException {
		// Definimos el intérprete para las conversiones
		interprete = contexto.getInterpreteJson();

		// Realizamos las conversiones de cada metamensaje
		for (final MensajeVortex mensajeVortex : mensajes) {
			if (!mensajeVortex.esMetaMensaje()) {
				continue;
			}
			convertirValorAJson(mensajeVortex);
		}

		// Realizamos el envío
		final EnviarMensajesWorkUnit envio = EnviarMensajesWorkUnit.create(contexto, mensajes);
		contexto.getProcessor().process(envio);
	}

	/**
	 * Convierte el objeto del valor del mensaje pasado a JSON y lo reasigna como valor actual
	 * 
	 * @param mensajeVortex
	 *            El mensaje contenedor del metamensaje
	 */
	private void convertirValorAJson(final MensajeVortex mensajeVortex) {
		final ContenidoVortex contenidoVortex = mensajeVortex.getContenido();
		final Object metamensaje = contenidoVortex.getValor();
		final String metamensajeComoJson = interprete.toJson(metamensaje);
		contenidoVortex.setValor(metamensajeComoJson);
	}

	public static ConvertirMetamensajesEnStringsWorkUnit create(final ContextoDeOperacionHttp contexto,
			final List<MensajeVortex> mensajesAEnviar) {
		final ConvertirMetamensajesEnStringsWorkUnit convertir = new ConvertirMetamensajesEnStringsWorkUnit();
		convertir.contexto = contexto;
		convertir.mensajes = mensajesAEnviar;
		return convertir;
	}
}
