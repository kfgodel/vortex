/**
 * 25/01/2013 14:51:59 Copyright (C) 2011 Darío L. García
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
 * Esta interfaz es una marca y contrato para los mensajes que indican el de la pata que los recibe.<br>
 * De manera que el receptor sabe porque pata vino
 * 
 * @author D. García
 */
public interface MensajeConIdDePataReceptora {

	/**
	 * Nombre del atributo en el que el mensaje indica el ID de la pata
	 */
	public static final String idLocalAlReceptor_FIELD = "idLocalAlReceptor";

	/**
	 * Devuelve el ID que identifica la pata que recibe este mensaje, del lado del receptor
	 * 
	 * @return El identificador que el nodo receptor nos indicó para la pata bidi por donde se envía
	 *         el mensaje
	 */
	public Long getIdLocalAlReceptor();
}
