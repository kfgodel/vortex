/**
 * 14/07/2012 19:19:12 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.comm.api.messages;

import net.gaia.vortex.comm.impl.messages.VortexChatSupport;

/**
 * Esta clase representa un pedido a otros de que informen su estado de conexión
 * 
 * @author D. García
 */
public class PedidoDePresencia extends VortexChatSupport {
	public static final String PEDIDO_DE_PRESENCIA = "PedidoDePresencia";

	public PedidoDePresencia() {
		super(PEDIDO_DE_PRESENCIA);
	}

}
