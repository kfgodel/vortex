/**
 * 19/08/2013 19:14:00 Copyright (C) 2013 Darío L. García
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
package net.gaia.vortex.api.proto;

import net.gaia.vortex.api.basic.Receptor;
import net.gaia.vortex.api.basic.emisores.MonoEmisor;

/**
 * Esta interfaz representa el componente vortex que permite conectar con otros receptores para
 * generar rutas de mensajes.<br>
 * Al recibir el mensaje, el conector lo deriva al receptor conectado
 * 
 * @author D. García
 */
public interface Conector extends Receptor, MonoEmisor {

	/**
	 * Devuelve el componente al que este conector está conectado
	 * 
	 * @return El receptor conectado a este componente
	 */
	Receptor getConectado();
}
