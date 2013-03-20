/**
 * 27/07/2012 21:24:43 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.http.impl.tasks;

import net.gaia.taskprocessor.api.WorkUnit;
import net.gaia.vortex.core.api.mensaje.MensajeVortex;
import net.gaia.vortex.http.sesiones.SesionVortexHttp;

/**
 * Esta clase representa la tarea realizada por un componente para dejar el mensaje recibido en la
 * sesión asociada
 * 
 * @author D. García
 */
public class AcumularEnSesion implements WorkUnit {

	private SesionVortexHttp sesion;

	private MensajeVortex mensaje;

	/**
	 * @see net.gaia.taskprocessor.api.WorkUnit#doWork()
	 */
	
	public WorkUnit doWork() throws InterruptedException {
		sesion.onMensajeDesdeVortex(mensaje);
		return null;
	}

	public static AcumularEnSesion create(final MensajeVortex mensaje, final SesionVortexHttp sesion) {
		final AcumularEnSesion acumular = new AcumularEnSesion();
		acumular.sesion = sesion;
		acumular.mensaje = mensaje;
		return acumular;
	}
}
