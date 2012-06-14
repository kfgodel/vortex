/**
 * 14/06/2012 19:59:14 Copyright (C) 2011 10Pines S.R.L.
 */
package net.gaia.vortex.core3.api.atomos;

/**
 * Esta interfaz representa un componente de la red vortex que puede recibir un mensaje y realizar
 * alguna acción con él. Es por lo tanto utilizable como destino de los mensajes.<br>
 * Esta interfaz, junto con la de Emisor es la base de todos los componentes
 * 
 * @author D. García
 */
public interface Receptor {

	/**
	 * Invocado al recibir un mensaje de la red
	 * 
	 * @param mensaje
	 *            El mensaje recibido por este componente
	 */
	public void recibir(MensajeVortex mensaje);
}
