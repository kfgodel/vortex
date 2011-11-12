/**
 * 16/08/2011 23:05:25 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.model.messages.protocolo;

import java.util.List;

import com.google.common.base.Objects;

/**
 * Esta clase representa un mensaje en la capa de transporte para Vortex.<br>
 * Este mensaje es entendible por los servidores Vortex que saben como rutearlo
 * 
 * @author D. García
 */
public class MensajeVortex {

	private ContenidoVortex contenido;
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

	public ContenidoVortex getContenido() {
		return contenido;
	}

	public void setContenido(final ContenidoVortex contenido) {
		this.contenido = contenido;
	}

	public List<String> getTagsDestino() {
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
}
