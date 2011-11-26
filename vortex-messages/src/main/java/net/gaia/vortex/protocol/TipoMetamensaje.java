/**
 * 26/11/2011 15:28:29 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.protocol;

import net.gaia.vortex.protocol.confirmations.ConfirmacionRecepcion;

/**
 * Este enum define todos los tipos de contenidos reservados del protocolo
 * 
 * @author D. García
 */
public enum TipoMetamensaje {

	/**
	 * Mensaje que confirma la recepción de otro mensaje
	 */
	CONFIRMACION_RECEPCION {
		@Override
		public String getTipoContenido() {
			return "vortex.metamensaje.ConfirmacionDeRecepcion";
		}

		@Override
		public Class<?> getClase() {
			return ConfirmacionRecepcion.class;
		}
	};

	/**
	 * Devuelve el texto usado como identificador del tipo de contenido
	 * 
	 * @return El nombre para este tipo de contenido en mensajes vortex
	 */
	public abstract String getTipoContenido();

	/**
	 * Devuelve la clase que se corresponde al contenido de este tipo de metamensajes
	 * 
	 * @return El tipo concreto de clase que contiene la data del mensaje
	 */
	public abstract Class<?> getClase();
}
