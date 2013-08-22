/**
 * 14/06/2012 20:27:48 Copyright (C) 2011 10Pines S.R.L.
 */
package net.gaia.vortex.core.api.atomos.transformacion;

import net.gaia.vortex.core.api.annotations.Atomo;
import net.gaia.vortex.core.api.atomos.forward.Nexo;
import net.gaia.vortex.core.api.transformaciones.Transformacion;

/**
 * Esta interfaz representa un componente vortex que realiza un transformación de los mensajes
 * recibidos antes de pasarlos al siguiente componente.<br>
 * La transformación puede alterar el mensaje original o incluso reemplazarlo por otro
 * 
 * @author D. García
 */
@Deprecated
@Atomo
public interface TransformadorViejo extends Nexo {

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
