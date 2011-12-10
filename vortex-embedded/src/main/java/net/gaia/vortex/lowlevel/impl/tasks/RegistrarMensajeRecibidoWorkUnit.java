/**
 * 10/12/2011 14:48:38 Copyright (C) 2011 Darío L. García
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
import net.gaia.vortex.lowlevel.impl.ControlDeRuteo;
import net.gaia.vortex.lowlevel.impl.IdentificadorDeEnvio;

/**
 * Esta clase representa la acción realizada por el nodo para registrar que un mensaje enviado fue
 * recibido por un receptor
 * 
 * @author D. García
 */
public class RegistrarMensajeRecibidoWorkUnit implements WorkUnit {

	private ContextoDeEnvio contexto;

	/**
	 * @see net.gaia.taskprocessor.api.WorkUnit#doWork()
	 */
	public void doWork() throws InterruptedException {
		// Registramos que lo recibimos
		final ControlDeRuteo controlDeRuteo = this.contexto.getControlDeRuteo();
		final IdentificadorDeEnvio idEnvio = contexto.getIdDeEnvio();
		controlDeRuteo.registrarRecepcionRealizada(idEnvio);

		// Continuamos con la espera de confirmación de consumo
		final ConfiguracionDeNodo configuracion = this.contexto.getConfig();
		final TimeMagnitude timeoutDeConfirmacion = configuracion.getTimeoutDeConfirmacionConsumo();

		// Disparamos la tarea para ejecutarse cuando se acabe el timeout
		final ConfirmarConsumoOSolicitarNuevamenteWorkUnit esperaConfirmacion = ConfirmarConsumoOSolicitarNuevamenteWorkUnit
				.create(contexto);
		this.contexto.getProcesador().processDelayed(timeoutDeConfirmacion, esperaConfirmacion);

	}

	public static RegistrarMensajeRecibidoWorkUnit create(final ContextoDeEnvio contexto) {
		final RegistrarMensajeRecibidoWorkUnit registrar = new RegistrarMensajeRecibidoWorkUnit();
		registrar.contexto = contexto;
		return registrar;
	}
}
