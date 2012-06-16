/**
 * 14/06/2012 19:59:14 Copyright (C) 2011 10Pines S.R.L.
 */
package net.gaia.vortex.core3.api.atomos;

import net.gaia.vortex.core3.api.mensaje.MensajeVortex;

/**
 * Esta interfaz representa un componente de la red vortex que puede recibir un mensaje y realizar
 * alguna acción con él. Es por lo tanto utilizable como destino de los mensajes.<br>
 * <br>
 * Esta interfaz, junto con la de Emisor son la base de todos los componentes de la red
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
