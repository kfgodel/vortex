/**
 * 19/06/2012 19:46:45 Copyright (C) 2011 10Pines S.R.L.
 */
package net.gaia.vortex.sockets.api;

import net.gaia.vortex.sockets.impl.moleculas.NexoSocket;

/**
 * Esta interfaz representa el contrato requerido por un handler de eventos de creación y
 * destrucción de {@link NexoSocket} que suceden en las conexiones de los sockets entrantes y
 * salientes
 * 
 * @author D. García
 */
public interface EstrategiaDeConexionDeNexos {

	/**
	 * Invocado por el administrador de los sockets durante la creación del socket y el nexo que
	 * abstrae el socket.<br>
	 * El nexo está conectado inicialmente al receptor nulo por defecto. Para que el nexo participe
	 * de la red, se debería inter-conectar a otros componentes (ida y vuelta) en la implementación
	 * de este método, de manera que no se pierdan mensajes recibidos
	 * 
	 * @param nuevoNexo
	 *            El nexo que se está abriendo y que debería ser conectado a la red para compartir
	 *            los mensajes recibidos
	 */
	public void onNexoSocketCreado(NexoSocket nuevoNexo);

	/**
	 * Invocado por el administrador de sockets durante el cierre del socket asociado al nexo
	 * pasado.<br>
	 * Normalmente el nexo pasado será desconectado de la red para que no participe de las
	 * comunicaciones ya que no tiene salida por el socket
	 * 
	 * @param nexoCerrado
	 *            El nexo que se está cerrando
	 */
	public void onNexoSocketCerrado(NexoSocket nexoCerrado);
}
