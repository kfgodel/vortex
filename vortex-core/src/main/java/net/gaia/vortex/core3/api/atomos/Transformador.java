/**
 * 14/06/2012 20:27:48 Copyright (C) 2011 10Pines S.R.L.
 */
package net.gaia.vortex.core3.api.atomos;

/**
 * Esta interfaz representa un componente vortex que realiza un transformacion de los mensajes
 * recibidos antes de pasarlos al siguiente componente
 * 
 * @author D. García
 */
public interface Transformador extends Nexo {

	/**
	 * Establece la transformación utilizada por esta instancia para modificar los mensajes
	 * recibidos
	 * 
	 * @param transformacion
	 *            La transformación realizada en los mensajes
	 */
	public void setTransformacion(Transformacion transformacion);

	/**
	 * Devuelve la transformación utilizada en esta instancia
	 * 
	 * @return La transformación realizada en cada mensaje
	 */
	public Transformacion getTransformacion();
}
