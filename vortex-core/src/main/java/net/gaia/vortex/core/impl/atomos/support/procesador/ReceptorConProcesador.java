/**
 * 12/01/2013 19:39:00 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.core.impl.atomos.support.procesador;

import net.gaia.taskprocessor.api.WorkUnit;
import net.gaia.vortex.core.api.atomos.Receptor;
import net.gaia.vortex.core.api.mensaje.MensajeVortex;
import net.gaia.vortex.core.prog.Loggers;

/**
 * Esta clase base define comportamiento para receptores que procesan los mensajes que reciben en su
 * propio procesador con threads dedicados
 * 
 * @author D. García
 */
public abstract class ReceptorConProcesador extends ComponenteConProcesadorSupport implements Receptor {

	/**
	 * @see net.gaia.vortex.core.api.atomos.Receptor#recibir(net.gaia.vortex.core.api.mensaje.MensajeVortex)
	 */
	@Override
	public void recibir(final MensajeVortex mensaje) {
		Loggers.ATOMOS.trace("Recibido en[{}] el mensaje[{}]", this.toShortString(), mensaje);
		final WorkUnit tareaDelMensaje = crearTareaAlRecibir(mensaje);
		procesarEnThreadPropio(tareaDelMensaje);
	}

	/**
	 * Crea la tarea específica de esta subclase para el mensaje recibido de manera de ser procesado
	 * en background por este componente
	 * 
	 * @param mensaje
	 *            El mensaje a procesar por esta instancia
	 * @return La tarea a procesar con el procesador interno de este componente
	 */
	protected abstract WorkUnit crearTareaAlRecibir(final MensajeVortex mensaje);

}
