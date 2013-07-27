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
package net.gaia.vortex.core.impl.tasks.forward;

import java.util.Collection;

import net.gaia.taskprocessor.api.WorkParallelizer;
import net.gaia.taskprocessor.api.WorkUnit;
import net.gaia.vortex.core.api.atomos.Receptor;
import net.gaia.vortex.core.api.mensaje.MensajeVortex;
import net.gaia.vortex.core.impl.tasks.metricas.RegistrarRuteoRealizado;
import net.gaia.vortex.core.prog.Loggers;
import ar.com.dgarcia.lang.metrics.ListenerDeMetricas;
import ar.com.dgarcia.lang.strings.ToString;

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

	private ListenerDeMetricas listenerDeMetricas;
	public static final String listenerDeMetricas_FIELD = "listenerDeMetricas";

	/**
	 * @see net.gaia.taskprocessor.api.WorkUnit#doWork()
	 */

	public void doWork(final WorkParallelizer parallelizer) throws InterruptedException {
		Loggers.ATOMOS.debug("Multiplexando mensaje[{}] a {} destinos{}", new Object[] { mensaje, destinos.size(),
				destinos });
		for (final Receptor destino : destinos) {
			final DelegarMensaje entregaEnBackground = DelegarMensaje.create(mensaje, destino);
			parallelizer.submitAndForget(entregaEnBackground);
		}
		if (listenerDeMetricas == null) {
			// Nada más que hacer
			return;
		}
		Loggers.ATOMOS.debug("Registrando output en metricas del mensaje[{}]", mensaje);
		// Si tenemos listener registramos que ya ruteamos cuando podamos
		final RegistrarRuteoRealizado registro = RegistrarRuteoRealizado.create(listenerDeMetricas);
		parallelizer.submitAndForget(registro);
	}

	public static MultiplexarMensaje create(final MensajeVortex mensaje, final Collection<? extends Receptor> destinos,
			final ListenerDeMetricas listenerMetricas) {
		final MultiplexarMensaje multiplexion = new MultiplexarMensaje();
		multiplexion.destinos = destinos;
		multiplexion.mensaje = mensaje;
		multiplexion.listenerDeMetricas = listenerMetricas;
		return multiplexion;
	}

	/**
	 * @see java.lang.Object#toString()
	 */

	@Override
	public String toString() {
		return ToString.de(this).add(destinos_FIELD, destinos).add(mensaje_FIELD, mensaje).toString();
	}
}
