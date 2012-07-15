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

/**
 * Esta superclase define algunos atributos comunes a todos los mensajes de la aplicación de chat
 * 
 * @author D. García
 */
public abstract class VortexCommMessageSupport {

	public static final String AVISO_DE_PRESENCIA = "AvisoDePresencia";
	public static final String AVISO_DE_AUSENCIA = "AvisoDeAusencia";
	public static final String PEDIDO_DE_PRESENCIA = "PedidoDePresencia";
	public static final String MENSAJE_DE_CHAT = "MensajeDeChat";
	public static final String APLICACION_VORTEX_COMM = "net.gaia.vortex.chat";

	private String aplicacion;
	private String tipoDeMensaje;

	/**
	 * Constructor para las subclases
	 */
	public VortexCommMessageSupport(String tipoDeMensaje) {
		aplicacion = APLICACION_VORTEX_COMM;
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

}
