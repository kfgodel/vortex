/**
 * 14/01/2012 17:34:01 Copyright (C) 2011 Darío L. García
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

/**
 * Esta clase representa la operación de espera del acuse de consumo por parte del nodo al que se le
 * envió el mensaje
 * 
 * @author D. García
 */
public class EsperarAcuseConsumoWorkUnit implements WorkUnit {

	private ContextoDeEnvio contexto;

	public static EsperarAcuseConsumoWorkUnit create(final ContextoDeEnvio contexto) {
		final EsperarAcuseConsumoWorkUnit espera = new EsperarAcuseConsumoWorkUnit();
		espera.contexto = contexto;
		return espera;
	}

	/**
	 * @see net.gaia.taskprocessor.api.WorkUnit#doWork()
	 */
	@Override
	public void doWork() throws InterruptedException {
		// Registramos el momento de inicio de la espera
		final EsperaDeAccion esperaDeAcuse = this.contexto.getEsperaDeAcuseConsumo();
		final ConfiguracionDeNodo configuracion = this.contexto.getConfig();
		final TimeMagnitude timeoutDeAcuse = configuracion.getTimeoutDeAcuseDeConsumo();
		esperaDeAcuse.iniciarEsperaDe(timeoutDeAcuse);

		// Disparamos la tarea para ejecutarse cuando se acabe el timeout
		final ConfirmarRecepcionDeAcuseConsumoWorkUnit confirmarAcuse = ConfirmarRecepcionDeAcuseConsumoWorkUnit
				.create(contexto);
		this.contexto.getProcesador().processDelayed(timeoutDeAcuse, confirmarAcuse);

	}

}
