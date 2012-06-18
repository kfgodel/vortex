/**
 * 13/06/2012 11:49:16 Copyright (C) 2011 10Pines S.R.L.
 */
package net.gaia.vortex.core3.impl.atomos.forward;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import net.gaia.taskprocessor.api.TaskProcessor;
import net.gaia.vortex.core3.api.annon.Atomo;
import net.gaia.vortex.core3.api.atomos.Receptor;
import net.gaia.vortex.core3.api.atomos.forward.Multiplexor;
import net.gaia.vortex.core3.api.mensaje.MensajeVortex;
import net.gaia.vortex.core3.impl.atomos.ComponenteConProcesadorSupport;
import net.gaia.vortex.core3.impl.metricas.ListenerDeMetricas;
import net.gaia.vortex.core3.impl.tasks.MultiplexarMensaje;
import net.gaia.vortex.core3.prog.Decision;
import ar.com.dgarcia.coding.anno.HasDependencyOn;

import com.google.common.base.Objects;

/**
 * Esta clase representa el multiplexor de mensajes que entrega a varios destinos lo que recibe
 * utilizando threads independientes para hacerlo lo más paralelo posible
 * 
 * @author D. García
 */
@Atomo
public class MultiplexorParalelo extends ComponenteConProcesadorSupport implements Multiplexor {

	/**
	 * La lista de destinos, implementada con un {@link CopyOnWriteArrayList} porque esperamos más
	 * recorridas que modificaciones
	 */
	private List<Receptor> destinos;
	public static final String destinos_FIELD = "destinos";

	private ListenerDeMetricas listenerMetricas;

	/**
	 * @see net.gaia.vortex.core3.api.atomos.Receptor#recibir(net.gaia.vortex.core3.api.mensaje.MensajeVortex)
	 */
	@Override
	@HasDependencyOn(Decision.LA_LISTA_DE_DESTINOS_ES_UN_COPY_ON_WRITE)
	public void recibir(final MensajeVortex mensaje) {
		// Por cada destino derivamos la entrega al procesador interno
		final MultiplexarMensaje multiplexion = MultiplexarMensaje.create(mensaje, destinos, getProcessor(),
				listenerMetricas);
		procesarEnThreadPropio(multiplexion);
	}

	/**
	 * @see net.gaia.vortex.core3.api.atomos.forward.Multiplexor#conectarCon(net.gaia.vortex.core3.api.atomos.Receptor)
	 */
	@Override
	public void conectarCon(final Receptor destino) {
		if (destino == null) {
			throw new IllegalArgumentException("El destino del multiplexor no puede ser null");
		}
		destinos.add(destino);
	}

	/**
	 * @see net.gaia.vortex.core3.api.atomos.forward.Multiplexor#desconectarDe(net.gaia.vortex.core3.api.atomos.Receptor)
	 */
	@Override
	public void desconectarDe(final Receptor destino) {
		destinos.remove(destino);
	}

	public ListenerDeMetricas getListenerMetricas() {
		return listenerMetricas;
	}

	public void setListenerMetricas(final ListenerDeMetricas listenerMetricas) {
		this.listenerMetricas = listenerMetricas;
	}

	/**
	 * @see net.gaia.vortex.core3.impl.atomos.ComponenteConProcesadorSupport#initializeWith(net.gaia.taskprocessor.api.TaskProcessor)
	 */
	@Override
	@HasDependencyOn(Decision.LA_LISTA_DE_DESTINOS_ES_UN_COPY_ON_WRITE)
	protected void initializeWith(final TaskProcessor processor) {
		super.initializeWith(processor);
		destinos = new CopyOnWriteArrayList<Receptor>();
	}

	public static MultiplexorParalelo create(final TaskProcessor processor) {
		final MultiplexorParalelo multiplexor = new MultiplexorParalelo();
		multiplexor.initializeWith(processor);
		return multiplexor;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return Objects.toStringHelper(this).add(destinos_FIELD, destinos).toString();
	}
}
