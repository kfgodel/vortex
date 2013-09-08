/**
 * 14/06/2012 20:42:52 Copyright (C) 2011 10Pines S.R.L.
 */
package net.gaia.vortex.api.moleculas.portal;

import net.gaia.vortex.api.condiciones.Condicion;
import net.gaia.vortex.deprecated.PortalViejo;

/**
 * Esta interfaz representa un handler de mensajes para utilizar en un {@link PortalViejo}, que
 * define una condición que deben cumplir los mensajes, y un tipo esperado de esos mensajes<br>
 * Esta interfaz asegura que los mensajes recibidos son del tipo esperado y que cumplen la condicion
 * asociada
 * 
 * @author D. García
 */
public interface HandlerDePortal<T> extends HandlerDeObjetos<T> {

	/**
	 * Indica el tipo de objeto esperado en este handler
	 * 
	 * @return La clase que representa el tipo de mensaje que puede tratar este handler
	 */
	public Class<T> getTipoEsperado();

	/**
	 * Devuelve la condición necesaria y suficiente que deben pasar todos los mensajes para poder
	 * ser entregados a este handler.<br>
	 * Un mensaje sera entregado a este handler si y sólo si cumple la condición pasada
	 * 
	 * @return La condición a evaluar en cada mensaje antes de invocar este handler
	 */
	public Condicion getBicondicional();
}
