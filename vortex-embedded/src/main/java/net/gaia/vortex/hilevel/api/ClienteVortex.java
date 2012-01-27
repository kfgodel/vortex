/**
 * 26/01/2012 22:45:43 Copyright (C) 2011 Darío L. García
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

/**
 * Esta interfaz define los métodos que ofrece un cliente de un nodo vortex.<br>
 * El cliente debe conectarse a un nodo para poder operar. El modo de conexión dependerá de al
 * implementación concreta
 * 
 * @author D. García
 */
public interface ClienteVortex {

	/**
	 * Envía un mensaje desde este cliente al nodo vortex para que este lo rutee
	 * 
	 * @param mensajeAEnviar
	 *            El mensaje a enviar
	 */
	public void enviar(MensajeVortexApi mensajeAEnviar);

	/**
	 * Establece el handler de mensajes que se llamará al recibir nuevos mensajes en este cliente
	 * desde el nodo.<br>
	 * Dependiendo de la implementación, es posible que este handler se defina en la construcción y
	 * no sea posible modificarlo después
	 * 
	 * @param handler
	 *            El handler de mensajes del cliente
	 */
	public void setHandlerDeMensajes(HandlerDeMensajesApi handler);

	/**
	 * Devuelve el objeto que representa un filtro positivo sobre los mensajes. Cualquiera de los
	 * tags presentes en este objeto acepta mensajes que lo posean, aunque el mensaje posea además
	 * otros tags
	 * 
	 * @return El objeto para administrar los tags de este cliente
	 */
	public FiltroDeMensajesDelCliente getFiltroDeMensajes();

}
