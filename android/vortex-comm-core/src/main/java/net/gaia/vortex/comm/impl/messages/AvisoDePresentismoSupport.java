/**
 * 14/07/2012 19:22:58 Copyright (C) 2011 Darío L. García
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

/**
 * Esta clase es comun a los mensajes de presencia y ausencia
 * 
 * @author D. García
 */
public abstract class AvisoDePresentismoSupport extends VortexChatSupport {

	public AvisoDePresentismoSupport(String tipoDeMensaje) {
		super(tipoDeMensaje);
	}

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

}
