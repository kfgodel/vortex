/**
 * 14/06/2012 20:27:48 Copyright (C) 2011 10Pines S.R.L.
 */
package net.gaia.vortex.core3.api.atomos.transformacion;

import net.gaia.vortex.core3.api.annon.Atomo;
import net.gaia.vortex.core3.api.atomos.forward.Nexo;

/**
 * Esta interfaz representa un componente vortex que realiza un transformación de los mensajes
 * recibidos antes de pasarlos al siguiente componente
 * 
 * @author D. García
 */
@Atomo
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
