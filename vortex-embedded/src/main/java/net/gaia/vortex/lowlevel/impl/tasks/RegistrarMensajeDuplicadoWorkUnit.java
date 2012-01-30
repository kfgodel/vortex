/**
 * 15/01/2012 13:38:56 Copyright (C) 2011 Darío L. García
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
import net.gaia.vortex.lowlevel.impl.envios.ContextoDeEnvio;
import net.gaia.vortex.lowlevel.impl.envios.IdentificadorDeEnvio;
import net.gaia.vortex.lowlevel.impl.ruteo.ControlDeRuteo;
import net.gaia.vortex.meta.Loggers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Esta clase representa la operación de registrar un mensaje como duplicado
 * 
 * @author D. García
 */
public class RegistrarMensajeDuplicadoWorkUnit implements WorkUnit {
	private static final Logger LOG = LoggerFactory.getLogger(RegistrarMensajeDuplicadoWorkUnit.class);

	private ContextoDeEnvio contexto;

	public static RegistrarMensajeDuplicadoWorkUnit create(final ContextoDeEnvio contexto) {
		final RegistrarMensajeDuplicadoWorkUnit registrar = new RegistrarMensajeDuplicadoWorkUnit();
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

		LOG.debug("Registrando duplicado para el envio[{}]", idEnvio);
		Loggers.RUTEO.info("ACUSE DUPLICADO confirmado para envio[{}]", idEnvio);
		controlDeRuteo.registrarMensajeDuplicados(idEnvio);

		// Verificamos si quedan más rutas o hay que terminar el ruteo
		final VerificarRutasPendientesWorkUnit verificarRutas = VerificarRutasPendientesWorkUnit.create(contexto);
		contexto.getProcesador().process(verificarRutas);
	}
}
