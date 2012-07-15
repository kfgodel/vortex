/**
 * 15/07/2012 11:17:17 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.android.service;

import java.net.InetSocketAddress;

import net.gaia.vortex.core.api.Nodo;
import ar.dgarcia.objectsockets.api.Disposable;

/**
 * Esta interfaz abstrae los detalles de mantener la conexión con vortex
 * 
 * @author D. García
 */
public interface VortexConnection extends Disposable {

	/**
	 * Define la dirección en la que se encuentra el servidor, conectando si está permitido. Realiza
	 * la conexión con la dirección indicada. Si ya existía una conexión previa, se desconecta y
	 * vuelve a conectar
	 * 
	 * @param serverAddress
	 *            La nueva dirección de conexión
	 */
	void usarLaDireccion(InetSocketAddress serverAddress);

	/**
	 * Esteblece a qué nodo se le agregará el nodo socket
	 * 
	 * @param nodoCentral
	 *            El nodo utilizado como punto central de la app
	 */
	void utilizarComoNodoCentralA(Nodo nodoCentral);

	/**
	 * Deconecta el nodo socket del nodo central
	 */
	void desconectarDeNodoCentral();

	/**
	 * Cierra la conexión actual con el servidor (en caso de que exista una)
	 */
	void desconectarDelServidor();

	/**
	 * Reconecta el nodo conector al servidor
	 */
	void reconectarAlServidor();

}
