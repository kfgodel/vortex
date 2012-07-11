/**
 * 18/02/2012 16:39:36 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.vorterix;

import java.net.SocketAddress;

import net.gaia.taskprocessor.api.TaskProcessor;
import net.gaia.taskprocessor.executor.ExecutorBasedTaskProcesor;
import net.gaia.vortex.core.impl.atomos.receptores.ReceptorNulo;
import net.gaia.vortex.core.impl.condiciones.SiempreTrue;
import net.gaia.vortex.portal.impl.moleculas.HandlerTipado;
import net.gaia.vortex.portal.impl.moleculas.PortalMapeador;
import net.gaia.vortex.sockets.impl.ClienteDeNexoSocket;
import net.gaia.vortex.sockets.impl.ServidorDeNexoSocket;
import net.gaia.vortex.sockets.impl.estrategias.RealizarConexiones;
import net.gaia.vortex.vorterix.messages.HandlerDeMensajesDeChat;
import net.gaia.vortex.vorterix.messages.MensajeDeChat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Esta clase representa un cliente de mensajes de chat vorterix
 * 
 * @author D. García
 */
public class VorterixClient implements HandlerDeMensajesDeChat {
	private static final Logger LOG = LoggerFactory.getLogger(VorterixClient.class);

	private TaskProcessor processor;
	private PortalMapeador portalConServidor;
	private PortalMapeador portalLocal;
	private HandlerDeMensajesDeChat handlerInterno;

	private ClienteDeNexoSocket clienteSocket;

	private ServidorDeNexoSocket servidorLocal;

	public static VorterixClient create() {
		final VorterixClient client = new VorterixClient();
		client.processor = ExecutorBasedTaskProcesor.create(1);
		HandlerTipado<MensajeDeChat> handlerDeMensajes = new HandlerTipado<MensajeDeChat>(SiempreTrue.getInstancia()) {
			public void onMensajeRecibido(MensajeDeChat mensaje) {
				client.onMensajeDeChatRecibido(mensaje);
			}
		};
		client.portalConServidor = PortalMapeador.createForOutputWith(client.processor, ReceptorNulo.getInstancia());
		client.portalConServidor.recibirCon(handlerDeMensajes);
		client.portalLocal = PortalMapeador.createForOutputWith(client.processor, ReceptorNulo.getInstancia());
		client.portalLocal.recibirCon(handlerDeMensajes);

		return client;
	}

	public void escucharEnLaDireccion(SocketAddress listeningAddress) {
		servidorLocal = ServidorDeNexoSocket.create(processor, listeningAddress, RealizarConexiones.con(portalLocal));
		servidorLocal.aceptarConexionesRemotas();
	}

	public void conectarAlaRed(SocketAddress serverAddress) {
		clienteSocket = ClienteDeNexoSocket.create(processor, serverAddress, RealizarConexiones.con(portalConServidor));
		clienteSocket.conectarASocketRomoto();
	}

	/**
	 * @see net.gaia.vortex.vorterix.messages.HandlerDeMensajesDeChat#onMensajeDeChatRecibido(net.gaia.vortex.vorterix.messages.MensajeDeChat)
	 */
	public void onMensajeDeChatRecibido(MensajeDeChat mensaje) {
		if (handlerInterno == null) {
			LOG.debug("No existe handler para el cliente de chat al recibir el mensaje[{}]. Descartando mensaje",
					mensaje);
			return;
		}
		try {
			handlerInterno.onMensajeDeChatRecibido(mensaje);
		} catch (Exception e) {
			LOG.error("Se produjo un error en el handler interno al recibir el mensaje[" + mensaje + "]", e);
		}
	}

	public void desconectarDeLaRed() {
		clienteSocket.closeAndDispose();
	}

	/**
	 * Envia el mensaje de chat indicado por la red vortex
	 * 
	 * @param mensajeDeChat
	 *            El mensaje a enviar
	 */
	public void send(MensajeDeChat mensajeDeChat) {
		portalConServidor.enviar(mensajeDeChat);
		portalLocal.enviar(mensajeDeChat);
	}

	/**
	 * Establece el hadnler de mensajes para utilizar al recibir nuevos
	 * 
	 * @param handlerDeMensajesDeChat
	 *            El handler de los mensajes de chat
	 */
	public void setHandlerDeMensajes(HandlerDeMensajesDeChat handlerDeMensajesDeChat) {
		this.handlerInterno = handlerDeMensajesDeChat;
	}
}
