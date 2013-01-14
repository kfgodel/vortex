/**
 * 13/06/2012 11:49:16 Copyright (C) 2011 10Pines S.R.L.
 */
package net.gaia.vortex.core.impl.atomos.forward;

import net.gaia.taskprocessor.api.TaskProcessor;
import net.gaia.taskprocessor.api.WorkUnit;
import net.gaia.vortex.core.api.annotations.Atomo;
import net.gaia.vortex.core.api.mensaje.MensajeVortex;
import net.gaia.vortex.core.impl.atomos.support.MultiplexorSupport;
import net.gaia.vortex.core.impl.tasks.MultiplexarMensaje;
import net.gaia.vortex.core.prog.Decision;
import ar.com.dgarcia.coding.anno.HasDependencyOn;
import ar.com.dgarcia.lang.metrics.ListenerDeMetricas;

/**
 * Esta clase representa el multiplexor de mensajes que entrega a varios destinos lo que recibe
 * utilizando threads independientes para hacerlo lo más paralelo posible
 * 
 * @author D. García
 */
@Atomo
public class MultiplexorParalelo extends MultiplexorSupport {

	private ListenerDeMetricas listenerMetricas;

	/**
	 * @see net.gaia.vortex.core.impl.atomos.support.procesador.ReceptorConProcesador#crearTareaAlRecibir(net.gaia.vortex.core.api.mensaje.MensajeVortex)
	 */
	@Override
	@HasDependencyOn(Decision.LA_LISTA_DE_DESTINOS_ES_UN_COPY_ON_WRITE)
	protected WorkUnit crearTareaAlRecibir(final MensajeVortex mensaje) {
		// Por cada destino derivamos la entrega al procesador interno
		final MultiplexarMensaje multiplexion = MultiplexarMensaje.create(mensaje, getDestinos(), getProcessor(),
				listenerMetricas);
		return multiplexion;
	}

	public ListenerDeMetricas getListenerMetricas() {
		return listenerMetricas;
	}

	public void setListenerMetricas(final ListenerDeMetricas listenerMetricas) {
		this.listenerMetricas = listenerMetricas;
	}

	public static MultiplexorParalelo create(final TaskProcessor processor) {
		final MultiplexorParalelo multiplexor = new MultiplexorParalelo();
		multiplexor.initializeWith(processor);
		return multiplexor;
	}

}
