/**
 * 20/05/2012 19:32:01 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.core.tests;

import net.gaia.vortex.core.api.HandlerDeMensajesVecinos;

/**
 * Esta clase representa un handler de los mensajes que permite medir el tiempo de entrega
 * 
 * @author D. García
 */
public class HandlerCronometro implements HandlerDeMensajesVecinos {

	private double nanosAcumulados;
	private double cantidadDeMensajes;

	/**
	 * @see net.gaia.vortex.core.api.HandlerDeMensajesVecinos#onMensajeDeVecinoRecibido(java.lang.Object)
	 */
	@Override
	public void onMensajeDeVecinoRecibido(final Object mensaje) {
		final MensajeCronometro cronoMensaje = (MensajeCronometro) mensaje;
		nanosAcumulados += cronoMensaje.getTranscurrido();
		cantidadDeMensajes++;
	}

	public double getMensajesPorSegundo() {
		final double mensajesPorSegundo = cantidadDeMensajes / (nanosAcumulados / 1000000);
		return mensajesPorSegundo;
	}

	public static HandlerCronometro create() {
		final HandlerCronometro handler = new HandlerCronometro();
		handler.nanosAcumulados = 0;
		handler.cantidadDeMensajes = 0;
		return handler;
	}
}
