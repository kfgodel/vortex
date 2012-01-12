/**
 * 27/11/2011 12:22:06 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.protocol;

import java.util.ArrayList;
import java.util.List;

import net.gaia.vortex.protocol.messages.IdVortex;

import com.google.common.base.Objects;

/**
 * Esta clase representa un mensaje vortex cuando se utiliza en memoria exclusivamente
 * 
 * @author D. García
 */
public class MensajeVortexEmbebido {

	public static final String TAG_INTERCAMBIO_VECINO = "CHE";

	private Object contenido;
	public static final String contenido_FIELD = "contenido";

	private List<String> tagsDestino;
	public static final String tagsDestino_FIELD = "tagsDestino";

	private IdVortex identificacion;
	public static final String identificacion_FIELD = "identificacion";

	public IdVortex getIdentificacion() {
		return identificacion;
	}

	public void setIdentificacion(final IdVortex identificacion) {
		this.identificacion = identificacion;
	}

	public Object getContenido() {
		return contenido;
	}

	public void setContenido(final Object contenido) {
		this.contenido = contenido;
	}

	public List<String> getTagsDestino() {
		if (tagsDestino == null) {
			tagsDestino = new ArrayList<String>();
		}
		return tagsDestino;
	}

	public void setTagsDestino(final List<String> tagsDestino) {
		this.tagsDestino = tagsDestino;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return Objects.toStringHelper(this) //
				.add(tagsDestino_FIELD, tagsDestino)//
				.add(identificacion_FIELD, identificacion)//
				.add(contenido_FIELD, contenido)//
				.toString();
	}

	public static MensajeVortexEmbebido create(final Object contenido2, final IdVortex identificacion2,
			final List<String> tags) {
		final MensajeVortexEmbebido mensaje = new MensajeVortexEmbebido();
		mensaje.contenido = contenido2;
		mensaje.identificacion = identificacion2;
		mensaje.tagsDestino = tags;
		return mensaje;
	}

	/**
	 * Este método indica si el mensaje representa un metamensaje. para lo cual debe poseer el tag
	 * de metamensaje
	 * 
	 * @return true si este mensaje está tagueado con el tag reservado para comunicacion entre
	 *         vecinos
	 */
	public boolean isMetaMensaje() {
		return getTagsDestino().contains(TAG_INTERCAMBIO_VECINO);
	}

}
