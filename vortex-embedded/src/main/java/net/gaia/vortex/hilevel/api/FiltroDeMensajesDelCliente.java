/**
 * 26/01/2012 23:40:32 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.hilevel.api;

import java.util.Set;

import net.gaia.vortex.protocol.messages.MensajeVortex;

/**
 * Esta interfaz define los métodos que ofrece la api del cliente de vortex para modificar los tags
 * que se utilizan para los mensajes
 * 
 * @author D. García
 */
public interface FiltroDeMensajesDelCliente {

	/**
	 * Declara tags adicionales que este cliente desea utilizar en el nodo para recibir mensajes
	 * 
	 * @param tagsAceptados
	 *            Cualquiera de los tags que pueden tener los mensajes recibidos
	 */
	public void agregarATagsActivos(Set<String> tagsAceptados);

	/**
	 * Quita los tags indicados como tags aceptados para los mensajes.<br>
	 * Aún es posible que se reciban mensajes con tags declarados como aceptados, a pesar de tener
	 * los declarados como no aceptados
	 * 
	 * @param tagsRechazados
	 *            Los tags que se quitan de los aceptados
	 */
	public void quitarDeTagsActivos(Set<String> tagsRechazados);

	/**
	 * Elimina los tags anteriores y los reemplaza por los indicados para el cliente
	 * 
	 * @param tagNuevos
	 *            Los nuevos tags que reemplazarán a los viejos
	 */
	public void reemplazarTagsActivos(Set<String> tagNuevos);

	/**
	 * Devuelve el conjunto de los tags activos que filtran positivamente los mensajes
	 * 
	 * @return El conjunto de todos los tags del cliente que fueron declarados
	 */
	public Set<String> getTagsActivos();

	/**
	 * Indica si este filtro de mensajes acepta al mensaje pasado, es decir si tiene algun tag
	 * activo que coincida con algun tag del mensaje
	 * 
	 * @param nuevoMensaje
	 *            El mensaje a comprobar
	 * @return true si el mensaje tiene al menos un tag de los tags activos de este filtro
	 */
	public boolean aceptaA(MensajeVortex nuevoMensaje);
}
