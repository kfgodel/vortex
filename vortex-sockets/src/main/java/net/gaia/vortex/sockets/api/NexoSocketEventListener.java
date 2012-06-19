/**
 * 19/06/2012 19:46:45 Copyright (C) 2011 10Pines S.R.L.
 */
package net.gaia.vortex.sockets.api;

import net.gaia.vortex.sockets.impl.moleculas.NexoSocket;

/**
 * Esta interfaz representa el contrato requerido por los listeners de creación y destrucción de
 * {@link NexoSocket} que suceden en las conexiones de los sockets
 * 
 * @author D. García
 */
public interface NexoSocketEventListener {

	/**
	 * Invocado por el administrador de los sockets al surgir un nuevo nexo. El nexo estará
	 * conectado al receptor nulo por defecto. Para que el nexo participe de la red se debería inter
	 * conectar a otros componentes (ida y vuelta)
	 * 
	 * @param nuevoNexo
	 *            El nexo que se está abriendo
	 */
	public void onNexoSocketCreado(NexoSocket nuevoNexo);

	/**
	 * Invocado por el administrador de sockets al cerrarse el socket asociado al nexo pasado
	 * 
	 * @param nexoCerrado
	 *            El nexo que se está cerrando
	 */
	public void onNexoSocketCerrado(NexoSocket nexoCerrado);
}
