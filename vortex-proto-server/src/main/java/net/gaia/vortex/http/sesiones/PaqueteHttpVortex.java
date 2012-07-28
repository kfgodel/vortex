/**
 * 27/07/2012 22:51:53 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.http.sesiones;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Esta clase representa el paquete de mensajes enviado y recibido por http para la transferencia de
 * mensajes vortex
 * 
 * @author D. García
 */
public class PaqueteHttpVortex {

	private List<Map<String, Object>> contenidos;

	private Long proximaEsperaMinima;
	private Long proximaEsperaMaxima;

	public List<Map<String, Object>> getContenidos() {
		if (contenidos == null) {
			contenidos = new ArrayList<Map<String, Object>>();
		}

		return contenidos;
	}

	public void setContenidos(final List<Map<String, Object>> contenidos) {
		this.contenidos = contenidos;
	}

	public Long getProximaEsperaMinima() {
		return proximaEsperaMinima;
	}

	public void setProximaEsperaMinima(final Long proximaEsperaMinima) {
		this.proximaEsperaMinima = proximaEsperaMinima;
	}

	public Long getProximaEsperaMaxima() {
		return proximaEsperaMaxima;
	}

	public void setProximaEsperaMaxima(final Long proximaEsperaMaxima) {
		this.proximaEsperaMaxima = proximaEsperaMaxima;
	}

	public static PaqueteHttpVortex create() {
		final PaqueteHttpVortex paquete = new PaqueteHttpVortex();
		return paquete;
	}

	/**
	 * Agrega el contenido pasado como parte de este paquete de salida
	 * 
	 * @param contenidoTextualizable
	 *            El contenido a enviar
	 */
	public void agregarContenido(final Map<String, Object> contenidoTextualizable) {
		getContenidos().add(contenidoTextualizable);
	}
}
