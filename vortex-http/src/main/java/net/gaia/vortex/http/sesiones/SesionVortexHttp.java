/**
 * 06/08/2012 08:10:07 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.http.sesiones;

import net.gaia.vortex.core.api.mensaje.MensajeVortex;
import net.gaia.vortex.http.impl.moleculas.NexoHttp;

/**
 * Esta interfaz representa el contrato de una sesión http que es compartido tanto por clientes como
 * por servidores de http.<br>
 * Cada sesión tiene un {@link NexoHttp} asociado con el cual interactúa con la red vortex desde los
 * requests del cliente http
 * 
 * @author D. García
 */
public interface SesionVortexHttp {

	/**
	 * Devuelve el identificador de esta sesión
	 * 
	 * @return El id de esta sesión
	 */
	public abstract String getIdDeSesion();

	/**
	 * Guarda el mensaje indicado para ser enviado al cliente cuando lo pida
	 * 
	 * @param mensaje
	 *            El mensaje a enviar
	 */
	public abstract void onMensajeDesdeVortex(MensajeVortex mensaje);

	/**
	 * Devuelve el nexo asociado a esta sesión
	 * 
	 * @return El nexo que esta sesión utiliza para meter mensajes en la red vortex
	 */
	public abstract NexoHttp getNexoAsociado();

	/**
	 * Establece el nexo que tendrá asociada esta sesión
	 * 
	 * @param nexoAsociado
	 *            El nexo que utilizará esta sesión
	 */
	public abstract void setNexoAsociado(NexoHttp nexoAsociado);

}