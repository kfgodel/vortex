/**
 * 23/01/2013 11:02:45 Copyright (C) 2011 Darío L. García
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

import net.gaia.vortex.tests.router2.api.Nodo;
import net.gaia.vortex.tests.router2.impl.patas.PataBidireccional;
import net.gaia.vortex.tests.router2.mensajes.ReconfirmacionDeIdRemoto;
import net.gaia.vortex.tests.router2.simulador.pasos.PasoSupport;

/**
 * Esta clase representa la acción realizada por la pata para reconfirmar una conexion bidid
 * 
 * @author D. García
 */
public class ReconfirmarIdRemoto extends PasoSupport {

	private PataBidireccional pataSalida;
	private ReconfirmacionDeIdRemoto reconfirmacion;

	/**
	 * @see net.gaia.vortex.tests.router2.simulador.PasoSimulacion#ejecutar()
	 */
	public void ejecutar() {
		final Nodo nodoRemoto = pataSalida.getNodoRemoto();
		nodoRemoto.recibirMensaje(reconfirmacion);
	}

	public PataBidireccional getPataSalida() {
		return pataSalida;
	}

	public void setPataSalida(final PataBidireccional pataSalida) {
		this.pataSalida = pataSalida;
	}

	public ReconfirmacionDeIdRemoto getReconfirmacion() {
		return reconfirmacion;
	}

	public void setReconfirmacion(final ReconfirmacionDeIdRemoto reconfirmacion) {
		this.reconfirmacion = reconfirmacion;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("Re-Confirmacion[");
		if (this.reconfirmacion != null) {
			builder.append(this.reconfirmacion.getIdDeMensaje());
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
			builder.append(getReconfirmacion().getIdLocalAlReceptor());
		}
		builder.append("] de id remoto");
		return builder.toString();
	}

	public static ReconfirmarIdRemoto create(final PataBidireccional salida,
			final ReconfirmacionDeIdRemoto reconfirmacion) {
		final ReconfirmarIdRemoto name = new ReconfirmarIdRemoto();
		name.setNodoLocal(salida.getNodoLocal());
		name.pataSalida = salida;
		name.reconfirmacion = reconfirmacion;
		return name;
	}

}
