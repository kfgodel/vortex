/**
 * 28/11/2011 00:22:14 Copyright (C) 2011 Darío L. García
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

import java.util.List;

import net.gaia.vortex.protocol.messages.IdVortex;
import net.gaia.vortex.protocol.messages.MensajeVortex;

/**
 * Esta interfaz define el contrato de un receptor vortex para el nodo
 * 
 * @author D. García
 */
public interface ReceptorVortex {

	/**
	 * Entrega el mensaje pasado a este receptor
	 * 
	 * @param mensaje
	 *            El mensaje a ser procesado por este receptor con el handler declarado
	 */
	public abstract void recibir(final MensajeVortex mensaje);

	/**
	 * Indica si este receptor está interesado en recibir cualquier de los tags pasados
	 * 
	 * @param tagsDelMensaje
	 *            Los tags a evaluar
	 * @return true si este receptor declaró interés en alguno de los tags pasados
	 */
	public abstract boolean estaInteresadoEnCualquieraDe(final List<String> tagsDelMensaje);

	/**
	 * Indica si este emisor envio en algun momento un mensaje con el ID indicado
	 * 
	 * @param idDeMensajeAConfirmar
	 *            El id del mensaje a verificar
	 * @return true si este emisor registrar el ID de mensaje pasado como enviado en algun momento.
	 *         False si no, o ya no lo recordamos
	 */
	public abstract boolean envioPreviamente(IdVortex idDeMensajeAConfirmar);

}