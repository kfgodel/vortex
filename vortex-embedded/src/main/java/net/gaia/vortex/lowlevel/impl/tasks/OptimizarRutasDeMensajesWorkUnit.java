/**
 * 14/01/2012 19:44:56 Copyright (C) 2011 Darío L. García
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
import net.gaia.vortex.lowlevel.impl.receptores.RegistroDeReceptores;
import net.gaia.vortex.lowlevel.impl.ruteo.ContextoDeRuteoDeMensaje;
import net.gaia.vortex.lowlevel.impl.ruteo.ControlDeRuteo;
import net.gaia.vortex.lowlevel.impl.ruteo.ReportePerformanceRuteo;
import net.gaia.vortex.protocol.messages.MensajeVortex;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Esta clase representa la operación de optimización de las rutas de mensajes de acuerdo al
 * desempeño de cada canal
 * 
 * @author D. García
 */
public class OptimizarRutasDeMensajesWorkUnit implements WorkUnit {
	private static final Logger LOG = LoggerFactory.getLogger(OptimizarRutasDeMensajesWorkUnit.class);

	private ContextoDeRuteoDeMensaje contexto;

	/**
	 * @see net.gaia.taskprocessor.api.WorkUnit#doWork()
	 */
	@Override
	public void doWork() throws InterruptedException {
		LOG.debug("Optimizando rutas de mensajes en base resultados de mensaje[{}]", contexto.getMensaje());
		// Calificamos a cada receptor segun su performance
		final ControlDeRuteo controlDeRuteo = contexto.getControl();
		final MensajeVortex mensaje = contexto.getMensaje();
		final ReportePerformanceRuteo reportePerformance = ReportePerformanceRuteo.create(controlDeRuteo, mensaje);

		// Ajustamos los pesos de cada receptor para modificar futuros ruteos
		final RegistroDeReceptores registroDeReceptores = contexto.getRegistroDeReceptoresDelNodo();
		registroDeReceptores.ajustarPesosDeAcuerdoA(reportePerformance);

	}

	public static OptimizarRutasDeMensajesWorkUnit create(final ContextoDeRuteoDeMensaje contexto) {
		final OptimizarRutasDeMensajesWorkUnit name = new OptimizarRutasDeMensajesWorkUnit();
		name.contexto = contexto;
		return name;
	}
}
