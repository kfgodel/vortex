/**
 * 14/07/2012 19:12:59 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.comm.impl.messages;

import java.util.ArrayList;
import java.util.List;

import ar.com.dgarcia.lang.strings.ToString;

/**
 * Esta superclase define algunos atributos comunes a todos los mensajes de la aplicación de chat
 * 
 * @author D. García
 */
public abstract class VortexChatSupport {

	public static final String NOMBRE_APLICACION_CHAT = "net.gaia.vortex.chat";

	private String aplicacion;
	public static final String aplicacion_FIELD = "aplicacion";

	private String tipoDeMensaje;
	public static final String tipoDeMensaje_FIELD = "tipoDeMensaje";

	private String usuario;
	public static final String usuario_FIELD = "usuario";

	private List<String> canales;
	public static final String canales_FIELD = "canales";

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public List<String> getCanales() {
		if (canales == null) {
			canales = new ArrayList<String>();
		}
		return canales;
	}

	public void setCanales(List<String> canales) {
		this.canales = canales;
	}

	/**
	 * Constructor para las subclases
	 */
	public VortexChatSupport(String tipoDeMensaje) {
		aplicacion = NOMBRE_APLICACION_CHAT;
		this.tipoDeMensaje = tipoDeMensaje;
	}

	public String getAplicacion() {
		return aplicacion;
	}

	public void setAplicacion(String aplicacion) {
		this.aplicacion = aplicacion;
	}

	public String getTipoDeMensaje() {
		return tipoDeMensaje;
	}

	public void setTipoDeMensaje(String tipoDeMensaje) {
		this.tipoDeMensaje = tipoDeMensaje;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return ToString.de(this).con(usuario_FIELD, usuario).con(canales_FIELD, canales).toString();
	}
}
