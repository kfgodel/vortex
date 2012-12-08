/**
 * 13/11/2012 21:08:37 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.tests.router2.simulador.pasos.mensajes;

import net.gaia.vortex.tests.router2.api.Nodo;
import net.gaia.vortex.tests.router2.impl.patas.PataBidireccional;
import net.gaia.vortex.tests.router2.mensajes.MensajeNormal;
import net.gaia.vortex.tests.router2.simulador.pasos.PasoSupport;

/**
 * Esta clase representa la acción realizada por un nodo par enviar el mensaje a una pata destino
 * 
 * @author D. García
 */
public class PropagarMensaje extends PasoSupport {

	private PataBidireccional pata;
	private MensajeNormal mensaje;

	/**
	 * @see net.gaia.vortex.tests.router2.simulador.PasoSimulacion#ejecutar()
	 */
	@Override
	public void ejecutar() {
		final Nodo nodoVecino = pata.getNodoRemoto();
		nodoVecino.recibirMensaje(mensaje);
	}

	public static PropagarMensaje create(final PataBidireccional pataSalida, final MensajeNormal mensaje) {
		final PropagarMensaje name = new PropagarMensaje();
		name.setNodoLocal(pataSalida.getNodoLocal());
		name.pata = pataSalida;
		name.mensaje = mensaje;
		return name;
	}

	public PataBidireccional getPata() {
		return pata;
	}

	public void setPata(final PataBidireccional pata) {
		this.pata = pata;
	}

	public MensajeNormal getMensaje() {
		return mensaje;
	}

	public void setMensaje(final MensajeNormal mensaje) {
		this.mensaje = mensaje;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("Propagación de mensaje[");
		if (this.mensaje != null) {
			builder.append(this.mensaje.getIdDeMensaje());
		}
		builder.append("] desde [");

		if (getNodoLocal() != null) {
			builder.append(getNodoLocal().getNombre());
		}
		builder.append(",");
		if (getPata() != null) {
			builder.append(getPata().getIdLocal());
		}
		builder.append("] por [");
		if (getPata() != null) {
			builder.append(getPata().getNodoRemoto().getNombre());
			builder.append(",");
			builder.append(getPata().getIdRemoto());
		}
		builder.append("] con tag[");
		if (this.mensaje != null) {
			builder.append(this.mensaje.getTag());
		}
		builder.append("]: \"");
		if (this.mensaje != null) {
			builder.append(this.mensaje.getTextoAdicional());
		}
		builder.append("\"");
		return builder.toString();
	}

}
