/**
 * 28/11/2011 14:44:31 Copyright (C) 2011 Darío L. García
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

import net.gaia.annotations.HasDependencyOn;
import net.gaia.taskprocessor.api.WorkUnit;
import net.gaia.vortex.lowlevel.impl.ContextoDeEnvio;
import net.gaia.vortex.lowlevel.impl.ContextoDeRuteoDeMensaje;
import net.gaia.vortex.lowlevel.impl.ControlDeRuteo;
import net.gaia.vortex.meta.Decision;

/**
 * Esta clase representa la tarea de registrar que un mensaje no recibió confirmación y se considera
 * perdido.<br>
 * Si esta es la última confirmación que faltaba se dispara una confirmación de consumo
 * 
 * @author D. García
 */
public class RegistrarMensajePerdidoWorkUnit implements WorkUnit {

	private ContextoDeEnvio contexto;

	/**
	 * @see net.gaia.taskprocessor.api.WorkUnit#doWork()
	 */
	@HasDependencyOn(Decision.EL_TIMEOUT_DE_RECEPCION_QUITA_EL_MENSAJE_DE_ESPERA)
	public void doWork() throws InterruptedException {
		final ContextoDeRuteoDeMensaje contextoDeRuteo = contexto.getContextoDeRuteo();
		final ControlDeRuteo controlDeRuteo = contextoDeRuteo.getControl();
		controlDeRuteo.registrarMensajePerdido();
		// Vemos si era el último o todavía hay más mensajes para rutear
		if (controlDeRuteo.existenMensajesEnRuta()) {
			return;
		}
		// Tenemos que seguir con el ruteo, enviar una confirmación de consumo
		final DevolverConfirmacionDeConsumoWorkUnit devolverConfirmacion = DevolverConfirmacionDeConsumoWorkUnit
				.create(contextoDeRuteo);
		contextoDeRuteo.getProcesador().process(devolverConfirmacion);
	}

	public static RegistrarMensajePerdidoWorkUnit create(final ContextoDeEnvio contexto) {
		final RegistrarMensajePerdidoWorkUnit registro = new RegistrarMensajePerdidoWorkUnit();
		registro.contexto = contexto;
		return registro;
	}
}
