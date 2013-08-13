/**
 * 13/10/2012 18:14:59 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.tests.router2.mensajes;

import java.util.concurrent.atomic.AtomicLong;

import net.gaia.vortex.tests.router2.api.Mensaje;

/**
 * Esta clase es super de todos los mensajes y tiene operaciones comunes
 * 
 * @author D. García
 */
public abstract class MensajeSupport implements Mensaje {

	private static final AtomicLong idDeMensajes = new AtomicLong(0);

	private Long idDeMensaje;

	/**
	 * Crea el mensaje tomando un id
	 */
	public MensajeSupport() {
		this.idDeMensaje = idDeMensajes.getAndIncrement();
	}

	public Long getIdDeMensaje() {
		return idDeMensaje;
	}

	protected void setIdDeMensaje(final Long idDeMensaje) {
		this.idDeMensaje = idDeMensaje;
	}

	/**
	 * Inicia el contador de mensajes en 0 nuevamente
	 */
	public static void resetIds() {
		idDeMensajes.set(0);
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "[" + getIdDeMensaje() + "]";
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(final Object obj) {
		if (obj instanceof MensajeSupport) {
			final MensajeSupport other = (MensajeSupport) obj;
			return this.getIdDeMensaje().equals(other.getIdDeMensaje());
		}
		return false;
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return this.getIdDeMensaje().hashCode();
	}
}
