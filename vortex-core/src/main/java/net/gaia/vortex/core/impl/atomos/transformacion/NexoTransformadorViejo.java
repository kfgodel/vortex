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
package net.gaia.vortex.core.impl.atomos.transformacion;

import net.gaia.taskprocessor.api.WorkUnit;
import net.gaia.taskprocessor.api.processor.TaskProcessor;
import net.gaia.vortex.api.annotations.clases.Atomo;
import net.gaia.vortex.api.basic.Receptor;
import net.gaia.vortex.api.mensajes.MensajeVortex;
import net.gaia.vortex.api.transformaciones.Transformacion;
import net.gaia.vortex.core.api.atomos.transformacion.TransformadorViejo;
import net.gaia.vortex.core.impl.atomos.support.NexoSupport;
import net.gaia.vortex.core.impl.tasks.TransformarYDelegarViejo;
import net.gaia.vortex.impl.transformaciones.TransformacionNula;
import ar.com.dgarcia.lang.strings.ToString;

/**
 * Esta clase representa un {@link ComponenteProxy} que realiza una transformación en el mensaje
 * antes de delegarlo al otro componente utilizando una instancia de {@link Transformacion}
 * 
 * @author D. García
 */
@Atomo
@Deprecated
public class NexoTransformadorViejo extends NexoSupport implements TransformadorViejo {

	private Transformacion transformacion;
	public static final String transformacion_FIELD = "transformacion";

	public Transformacion getTransformacion() {
		return transformacion;
	}

	public void setTransformacion(final Transformacion transformacion) {
		if (transformacion == null) {
			throw new IllegalArgumentException(
					"La transformacion del proxy no puede ser nula. A lo sumo una instancia de "
							+ TransformacionNula.class);
		}
		this.transformacion = transformacion;
	}

	public static NexoTransformadorViejo create(final TaskProcessor processor, final Transformacion transformacion,
			final Receptor delegado) {
		final NexoTransformadorViejo transformador = new NexoTransformadorViejo();
		transformador.initializeWith(processor, delegado, transformacion);
		return transformador;
	}

	private void initializeWith(final TaskProcessor processor, final Receptor delegado,
			final Transformacion transformacion) {
		super.initializeWith(processor, delegado);
		setTransformacion(transformacion);
	}

	/**
	 * @see java.lang.Object#toString()
	 */

	@Override
	public String toString() {
		return ToString.de(this).con(numeroDeInstancia_FIELD, getNumeroDeInstancia())
				.add(transformacion_FIELD, transformacion).add(destino_FIELD, getDestino()).toString();
	}

	/**
	 * @see net.gaia.vortex.core.impl.atomos.support.NexoSupport#crearTareaAlRecibir(net.gaia.vortex.api.mensajes.MensajeVortex)
	 */

	@Override
	protected WorkUnit crearTareaAlRecibir(final MensajeVortex mensaje) {
		return TransformarYDelegarViejo.create(mensaje, transformacion, getDestino());
	}
}
