/**
 * 14/06/2012 19:59:14 Copyright (C) 2011 10Pines S.R.L.
 */
package net.gaia.vortex.api.basic;

import net.gaia.vortex.api.mensajes.MensajeVortex;
import net.gaia.vortex.api.proto.ComponenteVortex;

/**
 * Esta interfaz representa un componente de la red vortex que puede recibir un mensaje y realizar
 * alguna acción con él. Es por lo tanto utilizable como destino de los mensajes.<br>
 * <br>
 * La implementación de esta interfaz implica que puede recibir mensajes, pero no que puede
 * emitirlo. Esta interfaz, junto con la de Emisor son la base de todos los componentes de la red y
 * permiten la interconexión
 * 
 * @author D. García
 */
public interface Receptor extends ComponenteVortex {

	/**
	 * Invocado al recibir un mensaje de la red. Dependiendo del tipo concreto de esta instancia
	 * cuál será el comportamiento adoptado al recibir el mensaje
	 * 
	 * @param mensaje
	 *            El mensaje recibido por este componente
	 */
	public void recibir(MensajeVortex mensaje);

}
