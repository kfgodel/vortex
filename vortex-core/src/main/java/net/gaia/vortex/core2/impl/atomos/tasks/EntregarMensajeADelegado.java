/**
 * 13/06/2012 10:48:33 Copyright (C) 2011 10Pines S.R.L.
 */
package net.gaia.vortex.core2.impl.atomos.tasks;

import net.gaia.taskprocessor.api.WorkUnit;
import net.gaia.vortex.core2.api.atomos.ComponenteVortex;
import net.gaia.vortex.core3.api.atomos.mensaje.MensajeVortex;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Objects;

/**
 * Esta clase representa la tarea realizada en un thread propio para entregar el mensaje a un
 * componente vortex
 * 
 * @author D. García
 */
public class EntregarMensajeADelegado implements WorkUnit {
	private static final Logger LOG = LoggerFactory.getLogger(EntregarMensajeADelegado.class);

	private ComponenteVortex delegado;
	public static final String delegado_FIELD = "delegado";

	private MensajeVortex mensaje;
	public static final String mensaje_FIELD = "mensaje";

	public ComponenteVortex getDelegado() {
		return delegado;
	}

	public void setDelegado(final ComponenteVortex delegado) {
		this.delegado = delegado;
	}

	public MensajeVortex getMensaje() {
		return mensaje;
	}

	public void setMensaje(final MensajeVortex mensaje) {
		this.mensaje = mensaje;
	}

	/**
	 * @see net.gaia.taskprocessor.api.WorkUnit#doWork()
	 */
	@Override
	public void doWork() throws InterruptedException {
		try {
			delegado.recibirMensaje(mensaje);
		} catch (final Exception e) {
			LOG.error("Se produjo un error al entregar un mensaje[" + mensaje + "] a un delegado[" + delegado
					+ "]. Ignorando", e);
		}
	}

	public static EntregarMensajeADelegado create(final MensajeVortex mensaje, final ComponenteVortex delegado) {
		final EntregarMensajeADelegado entregar = new EntregarMensajeADelegado();
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
