/**
 * 13/06/2012 01:37:46 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.core2.impl.atomos.transformador;

import net.gaia.taskprocessor.api.TaskProcessor;
import net.gaia.taskprocessor.api.WorkUnit;
import net.gaia.vortex.core2.api.annon.Atomo;
import net.gaia.vortex.core2.api.atomos.ComponenteProxy;
import net.gaia.vortex.core2.api.atomos.ComponenteVortex;
import net.gaia.vortex.core2.api.atomos.Transformacion;
import net.gaia.vortex.core2.impl.atomos.ProxySupport;
import net.gaia.vortex.core2.impl.atomos.tasks.EntregarMensajeADelegado;

import com.google.common.base.Objects;

/**
 * Esta clase representa un {@link ComponenteProxy} que realiza una transformación en el mensaje
 * antes de delegarlo al otro componente utilizando una instancia de {@link Transformacion}
 * 
 * @author D. García
 */
@Atomo
public class ProxyTransformador extends ProxySupport {

	private Transformacion transformacion;
	public static final String transformacion_FIELD = "transformacion";

	public Transformacion getTransformacion() {
		return transformacion;
	}

	public void setTransformacion(final Transformacion transformacion) {
		if (transformacion == null) {
			throw new IllegalArgumentException(
					"La transformacion del proxy ni puede ser nula. A lo sumo una instancia de "
							+ TransformacionNula.class);
		}
		this.transformacion = transformacion;
	}

	/**
	 * @see net.gaia.vortex.core2.impl.atomos.ProxySupport#agregarComportamientoA(net.gaia.vortex.core2.impl.atomos.tasks.EntregarMensajeADelegado)
	 */
	@Override
	protected WorkUnit agregarComportamientoA(final EntregarMensajeADelegado entregaEnBackground) {
		return TransformarElMensajeYDelegar.create(transformacion, entregaEnBackground);
	}

	public static ProxyTransformador create(final TaskProcessor processor, final Transformacion transformacion,
			final ComponenteVortex delegado) {
		final ProxyTransformador transformador = new ProxyTransformador();
		transformador.initializeWith(processor, delegado, transformacion);
		return transformador;
	}

	private void initializeWith(final TaskProcessor processor, final ComponenteVortex delegado,
			final Transformacion transformacion) {
		super.initializeWith(processor, delegado);
		setTransformacion(transformacion);
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return Objects.toStringHelper(this).add(transformacion_FIELD, transformacion)
				.add(delegado_FIELD, getDelegado()).toString();
	}
}
