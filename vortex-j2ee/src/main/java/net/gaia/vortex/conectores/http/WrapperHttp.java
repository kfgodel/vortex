/**
 * 25/08/2011 21:34:44 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.conectores.http;

import java.util.List;

import net.gaia.vortex.model.messages.protocolo.MensajeVortex;

import com.google.common.base.Objects;

/**
 * Esta clase es un wrapper de los mensaes que se manejan por http
 * 
 * @author D. García
 */
public class WrapperHttp {

	private Long id;
	public static final String id_FIELD = "id";

	private List<MensajeVortex> mensajes;
	public static final String mensajes_FIELD = "mensajes";

	public Long getId() {
		return id;
	}

	public void setId(final Long id) {
		this.id = id;
	}

	public List<MensajeVortex> getMensajes() {
		return mensajes;
	}

	public void setMensajes(final List<MensajeVortex> mensajes) {
		this.mensajes = mensajes;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return Objects.toStringHelper(this) //
				.add(id_FIELD, id)//
				.add(mensajes_FIELD, mensajes)//
				.toString();
	}

	public static WrapperHttp create(final Long idReceptor, final List<MensajeVortex> mensajes) {
		final WrapperHttp name = new WrapperHttp();
		name.id = idReceptor;
		name.mensajes = mensajes;
		return name;
	}
}
