/**
 * 14/01/2012 19:40:34 Copyright (C) 2011 Darío L. García
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

import net.gaia.vortex.lowlevel.impl.receptores.ReceptorVortex;
import net.gaia.vortex.lowlevel.impl.ruteo.ContextoDeRuteoDeMensaje;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Esta clase representa la operación de cierre del ruteo que envia el acuse de consumo y balancea
 * los pesos de los tags
 * 
 * @author D. García
 */
public class TerminarRuteoWorkUnit implements TareaParaReceptor {
	private static final Logger LOG = LoggerFactory.getLogger(TerminarRuteoWorkUnit.class);

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
		LOG.debug("Terminando ruteo para mensaje[{}]", contexto.getMensaje());
		// Devolvemos el acuse al emisor original
		final DevolverAcuseConsumoWorkUnit devolverAcuse = DevolverAcuseConsumoWorkUnit.create(contexto);
		contexto.getProcesador().process(devolverAcuse);

		// Recalibramos las rutas de los mensajes
		final OptimizarRutasDeMensajesWorkUnit optimizacion = OptimizarRutasDeMensajesWorkUnit.create(contexto);
		contexto.getProcesador().process(optimizacion);
	}

	public static TerminarRuteoWorkUnit create(final ContextoDeRuteoDeMensaje contextoDeRuteo) {
		final TerminarRuteoWorkUnit terminar = new TerminarRuteoWorkUnit();
		terminar.contexto = contextoDeRuteo;
		return terminar;
	}
}
