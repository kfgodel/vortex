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
import net.gaia.vortex.lowlevel.impl.mensajes.MemoriaDeMensajes;
import net.gaia.vortex.lowlevel.impl.receptores.ReceptorVortex;
import net.gaia.vortex.lowlevel.impl.ruteo.ContextoDeRuteoDeMensaje;
import net.gaia.vortex.protocol.messages.MensajeVortex;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Esta clase representa la tarea de verificar si el mensaje está duplicado respecto a los mensajes
 * recibidos en el nodo
 * 
 * @author D. García
 */
public class VerificarDuplicadoWorkUnit implements TareaParaReceptor {
	private static final Logger LOG = LoggerFactory.getLogger(VerificarDuplicadoWorkUnit.class);

	private ContextoDeRuteoDeMensaje contexto;

	/**
	 * @see net.gaia.vortex.lowlevel.impl.tasks.TareaParaReceptor#esPara(net.gaia.vortex.lowlevel.impl.receptores.ReceptorVortex)
	 */
	@Override
	public boolean esPara(final ReceptorVortex receptor) {
		final ReceptorVortex emisor = contexto.getEmisor();
		return emisor == receptor;
	}

	/**
	 * @see net.gaia.taskprocessor.api.WorkUnit#doWork()
	 */
	@Override
	public void doWork() throws InterruptedException {
		final MensajeVortex mensaje = contexto.getMensaje();
		LOG.debug("Verificando duplicados para mensaje[{}]", mensaje);

		final MemoriaDeMensajes memoria = contexto.getMemoriaDeMensajes();
		final boolean esDuplicado = memoria.registrarSiNoRecuerdaA(mensaje);
		if (esDuplicado) {
			LOG.info("Duplicado detectado en Mensaje[{}]", mensaje);
			final DevolverAcuseDuplicadoWorkUnit devolucion = DevolverAcuseDuplicadoWorkUnit.create(contexto);
			this.contexto.getProcesador().process(devolucion);
			return;
		}
		LOG.debug("Verificación superada para Mensaje[{}]: no duplicado", mensaje);
		// Puede que sea duplicado pero ya no lo recordamos ;) lo tratamos como nuevo
		final ProcesarRecepcionDeMensajeWorkUnit recibirMensaje = ProcesarRecepcionDeMensajeWorkUnit.create(contexto);
		final TaskProcessor procesador = this.contexto.getProcesador();
		procesador.process(recibirMensaje);
	}

	public static VerificarDuplicadoWorkUnit create(final ContextoDeRuteoDeMensaje contexto) {
		final VerificarDuplicadoWorkUnit verificacion = new VerificarDuplicadoWorkUnit();
		verificacion.contexto = contexto;
		return verificacion;
	}
}
