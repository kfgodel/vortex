/**
 * 09/12/2012 13:27:26 Copyright (C) 2011 Darío L. García
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

import net.gaia.vortex.tests.router2.impl.patas.PataBidireccional;
import net.gaia.vortex.tests.router2.mensajes.PedidoDeFiltros;
import net.gaia.vortex.tests.router2.simulador.NodoSimulacion;
import net.gaia.vortex.tests.router2.simulador.pasos.PasoSupport;

/**
 * Esta clase representa el paso del simulador en el que un nodo le manda el mensaje de pedido de
 * filtro a otro nodo
 * 
 * @author D. García
 */
public class PedirFiltros extends PasoSupport {

	private PataBidireccional pata;
	private PedidoDeFiltros mensaje;

	/**
	 * @see net.gaia.vortex.tests.router2.simulador.PasoSimulacion#ejecutar()
	 */
	@Override
	public void ejecutar() {
		final NodoSimulacion nodoRemoto = pata.getNodoRemoto();
		nodoRemoto.recibirMensaje(mensaje);
	}

	public static PedirFiltros create(final PataBidireccional pata, final PedidoDeFiltros mensaje) {
		final PedirFiltros pedido = new PedirFiltros();
		pedido.setNodoLocal(pata.getNodoLocal());
		pedido.mensaje = mensaje;
		pedido.pata = pata;
		return pedido;
	}

	public PataBidireccional getPata() {
		return pata;
	}

	public void setPata(final PataBidireccional pata) {
		this.pata = pata;
	}

	public PedidoDeFiltros getMensaje() {
		return mensaje;
	}

	public void setMensaje(final PedidoDeFiltros mensaje) {
		this.mensaje = mensaje;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("Solicitud[");
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
		builder.append("] de filtros remotos");
		return builder.toString();
	}
}
