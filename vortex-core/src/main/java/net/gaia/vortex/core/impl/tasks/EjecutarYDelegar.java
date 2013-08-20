/**
 * 17/06/2012 14:21:55 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.core.impl.tasks;

import net.gaia.taskprocessor.api.InterruptedThreadException;
import net.gaia.taskprocessor.api.WorkParallelizer;
import net.gaia.taskprocessor.api.WorkUnit;
import net.gaia.vortex.api.basic.Receptor;
import net.gaia.vortex.core.api.mensaje.MensajeVortex;
import net.gaia.vortex.core.impl.tasks.forward.DelegarMensaje;
import net.gaia.vortex.core.prog.Loggers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ar.com.dgarcia.lang.strings.ToString;

/**
 * Esta clase representa la tarea realizada en thread propio por un componente que ejecuta un
 * receptor con el mensaje antes de delegarlo a otro
 * 
 * @author D. García
 */
public class EjecutarYDelegar implements WorkUnit {
	private static final Logger LOG = LoggerFactory.getLogger(EjecutarYDelegar.class);

	private MensajeVortex mensaje;
	public static final String mensaje_FIELD = "mensaje";

	private Receptor ejecutante;
	public static final String ejecutante_FIELD = "ejecutante";

	private Receptor delegado;
	public static final String delegado_FIELD = "delegado";

	/**
	 * @see net.gaia.taskprocessor.api.WorkUnit#doWork()
	 */

	public void doWork(final WorkParallelizer parallelizer) throws InterruptedThreadException {
		Loggers.ATOMOS.trace("Ejecutando atomo[{}] con el mensaje[{}]", ejecutante.toShortString(), mensaje);
		try {
			ejecutante.recibir(mensaje);
		} catch (final Exception e) {
			LOG.error("Se produjo un error al ejecutar el receptor[" + ejecutante + "] con el mensaje[" + mensaje
					+ "]. Siguiendo con delegado", e);
		}

		// Finalmente delegamos al delegado
		final DelegarMensaje delegacion = DelegarMensaje.create(mensaje, delegado);
		parallelizer.submitAndForget(delegacion);
	}

	public static EjecutarYDelegar create(final MensajeVortex mensaje, final Receptor ejecutante,
			final Receptor delegado) {
		final EjecutarYDelegar ejecucion = new EjecutarYDelegar();
		ejecucion.delegado = delegado;
		ejecucion.ejecutante = ejecutante;
		ejecucion.mensaje = mensaje;
		return ejecucion;
	}

	/**
	 * @see java.lang.Object#toString()
	 */

	@Override
	public String toString() {
		return ToString.de(this).add(ejecutante_FIELD, ejecutante).add(delegado_FIELD, delegado)
				.add(mensaje_FIELD, mensaje).toString();
	}
}
