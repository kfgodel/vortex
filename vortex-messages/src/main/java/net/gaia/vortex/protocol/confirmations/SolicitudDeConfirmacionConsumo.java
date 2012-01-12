/**
 * 28/11/2011 13:15:22 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.protocol.confirmations;

import net.gaia.vortex.protocol.messages.IdVortex;

/**
 * Esta clase representa una solicitud de confirmación que es efectuada cuando pasa el timeout de la
 * primera confirmación y permite establecer un período de espera mayor
 * 
 * @author D. García
 */
public class SolicitudDeConfirmacionConsumo {

	private IdVortex idDeMensajeAConfirmar;

	public static SolicitudDeConfirmacionConsumo create(final IdVortex mensaje) {
		final SolicitudDeConfirmacionConsumo solicitud = new SolicitudDeConfirmacionConsumo();
		solicitud.idDeMensajeAConfirmar = mensaje;
		return solicitud;
	}

	public IdVortex getIdDeMensajeAConfirmar() {
		return idDeMensajeAConfirmar;
	}

	public void setIdDeMensajeAConfirmar(final IdVortex idDeMensajeAConfirmar) {
		this.idDeMensajeAConfirmar = idDeMensajeAConfirmar;
	}

}
