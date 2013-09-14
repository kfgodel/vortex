/**
 * Created on: Sep 14, 2013 3:08:10 PM by: Dario L. Garcia
 * 
 * <a rel="license" href="http://creativecommons.org/licenses/by/2.5/ar/"><img
 * alt="Creative Commons License" style="border-width:0"
 * src="http://i.creativecommons.org/l/by/2.5/ar/88x31.png" /></a><br />
 * <span xmlns:dc="http://purl.org/dc/elements/1.1/"
 * href="http://purl.org/dc/dcmitype/InteractiveResource" property="dc:title"
 * rel="dc:type">Bean2Bean</span> by <a xmlns:cc="http://creativecommons.org/ns#"
 * href="https://sourceforge.net/projects/bean2bean/" property="cc:attributionName"
 * rel="cc:attributionURL">Dar&#237;o Garc&#237;a</a> is licensed under a <a rel="license"
 * href="http://creativecommons.org/licenses/by/2.5/ar/">Creative Commons Attribution 2.5 Argentina
 * License</a>.<br />
 * Based on a work at <a xmlns:dc="http://purl.org/dc/elements/1.1/"
 * href="https://bean2bean.svn.sourceforge.net/svnroot/bean2bean"
 * rel="dc:source">bean2bean.svn.sourceforge.net</a>
 */
package net.gaia.vortex.impl.atomos;

import net.gaia.vortex.api.mensajes.MensajeVortex;
import net.gaia.vortex.impl.support.MonoEmisorSupport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ar.dgarcia.objectsockets.api.ObjectReceptionHandler;
import ar.dgarcia.objectsockets.api.ObjectSocket;

/**
 * Esta clase representa un componente vortex que al recibir un mensaje desde un socket lo introduce
 * en la red a través del receptor destino al que está conectado
 * 
 * @author dgarcia
 */
public class Desocketizador extends MonoEmisorSupport implements ObjectReceptionHandler {
	private static final Logger LOG = LoggerFactory.getLogger(Desocketizador.class);

	/**
	 * @see ar.dgarcia.objectsockets.api.ObjectReceptionHandler#onObjectReceived(java.lang.Object,
	 *      ar.dgarcia.objectsockets.api.ObjectSocket)
	 */
	public void onObjectReceived(final Object received, final ObjectSocket receivedFrom) {
		if (!(received instanceof MensajeVortex)) {
			LOG.error("Se recibio desde un socket[" + receivedFrom + "] un objeto[" + received
					+ "] que no es un mensaje vortex. Ignorando");
			return;
		}
		// El mensaje del socket lo introducimos en la red
		final MensajeVortex mensajeRecibido = (MensajeVortex) received;
		getConectado().recibir(mensajeRecibido);
	}

	public static Desocketizador create() {
		final Desocketizador desocketizador = new Desocketizador();
		return desocketizador;
	}
}
