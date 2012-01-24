/**
 * 15/01/2012 20:49:32 Copyright (C) 2011 Darío L. García
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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Esta clase representa la operación de registro de mensaje fallido en el nodo
 * 
 * @author D. García
 */
public class RegistrarMensajeFallidoWorkUnit implements WorkUnit {
	private static final Logger LOG = LoggerFactory.getLogger(RegistrarMensajeFallidoWorkUnit.class);

	private ContextoDeEnvio contexto;

	public static RegistrarMensajeFallidoWorkUnit create(final ContextoDeEnvio contexto) {
		final RegistrarMensajeFallidoWorkUnit registrar = new RegistrarMensajeFallidoWorkUnit();
		registrar.contexto = contexto;
		return registrar;
	}

	/**
	 * @see net.gaia.taskprocessor.api.WorkUnit#doWork()
	 */
	@Override
	public void doWork() throws InterruptedException {
		// Registramos que el mensaje fue duplicado
		final ControlDeRuteo controlDeRuteo = contexto.getControlDeRuteo();
		final IdentificadorDeEnvio idEnvio = contexto.getIdDeEnvio();
		LOG.debug("Registrando envio fallido para el envío[{}]", idEnvio);
		controlDeRuteo.registrarMensajeFallado(idEnvio);

		// Verificamos si quedan más rutas o hay que terminar el ruteo
		final VerificarRutasPendientesWorkUnit verificarRutas = VerificarRutasPendientesWorkUnit.create(contexto);
		contexto.getProcesador().process(verificarRutas);

	}

}
