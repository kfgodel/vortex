/**
 * 17/06/2012 16:16:10 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.core.impl.moleculas.ruteo;

import net.gaia.taskprocessor.api.TaskProcessor;
import net.gaia.vortex.core.api.annon.Molecula;
import net.gaia.vortex.core.api.atomos.forward.Nexo;

/**
 * Esta clase representa un hub que utiliza un nexo como lógica interna para permitir agregar
 * comportamiento adicional al recibir los mensajes
 * 
 * @author D. García
 */
@Molecula
public class HubConNexo extends HubConNexoSupport {
	/**
	 * Crea un hub cuyo nexo sólo envía los mensajes que recibe (un hub simple)
	 * 
	 * @param processor
	 *            El procesador de las tareas internas de este componente
	 * @return El hub creado
	 */
	public static HubConNexo create(final TaskProcessor processor) {
		final HubConNexo hubConNexo = new HubConNexo();
		hubConNexo.initializeWith(processor);
		return hubConNexo;
	}

	/**
	 * Crea un hub con el nexo pasado como core para operar sobre los mensajes recibidos
	 * 
	 * @param processor
	 *            El procesador de las tareas de este componente
	 * @param nexoCore
	 *            El nexo que define el core del comportamiento de este hub
	 * @return El hub creado
	 */
	public static HubConNexo create(final TaskProcessor processor, final Nexo nexoCore) {
		final HubConNexo hub = new HubConNexo();
		hub.initializeWith(processor);
		hub.setNexoCore(nexoCore);
		return hub;
	}

}
