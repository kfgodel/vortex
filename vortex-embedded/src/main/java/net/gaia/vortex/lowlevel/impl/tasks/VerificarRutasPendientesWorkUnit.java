/**
 * 10/12/2011 14:36:26 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.lowlevel.impl.tasks;

import net.gaia.taskprocessor.api.WorkUnit;
import net.gaia.vortex.lowlevel.impl.ContextoDeEnvio;
import net.gaia.vortex.lowlevel.impl.ContextoDeRuteoDeMensaje;
import net.gaia.vortex.lowlevel.impl.ControlDeRuteo;

/**
 * Esta clase representa la acción realizada por el nodo para terminar un ruteo cada vez que se
 * completa una ruta de mensaje enviado.<br>
 * Sólo se termina el ruteo si no quedan más rutas pendientes
 * 
 * @author D. García
 */
public class VerificarRutasPendientesWorkUnit implements WorkUnit {

	private ContextoDeEnvio contexto;

	/**
	 * @see net.gaia.taskprocessor.api.WorkUnit#doWork()
	 */
	@Override
	public void doWork() throws InterruptedException {
		final ControlDeRuteo controlDeRuteo = contexto.getControlDeRuteo();
		// Vemos si todavía hay más mensajes para rutear
		if (controlDeRuteo.existenMensajesEnRuta()) {
			// Quedan más rutas para confirmar, esperamos
			return;
		}
		// Tenemos que seguir con el ruteo, enviar una confirmación de consumo
		final ContextoDeRuteoDeMensaje contextoDeRuteo = contexto.getContextoDeRuteo();
		final TerminarRuteoWorkUnit terminarRuteo = TerminarRuteoWorkUnit.create(contextoDeRuteo);
		contextoDeRuteo.getProcesador().process(terminarRuteo);
	}

	public static VerificarRutasPendientesWorkUnit create(final ContextoDeEnvio contexto) {
		final VerificarRutasPendientesWorkUnit registro = new VerificarRutasPendientesWorkUnit();
		registro.contexto = contexto;
		return registro;
	}

}