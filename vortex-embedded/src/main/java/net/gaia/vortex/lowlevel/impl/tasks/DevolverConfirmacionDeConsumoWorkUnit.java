/**
 * 27/11/2011 22:26:52 Copyright (C) 2011 Darío L. García
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
import net.gaia.vortex.lowlevel.impl.ControlDeRuteo;
import net.gaia.vortex.lowlevel.impl.ReceptorVortex;
import net.gaia.vortex.meta.Decision;
import net.gaia.vortex.protocol.confirmations.ConfirmacionConsumo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Esta clase representa la tarea de devolver una confirmación de consumo con elementos en 0
 * 
 * @author D. García
 */
public class DevolverConfirmacionDeConsumoWorkUnit implements WorkUnit {
	private static final Logger LOG = LoggerFactory.getLogger(DevolverConfirmacionDeConsumoWorkUnit.class);

	private ContextoDeRuteoDeMensaje contexto;

	public static DevolverConfirmacionDeConsumoWorkUnit create(final ContextoDeRuteoDeMensaje contexto) {
		final DevolverConfirmacionDeConsumoWorkUnit devolucion = new DevolverConfirmacionDeConsumoWorkUnit();
		devolucion.contexto = contexto;
		return devolucion;
	}

	/**
	 * @see net.gaia.taskprocessor.api.WorkUnit#doWork()
	 */
	@HasDependencyOn({ Decision.LA_TAREA_DE_ENVIO_DE_CONFIRMACION_ASIGNA_EL_ID_DEL_MENSAJE,
			Decision.LA_TAREA_DE_ENVIO_DE_CONFIRMACION_REQUIERE_EMISOR_REAL })
	public void doWork() throws InterruptedException {
		final ReceptorVortex emisor = this.contexto.getEmisor();
		if (emisor == null) {
			// No existe a quién devolverle la confirmación
			LOG.error("Se recibió un mensaje[{}] de fuente nula que no fue interesante para nadie",
					contexto.getMensaje());
			return;
		}
		ControlDeRuteo controlDeRuteo = this.contexto.getControl();
		final ConfirmacionConsumo confirmacionConsumo = controlDeRuteo.crearConfirmacionDeConsumo();
		final EnviarConfirmacionWorkUnit envio = EnviarConfirmacionWorkUnit.create(contexto, confirmacionConsumo);
		contexto.getProcesador().process(envio);
	}
}
