/**
 * 26/01/2012 23:08:30 Copyright (C) 2011 Darío L. García
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

import net.gaia.vortex.hilevel.api.entregas.ReporteDeEntregaApi;

/**
 * Esta interfaz define los métodos que debe implementar el handler utilizado para recibir los
 * mensajes de alto nivel de un nodo vortex
 * 
 * @author D. García
 */
public interface HandlerDeMensajesApi {

	/**
	 * Invocado cuando un cliente recibe un mensaje desde el nodo vortex
	 * 
	 * @param mensajeRecibido
	 *            El mensaje recibido
	 */
	public void onMensajeRecibido(MensajeVortexApi mensajeRecibido);

	/**
	 * Este método es invocado al recibir un reporte de entrega de un mensaje enviado previamente
	 * 
	 * @param reporte
	 *            El reporte del mensaje con el estado del envío
	 */
	public void onReporteDeEntregaRecibido(ReporteDeEntregaApi reporte);

}
