/**
 * 28/11/2011 01:08:00 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.lowlevel.impl.tasks;

import net.gaia.taskprocessor.api.TimeMagnitude;
import net.gaia.taskprocessor.api.WorkUnit;
import net.gaia.vortex.lowlevel.impl.ConfiguracionDeNodo;
import net.gaia.vortex.lowlevel.impl.ContextoDeEnvio;
import net.gaia.vortex.lowlevel.impl.EsperaDeAccion;
import net.gaia.vortex.lowlevel.impl.IdentificadorDeEnvio;
import net.gaia.vortex.lowlevel.impl.MemoriaDeMensajes;
import net.gaia.vortex.lowlevel.impl.MensajesEnEspera;

/**
 * Esta clase representa la tarea de espera de timeout de un mensaje en el que se espera su
 * confirmación de recepción
 * 
 * @author D. García
 */
public class ConfirmarRecepcionOSolicitarNuevamenteWorkUnit implements WorkUnit {

	private ContextoDeEnvio contexto;

	/**
	 * @see net.gaia.taskprocessor.api.WorkUnit#doWork()
	 */
	public void doWork() throws InterruptedException {
		// Consultamos si el mensaje espera confirmación todavía
		final MemoriaDeMensajes memoria = this.contexto.getMemoriaDeMensajes();
		final MensajesEnEspera esperandoConfirmacion = memoria.getEsperandoConfirmacionDeRecepcion();
		final IdentificadorDeEnvio idDeEnvio = this.contexto.getIdDeEnvio();
		if (!esperandoConfirmacion.incluyeA(idDeEnvio)) {
			// Si el envío no está en espera, debería ser porque recibimos la confirmación
			return;
		}
		// Si aún espera, la solicitamos nuevamente
		final EnviarSolicitudDeConfirmacionRecepcionWorkUnit solicitarConfirmacion = EnviarSolicitudDeConfirmacionRecepcionWorkUnit
				.create(this.contexto);
		this.contexto.getProcesador().process(solicitarConfirmacion);

		// Iniciamos la espera nuevamente
		final ConfiguracionDeNodo configuracion = this.contexto.getConfig();
		final TimeMagnitude timeoutDeSolicitud = configuracion.getTimeoutDeSolicitudDeConfirmacionRecepcion();

		// Registramos el momento de inicio
		final EsperaDeAccion esperaDeConfirmacion = this.contexto.getEsperaDeConfirmacionRecepcion();
		esperaDeConfirmacion.iniciarEsperaDe(timeoutDeSolicitud);

		// Disparamos la tarea a realizar cuando se acabe el tiempo
		final ConfirmarRecepcionODarPorPerdidoWorkUnit confirmacion = ConfirmarRecepcionODarPorPerdidoWorkUnit
				.create(contexto);
		this.contexto.getProcesador().processDelayed(timeoutDeSolicitud, confirmacion);
	}

	public static ConfirmarRecepcionOSolicitarNuevamenteWorkUnit create(final ContextoDeEnvio contexto) {
		final ConfirmarRecepcionOSolicitarNuevamenteWorkUnit espera = new ConfirmarRecepcionOSolicitarNuevamenteWorkUnit();
		espera.contexto = contexto;
		return espera;
	}
}