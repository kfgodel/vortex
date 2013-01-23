/**
 * 13/11/2012 20:52:39 Copyright (C) 2011 Darío L. García
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

/**
 * Esta clase representa el mensaje vortex normal con un tag para discriminar y filtrarlo
 * 
 * @author D. García
 */
public class MensajeNormal extends MensajeBidiSuppor {

	private String tagDelMensaje;
	public static final String tagDelMensaje_FIELD = "tagDelMensaje";

	private String textoAdicional;
	public static final String textoAdicional_FIELD = "textoAdicional";

	public String getTagDelMensaje() {
		return tagDelMensaje;
	}

	public void setTagDelMensaje(final String tagDelMensaje) {
		this.tagDelMensaje = tagDelMensaje;
	}

	public String getTextoAdicional() {
		return textoAdicional;
	}

	public void setTextoAdicional(final String textoAdicional) {
		this.textoAdicional = textoAdicional;
	}

	/**
	 * @see net.gaia.vortex.tests.router2.api.Mensaje#getTag()
	 */
	@Override
	public String getTag() {
		return tagDelMensaje;
	}

	public static MensajeNormal create(final String tag, final String texto) {
		final MensajeNormal mensaje = new MensajeNormal();
		mensaje.setTagDelMensaje(tag);
		mensaje.setTextoAdicional(texto);
		return mensaje;
	}

	/**
	 * @see net.gaia.vortex.tests.router2.mensajes.MensajeSupport#toString()
	 */
	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("[");
		builder.append(getIdDeMensaje());
		builder.append(":\"");
		builder.append(getTag());
		builder.append("\" -(pata:");
		builder.append(getIdLocalAlReceptor());
		builder.append(")]: \"");
		builder.append(getTextoAdicional());
		builder.append("\"");
		return builder.toString();
	}

	public MensajeNormal clonar() {
		final MensajeNormal copia = MensajeNormal.create(tagDelMensaje, textoAdicional);
		copia.setIdLocalAlReceptor(getIdLocalAlReceptor());
		copia.setIdDeMensaje(getIdDeMensaje());
		return copia;
	}
}
