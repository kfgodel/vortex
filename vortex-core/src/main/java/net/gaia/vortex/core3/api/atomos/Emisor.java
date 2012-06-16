/**
 * 14/06/2012 20:01:11 Copyright (C) 2011 10Pines S.R.L.
 */
package net.gaia.vortex.core3.api.atomos;

/**
 * Esta interfaz representa un componente de la red vortex que tiene la capacidad de enviar mensajes
 * a receptores a los que esta conectado. La implementación de esta interfaz implica que el
 * componente puede mandar mensajes a otros, pero no implica que pueda recibirlos<br>
 * A través de esta interfaz un componente permite realizar conexiones a otros componentes para
 * generar una topología de red.<br>
 * <br>
 * Las implementaciones de esta interfaz pueden estar limitadas a 1 o N receptores, dependerá de la
 * implementacion si al conectar a uno nuevo se pierde la conexion previa, o si se agregan nuevos
 * 
 * @author D. García
 */
public interface Emisor {

	/**
	 * Realiza una conexion desde este componente al componente destino de manera de que el destino
	 * reciba los mensajes emitidos desde este componente.<br>
	 * La implementación de esta interfaz puede agregar el destino a un conjunto, o reemplazar al
	 * único destino que conoce
	 * 
	 * @param destino
	 *            El receptor que sera agregado o que reemplazará al previo (dependiendo de la
	 *            implementacion)
	 */
	public void conectarCon(Receptor destino);

	/**
	 * Realiza una desconexión del receptor pasado. Si no estaba previamente conectado al receptor
	 * nada sucede.<br>
	 * Si al quitar el destino indicado este emisor se queda sin destinos, dependerá de la
	 * implementación el comportamiento esperado. Normalmente un emisor sin destino no realiza
	 * ninguna acción
	 * 
	 * @param destino
	 *            El destino a quitar
	 */
	public void desconectarDe(Receptor destino);

}
