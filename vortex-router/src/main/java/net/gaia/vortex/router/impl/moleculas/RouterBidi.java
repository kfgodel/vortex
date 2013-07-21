/**
 * 22/12/2012 18:57:32 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.router.impl.moleculas;

import net.gaia.taskprocessor.api.processor.TaskProcessor;
import net.gaia.vortex.router.api.moleculas.Router;
import net.gaia.vortex.router.impl.moleculas.support.RouterSupport;

/**
 * Esta calse representa el router bidireccional que a partir de la publicación de los filtros de
 * los portales puede rutear mensajes evitando nodos no interesados
 * 
 * @author D. García
 */
public class RouterBidi extends RouterSupport implements Router {

	public static RouterBidi create(final TaskProcessor processor) {
		final RouterBidi router = new RouterBidi();
		router.initializeWith(processor);
		return router;
	}
}
