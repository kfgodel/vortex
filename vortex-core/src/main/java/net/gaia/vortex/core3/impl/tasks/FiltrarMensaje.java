/**
 * 13/06/2012 11:27:27 Copyright (C) 2011 10Pines S.R.L.
 */
package net.gaia.vortex.core3.impl.tasks;

import net.gaia.vortex.core3.api.atomos.Receptor;
import net.gaia.vortex.core3.api.atomos.condicional.Condicion;
import net.gaia.vortex.core3.api.atomos.mensaje.MensajeVortex;
import net.gaia.vortex.core3.impl.atomos.ReceptorNulo;

import com.google.common.base.Objects;

/**
 * Esta clase representa la tarea realizada en thread propio por un componente vortex para evaluar
 * una condición y en base al resultado descartar el mensaje o enviarlo a otro delegado
 * 
 * @author D. García
 */
public class FiltrarMensaje extends CondicionalSupport {

	public static FiltrarMensaje create(final MensajeVortex mensaje, final Condicion condicion, final Receptor delegado) {
		final FiltrarMensaje evaluacion = new FiltrarMensaje();
		final Receptor delegadoPorFalse = ReceptorNulo.getInstancia();
		evaluacion.initializeWith(mensaje, condicion, delegado, delegadoPorFalse);
		return evaluacion;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return Objects.toStringHelper(this).add(condicion_FIELD, getCondicion())
				.add(delegadoPorTrue_FIELD, getDelegadoPorTrue()).add(mensaje_FIELD, getMensaje()).toString();
	}
}
