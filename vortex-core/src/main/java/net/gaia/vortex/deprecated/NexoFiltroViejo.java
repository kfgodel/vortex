/**
 * 13/06/2012 00:43:49 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.deprecated;

import net.gaia.taskprocessor.api.processor.TaskProcessor;
import net.gaia.vortex.api.annotations.clases.Atomo;
import net.gaia.vortex.api.basic.Receptor;
import net.gaia.vortex.api.condiciones.Condicion;

/**
 * Esta clase es la implementación del {@link FiltroViejo} utilizando threads independientes para el
 * filtrado
 * 
 * @author D. García
 */
@Atomo
@Deprecated
public class NexoFiltroViejo extends NexoFiltroSupport {

	public static NexoFiltroViejo create(final TaskProcessor processor, final Condicion condicion, final Receptor delegado) {
		final NexoFiltroViejo condicional = new NexoFiltroViejo();
		condicional.initializeWith(processor, delegado, condicion);
		return condicional;
	}
}
