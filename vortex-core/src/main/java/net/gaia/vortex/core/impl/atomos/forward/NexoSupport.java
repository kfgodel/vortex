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
package net.gaia.vortex.core.impl.atomos.forward;

import net.gaia.taskprocessor.api.TaskProcessor;
import net.gaia.taskprocessor.api.WorkUnit;
import net.gaia.vortex.core.api.atomos.Receptor;
import net.gaia.vortex.core.api.atomos.forward.Nexo;
import net.gaia.vortex.core.api.mensaje.MensajeVortex;
import net.gaia.vortex.core.impl.atomos.ComponenteConProcesadorSupport;
import net.gaia.vortex.core.impl.atomos.ReceptorNulo;

import com.google.common.base.Objects;

/**
 * Esta clase implementa comportamiento base para las sub clases de {@link Nexo} utilizando un
 * procesador de tareas interno para realizar las tareas
 * 
 * @author D. García
 */
public abstract class NexoSupport extends ComponenteConProcesadorSupport implements Nexo {

	private Receptor destino;
	public static final String destino_FIELD = "destino";

	/**
	 * Inicializa esta instancia con las dependencias mínimas
	 * 
	 * @param processor
	 *            El procesador para las tareas internas
	 * @param delegado
	 *            El delegado que debe recibir el mensaje
	 */
	protected void initializeWith(final TaskProcessor processor, final Receptor delegado) {
		super.initializeWith(processor);
		setDestino(delegado);
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return Objects.toStringHelper(this).add(destino_FIELD, destino).toString();
	}

	/**
	 * @see net.gaia.vortex.core.api.atomos.forward.Nexo#conectarCon(net.gaia.vortex.core.api.atomos.Receptor)
	 */
	@Override
	public void conectarCon(final Receptor destino) {
		setDestino(destino);
	}

	/**
	 * @see net.gaia.vortex.core.api.atomos.forward.Nexo#desconectarDe(net.gaia.vortex.core.api.atomos.Receptor)
	 */
	@Override
	public void desconectarDe(final Receptor destino) {
		setDestino(ReceptorNulo.getInstancia());
	}

	/**
	 * @see net.gaia.vortex.core.api.atomos.forward.Nexo#setDestino(net.gaia.vortex.core.api.atomos.Receptor)
	 */
	@Override
	public void setDestino(final Receptor destino) {
		if (destino == null) {
			throw new IllegalArgumentException("El delegado del proxy no puede ser null. A lo sumo un "
					+ ReceptorNulo.class);
		}
		this.destino = destino;
	}

	/**
	 * @see net.gaia.vortex.core.api.atomos.forward.Nexo#getDestino()
	 */
	@Override
	public Receptor getDestino() {
		return destino;
	}

	/**
	 * @see net.gaia.vortex.core.api.atomos.Receptor#recibir(net.gaia.vortex.core.api.mensaje.MensajeVortex)
	 */
	@Override
	public void recibir(final MensajeVortex mensaje) {
		final WorkUnit tareaDelMensaje = crearTareaPara(mensaje);
		procesarEnThreadPropio(tareaDelMensaje);
	}

	/**
	 * Crea la tarea específica de esta subclase para el mensaje recibido de manera de ser procesado
	 * en background por este componente
	 * 
	 * @param mensaje
	 *            El mensaje a procesar por esta instancia
	 * @return La tarea a procesar con el procesador interno de este componente
	 */
	protected abstract WorkUnit crearTareaPara(final MensajeVortex mensaje);
}
