/**
 * 27/11/2011 21:59:02 Copyright (C) 2011 Darío L. García
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

import net.gaia.taskprocessor.api.TaskProcessor;
import net.gaia.taskprocessor.api.WorkUnit;
import net.gaia.vortex.lowlevel.impl.ContextoDeRuteoDeMensaje;
import net.gaia.vortex.lowlevel.impl.MemoriaDeMensajes;
import net.gaia.vortex.protocol.MensajeVortexEmbebido;

/**
 * Esta clase representa la tarea de verificar si el mensaje está duplicado respecto a los mensajes
 * recibidos en el nodo
 * 
 * @author D. García
 */
public class VerificarDuplicadoWorkUnit implements WorkUnit {

	private ContextoDeRuteoDeMensaje contexto;
	public static final String MENSAJE_IS_DUPLICATED_ERROR = "mensaje.isDuplicated";

	/**
	 * @see net.gaia.taskprocessor.api.WorkUnit#doWork()
	 */
	public void doWork() throws InterruptedException {
		final MensajeVortexEmbebido mensaje = contexto.getMensaje();
		final MemoriaDeMensajes memoria = contexto.getMemoriaDeMensajes();
		if (memoria.registrarSiNoRecuerdaA(mensaje)) {
			// Es duplicado
			final DevolverConfirmacionRecepcionWorkUnit devolucion = DevolverConfirmacionRecepcionWorkUnit.create(
					contexto, MENSAJE_IS_DUPLICATED_ERROR);
			this.contexto.getProcesador().process(devolucion);
			return;
		}
		// Si es duplicado ya no lo recordamos ;) enviamos el OK
		final DevolverConfirmacionRecepcionWorkUnit confirmacionExitosa = DevolverConfirmacionRecepcionWorkUnit.create(
				contexto, null);
		final TaskProcessor procesador = this.contexto.getProcesador();
		procesador.process(confirmacionExitosa);

		// Comenzamos a rutearlo
		final SeleccionarReceptoresWorkUnit proximoPaso = SeleccionarReceptoresWorkUnit.create(contexto);
		procesador.process(proximoPaso);
	}

	public static VerificarDuplicadoWorkUnit create(final ContextoDeRuteoDeMensaje contexto) {
		final VerificarDuplicadoWorkUnit verificacion = new VerificarDuplicadoWorkUnit();
		verificacion.contexto = contexto;
		return verificacion;
	}
}
