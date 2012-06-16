/**
 * 16/06/2012 11:59:52 Copyright (C) 2011 10Pines S.R.L.
 */
package net.gaia.vortex.core3.impl.tasks;

import net.gaia.taskprocessor.api.WorkUnit;
import net.gaia.vortex.core3.api.atomos.Receptor;
import net.gaia.vortex.core3.api.atomos.condicional.Condicion;
import net.gaia.vortex.core3.api.atomos.mensaje.MensajeVortex;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Objects;

/**
 * Esta clase implementa la lógica base para las tareas en background condicionales
 * 
 * @author D. García
 */
public class CondicionalSupport implements WorkUnit {
	private static final Logger LOG = LoggerFactory.getLogger(CondicionalSupport.class);

	private MensajeVortex mensaje;
	public static final String mensaje_FIELD = "mensaje";

	private Condicion condicion;
	public static final String condicion_FIELD = "condicion";

	private Receptor delegadoPorTrue;
	public static final String delegadoPorTrue_FIELD = "delegadoPorTrue";

	private Receptor delegadoPorFalse;
	public static final String delegadoPorFalse_FIELD = "delegadoPorFalse";

	public MensajeVortex getMensaje() {
		return mensaje;
	}

	public void setMensaje(final MensajeVortex mensaje) {
		this.mensaje = mensaje;
	}

	public Condicion getCondicion() {
		return condicion;
	}

	public void setCondicion(final Condicion condicion) {
		this.condicion = condicion;
	}

	public Receptor getDelegadoPorTrue() {
		return delegadoPorTrue;
	}

	public void setDelegadoPorTrue(final Receptor delegadoPorTrue) {
		this.delegadoPorTrue = delegadoPorTrue;
	}

	public Receptor getDelegadoPorFalse() {
		return delegadoPorFalse;
	}

	public void setDelegadoPorFalse(final Receptor delegadoPorFalse) {
		this.delegadoPorFalse = delegadoPorFalse;
	}

	/**
	 * @see net.gaia.taskprocessor.api.WorkUnit#doWork()
	 */
	@Override
	public WorkUnit doWork() throws InterruptedException {
		boolean delegarPorTrue;
		try {
			delegarPorTrue = condicion.esCumplidaPor(mensaje);
		} catch (final Exception e) {
			LOG.error("Se produjo un error al evaluar la condicion[" + condicion + "] sobre el mensaje[" + mensaje
					+ "] para bifurcar. Descartando mensaje", e);
			return null;
		}
		Receptor delegadoElegido;
		if (delegarPorTrue) {
			delegadoElegido = delegadoPorTrue;
		} else {
			delegadoElegido = delegadoPorFalse;
		}
		return DelegarMensaje.create(mensaje, delegadoElegido);
	}

	/**
	 * Inicializa esta instancia con los valores minimos
	 */
	protected void initializeWith(final MensajeVortex mensaje, final Condicion condicion,
			final Receptor delegadoPorTrue, final Receptor delegadoPorFalse) {
		this.condicion = condicion;
		this.delegadoPorFalse = delegadoPorFalse;
		this.delegadoPorTrue = delegadoPorTrue;
		this.mensaje = mensaje;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return Objects.toStringHelper(this).add(condicion_FIELD, condicion).add(delegadoPorTrue_FIELD, delegadoPorTrue)
				.add(delegadoPorFalse_FIELD, delegadoPorFalse).add(mensaje_FIELD, mensaje).toString();
	}
}