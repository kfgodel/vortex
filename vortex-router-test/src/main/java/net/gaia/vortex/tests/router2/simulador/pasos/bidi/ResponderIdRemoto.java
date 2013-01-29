/**
 * 13/10/2012 21:36:48 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.tests.router2.simulador.pasos.bidi;

import net.gaia.vortex.tests.router2.impl.patas.PataBidireccional;
import net.gaia.vortex.tests.router2.mensajes.RespuestaDeIdRemoto;
import net.gaia.vortex.tests.router2.simulador.NodoSimulacion;
import net.gaia.vortex.tests.router2.simulador.pasos.PasoSupport;

/**
 * Esta clase representa el paso en el que un nodo le responde a otro el id remoto
 * 
 * @author D. García
 */
public class ResponderIdRemoto extends PasoSupport {

	private PataBidireccional pataSalida;
	private RespuestaDeIdRemoto respuesta;

	/**
	 * @see net.gaia.vortex.tests.router2.simulador.PasoSimulacion#ejecutar()
	 */
	@Override
	public void ejecutar() {
		final NodoSimulacion nodoRemoto = pataSalida.getNodoRemoto();
		nodoRemoto.recibirMensaje(respuesta);
	}

	public PataBidireccional getPataSalida() {
		return pataSalida;
	}

	public void setPataSalida(final PataBidireccional pataSalida) {
		this.pataSalida = pataSalida;
	}

	public RespuestaDeIdRemoto getRespuesta() {
		return respuesta;
	}

	public void setRespuesta(final RespuestaDeIdRemoto respuesta) {
		this.respuesta = respuesta;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("Respuesta[");
		if (this.respuesta != null) {
			builder.append(this.respuesta.getIdDeMensaje());
		}
		builder.append("] desde [");

		if (getNodoLocal() != null) {
			builder.append(getNodoLocal().getNombre());
		}
		builder.append(",");
		if (getPataSalida() != null) {
			builder.append(getPataSalida().getIdLocal());
		}
		builder.append("] a [");
		if (getPataSalida() != null) {
			builder.append(getPataSalida().getNodoRemoto().getNombre());
		}
		builder.append("] de id remoto por pedido[");
		if (respuesta != null) {
			builder.append(respuesta.getPedido().getIdDeMensaje());
		}
		builder.append("]");
		return builder.toString();
	}

	public static ResponderIdRemoto create(final PataBidireccional pataSalida, final RespuestaDeIdRemoto respuesta) {
		final ResponderIdRemoto name = new ResponderIdRemoto();
		name.setNodoLocal(pataSalida.getNodoLocal());
		name.pataSalida = pataSalida;
		name.respuesta = respuesta;
		return name;
	}
}
