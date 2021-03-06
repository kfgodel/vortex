/**
 * 14/06/2012 20:27:48 Copyright (C) 2011 10Pines S.R.L.
 */
package net.gaia.vortex.deprecated;

import net.gaia.vortex.api.annotations.clases.Atomo;
import net.gaia.vortex.api.transformaciones.Transformacion;

/**
 * Esta interfaz representa un componente vortex que realiza un transformación de los mensajes
 * recibidos antes de pasarlos al siguiente componente.<br>
 * La transformación puede alterar el mensaje original o incluso reemplazarlo por otro
 * 
 * @author D. García
 */
@Deprecated
@Atomo
public interface TransformadorViejo extends NexoViejo {

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
