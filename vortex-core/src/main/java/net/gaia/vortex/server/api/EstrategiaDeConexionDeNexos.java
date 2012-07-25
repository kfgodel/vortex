/**
 * 19/06/2012 19:46:45 Copyright (C) 2011 10Pines S.R.L.
 */
package net.gaia.vortex.server.api;

import net.gaia.vortex.core.api.atomos.forward.Nexo;

/**
 * Esta interfaz representa una estrategia brindada externamente a una entidad creadora/destructora
 * de {@link Nexo}s.<br>
 * A través de instancias de esta interfaz un tercero puede indicar a la entidad creadora (sockets,
 * http) como conectar los nexos nuevos creados a una parte de la red existente.<br>
 * <br>
 * Esta estrategia indica también como hacer la desconexión antes de eliminar el nexo
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
	public void onNexoCreado(Nexo nuevoNexo);

	/**
	 * Invocado por el administrador de sockets durante el cierre del socket asociado al nexo
	 * pasado.<br>
	 * Normalmente el nexo pasado será desconectado de la red para que no participe de las
	 * comunicaciones ya que no tiene salida por el socket
	 * 
	 * @param nexoCerrado
	 *            El nexo que se está cerrando
	 */
	public void onNexoCerrado(Nexo nexoCerrado);
}
