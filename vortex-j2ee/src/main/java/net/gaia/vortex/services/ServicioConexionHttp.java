/**
 * 20/08/2011 13:22:03 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.services;

import net.gaia.vortex.conectores.http.ComandoHttp;
import net.gaia.vortex.externals.http.OperacionHttp;
import net.gaia.vortex.model.servidor.localizadores.ReferenciaAReceptor;

/**
 * Esta interfaz representa el servicio de conexión a través de requests http
 * 
 * @author D. García
 */
public interface ServicioConexionHttp {

	/**
	 * Crea un comando que es la interpretación del pedido recibido. El comando puede solicitar
	 * mensajes nuevos para un ID, enviar mensajes o ninguno de los dos
	 * 
	 * @param pedido
	 *            El pedido http a interpretar
	 * @return El comando http interpretado o null si no fue posible interpretarlo
	 */
	ComandoHttp interpretarComoComando(OperacionHttp pedido);

	/**
	 * Envía los mensajes pendientes en la respuesta para el receptor indicado
	 * 
	 * @param localizador
	 * @param pedido
	 */
	void enviarPendientesA(ReferenciaAReceptor localizador, OperacionHttp pedido);

}
