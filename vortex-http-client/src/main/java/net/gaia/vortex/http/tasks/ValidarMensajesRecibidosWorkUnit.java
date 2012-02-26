/**
 * 26/02/2012 16:54:33 Copyright (C) 2011 10Pines S.R.L.
 */
package net.gaia.vortex.http.tasks;

import net.gaia.taskprocessor.api.WorkUnit;
import net.gaia.vortex.http.tasks.contexts.ContextoDeOperacionHttp;
import net.gaia.vortex.protocol.http.VortexWrapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Esta clase representa la operación de validación del cliente al recibir mensajes del nodo
 * 
 * @author D. García
 */
public class ValidarMensajesRecibidosWorkUnit implements WorkUnit {
	private static final Logger LOG = LoggerFactory.getLogger(ValidarMensajesRecibidosWorkUnit.class);

	private ContextoDeOperacionHttp contexto;
	private VortexWrapper recibido;

	/**
	 * @see net.gaia.taskprocessor.api.WorkUnit#doWork()
	 */
	@Override
	public void doWork() throws InterruptedException {
		if (recibido.getExtensionDeSesion() == null) {
			LOG.debug("Recibimos un wrapper sin extension de sesión. Ignorándo mensajes");
			final TerminarEnvioDeMensajeWorkUnit terminarEnvio = TerminarEnvioDeMensajeWorkUnit.create(contexto);
			contexto.getProcessor().process(terminarEnvio);
			return;
		}

	}

	public static ValidarMensajesRecibidosWorkUnit create(final ContextoDeOperacionHttp contexto,
			final VortexWrapper recibido) {
		final ValidarMensajesRecibidosWorkUnit name = new ValidarMensajesRecibidosWorkUnit();
		name.contexto = contexto;
		name.recibido = recibido;
		return name;
	}

}
