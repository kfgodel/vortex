/**
 * 17/06/2012 14:14:58 Copyright (C) 2011 Darío L. García
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

import net.gaia.vortex.api.basic.Receptor;

/**
 * Esta interfaz representa un componente vortex que al recibir un mensaje ejecuta el código de un
 * receptor antes de pasarle el mensaje a otro receptor delegado.<br>
 * Esta interfaz permite "escuchar" los mensajes de un componente a otro, sin alterar el flujo
 * 
 * @author D. García
 */
@Deprecated
public interface EjecutorViejo extends NexoViejo {

	/**
	 * Establece el receptor utilizado para escuchar los mensajes que pasan en este nexo
	 * 
	 * @param escucha
	 *            El receptor para escuchar los mensajes que pasan
	 */
	public void setEjecutante(Receptor escucha);

	/**
	 * El receptor utilizado en este nexo para escuchar los mensajes que pasan
	 * 
	 * @return El receptor para las escuchas
	 */
	public Receptor getEjecutante();
}
