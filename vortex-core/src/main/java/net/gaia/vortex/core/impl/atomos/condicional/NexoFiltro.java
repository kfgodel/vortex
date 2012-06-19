/**
 * 13/06/2012 00:43:49 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.core.impl.atomos.condicional;

import net.gaia.taskprocessor.api.TaskProcessor;
import net.gaia.taskprocessor.api.WorkUnit;
import net.gaia.vortex.core.api.annon.Atomo;
import net.gaia.vortex.core.api.atomos.Receptor;
import net.gaia.vortex.core.api.atomos.condicional.Filtro;
import net.gaia.vortex.core.api.condiciones.Condicion;
import net.gaia.vortex.core.api.mensaje.MensajeVortex;
import net.gaia.vortex.core.impl.atomos.forward.NexoSupport;
import net.gaia.vortex.core.impl.condiciones.SiempreFalse;
import net.gaia.vortex.core.impl.condiciones.SiempreTrue;
import net.gaia.vortex.core.impl.tasks.FiltrarMensaje;

import com.google.common.base.Objects;

/**
 * Esta clase es la implementación del {@link Filtro} utilizando threads independientes para el
 * filtrado
 * 
 * @author D. García
 */
@Atomo
public class NexoFiltro extends NexoSupport implements Filtro {

	private Condicion condicion;
	public static final String condicion_FIELD = "condicion";

	@Override
	public Condicion getCondicion() {
		return condicion;
	}

	@Override
	public void setCondicion(final Condicion condicion) {
		if (condicion == null) {
			throw new IllegalArgumentException("La condicion del proxy no puede ser null. A lo sumo una entre "
					+ SiempreTrue.class + " y " + SiempreFalse.class);
		}
		this.condicion = condicion;
	}

	protected void initializeWith(final TaskProcessor processor, final Receptor delegado, final Condicion condicion) {
		super.initializeWith(processor, delegado);
		setCondicion(condicion);
	}

	public static NexoFiltro create(final TaskProcessor processor, final Condicion condicion, final Receptor delegado) {
		final NexoFiltro condicional = new NexoFiltro();
		condicional.initializeWith(processor, delegado, condicion);
		return condicional;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return Objects.toStringHelper(this).add(condicion_FIELD, condicion).add(destino_FIELD, getDestino())
				.toString();
	}

	/**
	 * @see net.gaia.vortex.core.impl.atomos.forward.NexoSupport#crearTareaPara(net.gaia.vortex.core.api.mensaje.MensajeVortex)
	 */
	@Override
	protected WorkUnit crearTareaPara(final MensajeVortex mensaje) {
		return FiltrarMensaje.create(mensaje, condicion, getDestino());
	}
}
