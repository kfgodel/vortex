/**
 * 13/06/2012 17:20:12 Copyright (C) 2011 10Pines S.R.L.
 */
package net.gaia.vortex.core3.impl.tasks;

import net.gaia.vortex.core3.api.atomos.Receptor;
import net.gaia.vortex.core3.api.condiciones.Condicion;
import net.gaia.vortex.core3.api.mensaje.MensajeVortex;

/**
 * Esta clase representa la tarea realizada en thread propio que evalua una condición para elegir el
 * delegado al que envia el mensaje
 * 
 * @author D. García
 */
public class BifurcarMensaje extends CondicionalSupport {

	public static BifurcarMensaje create(final MensajeVortex mensaje, final Condicion condicion,
			final Receptor delegadoPorTrue, final Receptor delegadoPorFalse) {
		final BifurcarMensaje bifurcar = new BifurcarMensaje();
		bifurcar.initializeWith(mensaje, condicion, delegadoPorTrue, delegadoPorFalse);
		return bifurcar;
	}
}
