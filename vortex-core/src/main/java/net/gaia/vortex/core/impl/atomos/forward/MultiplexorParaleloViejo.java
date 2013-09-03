/**
 * 13/06/2012 11:49:16 Copyright (C) 2011 10Pines S.R.L.
 */
package net.gaia.vortex.core.impl.atomos.forward;

import net.gaia.taskprocessor.api.WorkUnit;
import net.gaia.taskprocessor.api.processor.TaskProcessor;
import net.gaia.vortex.api.annotations.clases.Atomo;
import net.gaia.vortex.api.mensajes.MensajeVortex;
import net.gaia.vortex.core.impl.atomos.support.MultiplexorSupportViejo;
import net.gaia.vortex.core.impl.tasks.forward.MultiplexarMensajeViejo;
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
@Deprecated
public class MultiplexorParaleloViejo extends MultiplexorSupportViejo {

	private ListenerDeMetricas listenerMetricas;

	/**
	 * @see net.gaia.vortex.core.impl.atomos.support.procesador.ReceptorConProcesador#crearTareaAlRecibir(net.gaia.vortex.api.mensajes.MensajeVortex)
	 */

	@Override
	@HasDependencyOn(Decision.LA_LISTA_DE_DESTINOS_ES_UN_COPY_ON_WRITE)
	protected WorkUnit crearTareaAlRecibir(final MensajeVortex mensaje) {
		// Por cada destino derivamos la entrega al procesador interno
		final MultiplexarMensajeViejo multiplexion = MultiplexarMensajeViejo.create(mensaje, getDestinos(), listenerMetricas);
		return multiplexion;
	}

	public ListenerDeMetricas getListenerMetricas() {
		return listenerMetricas;
	}

	public void setListenerMetricas(final ListenerDeMetricas listenerMetricas) {
		this.listenerMetricas = listenerMetricas;
	}

	public static MultiplexorParaleloViejo create(final TaskProcessor processor) {
		final MultiplexorParaleloViejo multiplexor = new MultiplexorParaleloViejo();
		multiplexor.initializeWith(processor);
		return multiplexor;
	}

}
