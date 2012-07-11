/**
 * 19/02/2012 14:18:36 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.vorterix.messages;

/**
 * Esta clase representa un mensaje Vorterix
 * 
 * @author D. García
 */
public class MensajeDeChat implements MensajeConContexto {

	/**
	 * El contexto de estos mensajes
	 */
	private static final String CONTEXTO_GAIA_CHAT = "gaia.chat";

	private String autor;
	private String cuerpo;

	public String getAutor() {
		return autor;
	}

	public void setAutor(String autor) {
		this.autor = autor;
	}

	public String getCuerpo() {
		return cuerpo;
	}

	public void setCuerpo(String cuerpo) {
		this.cuerpo = cuerpo;
	}

	public String getContexto() {
		return CONTEXTO_GAIA_CHAT;
	}

	public static MensajeDeChat create(String autor, String cuerpo) {
		MensajeDeChat mensaje = new MensajeDeChat();
		mensaje.setAutor(autor);
		mensaje.setCuerpo(cuerpo);
		return mensaje;
	}
}
