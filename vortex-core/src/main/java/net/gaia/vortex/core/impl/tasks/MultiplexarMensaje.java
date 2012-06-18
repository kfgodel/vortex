/**
 * 17/06/2012 13:25:41 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.core.impl.tasks;

import java.util.Collection;

import net.gaia.taskprocessor.api.TaskProcessor;
import net.gaia.taskprocessor.api.WorkUnit;
import net.gaia.vortex.core.api.atomos.Receptor;
import net.gaia.vortex.core.api.mensaje.MensajeVortex;
import net.gaia.vortex.core.impl.metricas.ListenerDeMetricas;

import com.google.common.base.Objects;

/**
 * Esta clase representa la tarea realizada en un thread propio por un componente vortex que envía
 * el mensaje recibido a un conjunto de receptores
 * 
 * @author D. García
 */
public class MultiplexarMensaje implements WorkUnit {

	private MensajeVortex mensaje;
	public static final String mensaje_FIELD = "mensaje";

	private Collection<? extends Receptor> destinos;
	public static final String destinos_FIELD = "destinos";

	private TaskProcessor processor;
	public static final String processor_FIELD = "processor";

	private ListenerDeMetricas listenerDeMetricas;
	public static final String listenerDeMetricas_FIELD = "listenerDeMetricas";

	/**
	 * @see net.gaia.taskprocessor.api.WorkUnit#doWork()
	 */
	@Override
	public WorkUnit doWork() throws InterruptedException {
		for (final Receptor destino : destinos) {
			final DelegarMensaje entregaEnBackground = DelegarMensaje.create(mensaje, destino);
			processor.process(entregaEnBackground);
		}
		if (listenerDeMetricas == null) {
			// Nada más que hacer
			return null;
		}
		// Si tenemos listener registramos que ya ruteamos cuando podamos
		return RegistrarRuteoRealizado.create(listenerDeMetricas);
	}

	public static MultiplexarMensaje create(final MensajeVortex mensaje, final Collection<? extends Receptor> destinos,
			final TaskProcessor processor, final ListenerDeMetricas listenerMetricas) {
		final MultiplexarMensaje multiplexion = new MultiplexarMensaje();
		multiplexion.destinos = destinos;
		multiplexion.mensaje = mensaje;
		multiplexion.processor = processor;
		multiplexion.listenerDeMetricas = listenerMetricas;
		return multiplexion;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return Objects.toStringHelper(this).add(destinos_FIELD, destinos).add(mensaje_FIELD, mensaje)
				.add(processor_FIELD, processor).toString();
	}
}
