/**
 * 19/08/2013 19:56:06 Copyright (C) 2013 Darío L. García
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
package net.gaia.vortex.api.basic.emisores;

import java.util.List;

import net.gaia.vortex.api.basic.Emisor;
import net.gaia.vortex.api.basic.Receptor;

/**
 * Esta interfaz representa un emisor de mensajes que sólo puede ser conectado a un receptor,
 * teniendo entonces una única salida para sus mensajes.<br>
 * Esta interfaz sirve mayormente como marca, opuesto a {@link MultiEmisor}
 * 
 * @author D. García
 */
public interface MonoEmisor extends Emisor {

	/**
	 * Devuelve el unico componente al que este emisor está conectado y que recibirá los mensajes
	 * emitidos
	 * 
	 * @return El receptor conectado desde este componente
	 */
	Receptor getConectado();

	/**
	 * Devuelve una lista con el unico receptor al que este emisor está conectado
	 * 
	 * @see net.gaia.vortex.api.basic.emisores.Conectable#getConectados()
	 */
	public List<Receptor> getConectados();

}
