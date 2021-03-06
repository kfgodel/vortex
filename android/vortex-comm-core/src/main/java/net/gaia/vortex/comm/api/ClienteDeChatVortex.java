/**
 * 14/07/2012 23:14:56 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.comm.api;

import java.util.List;

import net.gaia.vortex.core.api.Nodo;
import ar.dgarcia.objectsockets.api.Disposable;

/**
 * Esta interfaz representa el cliente de mensajes de chat usando vortex
 * 
 * @author D. García
 */
public interface ClienteDeChatVortex extends Disposable {

	/**
	 * Devuelve la lista actual de canales
	 * 
	 * @return La lista de canales existentes
	 */
	List<CanalDeChat> getCanales();

	/**
	 * Agrega en este cliente un nuevo canal con el nombre dado.<br>
	 * 
	 * @param nombreDeCanal
	 *            El nombre para el nuevo canal
	 * @return El canal creado
	 */
	CanalDeChat agregarCanal(String nombreDeCanal);

	/**
	 * Quita el canal indicado por nombre
	 * 
	 * @param nombreDeCanal
	 *            El nombre del canal a quitar
	 */
	void quitarCanal(String nombreDeCanal);

	/**
	 * Devuelve el canal asociado al nombre indicado o null si no existe ninguno
	 * 
	 * @param nombreDeCanal
	 *            El nombre del canal a buscar
	 * @return El canal creado previamente o null
	 */
	CanalDeChat getCanal(String nombreDeCanal);

	/**
	 * Actualiza el estado actual de presentismo de clientes en cada canal, avisando los canales en
	 * lo que está presente este cliente y solicitando al resto que avise su presencia.<br>
	 * Después de invocar este método todos los canales estarán vacíos y dejarán de estarlo a medida
	 * que reciban respuestas
	 */
	void actualizarPresentismo();

	/**
	 * Establece el listener global de los canales de este cliente
	 * 
	 * @param listenerDeEstadoDeCanal
	 *            El listener a utilizar
	 */
	void setListenerDeEstado(ListenerDeEstadoDeCanal listenerDeEstadoDeCanal);

	/**
	 * Desconecta este cliente del nodo central de vortex
	 */
	void desconectar();

	/**
	 * Conecta este cliente al nodo vortex indicado como nodo central de las comunicaciones
	 * 
	 * @param nodoCentral
	 *            El nodo para las comunicaciones
	 */
	void conectarA(Nodo nodoCentral);

}
