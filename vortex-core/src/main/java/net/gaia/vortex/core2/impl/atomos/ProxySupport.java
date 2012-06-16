/**
 * 13/06/2012 00:47:39 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.core2.impl.atomos;

import net.gaia.taskprocessor.api.TaskProcessor;
import net.gaia.taskprocessor.api.WorkUnit;
import net.gaia.vortex.core2.api.atomos.ComponenteProxy;
import net.gaia.vortex.core2.api.atomos.ComponenteVortex;
import net.gaia.vortex.core3.api.mensaje.MensajeVortex;
import net.gaia.vortex.core3.impl.atomos.ComponenteConProcesadorSupport;
import net.gaia.vortex.core3.impl.atomos.ReceptorNulo;
import net.gaia.vortex.core3.impl.tasks.DelegarMensaje;

import com.google.common.base.Objects;

/**
 * Esta clase implementa comportamiento base para las sub clases de {@link ComponenteProxy}
 * 
 * @author D. García
 */
public abstract class ProxySupport extends ComponenteConProcesadorSupport implements ComponenteProxy {

	private ComponenteVortex delegado;
	public static final String delegado_FIELD = "delegado";

	/**
	 * @see net.gaia.vortex.core3.impl.atomos.ComponenteConProcesadorSupport#initializeWith(net.gaia.taskprocessor.api.TaskProcessor)
	 */
	protected void initializeWith(final TaskProcessor processor, final ComponenteVortex delegado) {
		super.initializeWith(processor);
		setDelegado(delegado);
	}

	/**
	 * @see net.gaia.vortex.core2.api.atomos.ComponenteProxy#setDelegado(net.gaia.vortex.core2.api.atomos.ComponenteVortex)
	 */
	@Override
	public void setDelegado(final ComponenteVortex delegado) {
		if (delegado == null) {
			throw new IllegalArgumentException("El delegado del proxy no puede ser null. A lo sumo un "
					+ ReceptorNulo.class);
		}
		this.delegado = delegado;
	}

	public ComponenteVortex getDelegado() {
		return delegado;
	}

	/**
	 * @see net.gaia.vortex.core2.api.atomos.ComponenteVortex#recibirMensaje(net.gaia.vortex.core3.api.mensaje.MensajeVortex)
	 */
	@Override
	public void recibirMensaje(final MensajeVortex mensaje) {
		final DelegarMensaje entregaEnBackground = DelegarMensaje.create(mensaje, delegado);
		final WorkUnit tareaEnBackground = agregarComportamientoA(entregaEnBackground);
		procesarEnThreadPropio(tareaEnBackground);
	}

	/**
	 * Permite a las sublcases agregar comportamiento adicional a la tarea de entrega
	 * 
	 * @param entregaEnBackground
	 *            La tarea que entrega el mensaje al delegado
	 * @return Un tarea que realice el comportamiento adicional para utilizar en el procesador
	 */
	protected WorkUnit agregarComportamientoA(final DelegarMensaje entregaEnBackground) {
		return entregaEnBackground;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return Objects.toStringHelper(this).add(delegado_FIELD, delegado).toString();
	}
}
