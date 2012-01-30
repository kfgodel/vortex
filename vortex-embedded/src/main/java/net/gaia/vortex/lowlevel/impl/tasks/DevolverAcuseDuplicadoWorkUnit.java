/**
 * 12/01/2012 23:00:48 Copyright (C) 2011 Darío L. García
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
import net.gaia.vortex.lowlevel.impl.receptores.ReceptorVortex;
import net.gaia.vortex.lowlevel.impl.ruteo.ContextoDeRuteoDeMensaje;
import net.gaia.vortex.meta.Loggers;
import net.gaia.vortex.protocol.messages.routing.AcuseDuplicado;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Esta clase representa la tarea realizada por el nodo cuando encuentra un duplicado y devuelve un
 * acuse por el error
 * 
 * @author D. García
 */
public class DevolverAcuseDuplicadoWorkUnit implements WorkUnit {
	private static final Logger LOG = LoggerFactory.getLogger(DevolverAcuseDuplicadoWorkUnit.class);

	private ContextoDeRuteoDeMensaje contexto;

	private final Runnable terminarProcesoActual = new Runnable() {
		@Override
		public void run() {
			// Seguimos con el próximo mensaje recibido del receptor
			final ReceptorVortex emisor = contexto.getEmisor();
			final TerminarProcesoDeMensajeWorkUnit terminarProceso = TerminarProcesoDeMensajeWorkUnit.create(emisor,
					contexto.getNodo());
			contexto.getProcesador().process(terminarProceso);
		}
	};

	public static DevolverAcuseDuplicadoWorkUnit create(final ContextoDeRuteoDeMensaje contexto) {
		final DevolverAcuseDuplicadoWorkUnit devolucion = new DevolverAcuseDuplicadoWorkUnit();
		devolucion.contexto = contexto;
		return devolucion;
	}

	/**
	 * @see net.gaia.taskprocessor.api.WorkUnit#doWork()
	 */
	@Override
	public void doWork() throws InterruptedException {
		final ReceptorVortex emisor = contexto.getEmisor();
		if (emisor == null) {
			// No existe a quién devolverle la confirmación
			LOG.error("Se recibió un mensaje[{}] duplicado[{}] de fuente nula. Ignorando error", contexto.getMensaje());
			return;
		}
		LOG.debug("Armando acuse de duplicado para el mensaje[{}]", contexto.getMensaje());
		Loggers.RUTEO.info("DUPLICADO mensaje[{}]", contexto.getMensaje());
		final AcuseDuplicado acuseDeFalla = AcuseDuplicado.create();
		final EnviarAcuseWorkUnit envio = EnviarAcuseWorkUnit.create(contexto, acuseDeFalla, terminarProcesoActual);
		contexto.getProcesador().process(envio);

	}

}
