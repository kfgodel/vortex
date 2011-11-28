/**
 * 27/11/2011 21:43:40 Copyright (C) 2011 Darío L. García
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
import net.gaia.vortex.lowlevel.impl.ReceptorVortex;
import net.gaia.vortex.meta.Decision;
import net.gaia.vortex.protocol.confirmations.ConfirmacionRecepcion;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Esta clase representa la tarea de devolver una confirmación de recepción del mensaje. Según si
 * hubo error o no, esta tarea creará una confirmación positiva o negativa.<br>
 * Si no existe receptor o el mensaje no posee ID, esta tarea sólo loguea el error
 * 
 * @author D. García
 */
public class DevolverConfirmacionRecepcionWorkUnit implements WorkUnit {
	private static final Logger LOG = LoggerFactory.getLogger(DevolverConfirmacionRecepcionWorkUnit.class);

	private ContextoDeRuteoDeMensaje contexto;
	private String errorRegistrado;

	/**
	 * Crea una tarea de devolución de mensaje de confirmación con el cual el emisor puede obtener
	 * feedback acerca de su pedido de ruteo. Si se indica error la confirmación será de rechazo
	 * 
	 * @param contexto
	 *            El contexto de ruteo para esta tarea
	 * @param error
	 *            El error producido durante la validación del mensaje
	 * @return La tarea para devolver un mensaje de confirmación
	 */
	public static DevolverConfirmacionRecepcionWorkUnit create(final ContextoDeRuteoDeMensaje contexto,
			final String error) {
		final DevolverConfirmacionRecepcionWorkUnit devolucion = new DevolverConfirmacionRecepcionWorkUnit();
		devolucion.contexto = contexto;
		devolucion.errorRegistrado = error;
		return devolucion;
	}

	/**
	 * @see net.gaia.taskprocessor.api.WorkUnit#doWork()
	 */
	@HasDependencyOn({ Decision.LA_TAREA_DE_ENVIO_DE_CONFIRMACION_ASIGNA_EL_ID_DEL_MENSAJE,
			Decision.LA_TAREA_DE_ENVIO_DE_CONFIRMACION_REQUIERE_EMISOR_REAL })
	public void doWork() throws InterruptedException {
		final ReceptorVortex emisor = contexto.getEmisor();
		if (emisor == null) {
			// No existe a quién devolverle la confirmación
			LOG.error("Se recibió un mensaje[{}] invalido[{}] de fuente nula. Ignorando error", contexto.getMensaje(),
					errorRegistrado);
			return;
		}

		final ConfirmacionRecepcion confirmacionRecepcion = ConfirmacionRecepcion.create(null, errorRegistrado);
		final EnviarConfirmacionWorkUnit envio = EnviarConfirmacionWorkUnit.create(contexto, confirmacionRecepcion);
		contexto.getProcesador().process(envio);
	}
}
