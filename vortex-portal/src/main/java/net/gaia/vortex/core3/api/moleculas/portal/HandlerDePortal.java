/**
 * 14/06/2012 20:42:52 Copyright (C) 2011 10Pines S.R.L.
 */
package net.gaia.vortex.core3.api.moleculas.portal;

import net.gaia.vortex.core3.api.condiciones.Condicion;

/**
 * Esta interfaz representa un handler de mensajes para utilizar con un {@link Portal} que tiene
 * asociado un tipo para la recepción de los mensajes y una condición que deben ser respetados al
 * invocarla.<br>
 * Esta interfaz asegura que los mensajes recibidos son del tipo esperado y que cumplen la condicion
 * asociada
 * 
 * @author D. García
 */
public interface HandlerDePortal<T> extends HandlerDeMensaje<T> {

	/**
	 * Indica el tipo de objeto esperado en este handler
	 * 
	 * @return La clase que representa el tipo de mensaje que puede tratar este handler
	 */
	public Class<T> getTipoEsperado();

	/**
	 * Devuelve la condición asociada a este handler que debe ser cumplida por todos los mensajes
	 * para poder ser pasado a este handler
	 * 
	 * @return La condición a evaluar en cada mensaje antes de invocar este handler
	 */
	public Condicion getCondicionNecesaria();
}
