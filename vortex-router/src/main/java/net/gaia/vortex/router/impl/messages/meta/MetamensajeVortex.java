/**
 * 23/01/2013 14:49:36 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.router.impl.messages.meta;

/**
 * Esta interfaz define el contrato esperable de todo metamensaje vortex
 * 
 * @author D. García
 */
public interface MetamensajeVortex {

	/**
	 * Prefijo usado para los mensajes bidi
	 */
	public static final String PREFIJO_METAMENSAJE = "Vortex.";

	/**
	 * Atributo para describir el tipo de mensaje
	 */
	public static final String tipoDeMensaje_FIELD = "tipoDeMensaje";

	/**
	 * Devuelve el nombre del tipo de mensaje que permite discriminarlo
	 * 
	 * @return El nombre de este tipo de mensaje en particular
	 */
	public String getTipoDeMensaje();

}
