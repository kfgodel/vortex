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

import net.gaia.taskprocessor.api.TaskProcessor;
import net.gaia.vortex.core.api.atomos.Emisor;
import net.gaia.vortex.core.api.atomos.Receptor;
import net.gaia.vortex.core.api.mensaje.MensajeVortex;
import net.gaia.vortex.core.impl.atomos.ComponenteConProcesadorSupport;
import net.gaia.vortex.core.impl.tasks.DelegarMensaje;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ar.com.dgarcia.lang.strings.ToString;
import ar.dgarcia.objectsockets.api.ObjectReceptionHandler;
import ar.dgarcia.objectsockets.api.ObjectSocket;

/**
 * Esta clase representa un componente vortex que al recibir un mensaje desde un socket lo introduce
 * en la red a través del receptor que conoce
 * 
 * @author D. García
 */
public class Desocketizador extends ComponenteConProcesadorSupport implements Emisor, ObjectReceptionHandler {
	private static final Logger LOG = LoggerFactory.getLogger(Desocketizador.class);

	private Receptor destino;
	public static final String destino_FIELD = "destino";

	/**
	 * Reemplaza el receptor previo con el nuevo pasado
	 * 
	 * @see net.gaia.vortex.core.api.atomos.Emisor#conectarCon(net.gaia.vortex.core.api.atomos.Receptor)
	 */
	@Override
	public void conectarCon(final Receptor destino) {
		if (destino == null) {
			throw new IllegalArgumentException("El dstino[" + destino + "] no puede ser null en esta desocketizador");
		}
		this.destino = destino;
	}

	/**
	 * @see net.gaia.vortex.core.api.atomos.Emisor#desconectarDe(net.gaia.vortex.core.api.atomos.Receptor)
	 */
	@Override
	public void desconectarDe(final Receptor destino) {
		LOG.info("Se intentó desconectar un destino[" + destino + "] del desocketizador. Ignorando");
	}

	/**
	 * @see ar.dgarcia.objectsockets.api.ObjectReceptionHandler#onObjectReceived(java.lang.Object,
	 *      ar.dgarcia.objectsockets.api.ObjectSocket)
	 */
	@Override
	public void onObjectReceived(final Object received, final ObjectSocket receivedFrom) {
		if (!(received instanceof MensajeVortex)) {
			LOG.error("Se recibio desde un socket[" + receivedFrom + "] un objeto[" + received
					+ "] que no es un mensaje vortex. Ignorando");
			return;
		}
		// Le pasamos el mensaje que vino desde el socket al receptor destino de la red
		final MensajeVortex mensajeRecibido = (MensajeVortex) received;
		final DelegarMensaje delegacion = DelegarMensaje.create(mensajeRecibido, destino);
		procesarEnThreadPropio(delegacion);
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return ToString.de(this).add(destino_FIELD, destino).toString();
	}

	public static Desocketizador create(final TaskProcessor processor, final Receptor destino) {
		final Desocketizador desocketizador = new Desocketizador();
		desocketizador.initializeWith(processor);
		desocketizador.conectarCon(destino);
		return desocketizador;
	}
}