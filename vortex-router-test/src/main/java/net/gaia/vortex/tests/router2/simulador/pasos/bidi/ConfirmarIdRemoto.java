/**
 * 30/10/2012 12:26:29 Copyright (C) 2011 10Pines S.R.L.
 */
package net.gaia.vortex.tests.router2.simulador.pasos.bidi;

import net.gaia.vortex.tests.router2.api.Nodo;
import net.gaia.vortex.tests.router2.impl.patas.PataBidireccional;
import net.gaia.vortex.tests.router2.mensajes.ConfirmacionDeIdRemoto;
import net.gaia.vortex.tests.router2.simulador.pasos.PasoSupport;

/**
 * Esta clase representa la tarea hecha por el nodo para finalizar el handshake
 * 
 * @author D. García
 */
public class ConfirmarIdRemoto extends PasoSupport {

	private PataBidireccional pataSalida;
	private ConfirmacionDeIdRemoto confirmacion;

	/**
	 * @see net.gaia.vortex.tests.router2.simulador.PasoSimulacion#ejecutar()
	 */
	@Override
	public void ejecutar() {
		final Nodo nodoRemoto = pataSalida.getNodoRemoto();
		nodoRemoto.recibirMensaje(confirmacion);
	}

	public PataBidireccional getPataSalida() {
		return pataSalida;
	}

	public void setPataSalida(final PataBidireccional pataSalida) {
		this.pataSalida = pataSalida;
	}

	public ConfirmacionDeIdRemoto getConfirmacion() {
		return confirmacion;
	}

	public void setConfirmacion(final ConfirmacionDeIdRemoto confirmacion) {
		this.confirmacion = confirmacion;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("Confirmacion[");
		if (this.confirmacion != null) {
			builder.append(this.confirmacion.getIdDeMensaje());
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
			builder.append(",");
			builder.append(getConfirmacion().getIdLocalAlReceptor());
		}
		builder.append("] de id remoto");
		return builder.toString();
	}

	public static ConfirmarIdRemoto create(final PataBidireccional salida, final ConfirmacionDeIdRemoto respuesta) {
		final ConfirmarIdRemoto name = new ConfirmarIdRemoto();
		name.setNodoLocal(salida.getNodoLocal());
		name.pataSalida = salida;
		name.confirmacion = respuesta;
		return name;
	}

}
