/**
 * 13/06/2012 01:46:20 Copyright (C) 2011 Darío L. García
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
import net.gaia.vortex.core3.api.annon.Atomo;
import net.gaia.vortex.core3.api.atomos.Receptor;
import net.gaia.vortex.core3.api.mensaje.MensajeVortex;
import net.gaia.vortex.core3.impl.tasks.DelegarMensaje;

/**
 * Esta clase representa un {@link ComponenteProxy} que ejecuta el código de otro
 * {@link ComponenteVortex} al recibir un mensaje
 * 
 * @author D. García
 */
@Atomo
public class NexoEjecutor extends NexoSupport {

	public static NexoEjecutor create(final TaskProcessor processor, final Receptor delegado) {
		final NexoEjecutor ejecutor = new NexoEjecutor();
		ejecutor.initializeWith(processor, delegado);
		return ejecutor;
	}

	/**
	 * @see net.gaia.vortex.core3.impl.atomos.forward.NexoSupport#crearTareaPara(net.gaia.vortex.core3.api.mensaje.MensajeVortex)
	 */
	@Override
	protected WorkUnit crearTareaPara(final MensajeVortex mensaje) {
		return DelegarMensaje.create(mensaje, getDestino());
	}

}
