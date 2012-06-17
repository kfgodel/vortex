/**
 * 16/06/2012 19:03:38 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.core3.impl.mensaje;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicReference;

import net.gaia.vortex.core3.api.atomos.Receptor;
import net.gaia.vortex.core3.api.mensaje.MensajeVortex;

import com.google.common.base.Objects;

/**
 * Esta clase es la implementación de un mensaje vortex utilizando un mapa para conservar la
 * representación de estado de los datos transmitidos
 * 
 * @author D. García
 */
public class MensajeMapa implements MensajeVortex {

	private AtomicReference<Receptor> remitenteDirecto;
	public static final String remitenteDirecto_FIELD = "remitenteDirecto";

	private ConcurrentMap<String, Object> contenido;
	public static final String contenido_FIELD = "contenido";

	/**
	 * @see net.gaia.vortex.core3.api.mensaje.MensajeVortex#getRemitenteDirecto()
	 */
	@Override
	public Receptor getRemitenteDirecto() {
		return remitenteDirecto.get();
	}

	/**
	 * @see net.gaia.vortex.core3.api.mensaje.MensajeVortex#setRemitenteDirecto(net.gaia.vortex.core3.api.atomos.Receptor)
	 */
	@Override
	public void setRemitenteDirecto(final Receptor remitente) {
		this.remitenteDirecto.set(remitente);
	}

	public static MensajeMapa create() {
		final MensajeMapa mensaje = new MensajeMapa();
		mensaje.remitenteDirecto = new AtomicReference<Receptor>();
		mensaje.contenido = new ConcurrentHashMap<String, Object>();
		return mensaje;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return Objects.toStringHelper(this).add(remitenteDirecto_FIELD, remitenteDirecto)
				.add(contenido_FIELD, contenido).toString();
	}
}
