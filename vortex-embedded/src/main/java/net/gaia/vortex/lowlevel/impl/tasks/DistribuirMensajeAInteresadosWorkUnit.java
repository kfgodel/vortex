/**
 * 27/11/2011 22:52:47 Copyright (C) 2011 Darío L. García
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

import java.util.Set;

import net.gaia.taskprocessor.api.TaskProcessor;
import net.gaia.vortex.lowlevel.impl.envios.ContextoDeEnvio;
import net.gaia.vortex.lowlevel.impl.receptores.ReceptorVortex;
import net.gaia.vortex.lowlevel.impl.ruteo.ContextoDeRuteoDeMensaje;
import net.gaia.vortex.lowlevel.impl.ruteo.SeleccionDeReceptores;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Esta clase representa la tarea de enviar el mensaje a todos los receptores interesados
 * 
 * @author D. García
 */
public class DistribuirMensajeAInteresadosWorkUnit implements TareaParaReceptor {
	private static final Logger LOG = LoggerFactory.getLogger(DistribuirMensajeAInteresadosWorkUnit.class);

	private ContextoDeRuteoDeMensaje contexto;
	private SeleccionDeReceptores interesados;

	public static DistribuirMensajeAInteresadosWorkUnit create(final ContextoDeRuteoDeMensaje contexto,
			final SeleccionDeReceptores interesados) {
		final DistribuirMensajeAInteresadosWorkUnit envio = new DistribuirMensajeAInteresadosWorkUnit();
		envio.contexto = contexto;
		envio.interesados = interesados;
		return envio;
	}

	/**
	 * @see net.gaia.vortex.lowlevel.impl.tasks.TareaParaReceptor#esPara(net.gaia.vortex.lowlevel.impl.receptores.ReceptorVortex)
	 */
	@Override
	public boolean esPara(final ReceptorVortex receptor) {
		final ReceptorVortex emisor = contexto.getEmisor();
		return emisor == receptor;
	}

	/**
	 * @see net.gaia.taskprocessor.api.WorkUnit#doWork()
	 */
	@Override
	public void doWork() throws InterruptedException {
		final TaskProcessor procesador = this.contexto.getProcesador();
		final Set<ReceptorVortex> allInteresados = this.interesados.getSeleccionados();

		LOG.debug("Distribuyendo mensaje[{}] a {} interesados", contexto.getMensaje(), allInteresados.size());
		for (final ReceptorVortex interesado : allInteresados) {
			final ContextoDeEnvio contextoDeEnvioDelMensaje = ContextoDeEnvio.create(contexto, interesado);
			final ProcesarEnvioDeMensajeWorkUnit enviarMensaje = ProcesarEnvioDeMensajeWorkUnit
					.create(contextoDeEnvioDelMensaje);
			procesador.process(enviarMensaje);
		}
	}
}
