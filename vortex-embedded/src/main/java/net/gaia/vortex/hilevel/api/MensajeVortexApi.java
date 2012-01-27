/**
 * 26/01/2012 22:47:09 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.hilevel.api;

import java.util.Set;

import ar.com.fdvs.dgarcia.colecciones.sets.Sets;

import com.google.common.base.Objects;

/**
 * Esta clase representa un mensaje vortex de alto nivel. Como tal este mensaje no es ruteable por
 * sí, sólo sirve para la api de alto nivel
 * 
 * @author D. García
 */
public class MensajeVortexApi {

	private Set<String> tagsDelMensaje;
	public static final String tagsDelMensaje_FIELD = "tagsDelMensaje";

	private Object contenido;
	public static final String contenido_FIELD = "contenido";

	private String tipoDeContenido;
	public static final String tipoDeContenido_FIELD = "tipoDeContenido";

	public Set<String> getTagsDelMensaje() {
		return tagsDelMensaje;
	}

	public void setTagsDelMensaje(final Set<String> tagsDelMensaje) {
		this.tagsDelMensaje = tagsDelMensaje;
	}

	public Object getContenido() {
		return contenido;
	}

	public void setContenido(final Object contenido) {
		this.contenido = contenido;
	}

	public String getTipoDeContenido() {
		return tipoDeContenido;
	}

	public void setTipoDeContenido(final String tipoDeContenido) {
		this.tipoDeContenido = tipoDeContenido;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return Objects.toStringHelper(this).add(tagsDelMensaje_FIELD, tagsDelMensaje).add(contenido_FIELD, contenido)
				.add(tipoDeContenido_FIELD, tipoDeContenido).toString();
	}

	/**
	 * Versión más simple de este constructor que toma el nombre de la clase como contenido y como
	 * tag.<br>
	 * Si see pasa null se usará String.valueOf(null) para tags y tipo
	 * 
	 * @param contenido
	 *            El objeto a usar como contenido y cuya clase se usara para describirlo
	 * @return El mensaje creado
	 */
	public static MensajeVortexApi create(final Object contenido) {
		if (contenido == null) {
			final String nullString = String.valueOf(null);
			return create(null, nullString, nullString);
		}
		final String className = contenido.getClass().getName();
		return create(contenido, className, className);
	}

	public static MensajeVortexApi create(final Object contenido, final String tipoDeContenido, final String... tags) {
		final Set<String> tagsEnSet = Sets.newLinkedHashSet(tags);
		return create(contenido, tipoDeContenido, tagsEnSet);
	}

	/**
	 * Crea un nuevo mensaje con todos los datos indicados
	 * 
	 * @param contenido
	 *            El objeto que será el contenido a transmitir
	 * @param tipoDeContenido
	 *            Un texto que describe el tipo de contenido
	 * @param tags
	 *            Los tags que permite rutear el mensaje a los interesados
	 * @return El nuevo mensaje creado
	 */
	public static MensajeVortexApi create(final Object contenido, final String tipoDeContenido, final Set<String> tags) {
		final MensajeVortexApi nuevoMensaje = new MensajeVortexApi();
		nuevoMensaje.contenido = contenido;
		nuevoMensaje.tagsDelMensaje = tags;
		nuevoMensaje.tipoDeContenido = tipoDeContenido;
		return nuevoMensaje;
	}
}
