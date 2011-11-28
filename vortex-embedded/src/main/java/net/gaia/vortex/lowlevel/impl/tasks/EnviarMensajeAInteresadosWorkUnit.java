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

import java.util.List;

import net.gaia.taskprocessor.api.TaskProcessor;
import net.gaia.taskprocessor.api.WorkUnit;
import net.gaia.vortex.lowlevel.impl.ContextoDeRuteoDeMensaje;
import net.gaia.vortex.lowlevel.impl.ReceptorVortex;
import net.gaia.vortex.lowlevel.impl.SeleccionDeReceptores;

/**
 * Esta clase representa la tarea de enviar el mensaje a todos los receptores interesados
 * 
 * @author D. García
 */
public class EnviarMensajeAInteresadosWorkUnit implements WorkUnit {

	private ContextoDeRuteoDeMensaje contexto;
	private SeleccionDeReceptores interesados;

	public static EnviarMensajeAInteresadosWorkUnit create(final ContextoDeRuteoDeMensaje contexto,
			final SeleccionDeReceptores interesados) {
		final EnviarMensajeAInteresadosWorkUnit envio = new EnviarMensajeAInteresadosWorkUnit();
		envio.contexto = contexto;
		envio.interesados = interesados;
		return envio;
	}

	/**
	 * @see net.gaia.taskprocessor.api.WorkUnit#doWork()
	 */
	public void doWork() throws InterruptedException {
		final List<ReceptorVortex> allInteresados = this.interesados.getSeleccionados();
		final TaskProcessor procesador = this.contexto.getProcesador();
		for (final ReceptorVortex interesado : allInteresados) {
			final EnviarMensajeWorkUnit enviarMensaje = EnviarMensajeWorkUnit.create(contexto, interesado);
			procesador.process(enviarMensaje);
		}
	}
}
