/**
 * 13/06/2012 11:49:16 Copyright (C) 2011 10Pines S.R.L.
 */
package net.gaia.vortex.core2.impl.atomos.multiplexor;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import net.gaia.taskprocessor.api.TaskProcessor;
import net.gaia.vortex.core2.api.atomos.ComponenteVortex;
import net.gaia.vortex.core2.api.atomos.Multiplexor;
import net.gaia.vortex.core2.impl.atomos.ComponenteConProcesadorSupport;
import net.gaia.vortex.core3.api.annon.Atomo;
import net.gaia.vortex.core3.api.atomos.mensaje.MensajeVortex;
import net.gaia.vortex.core3.impl.tasks.DelegarMensaje;

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
	private List<ComponenteVortex> destinos;
	public static final String destinos_FIELD = "destinos";

	/**
	 * @see net.gaia.vortex.core2.api.atomos.ComponenteVortex#recibirMensaje(net.gaia.vortex.core3.api.atomos.mensaje.MensajeVortex)
	 */
	@Override
	public void recibirMensaje(final MensajeVortex mensaje) {
		// Por cada destino derivamos la entrega al procesador interno
		for (final ComponenteVortex destino : destinos) {
			final DelegarMensaje entregaEnBackground = DelegarMensaje.create(mensaje, destino);
			procesarEnThreadPropio(entregaEnBackground);
		}
	}

	/**
	 * @see net.gaia.vortex.core2.api.atomos.Multiplexor#agregarDestino(net.gaia.vortex.core2.api.atomos.ComponenteVortex)
	 */
	@Override
	public void agregarDestino(final ComponenteVortex destino) {
		if (destino == null) {
			throw new IllegalArgumentException("El destino del multiplexor no puede ser null");
		}
		destinos.add(destino);
	}

	/**
	 * @see net.gaia.vortex.core2.api.atomos.Multiplexor#quitarDestino(net.gaia.vortex.core2.api.atomos.ComponenteVortex)
	 */
	@Override
	public void quitarDestino(final ComponenteVortex destino) {
		destinos.remove(destino);
	}

	/**
	 * @see net.gaia.vortex.core2.impl.atomos.ComponenteConProcesadorSupport#initializeWith(net.gaia.taskprocessor.api.TaskProcessor)
	 */
	@Override
	protected void initializeWith(final TaskProcessor processor) {
		super.initializeWith(processor);
		destinos = new CopyOnWriteArrayList<ComponenteVortex>();
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
