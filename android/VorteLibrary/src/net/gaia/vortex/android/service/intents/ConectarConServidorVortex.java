/**
 * 15/07/2012 11:26:06 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.android.service.intents;

import java.net.InetSocketAddress;

import net.gaia.vortex.android.service.VortexConectorService;
import android.content.Context;
import android.content.Intent;
import ar.com.dgarcia.coding.exceptions.FaultyCodeException;

/**
 * Esta clase representa el intent que permite iniciar el servicio de conexión con el servidor
 * vortex
 * 
 * @author D. García
 */
public class ConectarConServidorVortex extends Intent {

	private static final String PUERTO_DEL_SERVIDOR_KEY = "PUERTO_DEL_SERVIDOR_KEY";
	private static final String HOST_DEL_SERVIDOR_KEY = "HOST_DEL_SERVIDOR_KEY";

	/**
	 * Constructor por copia para recibir este intent
	 */
	public ConectarConServidorVortex(Intent copia) {
		super(copia);
	}

	/**
	 * Constructor para codigo cliente que inicia el servicio
	 */
	public ConectarConServidorVortex(Context contextoAndroid, String host, Integer puerto) {
		super(contextoAndroid, VortexConectorService.class);
		putExtra(HOST_DEL_SERVIDOR_KEY, host);
		putExtra(PUERTO_DEL_SERVIDOR_KEY, puerto);
	}

	/**
	 * Devuelve el host indicado en este intent como dato.<br>
	 * produce una excepción si no existe el dato
	 * 
	 * @return El host del servidor
	 */
	public String getHost() {
		String host = getStringExtra(HOST_DEL_SERVIDOR_KEY);
		if (host == null) {
			throw new FaultyCodeException("Este intent no tiene el host como dato");
		}
		return host;
	}

	/**
	 * Devuelve el puerto del servidor indicado como dato en este intent.<br>
	 * Produce una excepción si no existe el dato
	 * 
	 * @return El puerto a conectar
	 */
	public Integer getPuerto() {
		int puerto = getIntExtra(PUERTO_DEL_SERVIDOR_KEY, -1);
		if (puerto == -1) {
			throw new FaultyCodeException("Este intent no tiene el puerto como dato");
		}
		return puerto;
	}

	/**
	 * Devuelve la dirección completa del servidor tomando el host y puerto de este intent
	 * 
	 * @return El socket al que se debe conectar
	 */
	public InetSocketAddress getServerAddress() {
		String serverhost = getHost();
		Integer puerto = getPuerto();
		InetSocketAddress serverAddress = new InetSocketAddress(serverhost, puerto);
		return serverAddress;
	}

}
