/**
 * 09/05/2012 20:57:31 Copyright (C) 2011 Darío L. García
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
 * Esta interfaz representa la red de vortex como entorno para la circulación de mensajes.<br>
 * A través de implementaciones de esta clase es posible conectarse a la red y participar en las
 * comunicaciones
 * 
 * @author D. García
 */
public interface BlueVortex {

	/**
	 * Crea una nueva conexión con vortex que sólo permite enviar mensajes a la red.<br>
	 * Se utiliza una configuración default
	 * 
	 * @return La conexión creada
	 */
	ConexionVortex crearConexion();

	/**
	 * Crea una conexión en estado de preparación, con lo que debe ser finalizar su preparación para
	 * ser utilizable
	 * 
	 * @return Una pre-conexión que permite configurarla antes de utilizarla
	 */
	ConexionEnPreparacion prepararConexion();

}
