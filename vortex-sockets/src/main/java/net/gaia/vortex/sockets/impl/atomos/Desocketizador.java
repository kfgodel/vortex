/**
 * 18/06/2012 22:48:15 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.sockets.impl.atomos;

import net.gaia.taskprocessor.api.WorkUnit;
import net.gaia.taskprocessor.api.processor.TaskProcessor;
import net.gaia.vortex.api.basic.Receptor;
import net.gaia.vortex.core.api.mensaje.MensajeVortex;
import net.gaia.vortex.core.impl.atomos.support.NexoSupport;
import net.gaia.vortex.core.impl.tasks.forward.DelegarMensaje;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ar.com.dgarcia.lang.strings.ToString;
import ar.dgarcia.objectsockets.api.ObjectReceptionHandler;
import ar.dgarcia.objectsockets.api.ObjectSocket;

/**
 * Esta clase representa un componente vortex que al recibir un mensaje desde un socket lo introduce
 * en la red a través del receptor destino al que está conectado
 * 
 * @author D. García
 */
public class Desocketizador extends NexoSupport implements ObjectReceptionHandler {
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
		// El mensaje del socket lo introducimos en la red desde nosotros como punto inicial
		final MensajeVortex mensajeRecibido = (MensajeVortex) received;
		this.recibir(mensajeRecibido);
	}

	/**
	 * @see net.gaia.vortex.core.impl.atomos.support.procesador.ReceptorConProcesador#crearTareaAlRecibir(net.gaia.vortex.core.api.mensaje.MensajeVortex)
	 */
	
	protected WorkUnit crearTareaAlRecibir(final MensajeVortex mensaje) {
		final DelegarMensaje delegacion = DelegarMensaje.create(mensaje, getDestino());
		return delegacion;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	
	public String toString() {
		return ToString.de(this).con(numeroDeInstancia_FIELD, getNumeroDeInstancia()).add(destino_FIELD, getDestino())
				.toString();
	}

	public static Desocketizador create(final TaskProcessor processor, final Receptor destino) {
		final Desocketizador desocketizador = new Desocketizador();
		desocketizador.initializeWith(processor, destino);
		return desocketizador;
	}

}
