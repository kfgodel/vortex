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
package net.gaia.vortex.core2.impl.atomos.condicional;

import net.gaia.taskprocessor.api.TaskProcessor;
import net.gaia.taskprocessor.api.WorkUnit;
import net.gaia.vortex.core2.api.annon.Atomo;
import net.gaia.vortex.core2.api.atomos.ComponenteProxy;
import net.gaia.vortex.core2.api.atomos.ComponenteVortex;
import net.gaia.vortex.core2.api.atomos.Condicion;
import net.gaia.vortex.core2.impl.atomos.ProxySupport;
import net.gaia.vortex.core2.impl.atomos.tasks.EntregarMensajeADelegado;

import com.google.common.base.Objects;

/**
 * Esta clase representa un {@link ComponenteProxy} que puede filtrar mensajes recibidos en base a
 * una {@link Condicion}
 * 
 * @author D. García
 */
@Atomo
public class ProxyCondicional extends ProxySupport {

	private Condicion condicion;
	public static final String condicion_FIELD = "condicion";

	public Condicion getCondicion() {
		return condicion;
	}

	public void setCondicion(final Condicion condicion) {
		if (condicion == null) {
			throw new IllegalArgumentException("La condicion del proxy no puede ser null. A lo sumo una entre "
					+ AceptarTodos.class + " y " + RechazarTodos.class);
		}
		this.condicion = condicion;
	}

	/**
	 * @see net.gaia.vortex.core2.impl.atomos.ProxySupport#agregarComportamientoA(net.gaia.vortex.core2.impl.atomos.tasks.EntregarMensajeADelegado)
	 */
	@Override
	protected WorkUnit agregarComportamientoA(final EntregarMensajeADelegado entregaEnBackground) {
		return EvaluarCondicionYDelegar.create(condicion, entregaEnBackground);
	}

	protected void initializeWith(final TaskProcessor processor, final ComponenteVortex delegado,
			final Condicion condicion) {
		super.initializeWith(processor, delegado);
		setCondicion(condicion);
	}

	public static ProxyCondicional create(final TaskProcessor processor, final Condicion condicion,
			final ComponenteVortex delegado) {
		final ProxyCondicional condicional = new ProxyCondicional();
		condicional.initializeWith(processor, delegado, condicion);
		return condicional;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return Objects.toStringHelper(this).add(condicion_FIELD, condicion).add(delegado_FIELD, getDelegado())
				.toString();
	}
}
