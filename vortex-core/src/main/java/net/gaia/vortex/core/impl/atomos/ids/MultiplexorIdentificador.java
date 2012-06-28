/**
 * 27/06/2012 18:31:21 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.core.impl.atomos.ids;

import net.gaia.taskprocessor.api.TaskProcessor;
import net.gaia.vortex.core.api.annon.Atomo;
import net.gaia.vortex.core.api.atomos.Receptor;
import net.gaia.vortex.core.api.moleculas.ids.IdentificadorVortex;
import net.gaia.vortex.core.impl.atomos.forward.MultiplexorParalelo;

/**
 * Esta clase representa un hub que identifica los mensajes registrándose como nodo visitado, y
 * descartando los mensajes que ya lo visitaron
 * 
 * @author D. García
 */
@Atomo
public class MultiplexorIdentificador extends MultiplexorIdentificadorSupport {

	/**
	 * Crea el atomo que servirá como proceso de entrada al recibir los mensajes
	 * 
	 * @param processor
	 *            El procesador para las tareas internas
	 * @param identificador
	 *            El identificador asociado a este multiplexor
	 * @param multiplexorDeSalida
	 *            El multiplexor de las salidas
	 * @return El receptor creado para procesar las entradas
	 */
	@Override
	protected Receptor crearProcesoDeEntrada(final TaskProcessor processor, final IdentificadorVortex identificador,
			final MultiplexorParalelo multiplexorDeSalida) {
		return NexoIdentificador.create(processor, identificador, multiplexorDeSalida);
	}

	public static MultiplexorIdentificador create(final TaskProcessor processor, final IdentificadorVortex identificador) {
		final MultiplexorIdentificador multiplexor = new MultiplexorIdentificador();
		multiplexor.initializeWith(processor, identificador);
		return multiplexor;
	}

}
