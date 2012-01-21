/**
 * 14/01/2012 17:51:47 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.lowlevel.impl;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Esta clase representa la información de control del acuse de consumo de un envio
 * 
 * @author D. García
 */
public class ControlDeConsumoDeEnvio {

	private AtomicBoolean despuesDeLaSolicitudNosPidieronEspera;

	public static ControlDeConsumoDeEnvio create() {
		final ControlDeConsumoDeEnvio control = new ControlDeConsumoDeEnvio();
		control.despuesDeLaSolicitudNosPidieronEspera = new AtomicBoolean(false);
		return control;
	}

	/**
	 * Indica si para el envio realizado corresponde solicitar acuse o no
	 * 
	 * @return
	 */
	public boolean correspondeSolicitud() {
		final boolean correspondeOtraSolicitud = despuesDeLaSolicitudNosPidieronEspera.get();
		return correspondeOtraSolicitud;
	}

	/**
	 * Registra la recepción de la solicitud de espera, de manera que solicitemos el acuse la
	 * próxima vez
	 */
	public void registrarRecepcionDeSolicitudDeEspera() {
		despuesDeLaSolicitudNosPidieronEspera.set(true);
	}

	/**
	 * Registra en este control que se envió una nueva solicitud de acuse que puede receibir una
	 * nueva solicitud de espera
	 */
	public void registrarEnvioDeSolicitud() {
		// Reseteamos el valor
		despuesDeLaSolicitudNosPidieronEspera.set(false);
	}
}
