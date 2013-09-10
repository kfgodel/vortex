/**
 * 22/08/2013 00:50:43 Copyright (C) 2013 Darío L. García
 * 
 * <a rel="license" href="http://creativecommons.org/licenses/by/3.0/"><img
 * alt="Creative Commons License" style="border-width:0"
 * src="http://i.creativecommons.org/l/by/3.0/88x31.png" /></a><br />
 * <span xmlns:dct="http://purl.org/dc/terms/" href="http://purl.org/dc/dcmitype/Text"
 * property="dct:title" rel="dct:type">Software</span> by <span
 * xmlns:cc="http://creativecommons.org/ns#" property="cc:attributionName">Darío García</span> is
 * licensed under a <a rel="license" href="http://creativecommons.org/licenses/by/3.0/">Creative
 * Commons Attribution 3.0 Unported License</a>.
 */
package net.gaia.vortex.api.atomos;

import net.gaia.vortex.api.basic.Nodo;
import net.gaia.vortex.api.basic.emisores.MonoConectable;
import net.gaia.vortex.api.transformaciones.Transformacion;

/**
 * Esta interfaz representa un atomo vortex que modifica el mensaje recibido utilizando una
 * {@link Transformacion}.<br>
 * La transformación utilizada puede alterar el mensaje recibido o reemplazarlo completamente por
 * otra instancia.<br>
 * A través de las transformaciones se puede modificar la semántica del mensaje durante el ruteo
 * 
 * @author D. García
 */
public interface Transformador extends Nodo, MonoConectable {

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
