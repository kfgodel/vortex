/**
 * 13/06/2012 17:20:12 Copyright (C) 2011 10Pines S.R.L.
 */
package net.gaia.vortex.core2.impl.atomos.bifurcador;

import net.gaia.taskprocessor.api.WorkUnit;
import net.gaia.vortex.core2.api.MensajeVortex;
import net.gaia.vortex.core2.api.atomos.ComponenteVortex;
import net.gaia.vortex.core2.api.atomos.conditional.Condicion;
import net.gaia.vortex.core2.impl.atomos.tasks.EntregarMensajeADelegado;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Esta clase representa la tarea realizada en thread propio que evalua una condición para elegir el
 * delegado al que envia el mensaje
 * 
 * @author D. García
 */
public class ElegirDelegadoConCondicion implements WorkUnit {
	private static final Logger LOG = LoggerFactory.getLogger(ElegirDelegadoConCondicion.class);

	private MensajeVortex mensaje;
	private Condicion condicion;
	private ComponenteVortex delegadoPorTrue;
	private ComponenteVortex delegadoPorFalse;

	/**
	 * @see net.gaia.taskprocessor.api.WorkUnit#doWork()
	 */
	@Override
	public void doWork() throws InterruptedException {
		boolean delegarPorTrue;
		try {
			delegarPorTrue = condicion.esCumplidaPor(mensaje);
		} catch (final Exception e) {
			LOG.error("Se produjo un error al evaluar la condicion[" + condicion + "] sobre el mensaje[" + mensaje
					+ "] para bifurcar. Descartando mensaje", e);
			return;
		}
		ComponenteVortex delegadoElegido;
		if (delegarPorTrue) {
			delegadoElegido = delegadoPorTrue;
		} else {
			delegadoElegido = delegadoPorFalse;
		}
		final EntregarMensajeADelegado entregaDeMensaje = EntregarMensajeADelegado.create(mensaje, delegadoElegido);
		entregaDeMensaje.doWork();
	}

	public static ElegirDelegadoConCondicion create(final MensajeVortex mensaje, final Condicion condicion,
			final ComponenteVortex delegadoPorTrue, final ComponenteVortex delegadoPorFalse) {
		final ElegirDelegadoConCondicion elegir = new ElegirDelegadoConCondicion();
		elegir.condicion = condicion;
		elegir.delegadoPorFalse = delegadoPorFalse;
		elegir.delegadoPorTrue = delegadoPorTrue;
		elegir.mensaje = mensaje;
		return elegir;
	}
}
