/**
 * 27/06/2012 19:58:47 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.core.impl.moleculas;

import net.gaia.taskprocessor.api.TaskProcessor;
import net.gaia.vortex.core.api.Nodo;
import net.gaia.vortex.core.api.annon.Molecula;
import net.gaia.vortex.core.api.atomos.Receptor;
import net.gaia.vortex.core.api.moleculas.ids.IdentificadorVortex;
import net.gaia.vortex.core.impl.atomos.forward.MultiplexorParalelo;
import net.gaia.vortex.core.impl.atomos.ids.MultiplexorIdentificadorSupport;
import net.gaia.vortex.core.impl.atomos.ids.NexoIdentificador;
import net.gaia.vortex.core.impl.moleculas.ids.GeneradorDeIdsEstaticos;

/**
 * Esta clase representa un nodo que puede identificar los mensajes para descartar los propios y si
 * recibe un mensaje que no es propio se lo reenvía a todos los receptores conocidos.<br>
 * Esta clase sirve como la forma más básica de interconexión de moléculas
 * 
 * @author D. García
 */
@Molecula
public class NodoMultiplexor extends MultiplexorIdentificadorSupport implements Nodo {

	public static NodoMultiplexor create(final TaskProcessor processor) {
		final NodoMultiplexor nodo = new NodoMultiplexor();
		nodo.inicializarCon(processor);
		return nodo;
	}

	/**
	 * Inicializa el estado de esta instancia
	 * 
	 * @param processor
	 *            El procesador para las tareas internas
	 */
	protected void inicializarCon(final TaskProcessor processor) {
		final IdentificadorVortex idPropio = GeneradorDeIdsEstaticos.getInstancia().generarId();
		initializeWith(processor, idPropio);
	}

	/**
	 * @see net.gaia.vortex.core.impl.atomos.ids.MultiplexorIdentificadorSupport#crearProcesoDeEntrada(net.gaia.taskprocessor.api.TaskProcessor,
	 *      net.gaia.vortex.core.api.moleculas.ids.IdentificadorVortex,
	 *      net.gaia.vortex.core.impl.atomos.forward.MultiplexorParalelo)
	 */
	@Override
	protected Receptor crearProcesoDeEntrada(final TaskProcessor processor, final IdentificadorVortex identificador,
			final MultiplexorParalelo multiplexorDeSalida) {
		return NexoIdentificador.create(processor, identificador, multiplexorDeSalida);
	}

}
