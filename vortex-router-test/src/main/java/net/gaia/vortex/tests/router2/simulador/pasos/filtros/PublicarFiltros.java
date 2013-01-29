/**
 * 13/10/2012 12:28:55 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.tests.router2.simulador.pasos.filtros;

import net.gaia.vortex.tests.router2.api.Nodo;
import net.gaia.vortex.tests.router2.impl.patas.PataBidireccional;
import net.gaia.vortex.tests.router2.mensajes.PublicacionDeFiltros;
import net.gaia.vortex.tests.router2.simulador.pasos.PasoSupport;

/**
 * Esta clase representa el paso donde un nodo le publica sus filtros al vecino
 * 
 * @author D. García
 */
public class PublicarFiltros extends PasoSupport {

	private PataBidireccional pata;
	private PublicacionDeFiltros mensaje;

	/**
	 * @see net.gaia.vortex.tests.router2.simulador.PasoSimulacion#ejecutar()
	 */
	@Override
	public void ejecutar() {
		final Nodo nodoVecino = pata.getNodoRemoto();
		nodoVecino.recibirMensaje(mensaje);
	}

	public static PublicarFiltros create(final PataBidireccional pataSalida, final PublicacionDeFiltros publicacion) {
		final PublicarFiltros name = new PublicarFiltros();
		name.setNodoLocal(pataSalida.getNodoLocal());
		name.pata = pataSalida;
		name.mensaje = publicacion;
		return name;
	}

	public PublicacionDeFiltros getMensaje() {
		return mensaje;
	}

	public void setMensaje(final PublicacionDeFiltros mensaje) {
		this.mensaje = mensaje;
	}

	public PataBidireccional getPata() {
		return pata;
	}

	public void setPata(final PataBidireccional pata) {
		this.pata = pata;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("Publicación[");
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
		builder.append("] a [");
		if (getPata() != null) {
			builder.append(getPata().getNodoRemoto().getNombre());
			builder.append(",");
			builder.append(getPata().getIdRemoto());
		}
		builder.append("] de filtros: ");
		if (getMensaje() != null) {
			builder.append(this.getMensaje().getFiltro());
		}
		return builder.toString();
	}
}
