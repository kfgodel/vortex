/**
 * 04/06/2012 21:47:29 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.sockets.api;

import net.gaia.vortex.core.api.Nodo;
import ar.dgarcia.objectsockets.api.Disposable;
import ar.dgarcia.objectsockets.impl.ObjectSocketException;

/**
 * Esta interfaz representa un nodo que utiliza sockets como cliente para conectarse a otro que lo
 * escucha y permite la comunicación de mensajes vortex
 * 
 * @author D. García
 */
public interface NodoSocketCliente extends Nodo, Disposable {

	/**
	 * Conecta como cliente este nodo a la dirección indicada en la creación
	 * 
	 * @throws ObjectSocketException
	 *             Si no se puede conectar con el socket remoto
	 */
	public abstract void conectarASocketRomoto() throws ObjectSocketException;

}