/**
 * 13/06/2012 11:27:27 Copyright (C) 2011 10Pines S.R.L.
 */
package net.gaia.vortex.core2.impl.atomos.condicional;

import net.gaia.taskprocessor.api.WorkUnit;
import net.gaia.vortex.core2.impl.atomos.tasks.EntregarMensajeADelegado;
import net.gaia.vortex.core3.api.atomos.Condicion;
import net.gaia.vortex.core3.api.atomos.MensajeVortex;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Objects;

/**
 * Esta clase representa la tarea realizada en un thread propio que evalua la condicion y delega el
 * mensaje
 * 
 * @author D. García
 */
public class DelegarOpcionalmente implements WorkUnit {
	private static final Logger LOG = LoggerFactory.getLogger(DelegarOpcionalmente.class);

	private EntregarMensajeADelegado delegacion;
	public static final String delegacion_FIELD = "delegacion";

	private Condicion condicion;
	public static final String condicion_FIELD = "condicion";

	/**
	 * @see net.gaia.taskprocessor.api.WorkUnit#doWork()
	 */
	@Override
	public void doWork() throws InterruptedException {
		final MensajeVortex mensaje = delegacion.getMensaje();
		boolean elMensajeDebeDelegarse = false;
		try {
			elMensajeDebeDelegarse = condicion.esCumplidaPor(mensaje);
		} catch (final Exception e) {
			LOG.error("Se produjo un error evaluando la condicion[" + condicion + "] sobre el mensaje[" + mensaje
					+ "] antes de delegarlo en [" + delegacion.getDelegado() + "]. Descartando mensaje", e);
			return;
		}
		if (!elMensajeDebeDelegarse) {
			// La condicion indica que lo descartemos
			return;
		}
		// El mensaje tiene que ser entregado
		delegacion.doWork();
	}

	public static DelegarOpcionalmente create(final Condicion condicion, final EntregarMensajeADelegado delegacion) {
		final DelegarOpcionalmente evaluacion = new DelegarOpcionalmente();
		evaluacion.condicion = condicion;
		evaluacion.delegacion = delegacion;
		return evaluacion;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return Objects.toStringHelper(this).add(condicion_FIELD, condicion).add(delegacion_FIELD, delegacion)
				.toString();
	}
}
