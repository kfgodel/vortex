/**
 * 27/07/2012 21:28:48 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.http.impl.atomos;

import net.gaia.taskprocessor.api.WorkUnit;
import net.gaia.taskprocessor.api.processor.TaskProcessor;
import net.gaia.vortex.api.mensajes.MensajeVortex;
import net.gaia.vortex.core.impl.atomos.support.procesador.ReceptorConProcesador;
import net.gaia.vortex.http.impl.tasks.AcumularEnSesion;
import net.gaia.vortex.http.sesiones.SesionVortexHttp;
import ar.com.dgarcia.lang.strings.ToString;

/**
 * Esta clase representa un componente vortex que los mensajes recibidos los pasa a una sesión http
 * para ser enviados al cliente asociado
 * 
 * @author D. García
 */
public class Httpizador extends ReceptorConProcesador {

	private SesionVortexHttp sesion;
	public static final String sesion_FIELD = "sesion";

	/**
	 * @see net.gaia.vortex.core.impl.atomos.support.procesador.ReceptorConProcesador#crearTareaAlRecibir(net.gaia.vortex.api.mensajes.MensajeVortex)
	 */
	
	protected WorkUnit crearTareaAlRecibir(final MensajeVortex mensaje) {
		final AcumularEnSesion envio = AcumularEnSesion.create(mensaje, sesion);
		return envio;
	}

	public static Httpizador create(final TaskProcessor processor, final SesionVortexHttp sesion) {
		final Httpizador httpizador = new Httpizador();
		httpizador.initializeWith(processor);
		httpizador.sesion = sesion;
		return httpizador;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	
	public String toString() {
		return ToString.de(this).con(numeroDeInstancia_FIELD, getNumeroDeInstancia()).add(sesion_FIELD, sesion)
				.toString();
	}
}
