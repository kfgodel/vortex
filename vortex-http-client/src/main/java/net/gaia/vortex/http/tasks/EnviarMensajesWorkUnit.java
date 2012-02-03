/**
 * 01/02/2012 20:25:00 Copyright (C) 2011 Darío L. García
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
import net.gaia.vortex.http.externals.http.ConectorHttp;
import net.gaia.vortex.http.sessions.SesionConId;
import net.gaia.vortex.http.tasks.contexts.ContextoDeOperacionHttp;
import net.gaia.vortex.protocol.http.VortexWrapper;
import net.gaia.vortex.protocol.messages.MensajeVortex;

/**
 * Esta clase representa la operación de envio de mensaje desde un nodo remoto HTTP
 * 
 * @author D. García
 */
public class EnviarMensajesWorkUnit implements WorkUnit {

	private ContextoDeOperacionHttp contexto;
	private List<MensajeVortex> mensajes;

	/**
	 * @see net.gaia.taskprocessor.api.WorkUnit#doWork()
	 */
	@Override
	public void doWork() throws InterruptedException {
		// Generamos el wrapper de lo que vamos a enviar
		final SesionConId sesionEmisora = contexto.getSesionInvolucrada();
		final Long currentSessionId = sesionEmisora.getSessionId();
		final VortexWrapper wrapperAEnviar = VortexWrapper.create(currentSessionId, mensajes);

		// Enviamos el mensaje
		final ConectorHttp conector = contexto.getConectorHttp();
		final VortexWrapper wrapperRecibido = conector.enviarYRecibir(wrapperAEnviar);

		// Procesamos las respuestas recibidas
		final RecibirMensajesWorkUnit recibirMensajes = RecibirMensajesWorkUnit.create(contexto, wrapperRecibido);
		contexto.getProcessor().process(recibirMensajes);
	}

	public static EnviarMensajesWorkUnit create(final ContextoDeOperacionHttp contexto,
			final List<MensajeVortex> mensajes) {
		final EnviarMensajesWorkUnit envio = new EnviarMensajesWorkUnit();
		envio.contexto = contexto;
		envio.mensajes = mensajes;
		return envio;
	}
}
