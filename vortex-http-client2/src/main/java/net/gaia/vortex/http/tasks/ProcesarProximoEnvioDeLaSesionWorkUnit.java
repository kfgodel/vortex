/**
 * 24/01/2012 07:47:53 Copyright (C) 2011 Darío L. García
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

import net.gaia.taskprocessor.api.WorkUnit;
import net.gaia.vortex.http.sessions.SesionConId;
import net.gaia.vortex.http.tasks.contexts.ContextoDeOperacionHttp;
import net.gaia.vortex.lowlevel.impl.receptores.ColaDeMensajesVortex;
import net.gaia.vortex.protocol.messages.MensajeVortex;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Esta clase representa la operación realizada por el nodo para procesar el proximo mensaje en cola
 * de un receptor
 * 
 * @author D. García
 */
public class ProcesarProximoEnvioDeLaSesionWorkUnit implements WorkUnit {
	private static final Logger LOG = LoggerFactory.getLogger(ProcesarProximoEnvioDeLaSesionWorkUnit.class);

	private ContextoDeOperacionHttp contexto;

	/**
	 * @see net.gaia.taskprocessor.api.WorkUnit#doWork()
	 */
	@Override
	public void doWork() throws InterruptedException {
		final SesionConId sesion = contexto.getSesionInvolucrada();
		LOG.debug("Tomando proximo mensaje disponible de la sesión[{}]", sesion);

		// Verificamos si podemos seguir procesando o ya hay otro mensaje en proceso
		final ColaDeMensajesVortex colaDeMensajes = sesion.getColaDeMensajes();
		final MensajeVortex proximoMensaje = colaDeMensajes.tomarProximoMensaje();
		if (proximoMensaje == null) {
			LOG.debug("No quedan mensajes pendientes para la sesión[{}]. Descansando", sesion);
			return;
		}

		// Comenzamos con el procesamiento
		final ValidarMensajesPrevioEnvioWorkUnit validacion = ValidarMensajesPrevioEnvioWorkUnit.create(contexto,
				proximoMensaje);
		contexto.getProcessor().process(validacion);
	}

	public static ProcesarProximoEnvioDeLaSesionWorkUnit create(final ContextoDeOperacionHttp contexto) {
		final ProcesarProximoEnvioDeLaSesionWorkUnit name = new ProcesarProximoEnvioDeLaSesionWorkUnit();
		name.contexto = contexto;
		return name;
	}

}