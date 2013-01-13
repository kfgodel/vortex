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
package net.gaia.vortex.core.impl.atomos.memoria;

import net.gaia.taskprocessor.api.TaskProcessor;
import net.gaia.vortex.core.api.annotations.Atomo;
import net.gaia.vortex.core.impl.atomos.support.MultiplexorIdentificadorSupport;

/**
 * Esta clase representa un átomo que identifica los mensajes que recibe, descartando los
 * duplicados.<br>
 * Los mensajes que tengan el ID indicado serán descartados, los que no se forwardean a todos los
 * receptores conectados.<br>
 * <br>
 * 
 * @author D. García
 */
@Atomo
public class MultiplexorSinDuplicados extends MultiplexorIdentificadorSupport {

	public static MultiplexorSinDuplicados create(final TaskProcessor processor) {
		final MultiplexorSinDuplicados multiplexor = new MultiplexorSinDuplicados();
		multiplexor.initializeWith(processor);
		return multiplexor;
	}

}
