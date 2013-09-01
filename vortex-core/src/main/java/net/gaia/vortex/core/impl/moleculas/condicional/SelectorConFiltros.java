/**
 * 22/01/2013 17:18:56 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.core.impl.moleculas.condicional;

import java.util.HashMap;
import java.util.Map;

import net.gaia.taskprocessor.api.processor.TaskProcessor;
import net.gaia.vortex.api.annotations.clases.Molecula;
import net.gaia.vortex.api.basic.Receptor;
import net.gaia.vortex.api.condiciones.Condicion;
import net.gaia.vortex.core.api.atomos.forward.MultiplexorViejo;
import net.gaia.vortex.core.api.moleculas.FlujoVortexViejo;
import net.gaia.vortex.core.api.moleculas.condicional.SelectorViejo;
import net.gaia.vortex.core.impl.atomos.condicional.NexoFiltroViejo;
import net.gaia.vortex.core.impl.atomos.forward.MultiplexorParaleloViejo;
import net.gaia.vortex.core.impl.condiciones.SiempreTrue;
import net.gaia.vortex.core.impl.moleculas.flujos.FlujoInmutableViejo;
import net.gaia.vortex.core.impl.moleculas.support.NodoMoleculaSupport;
import ar.com.dgarcia.coding.exceptions.FaultyCodeException;

/**
 * Esta clase implementa el selector utilizando {@link NexoFiltroViejo} en cada salida para evaluar los
 * mensajes antes de entregarlos a destino
 * 
 * @author D. García
 */
@Molecula
public class SelectorConFiltros extends NodoMoleculaSupport implements SelectorViejo {

	/**
	 * Mapa con el que registramos que filtro usamos con cada destino
	 */
	private Map<Receptor, NexoFiltroViejo> filtrosPorDestino;

	/**
	 * El procesador para los nuevos filtros
	 */
	private TaskProcessor processor;

	/**
	 * El multiplexor al que conectamos cada filtro de salida
	 */
	private MultiplexorViejo multiplexorDeEntrada;

	/**
	 * @see net.gaia.vortex.core.impl.moleculas.support.NodoMoleculaSupport#conectarCon(net.gaia.vortex.api.basic.Receptor)
	 */

	@Override
	public void conectarCon(final Receptor destino) {
		// Si no indica condición, recibe todo
		conectarCon(destino, SiempreTrue.getInstancia());
	}

	/**
	 * @see net.gaia.vortex.core.api.moleculas.condicional.SelectorViejo#conectarCon(net.gaia.vortex.api.basic.Receptor,
	 *      net.gaia.vortex.api.condiciones.Condicion)
	 */

	public void conectarCon(final Receptor destino, final Condicion condicion) {
		final NexoFiltroViejo filtroDelDestino = getFiltroPara(destino);
		if (filtroDelDestino != null) {
			// Ya estamos conectado al destino
			throw new FaultyCodeException("Ya existe una conexion con el destino[" + destino
					+ "]. No es posibel crear otra");
		}
		// Creamos el nexo con un filtro de la condicion
		final NexoFiltroViejo nuevoFiltro = NexoFiltroViejo.create(processor, condicion, destino);
		filtrosPorDestino.put(destino, nuevoFiltro);

		// Conectamos a la entrada
		multiplexorDeEntrada.conectarCon(nuevoFiltro);
	}

	/**
	 * @see net.gaia.vortex.core.api.moleculas.condicional.SelectorViejo#modificarCondicionPara(net.gaia.vortex.api.basic.Receptor,
	 *      net.gaia.vortex.api.condiciones.Condicion)
	 */

	public void modificarCondicionPara(final Receptor destino, final Condicion nuevaCondicion) {
		final NexoFiltroViejo filtroDelDestino = getFiltroPara(destino);
		if (filtroDelDestino == null) {
			// No estamos conectado al destino
			return;
		}
		// Modificamos la condición
		filtroDelDestino.setCondicion(nuevaCondicion);
	}

	/**
	 * Devuelve el nexoFiltro correspondiente al destino indicado
	 * 
	 * @param destino
	 *            El destino para el que se solicita el {@link NexoFiltroViejo}
	 * @return El filtro que corresponde al destino indicado
	 */
	protected NexoFiltroViejo getFiltroPara(final Receptor destino) {
		final NexoFiltroViejo nexoFiltro = filtrosPorDestino.get(destino);
		return nexoFiltro;
	}

	/**
	 * @see net.gaia.vortex.core.impl.moleculas.support.NodoMoleculaSupport#desconectarDe(net.gaia.vortex.api.basic.Receptor)
	 */

	@Override
	public void desconectarDe(final Receptor destino) {
		final NexoFiltroViejo filtroDelDestino = getFiltroPara(destino);
		if (filtroDelDestino == null) {
			// No estamos conectados
			return;
		}
		multiplexorDeEntrada.desconectarDe(filtroDelDestino);
		filtrosPorDestino.remove(destino);
	}

	public static SelectorConFiltros create(final TaskProcessor processor) {
		final SelectorConFiltros selector = new SelectorConFiltros();
		selector.initializeWithProcessor(processor);
		return selector;
	}

	/**
	 * Inicializa el estado de esta instancia
	 * 
	 * @param processor
	 *            El procesador para los atomos internos
	 */
	private void initializeWithProcessor(final TaskProcessor processor) {
		this.processor = processor;
		filtrosPorDestino = new HashMap<Receptor, NexoFiltroViejo>();

		multiplexorDeEntrada = MultiplexorParaleloViejo.create(processor);
		final FlujoVortexViejo flujoInterno = FlujoInmutableViejo.create(multiplexorDeEntrada, this);
		initializeWith(flujoInterno);
	}
}
