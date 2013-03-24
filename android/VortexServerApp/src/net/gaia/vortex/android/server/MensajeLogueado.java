/**
 * 24/03/2013 14:36:38 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.android.server;

import net.gaia.vortex.core.api.mensaje.MensajeVortex;

/**
 * Esta clase representa una entrada en el log de mensajes vortex
 * 
 * @author D. García
 */
public class MensajeLogueado {

	private MensajeVortex mensajeOriginal;
	private long momentoRecepcion;

	public MensajeVortex getMensajeOriginal() {
		return mensajeOriginal;
	}

	public void setMensajeOriginal(MensajeVortex mensajeOriginal) {
		this.mensajeOriginal = mensajeOriginal;
	}

	public long getMomentoRecepcion() {
		return momentoRecepcion;
	}

	public void setMomentoRecepcion(long momentoRecepcion) {
		this.momentoRecepcion = momentoRecepcion;
	}

	public static MensajeLogueado create(MensajeVortex mensajeRecibido) {
		MensajeLogueado mensaje = new MensajeLogueado();
		mensaje.mensajeOriginal = mensajeRecibido;
		mensaje.momentoRecepcion = System.currentTimeMillis();
		return mensaje;
	}

	public String representarComoTexto() {
		StringBuilder builder = new StringBuilder();
		builder.append("(");
		builder.append(momentoRecepcion);
		builder.append(")\nID: ");
		builder.append(mensajeOriginal.getIdDeMensaje().toShortString());
		builder.append("\n");
		builder.append(mensajeOriginal.getContenido().toString());
		return builder.toString();
	}
}
