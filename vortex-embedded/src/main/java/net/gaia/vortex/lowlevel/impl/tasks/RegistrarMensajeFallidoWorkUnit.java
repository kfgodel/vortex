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

import net.gaia.vortex.lowlevel.impl.envios.ContextoDeEnvio;
import net.gaia.vortex.lowlevel.impl.envios.IdentificadorDeEnvio;
import net.gaia.vortex.lowlevel.impl.receptores.ReceptorVortex;
import net.gaia.vortex.lowlevel.impl.ruteo.ContextoDeRuteoDeMensaje;
import net.gaia.vortex.lowlevel.impl.ruteo.ControlDeRuteo;
import net.gaia.vortex.meta.Loggers;
import net.gaia.vortex.protocol.messages.routing.AcuseFallaRecepcion;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Esta clase representa la operación de registro de mensaje fallido en el nodo
 * 
 * @author D. García
 */
public class RegistrarMensajeFallidoWorkUnit implements TareaParaReceptor {
	private static final Logger LOG = LoggerFactory.getLogger(RegistrarMensajeFallidoWorkUnit.class);

	private ContextoDeEnvio contexto;

	private AcuseFallaRecepcion acuse;

	public static RegistrarMensajeFallidoWorkUnit create(final ContextoDeEnvio contexto, final AcuseFallaRecepcion acuse) {
		final RegistrarMensajeFallidoWorkUnit registrar = new RegistrarMensajeFallidoWorkUnit();
		registrar.contexto = contexto;
		registrar.acuse = acuse;
		return registrar;
	}

	/**
	 * @see net.gaia.vortex.lowlevel.impl.tasks.TareaParaReceptor#esPara(net.gaia.vortex.lowlevel.impl.receptores.ReceptorVortex)
	 */
	@Override
	public boolean esPara(final ReceptorVortex receptor) {
		final ContextoDeRuteoDeMensaje contextoDeRuteo = contexto.getContextoDeRuteo();
		final ReceptorVortex emisor = contextoDeRuteo.getEmisor();
		return emisor == receptor;
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
		Loggers.RUTEO.info("ACUSE FALLA confirmado para envio[{}]: [{}]", idEnvio, acuse.getCodigoError());
		controlDeRuteo.registrarMensajeFallado(idEnvio);

		// Verificamos si quedan más rutas o hay que terminar el ruteo
		final VerificarRutasPendientesWorkUnit verificarRutas = VerificarRutasPendientesWorkUnit.create(contexto);
		contexto.getProcesador().process(verificarRutas);

	}

}
