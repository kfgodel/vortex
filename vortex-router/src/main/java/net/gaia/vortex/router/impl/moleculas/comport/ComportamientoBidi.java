/**
 * 22/01/2013 16:25:47 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.router.impl.moleculas.comport;

import net.gaia.taskprocessor.api.processor.TaskProcessor;
import net.gaia.vortex.core.api.moleculas.FlujoVortexViejo;
import net.gaia.vortex.portal.impl.transformaciones.GenerarIdEnMensaje;

/**
 * Esta interfaz representa el comportamiento configurable para un nodo bidireccional.<br>
 * A través de esta interfaz el nodo delega partes de su comportamiento en instancias de esta clase
 * 
 * @author D. García
 */
public interface ComportamientoBidi {

	/**
	 * Devuelve un nuevo flujo para la recepción de mensajes. La salida de este flujo será conectado
	 * a las nuevas patas del nodo bidireccional.<br>
	 * La salida debería soportar multiples conexiones, una por cada pata
	 * 
	 * @param processor
	 *            El procesaro usado por el nodo
	 * @return El flujo con el cual el nodo bidi recibe los mensajes y delega en las patas
	 */
	FlujoVortexViejo crearFlujoParaMensajesRecibidos(TaskProcessor processor);

	/**
	 * Obtiene el ID que corresponde al componente.<br>
	 * En el caso del portal se obtiene de su portal interno
	 * 
	 * @return El identificador para el nuevo componente bidi
	 */
	GenerarIdEnMensaje obtenerGeneradorDeIdParaMensajes();

}
