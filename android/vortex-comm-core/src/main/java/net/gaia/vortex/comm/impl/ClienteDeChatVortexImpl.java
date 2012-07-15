/**
 * 14/07/2012 23:08:25 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.comm.impl;

import java.util.List;

import net.gaia.vortex.comm.api.CanalDeChat;
import net.gaia.vortex.comm.api.ClienteDeChatVortex;
import net.gaia.vortex.core.api.Nodo;

/**
 * Esta clase representa el conjunto de los canales utilizables por un cliente de chat
 * 
 * @author D. García
 */
public class ClienteDeChatVortexImpl implements ClienteDeChatVortex {

	private Nodo nodoCentral;

	/**
	 * Crea un cliente de chat que utilizará el nodo pasado como nodo central de las comunicaciones
	 * 
	 * @param nodoCentral
	 *            El nodo a utilizar como punto de agregado de otros nodos para los canales creados
	 * @return El cliente creado
	 */
	public static ClienteDeChatVortexImpl create(Nodo nodoCentral) {
		ClienteDeChatVortexImpl cliente = new ClienteDeChatVortexImpl();
		cliente.nodoCentral = nodoCentral;
		return cliente;
	}

	/**
	 * @see net.gaia.vortex.comm.api.ClienteDeChatVortex#getCanales()
	 */
	public List<CanalDeChat> getCanales() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @see net.gaia.vortex.comm.api.ClienteDeChatVortex#agregarCanal(java.lang.String)
	 */
	public CanalDeChat agregarCanal(String nombreDeCanal) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @see net.gaia.vortex.comm.api.ClienteDeChatVortex#quitarCanal(java.lang.String)
	 */
	public void quitarCanal(String nombreDeCanal) {
		// TODO Auto-generated method stub

	}

	/**
	 * @see net.gaia.vortex.comm.api.ClienteDeChatVortex#getCanal(java.lang.String)
	 */
	public CanalDeChat getCanal(String nombreDeCanal) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @see net.gaia.vortex.comm.api.ClienteDeChatVortex#actualizarPresentismo()
	 */
	public void actualizarPresentismo() {
		// TODO Auto-generated method stub

	}
}
