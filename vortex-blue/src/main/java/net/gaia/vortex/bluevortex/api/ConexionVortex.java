/**
 * 10/05/2012 00:08:33 Copyright (C) 2011 Darío L. García
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
 * Esta interfaz representa una conexión con la red vortex desde la cual interactuar con mensajes
 * 
 * @author D. García
 */
public interface ConexionVortex {

	/**
	 * Cambia el handler de mensajes utilizado actualmente por esta conexión.<br>
	 * Si no se define un filtro para los mensajes de esta conexión, al definir un handler se
	 * recibirán todos los mensajes que circulen en la red
	 * 
	 * @param nuevoHandler
	 *            El handler que reemplazará al actual
	 */
	void setHandlerDeMensajes(HandlerDeMensajes nuevoHandler);

	/**
	 * Envía por esta conexión el objeto pasado como mensaje de la red vortex para ser recibido por
	 * los interesados
	 * 
	 * @param mensaje
	 *            El mensaje a enviar
	 * @return El reporte que permite conocer el estado del envío
	 */
	ReporteDeEntrega enviar(Object mensaje);

}
