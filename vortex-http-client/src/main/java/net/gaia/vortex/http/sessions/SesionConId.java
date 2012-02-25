/**
 * 01/02/2012 21:36:13 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.http.sessions;

import net.gaia.vortex.lowlevel.api.ErroresDelMensaje;
import net.gaia.vortex.lowlevel.impl.receptores.ColaDeMensajesVortex;
import net.gaia.vortex.protocol.messages.MensajeVortex;

/**
 * Esta interfaz define el contrato que ofrece la sesión remota por http
 * 
 * @author D. García
 */
public interface SesionConId {

	/**
	 * El ID de la sesión utilizado para las interacciones con el servidor
	 * 
	 * @return El identificador brindado por el servidor o null si no tiene ID
	 */
	public Long getSessionId();

	/**
	 * Cambia el id actual por el recibido desde el nodo
	 * 
	 * @param nuevoId
	 *            El ID que el nodo nos adjudica
	 */
	public void cambiarId(Long nuevoId);

	/**
	 * Invocado al recibir un mensaje del nodo
	 * 
	 * @param mensajeRecibido
	 *            El mensaje que el nodo envia a esta sesión
	 */
	public void recibirDelNodo(MensajeVortex mensajeRecibido);

	/**
	 * Devuelve la cola de mensajes de la sesión
	 * 
	 * @return La cola de mensajes acumulados para envío de esta sesión
	 */
	public ColaDeMensajesVortex getColaDeMensajes();

	/**
	 * Invocado cuando durante el proceso de envío del mensaje se produce un error
	 * 
	 * @param mensajeAEnviar
	 *            El mensaje fallido
	 * @param errores
	 *            La descripción del error
	 */
	public void onErrorDeMensaje(MensajeVortex mensajeAEnviar, ErroresDelMensaje errores);

	/**
	 * Devuelve la cantidad de segundos que esta sesión solicita como máximo para permanecer sin
	 * actividad
	 * 
	 * @return La cantidad de segundos
	 */
	public Long getSegundosSinActividadSolicitados();

	/**
	 * Establece la cantidad máxima de segundos que esta sesión puede permanecer sin actividad
	 * 
	 * @param segundosOtorgadosPorServer
	 *            Los segundos que el server nos garantiza antes de matar la sesión
	 */
	public void setSegundosSinActividadotorgados(Long segundosOtorgadosPorServer);

}
