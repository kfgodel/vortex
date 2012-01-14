/**
 * 01/12/2011 22:59:08 Copyright (C) 2011 Darío L. García
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
import net.gaia.vortex.lowlevel.impl.ContextoDeRuteoDeMensaje;
import net.gaia.vortex.protocol.messages.ContenidoVortex;
import net.gaia.vortex.protocol.messages.MensajeVortex;
import net.gaia.vortex.protocol.messages.meta.MetamensajeVortex;
import net.gaia.vortex.protocol.messages.routing.AcuseConsumo;
import net.gaia.vortex.protocol.messages.routing.SolicitudAcuseConsumo;
import net.gaia.vortex.protocol.messages.routing.SolicitudEsperaAcuseConsumo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Esta clase representa el trabajo que hace el nodo para procesar un metamensaje recibido
 * 
 * @author D. García
 */
public class ProcesarRecepcionDeMetamensajeWorkUnit implements WorkUnit {
	private static final Logger LOG = LoggerFactory.getLogger(ProcesarRecepcionDeMetamensajeWorkUnit.class);

	private ContextoDeRuteoDeMensaje contexto;

	public static ProcesarRecepcionDeMetamensajeWorkUnit create(final ContextoDeRuteoDeMensaje contexto) {
		final ProcesarRecepcionDeMetamensajeWorkUnit procesado = new ProcesarRecepcionDeMetamensajeWorkUnit();
		procesado.contexto = contexto;
		return procesado;
	}

	/**
	 * @see net.gaia.taskprocessor.api.WorkUnit#doWork()
	 */
	@Override
	public void doWork() throws InterruptedException {
		// Tenemos que ver que tipo de metamensaje es, y en base a eso el tipo de acción a realizar
		final MensajeVortex mensaje = this.contexto.getMensaje();
		final ContenidoVortex contenido = mensaje.getContenido();
		final Object metamensaje = contenido.getValor();
		if (!(metamensaje instanceof MetamensajeVortex)) {
			LOG.error("Se recibió como metamensaje uno que no es: " + metamensaje + ". Abortando procesamiento");
			return;
		}
		final WorkUnit tareaDelMetamensaje = crearTareaDesdeMetamensaje(metamensaje);
		if (tareaDelMetamensaje != null) {
			contexto.getProcesador().process(tareaDelMetamensaje);
		}
	}

	/**
	 * Evalua el metamensaje para determinar el tipo de tarea que se debe realizar
	 * 
	 * @param mensaje
	 *            El mensaje recibido como metamensaje
	 * @return La tarea a realizar
	 */
	private WorkUnit crearTareaDesdeMetamensaje(final Object metamensaje) {
		if (metamensaje instanceof AcuseConsumo) {
			final AcuseConsumo acuseConsumo = (AcuseConsumo) metamensaje;
			final RecibirAcuseConsumoWorkUnit recibirAcuse = RecibirAcuseConsumoWorkUnit.create(contexto, acuseConsumo);
			return recibirAcuse;
		}
		if (metamensaje instanceof SolicitudAcuseConsumo) {
			final SolicitudAcuseConsumo solicitud = (SolicitudAcuseConsumo) metamensaje;
			final RecibirSolicitudDeAcuseConsumoWorkUnit recibirSolicitud = RecibirSolicitudDeAcuseConsumoWorkUnit
					.create(contexto, solicitud);
			return recibirSolicitud;
		}
		if (metamensaje instanceof SolicitudEsperaAcuseConsumo) {
			// Deberíamos enviar la confirmación nuevamente
			final SolicitudEsperaAcuseConsumo solicitud = (SolicitudEsperaAcuseConsumo) metamensaje;
			final RecibirSolicitudDeEsperaAcuseConsumoWorkUnit recibirSolicitud = RecibirSolicitudDeEsperaAcuseConsumoWorkUnit.create(
					contexto, solicitud);
			return recibirSolicitud;
		}
		return null;
	}
}
