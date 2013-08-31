/**
 * 01/09/2012 02:18:38 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.core.impl.atomos.support;

import net.gaia.taskprocessor.api.WorkUnit;
import net.gaia.taskprocessor.api.processor.TaskProcessor;
import net.gaia.vortex.api.basic.Receptor;
import net.gaia.vortex.api.condiciones.Condicion;
import net.gaia.vortex.api.mensajes.MensajeVortex;
import net.gaia.vortex.core.api.atomos.condicional.FiltroViejo;
import net.gaia.vortex.core.impl.condiciones.SiempreFalse;
import net.gaia.vortex.core.impl.condiciones.SiempreTrue;
import net.gaia.vortex.core.impl.tasks.condicional.FiltrarMensaje;
import ar.com.dgarcia.lang.strings.ToString;

/**
 * Esta clase sirve de base para los átomos que representan filtros de mensajes
 * 
 * @author D. García
 */
@Deprecated
public abstract class NexoFiltroSupport extends NexoSupport implements FiltroViejo {

	private Condicion condicion;
	public static final String condicion_FIELD = "condicion";

	public Condicion getCondicion() {
		return condicion;
	}

	public void setCondicion(final Condicion condicion) {
		if (condicion == null) {
			throw new IllegalArgumentException("La condicion del proxy no puede ser null. A lo sumo una entre "
					+ SiempreTrue.class + " y " + SiempreFalse.class);
		}
		this.condicion = condicion;
	}

	protected void initializeWith(final TaskProcessor processor, final Receptor delegado, final Condicion condicion) {
		this.initializeWith(processor, delegado);
		setCondicion(condicion);
	}

	/**
	 * @see java.lang.Object#toString()
	 */

	@Override
	public String toString() {
		return ToString.de(this).con(numeroDeInstancia_FIELD, getNumeroDeInstancia()).con(condicion_FIELD, condicion)
				.con(destino_FIELD, getDestino()).toString();
	}

	/**
	 * @see net.gaia.vortex.core.impl.atomos.support.NexoSupport#crearTareaAlRecibir(net.gaia.vortex.api.mensajes.MensajeVortex)
	 */

	@Override
	protected WorkUnit crearTareaAlRecibir(final MensajeVortex mensaje) {
		return FiltrarMensaje.create(mensaje, condicion, getDestino());
	}

}