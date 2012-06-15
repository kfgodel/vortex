/**
 * 13/06/2012 18:57:03 Copyright (C) 2011 10Pines S.R.L.
 */
package net.gaia.vortex.core2.impl.mensajes;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import net.gaia.vortex.core2.api.atomos.ComponenteVortex;
import net.gaia.vortex.core3.api.atomos.mensaje.MensajeVortex;

/**
 * Esta clase es la implementación del mensaje vortex usando un mapa interno
 * 
 * @author D. García
 */
public class MensajeMapa implements MensajeVortex {

	public static final String EMISOR_KEY = "EMISOR_VORTEX_KEY";

	private ConcurrentMap<String, Object> contenido;

	public ConcurrentMap<String, Object> getContenido() {
		if (contenido == null) {
			contenido = new ConcurrentHashMap<String, Object>();
		}
		return contenido;
	}

	public void setContenido(final ConcurrentMap<String, Object> contenido) {
		this.contenido = contenido;
	}

	/**
	 * @see net.gaia.vortex.core3.api.atomos.mensaje.MensajeVortex#setEmisor(net.gaia.vortex.core2.api.atomos.ComponenteVortex)
	 */
	@Override
	public void setEmisor(final ComponenteVortex emisor) {
		getContenido().put(EMISOR_KEY, emisor);
	}

	public static MensajeMapa create() {
		final MensajeMapa mensaje = new MensajeMapa();
		return mensaje;
	}

}
