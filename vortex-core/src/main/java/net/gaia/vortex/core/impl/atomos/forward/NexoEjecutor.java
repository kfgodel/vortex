/**
 * 13/06/2012 01:46:20 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.core.impl.atomos.forward;

import net.gaia.taskprocessor.api.WorkUnit;
import net.gaia.taskprocessor.api.processor.TaskProcessor;
import net.gaia.vortex.api.basic.Receptor;
import net.gaia.vortex.api.proto.ComponenteVortex;
import net.gaia.vortex.core.api.annotations.Atomo;
import net.gaia.vortex.core.api.atomos.forward.EjecutorViejo;
import net.gaia.vortex.core.api.mensaje.MensajeVortex;
import net.gaia.vortex.core.impl.atomos.support.NexoSupport;
import net.gaia.vortex.core.impl.tasks.EjecutarYDelegar;
import net.gaia.vortex.core.prog.Loggers;
import ar.com.dgarcia.lang.strings.ToString;

/**
 * Esta clase representa un {@link ComponenteProxy} que ejecuta el código de otro
 * {@link ComponenteVortex} al recibir un mensaje
 * 
 * @author D. García
 */
@Atomo
public class NexoEjecutor extends NexoSupport implements EjecutorViejo {

	private Receptor ejecutante;
	public static final String ejecutante_FIELD = "ejecutante";

	public static NexoEjecutor create(final TaskProcessor processor, final Receptor escucha, final Receptor delegado) {
		final NexoEjecutor ejecutor = new NexoEjecutor();
		ejecutor.initializeWith(processor, delegado);
		ejecutor.setEjecutante(escucha);
		return ejecutor;
	}

	/**
	 * @see java.lang.Object#toString()
	 */

	@Override
	public String toString() {
		return ToString.de(this).con(numeroDeInstancia_FIELD, getNumeroDeInstancia()).add(ejecutante_FIELD, ejecutante)
				.add(destino_FIELD, getDestino()).toString();
	}

	/**
	 * @see net.gaia.vortex.core.impl.atomos.support.NexoSupport#crearTareaAlRecibir(net.gaia.vortex.core.api.mensaje.MensajeVortex)
	 */

	@Override
	protected WorkUnit crearTareaAlRecibir(final MensajeVortex mensaje) {
		final Receptor destino = getDestino();
		Loggers.ATOMOS.debug("Ejecutando [{}] antes de delegar mensaje[{}] a nodo[{}]",
				new Object[] { ejecutante.toShortString(), mensaje, destino });
		return EjecutarYDelegar.create(mensaje, ejecutante, destino);
	}

	/**
	 * @see net.gaia.vortex.core.api.atomos.forward.EjecutorViejo#setEjecutante(net.gaia.vortex.api.basic.Receptor)
	 */

	public void setEjecutante(final Receptor escucha) {
		if (escucha == null) {
			throw new IllegalArgumentException("El receptor escucha no puede ser null para un ejecutor");
		}
		this.ejecutante = escucha;
	}

	/**
	 * @see net.gaia.vortex.core.api.atomos.forward.EjecutorViejo#getEjecutante()
	 */

	public Receptor getEjecutante() {
		return ejecutante;
	}

}
