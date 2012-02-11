/**
 * 11/02/2012 12:41:11 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.hilevel.api.impl;

import java.util.concurrent.ConcurrentMap;

import net.gaia.vortex.hilevel.api.MensajeVortexApi;
import net.gaia.vortex.protocol.messages.IdVortex;

import com.google.common.collect.MapMaker;

/**
 * Esta clase representa una memoria de los mensajes enviados por ID a nivel cliente, de manera de
 * identificar los acuses respondidos del servidor
 * 
 * @author D. García
 */
public class MemoriaDeEnvios {

	private ConcurrentMap<IdVortex, MensajeVortexApi> mensajesPorId;

	public static MemoriaDeEnvios create() {
		final MemoriaDeEnvios memoria = new MemoriaDeEnvios();
		memoria.mensajesPorId = new MapMaker().softValues().makeMap();
		return memoria;
	}

	/**
	 * Devuelve el mensaje a nivel cliente que fue registrado con el ID de mensaje vortex indicado,
	 * olvidándolo en esta memoria
	 * 
	 * @param idMensajeVortex
	 *            El id del mensaje vortex que identifica el mensaje enviado
	 * @return EL mensaje registrado o null si ya no existe registro en esta memoria
	 */
	public MensajeVortexApi olvidarMensajeDe(final IdVortex idMensajeVortex) {
		final MensajeVortexApi mensaje = mensajesPorId.remove(idMensajeVortex);
		return mensaje;
	}

	/**
	 * Registra en esta memoria el mensaje pasado enviado con el ID indicado
	 * 
	 * @param idVortex
	 *            El ID con el que se envío e identifica el mensaje
	 * @param mensajeAEnviar
	 *            El mensaje a enviar
	 */
	public void recordar(final IdVortex idVortex, final MensajeVortexApi mensajeAEnviar) {
		mensajesPorId.put(idVortex, mensajeAEnviar);
	}
}
