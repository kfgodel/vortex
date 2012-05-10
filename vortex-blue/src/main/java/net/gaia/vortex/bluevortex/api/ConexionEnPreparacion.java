/**
 * 10/05/2012 00:32:19 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.bluevortex.api;

/**
 * Esta interfaz representa una conexion en estado de preparación. Antes de ser utilizable
 * 
 * @author D. García
 */
public interface ConexionEnPreparacion {

	/**
	 * Establece cuál es el handler que se debe utilizar al recibir los mensajes.<br>
	 * Los mensajes atendidos por este handler dependerán del filtro indicado
	 * 
	 * @param handler
	 *            El handler que recibirá los mensajes de la red
	 */
	void setMessageHandler(HandlerDeMensajes handler);

	/**
	 * Establece el filtro de los mensajes aceptados por esta conexión.<br>
	 * Este filtro determinará los mensajes que lleguen al handler
	 * 
	 * @param filtro
	 *            El filtro que indica los mensajes aceptados
	 */
	void setMessageFilter(FiltroDeMensajes filtro);

	/**
	 * Crea la nueva conexión a partir de la definición de esta preparación
	 * 
	 * @return La conexión creada
	 */
	ConexionVortex crearConexion();

}
