/**
 * Created on: Sep 1, 2013 5:42:36 PM by: Dario L. Garcia
 * 
 * <a rel="license" href="http://creativecommons.org/licenses/by/2.5/ar/"><img
 * alt="Creative Commons License" style="border-width:0"
 * src="http://i.creativecommons.org/l/by/2.5/ar/88x31.png" /></a><br />
 * <span xmlns:dc="http://purl.org/dc/elements/1.1/"
 * href="http://purl.org/dc/dcmitype/InteractiveResource" property="dc:title"
 * rel="dc:type">Bean2Bean</span> by <a xmlns:cc="http://creativecommons.org/ns#"
 * href="https://sourceforge.net/projects/bean2bean/" property="cc:attributionName"
 * rel="cc:attributionURL">Dar&#237;o Garc&#237;a</a> is licensed under a <a rel="license"
 * href="http://creativecommons.org/licenses/by/2.5/ar/">Creative Commons Attribution 2.5 Argentina
 * License</a>.<br />
 * Based on a work at <a xmlns:dc="http://purl.org/dc/elements/1.1/"
 * href="https://bean2bean.svn.sourceforge.net/svnroot/bean2bean"
 * rel="dc:source">bean2bean.svn.sourceforge.net</a>
 */
package net.gaia.vortex.impl.tasks;

import net.gaia.taskprocessor.api.InterruptedThreadException;
import net.gaia.taskprocessor.api.WorkParallelizer;
import net.gaia.taskprocessor.api.WorkUnit;
import net.gaia.vortex.api.basic.Receptor;
import net.gaia.vortex.api.mensajes.MensajeVortex;
import net.gaia.vortex.core.prog.Loggers;
import net.gaia.vortex.impl.proto.ConectorAsincrono;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Esta clase representa la tarea de los {@link ConectorAsincrono} procesada en un thread propio
 * para entregar el mensaje al receptor indicado sin utilizar el thread original
 * 
 * @author dgarcia
 */
public class EntregarAsincronamente implements WorkUnit {
	private static final Logger LOG = LoggerFactory.getLogger(EntregarAsincronamente.class);

	private Receptor enCasoDeError;
	public static final String enCasoDeError_FIELD = "enCasoDeError";

	private Receptor receptor;
	public static final String receptor_FIELD = "receptor";

	private MensajeVortex mensaje;
	public static final String mensaje_FIELD = "mensaje";

	/**
	 * @see net.gaia.taskprocessor.api.WorkUnit#doWork(net.gaia.taskprocessor.api.WorkParallelizer)
	 */
	public void doWork(final WorkParallelizer parallelizer) throws InterruptedThreadException {
		if (Loggers.ATOMOS.isDebugEnabled()) {
			Loggers.ATOMOS.debug("Entregando a nodo[{}] el mensaje[{}]", receptor.toShortString(), mensaje);
		}
		try {
			receptor.recibir(mensaje);
		}
		catch (final Exception e) {
			LOG.error("Se produjo un error al entregar un mensaje[" + mensaje + "] a un delegado[" + receptor
					+ "]. Descartando", e);
			enCasoDeError.recibir(mensaje);
		}
		// Nada más que hacer
	}

	public static EntregarAsincronamente create(final MensajeVortex mensaje, final Receptor receptor,
			final Receptor enCasoDeError) {
		final EntregarAsincronamente tarea = new EntregarAsincronamente();
		tarea.enCasoDeError = enCasoDeError;
		tarea.mensaje = mensaje;
		tarea.receptor = receptor;
		return tarea;
	}
}
