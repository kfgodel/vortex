/**
 * 25/12/2012 16:47:51 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.router.impl.tasks;

import java.util.List;

import net.gaia.taskprocessor.api.WorkUnit;
import net.gaia.vortex.core.api.mensaje.MensajeVortex;
import net.gaia.vortex.router.impl.moleculas.patas.PataBidireccional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ar.com.dgarcia.lang.strings.ToString;

/**
 * Esta clase representa la tarea realizada por un nodo bidireccional al propagar un mensaje
 * recibido a sus patas
 * 
 * @author D. García
 */
public class PropagarAPatas implements WorkUnit {
	private static final Logger LOG = LoggerFactory.getLogger(PropagarAPatas.class);

	private MensajeVortex mensaje;
	public static final String mensaje_FIELD = "mensaje";

	private List<PataBidireccional> patas;
	public static final String patas_FIELD = "patas";

	/**
	 * @see net.gaia.taskprocessor.api.WorkUnit#doWork()
	 */
	@Override
	public WorkUnit doWork() throws InterruptedException {
		if (patas.isEmpty()) {
			LOG.debug("  Rechazando mensaje [{}] porque no existen patas de comunicacion para procesar",
					new Object[] { mensaje });
			return null;
		}

		for (final PataBidireccional pataLocal : patas) {
			pataLocal.recibir(mensaje);
		}
		return null;
	}

	public static PropagarAPatas create(final MensajeVortex mensaje, final List<PataBidireccional> patasDestino) {
		final PropagarAPatas propagacion = new PropagarAPatas();
		propagacion.mensaje = mensaje;
		propagacion.patas = patasDestino;
		return propagacion;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return ToString.de(this).con(mensaje_FIELD, mensaje).con(patas_FIELD, patas).toString();
	}

}
