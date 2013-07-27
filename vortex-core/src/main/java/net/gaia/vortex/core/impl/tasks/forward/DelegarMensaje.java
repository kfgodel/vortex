/**
 * 13/06/2012 10:48:33 Copyright (C) 2011 10Pines S.R.L.
 */
package net.gaia.vortex.core.impl.tasks.forward;

import net.gaia.taskprocessor.api.WorkParallelizer;
import net.gaia.taskprocessor.api.WorkUnit;
import net.gaia.vortex.core.api.atomos.Receptor;
import net.gaia.vortex.core.api.memoria.ComponenteConMemoria;
import net.gaia.vortex.core.api.mensaje.MensajeVortex;
import net.gaia.vortex.core.prog.Loggers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ar.com.dgarcia.lang.strings.ToString;

/**
 * Esta clase es la tarea que realiza un componente vortex para delegar un mensaje a otro componente
 * en un thread aislado
 * 
 * @author D. García
 */
public class DelegarMensaje implements WorkUnit {
	private static final Logger LOG = LoggerFactory.getLogger(DelegarMensaje.class);

	private Receptor delegado;
	public static final String delegado_FIELD = "delegado";

	private MensajeVortex mensaje;
	public static final String mensaje_FIELD = "mensaje";

	/**
	 * @see net.gaia.taskprocessor.api.WorkUnit#doWork()
	 */

	public void doWork(final WorkParallelizer parallelizer) throws InterruptedException {
		Loggers.ATOMOS.debug("Delegando a nodo[{}] el mensaje[{}]", delegado.toShortString(), mensaje);
		// Intentamos optimizar la entrega de mensaje no mandando a uno que ya lo recibió
		if (delegado instanceof ComponenteConMemoria) {
			final ComponenteConMemoria receptorConMemoria = (ComponenteConMemoria) delegado;
			if (receptorConMemoria.yaRecibio(mensaje)) {
				Loggers.ATOMOS.debug("Evitando delegación duplicada a nodo[{}] del mensaje[{}]",
						delegado.toShortString(), mensaje);
				// Evitamos mandarlo a un componente que ya pasó para optimizar
				return;
			}
		}
		// No paso por el delegado, o no podemos saberlo, en cualquier caso lo entregamos
		try {
			delegado.recibir(mensaje);
		} catch (final Exception e) {
			LOG.error("Se produjo un error al entregar un mensaje[" + mensaje + "] a un delegado[" + delegado
					+ "]. Ignorando", e);
		}
		// Nada más que hacer
	}

	public static DelegarMensaje create(final MensajeVortex mensaje, final Receptor delegado) {
		final DelegarMensaje entregar = new DelegarMensaje();
		entregar.mensaje = mensaje;
		entregar.delegado = delegado;
		return entregar;
	}

	/**
	 * @see java.lang.Object#toString()
	 */

	@Override
	public String toString() {
		return ToString.de(this).add(mensaje_FIELD, mensaje).add(delegado_FIELD, delegado).toString();
	}
}
