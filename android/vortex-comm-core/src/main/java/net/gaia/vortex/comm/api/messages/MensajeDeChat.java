/**
 * 14/07/2012 19:19:55 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.comm.api.messages;

import net.gaia.vortex.comm.impl.messages.VortexCommMessageSupport;

/**
 * Esta clase representa el mensaje de chat enviado desde un cliente para ser recibido por otros
 * 
 * @author D. García
 */
public class MensajeDeChat extends VortexCommMessageSupport {

	public MensajeDeChat() {
		super(MENSAJE_DE_CHAT);
	}

	private String usuario;
	private String texto;
	private String canal;

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public String getTexto() {
		return texto;
	}

	public void setTexto(String texto) {
		this.texto = texto;
	}

	public String getCanal() {
		return canal;
	}

	public void setCanal(String canal) {
		this.canal = canal;
	}

	public static MensajeDeChat create(String usuario, String texto, String canal) {
		MensajeDeChat mensaje = new MensajeDeChat();
		mensaje.canal = canal;
		mensaje.texto = texto;
		mensaje.usuario = usuario;
		return mensaje;
	}
}