/**
 * 17/06/2012 15:20:32 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.core3.impl.atomos.forward;

import net.gaia.taskprocessor.api.TaskProcessor;
import net.gaia.taskprocessor.api.WorkUnit;
import net.gaia.vortex.core3.api.atomos.Receptor;
import net.gaia.vortex.core3.api.mensaje.MensajeVortex;
import net.gaia.vortex.core3.impl.tasks.DelegarMensaje;

/**
 * Esta clase representa el nexo nulo, en el cual no tiene efecto secundario la invocacion, más que
 * delegar el mensje al destino
 * 
 * @author D. García
 */
public class NexoNulo extends NexoSupport {

	/**
	 * @see net.gaia.vortex.core3.impl.atomos.forward.NexoSupport#crearTareaPara(net.gaia.vortex.core3.api.mensaje.MensajeVortex)
	 */
	@Override
	protected WorkUnit crearTareaPara(final MensajeVortex mensaje) {
		return DelegarMensaje.create(mensaje, getDestino());
	}

	public static NexoNulo create(final TaskProcessor processor, final Receptor delegado) {
		final NexoNulo nexo = new NexoNulo();
		nexo.initializeWith(processor, delegado);
		return nexo;
	}
}
