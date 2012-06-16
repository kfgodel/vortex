/**
 * 13/06/2012 10:48:33 Copyright (C) 2011 10Pines S.R.L.
 */
package net.gaia.vortex.core3.impl.tasks;

import net.gaia.taskprocessor.api.WorkUnit;
import net.gaia.vortex.core3.api.atomos.Receptor;
import net.gaia.vortex.core3.api.atomos.mensaje.MensajeVortex;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Objects;

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
	@Override
	public WorkUnit doWork() throws InterruptedException {
		try {
			delegado.recibir(mensaje);
		} catch (final Exception e) {
			LOG.error("Se produjo un error al entregar un mensaje[" + mensaje + "] a un delegado[" + delegado
					+ "]. Ignorando", e);
		}
		// Nada más que hacer
		return null;
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
		return Objects.toStringHelper(this).add(mensaje_FIELD, mensaje).add(delegado_FIELD, delegado).toString();
	}
}
