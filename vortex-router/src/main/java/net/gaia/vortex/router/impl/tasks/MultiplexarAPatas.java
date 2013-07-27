/**
 * 25/01/2013 14:43:02 Copyright (C) 2011 Darío L. García
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import net.gaia.taskprocessor.api.WorkParallelizer;
import net.gaia.taskprocessor.api.WorkUnit;
import net.gaia.taskprocessor.api.processor.TaskProcessor;
import net.gaia.vortex.core.api.condiciones.Condicion;
import net.gaia.vortex.core.api.condiciones.ResultadoDeCondicion;
import net.gaia.vortex.core.api.mensaje.ContenidoVortex;
import net.gaia.vortex.core.api.mensaje.MensajeVortex;
import net.gaia.vortex.core.impl.tasks.forward.MultiplexarMensaje;
import net.gaia.vortex.helpers.VortexEquals;
import net.gaia.vortex.router.impl.atomos.MultiplexorDePatas;
import net.gaia.vortex.router.impl.condiciones.EsMetaMensaje;
import net.gaia.vortex.router.impl.messages.meta.MensajeConIdDePataReceptora;
import net.gaia.vortex.router.impl.moleculas.patas.PataBidireccional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ar.com.dgarcia.lang.strings.ToString;

/**
 * Esta clase representa la tarea del {@link MultiplexorDePatas} que envía un mensaje recibido a
 * todas las patas que corresponda.<br>
 * Pudiendo identificar si va para una sola
 * 
 * @author D. García
 */
public class MultiplexarAPatas implements WorkUnit {
	private static final Logger LOG = LoggerFactory.getLogger(MultiplexarAPatas.class);

	private MensajeVortex mensaje;
	public static final String mensaje_FIELD = "mensaje";

	private Collection<PataBidireccional> patas;
	public static final String patas_FIELD = "patas";

	private TaskProcessor processor;
	public static final String processor_FIELD = "processor";

	private Condicion esMetamensaje;

	/**
	 * @see net.gaia.taskprocessor.api.WorkUnit#doWork()
	 */

	public void doWork(final WorkParallelizer parallelizer) throws InterruptedException {
		if (patas.isEmpty()) {
			LOG.debug("El mensaje[{}] es decartado porque no existen patas para recibirlo", mensaje.toShortString());
			return;
		}
		final Collection<PataBidireccional> destinos = determinarDestinosDeAcuerdoAlMensaje();
		if (destinos.isEmpty()) {
			return;
		}

		// Loggers.ATOMOS.debug("Multiplexando mensaje[{}] a {} patas{}", new Object[] { mensaje,
		// destinos.size(),
		// destinos });
		final MultiplexarMensaje multiplexarMensaje = MultiplexarMensaje.create(mensaje, destinos, null);
		multiplexarMensaje.doWork(parallelizer);
		// for (final PataBidireccional destino : destinos) {
		// LOG.debug("Delegando mensaje[{}] a [{}]", mensaje.toShortString(),
		// destino.toShortString());
		// final DelegarMensaje entregaEnBackground = DelegarMensaje.create(mensaje, destino);
		// processor.process(entregaEnBackground);
		// }
	}

	/**
	 * Intenta determinar a que conjunto de patas va dirigido el mensaje.<br>
	 * Si el mensaje no indica pata, irá a todas
	 * 
	 * @return La lista de patas para las que va dirigido el mensaje
	 */
	private Collection<PataBidireccional> determinarDestinosDeAcuerdoAlMensaje() {
		final ContenidoVortex contenidoDelMensaje = mensaje.getContenido();
		if (ResultadoDeCondicion.FALSE.equals(esMetamensaje.esCumplidaPor(mensaje))) {
			// Si no es un metamensaje no le damos pelota al id de pata que pueda tener
			return patas;
		}

		final Object posibleIdentificador = contenidoDelMensaje
				.get(MensajeConIdDePataReceptora.idLocalAlReceptor_FIELD);
		if (posibleIdentificador == null) {
			// No sabemos a donde va dirigido el mensaje, lo mandamos a todas
			return patas;
		}
		for (final PataBidireccional pata : patas) {
			if (VortexEquals.areEquals(pata.getIdLocal(), posibleIdentificador)) {
				// Es la pata a la que va dirigida
				final ArrayList<PataBidireccional> destinos = new ArrayList<PataBidireccional>(1);
				destinos.add(pata);
				return destinos;
			}
		}
		LOG.debug("El mensaje[{}] es decartado porque el id de pata[{}] no coincide con ninguna de las existentes{}",
				new Object[] { mensaje.toShortString(), posibleIdentificador, patas });
		return Collections.emptyList();
	}

	public static MultiplexarAPatas create(final MensajeVortex mensaje, final Collection<PataBidireccional> patas,
			final TaskProcessor processor) {
		final MultiplexarAPatas multiplexion = new MultiplexarAPatas();
		multiplexion.patas = patas;
		multiplexion.mensaje = mensaje;
		multiplexion.processor = processor;
		multiplexion.esMetamensaje = EsMetaMensaje.create();
		return multiplexion;
	}

	/**
	 * @see java.lang.Object#toString()
	 */

	@Override
	public String toString() {
		return ToString.de(this).add(patas_FIELD, patas).add(mensaje_FIELD, mensaje.toShortString()).toString();
	}
}
