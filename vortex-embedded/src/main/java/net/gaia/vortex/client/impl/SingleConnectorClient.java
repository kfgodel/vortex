/**
 * 12/11/2011 17:38:16 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.client.impl;

import java.util.Set;

import net.gaia.vortex.client.ClienteVortex;
import net.gaia.vortex.client.HandlerMensajesRecibidos;
import net.gaia.vortex.client.VortexDeliveryResult;

/**
 * Esta clase representa un cliente vortex que sólo utiliza o tiene un conector.<br>
 * Este tipo de clientes es el más fácil de levantar y configurar dentro de la aplicación
 * 
 * @author D. García
 */
public class SingleConnectorClient implements ClienteVortex {

	public static SingleConnectorClient create() {
		final SingleConnectorClient client = new SingleConnectorClient();
		return client;
	}

	/**
	 * @see net.gaia.vortex.client.ClienteVortex#deliverMessage(java.lang.Object, java.util.Set)
	 */
	public VortexDeliveryResult deliverMessage(final Object message, final Set<String> messageTags) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @see net.gaia.vortex.client.ClienteVortex#recibirMensajesTagueadosCon(java.util.Set,
	 *      net.gaia.vortex.client.HandlerMensajesRecibidos)
	 */
	public void recibirMensajesTagueadosCon(final Set<String> tagsToReceive, final HandlerMensajesRecibidos handler) {
		// TODO Auto-generated method stub

	}

	/**
	 * @see net.gaia.vortex.client.ClienteVortex#ofrecerMensajesTagueadosCon(java.util.Set)
	 */
	public void ofrecerMensajesTagueadosCon(final Set<String> tagsToSend) {
		// TODO Auto-generated method stub

	}

	/**
	 * @see net.gaia.vortex.client.ClienteVortex#detenerIntercambioDeMensajes()
	 */
	public void detenerIntercambioDeMensajes() {
		// TODO Auto-generated method stub

	}

	/**
	 * @see net.gaia.vortex.client.ClienteVortex#dejarDeRecibirMensajesTagueadosCon(java.util.Set)
	 */
	public void dejarDeRecibirMensajesTagueadosCon(final Set<String> tagsToReceive) {
		// TODO Auto-generated method stub

	}

	/**
	 * @see net.gaia.vortex.client.ClienteVortex#dejarDeOfrecerMensajesTagueadosCon(java.util.Set)
	 */
	public void dejarDeOfrecerMensajesTagueadosCon(final Set<String> tagsToSend) {
		// TODO Auto-generated method stub

	}
}
