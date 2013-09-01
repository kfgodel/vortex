/**
 * 19/08/2013 19:58:12 Copyright (C) 2013 Darío L. García
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
import net.gaia.vortex.api.proto.Conector;

/**
 * Esta interfaz representa un emisor multiple de mensajes, permitiendo conectar multiples
 * receptores a sus salidas para que reciban los mensajes emitidos.
 * 
 * @author D. García
 */
public interface MultiEmisor extends Emisor {

	/**
	 * Devuelve la lista de conectores existentes en este emisor
	 * 
	 * @return La lista de los conectores creados
	 */
	List<Conector> getConectores();

}
