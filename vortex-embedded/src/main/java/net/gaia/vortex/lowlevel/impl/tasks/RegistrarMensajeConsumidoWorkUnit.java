/**
 * 10/12/2011 14:33:29 Copyright (C) 2011 Darío L. García
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

import net.gaia.taskprocessor.api.WorkUnit;
import net.gaia.vortex.lowlevel.impl.ContextoDeEnvio;
import net.gaia.vortex.lowlevel.impl.ControlDeRuteo;
import net.gaia.vortex.lowlevel.impl.IdentificadorDeEnvio;

/**
 * Esta clase representa la acción realizada por el nodo para registrar que un mensaje fue consumido
 * 
 * @author D. García
 */
public class RegistrarMensajeConsumidoWorkUnit implements WorkUnit {

	private ContextoDeEnvio contexto;

	/**
	 * @see net.gaia.taskprocessor.api.WorkUnit#doWork()
	 */
	public void doWork() throws InterruptedException {
		// Registramos que el mensaje fue consumido
		final ControlDeRuteo controlDeRuteo = contexto.getControlDeRuteo();
		final IdentificadorDeEnvio idEnvio = contexto.getIdDeEnvio();
		controlDeRuteo.registrarConsumoRealizado(idEnvio);

		// Verificamos si ya no quedan rutas y tenemos que terminar el ruteo
		final VerificarRutasPendientesWorkUnit verificarRutasPendientes = VerificarRutasPendientesWorkUnit
				.create(contexto);
		contexto.getProcesador().process(verificarRutasPendientes);
	}

	public static RegistrarMensajeConsumidoWorkUnit create(final ContextoDeEnvio contexto) {
		final RegistrarMensajeConsumidoWorkUnit registro = new RegistrarMensajeConsumidoWorkUnit();
		registro.contexto = contexto;
		return registro;
	}

}