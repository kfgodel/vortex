/**
 * 13/10/2012 21:21:55 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.tests.router.impl.pasos;

import net.gaia.vortex.tests.router.Nodo;
import net.gaia.vortex.tests.router.impl.mensajes.PedidoDeIdRemoto;
import net.gaia.vortex.tests.router.impl.patas.PataConectora;

/**
 * Esta clase representa el paso que realzia un nodo para solicitar el id en otro nodo
 * 
 * @author D. García
 */
public class PedirIdRemoto extends PasoSupport {

	private PataConectora pata;
	private PedidoDeIdRemoto pedido;

	/**
	 * @see net.gaia.vortex.tests.router.PasoSimulacion#ejecutar()
	 */
	@Override
	public void ejecutar() {
		final Nodo nodoVecino = pata.getNodoRemoto();
		nodoVecino.recibirPedidoDeId(pedido);
	}

	public static PedirIdRemoto create(final Nodo nodoOrigen, final PataConectora pataSalida,
			final PedidoDeIdRemoto pedido) {
		final PedirIdRemoto name = new PedirIdRemoto();
		name.setNodoLocal(nodoOrigen);
		name.pata = pataSalida;
		name.pedido = pedido;
		return name;
	}

	public PataConectora getPata() {
		return pata;
	}

	public void setPata(final PataConectora pata) {
		this.pata = pata;
	}

	public PedidoDeIdRemoto getPedido() {
		return pedido;
	}

	public void setPedido(final PedidoDeIdRemoto pedido) {
		this.pedido = pedido;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("Pedido desde [");
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
		}
		builder.append("] de id remoto");
		if (this.pedido != null) {
			builder.append(". ID: ");
			builder.append(this.pedido.getId());
		}
		return builder.toString();
	}
}
