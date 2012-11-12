/**
 * 30/10/2012 12:26:29 Copyright (C) 2011 10Pines S.R.L.
 */
package net.gaia.vortex.tests.router.impl.pasos;

import net.gaia.vortex.tests.router.Nodo;
import net.gaia.vortex.tests.router.impl.mensajes.ConfirmacionDeIdRemoto;
import net.gaia.vortex.tests.router.impl.patas.PataConectora;

/**
 * Esta clase representa la tarea hecha por el nodo para finalizar el handshake
 * 
 * @author D. García
 */
public class ConfirmarIdRemoto extends PasoSupport {

	private PataConectora pataSalida;
	private ConfirmacionDeIdRemoto confirmacion;

	/**
	 * @see net.gaia.vortex.tests.router.PasoSimulacion#ejecutar()
	 */
	@Override
	public void ejecutar() {
		final Nodo nodoRemoto = pataSalida.getNodoRemoto();
		nodoRemoto.recibirConfirmacionDeIdRemoto(confirmacion);
	}

	public PataConectora getPataSalida() {
		return pataSalida;
	}

	public void setPataSalida(final PataConectora pataSalida) {
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
			builder.append(this.confirmacion.getId());
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
			builder.append(getConfirmacion().getRespuesta().getIdAsignado());
		}
		builder.append("] de id remoto por respuesta[");
		if (confirmacion != null) {
			builder.append(confirmacion.getRespuesta().getId());
		}
		builder.append("]");
		return builder.toString();
	}

	public static ConfirmarIdRemoto create(final Nodo origen, final PataConectora salida,
			final ConfirmacionDeIdRemoto respuesta) {
		final ConfirmarIdRemoto name = new ConfirmarIdRemoto();
		name.setNodoLocal(origen);
		name.pataSalida = salida;
		name.confirmacion = respuesta;
		return name;
	}

}
