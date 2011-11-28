/**
 * 28/11/2011 00:13:31 Copyright (C) 2011 Darío L. García
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

import net.gaia.vortex.protocol.MensajeVortexEmbebido;

/**
 * Esta clase representa el registro en memoria de los mensajes en lista de espera.<br>
 * Estos mensajes normalmente están esperando que la otra parte de la comunicación realice alguna
 * acción, o que un timeout se venza.<br>
 * 
 * @author D. García
 */
public class MensajesEnEspera {

	/**
	 * Agrega el mensaje pasado para ser registrado como mensaje a la espera. <br>
	 * El id del mensaje puede ser utilizado para recuperar el contexto asociado
	 * 
	 * @param mensajeEnEspera
	 *            El mensaje registrado como esperando
	 * @param contexto
	 *            El contexto de ruteo asociado al mensaje
	 */
	public void agregar(final MensajeVortexEmbebido mensajeEnEspera, final ContextoDeRuteoDeMensaje contexto) {
		// TODO Auto-generated method stub

	}

}
