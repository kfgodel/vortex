/**
 * 27/11/2011 22:32:07 Copyright (C) 2011 Darío L. García
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

import net.gaia.annotations.HasDependencyOn;
import net.gaia.taskprocessor.api.WorkUnit;
import net.gaia.vortex.lowlevel.impl.ContextoDeRuteoDeMensaje;
import net.gaia.vortex.lowlevel.impl.NodoVortexConTasks;
import net.gaia.vortex.lowlevel.impl.receptores.ReceptorVortex;
import net.gaia.vortex.meta.Decision;
import net.gaia.vortex.protocol.messages.IdVortex;
import net.gaia.vortex.protocol.messages.MensajeVortex;
import net.gaia.vortex.protocol.messages.routing.Acuse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Esta clase representa la tarea de enviar una confirmación al emisor de un mensaje. A diferencia
 * de otros envíos esta tarea no requiere confirmación del envío
 * 
 * @author D. García
 */
public class EnviarAcuseWorkUnit implements WorkUnit {
	private static final Logger LOG = LoggerFactory.getLogger(EnviarAcuseWorkUnit.class);

	private ContextoDeRuteoDeMensaje contexto;

	private Acuse acuse;
	private Runnable whenDone;

	public static EnviarAcuseWorkUnit create(final ContextoDeRuteoDeMensaje contexto, final Acuse acuse,
			final Runnable whenDone) {
		final EnviarAcuseWorkUnit envio = new EnviarAcuseWorkUnit();
		envio.contexto = contexto;
		envio.acuse = acuse;
		envio.whenDone = whenDone;
		return envio;
	}

	/**
	 * @see net.gaia.taskprocessor.api.WorkUnit#doWork()
	 */
	@Override
	@HasDependencyOn({ Decision.LA_TAREA_DE_ENVIO_DE_ACUSE_REQUIERE_EMISOR_REAL,
			Decision.LA_TAREA_DE_ENVIO_DE_ACUSE_ASIGNA_EL_ID_DEL_MENSAJE })
	public void doWork() throws InterruptedException {
		// Establecemos el ID del mensaje para el que se envía la confirmación
		final MensajeVortex mensaje = contexto.getMensaje();

		final IdVortex identificacion = mensaje.getIdentificacion();
		this.acuse.setIdMensajeInvolucrado(identificacion);
		LOG.debug("Acuse[{}] completado con ID para el mensaje[{}]", acuse, mensaje);

		final ReceptorVortex emisor = contexto.getEmisor();
		final NodoVortexConTasks nodo = contexto.getNodo();
		final ProcesarEnvioDeMetamensajeWorkUnit envioMentamensaje = ProcesarEnvioDeMetamensajeWorkUnit.create(nodo,
				emisor, acuse, whenDone);
		contexto.getProcesador().process(envioMentamensaje);
	}

}
