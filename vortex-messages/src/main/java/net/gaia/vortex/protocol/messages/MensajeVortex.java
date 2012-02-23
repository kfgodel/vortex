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
package net.gaia.vortex.protocol.messages;

import java.util.List;

import net.sf.oval.constraint.AssertValid;
import net.sf.oval.constraint.MinSize;
import net.sf.oval.constraint.NotNull;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import com.google.common.base.Objects;

/**
 * Esta clase representa un mensaje en la capa de transporte para Vortex.<br>
 * Este mensaje es entendible por los servidores Vortex que saben como rutearlo
 * 
 * @author D. García
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class MensajeVortex {

	public static final String TAG_INTERCAMBIO_VECINO = "CHE";

	@NotNull
	@AssertValid
	private IdVortex identificacion;
	public static final String identificacion_FIELD = "identificacion";

	@NotNull
	@AssertValid
	private ContenidoVortex contenido;
	public static final String contenido_FIELD = "contenido";

	@NotNull
	@MinSize(1)
	private List<String> tagsDestino;
	public static final String tagsDestino_FIELD = "tagsDestino";

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
				.add(identificacion_FIELD, identificacion)//
				.toString();
	}

	public static MensajeVortex create(final ContenidoVortex contenido, final IdVortex identificacion,
			final List<String> tags) {
		final MensajeVortex mensaje = new MensajeVortex();
		mensaje.contenido = contenido;
		mensaje.identificacion = identificacion;
		mensaje.tagsDestino = tags;
		return mensaje;
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return this.identificacion.hashCode();
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(final Object obj) {
		if (!(obj instanceof MensajeVortex)) {
			return false;
		}
		final MensajeVortex that = (MensajeVortex) obj;
		return Objects.equal(this.identificacion, that.identificacion);
	}

	/**
	 * Indica si este mensaje tiene como contenido un metamensaje
	 * 
	 * @return true si el contenido es una instancia de metamensaje
	 */
	public boolean esMetaMensaje() {
		final boolean esMetamensaje = this.getTagsDestino().contains(MensajeVortex.TAG_INTERCAMBIO_VECINO);
		return esMetamensaje;
	}

	/**
	 * Devuelve una versión toString de este mensaje con la información completa
	 * 
	 * @return El mensaje formateado con todos los datos
	 */
	public String toPrettyPrint() {
		return Objects.toStringHelper(this).add(identificacion_FIELD, identificacion)
				.add(tagsDestino_FIELD, tagsDestino).add(contenido_FIELD, contenido).toString();
	}
}
