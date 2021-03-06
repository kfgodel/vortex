/**
 * 15/07/2012 18:28:44 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.comm.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import net.gaia.taskprocessor.api.TaskProcessor;
import net.gaia.vortex.comm.api.CanalDeChat;
import net.gaia.vortex.comm.api.ListenerDeEstadoDeCanal;
import net.gaia.vortex.comm.api.ListenerDeMensajesDeChat;
import net.gaia.vortex.comm.api.messages.AvisoDePresencia;
import net.gaia.vortex.comm.api.messages.MensajeDeChat;
import net.gaia.vortex.core.impl.atomos.receptores.ReceptorNulo;
import net.gaia.vortex.core.impl.moleculas.NodoMultiplexor;
import net.gaia.vortex.portal.api.moleculas.Portal;
import net.gaia.vortex.portal.impl.moleculas.HandlerTipado;
import net.gaia.vortex.portal.impl.moleculas.PortalMapeador;
import net.gaia.vortex.sets.impl.And;
import net.gaia.vortex.sets.impl.ContieneA;
import net.gaia.vortex.sets.impl.ValorEsperadoIgual;

/**
 * Esta clase representa un canal de chat
 * 
 * @author D. García
 */
public class CanalDeChatImpl implements CanalDeChat {

	private String nombreDeCanal;
	private Portal portalDeSalida;
	private Portal portalDeEntrada;
	private ListenerDeEstadoDeCanal listenerDeEstado;
	private ListenerDeMensajesDeChat listenerDeMensajes;

	private List<MensajeDeChat> mensajesDelCanal;
	private List<String> otrosPresentes;
	private ClienteDeChatVortexImpl cliente;

	public static CanalDeChatImpl create(TaskProcessor processor, String nombreDeCanal,
			ClienteDeChatVortexImpl clienteDeChatVortexImpl) {
		CanalDeChatImpl canal = new CanalDeChatImpl();
		canal.nombreDeCanal = nombreDeCanal;
		canal.cliente = clienteDeChatVortexImpl;
		canal.initialize(processor);
		return canal;
	}

	/**
	 * Inicializa el estado de esta instancia
	 * 
	 * @param processor
	 */
	private void initialize(TaskProcessor processor) {
		listenerDeEstado = ListenerNuloDeCanal.getInstancia();
		listenerDeMensajes = ListenerNuloDeCanal.getInstancia();
		mensajesDelCanal = new CopyOnWriteArrayList<MensajeDeChat>();
		otrosPresentes = new ArrayList<String>();

		NodoMultiplexor nodoDelCliente = this.cliente.getNodoCoreDelCliente();
		this.portalDeSalida = PortalMapeador.createForOutputWith(processor, nodoDelCliente);

		this.portalDeEntrada = PortalMapeador.createForOutputWith(processor, ReceptorNulo.getInstancia());
		nodoDelCliente.conectarCon(this.portalDeEntrada);
		this.portalDeEntrada.recibirCon(new HandlerTipado<MensajeDeChat>(And.create(
				ValorEsperadoIgual.a(MensajeDeChat.MENSAJE_DE_CHAT, MensajeDeChat.tipoDeMensaje_FIELD),
				ContieneA.valor(nombreDeCanal, MensajeDeChat.canales_FIELD))) {
			public void onMensajeRecibido(MensajeDeChat mensaje) {
				onMensajeDeChatRecibido(mensaje);
			}
		});
		this.portalDeEntrada.recibirCon(new HandlerTipado<AvisoDePresencia>(And.create(
				ValorEsperadoIgual.a(AvisoDePresencia.AVISO_DE_PRESENCIA, AvisoDePresencia.tipoDeMensaje_FIELD),
				ContieneA.valor(nombreDeCanal, AvisoDePresencia.canales_FIELD))) {
			public void onMensajeRecibido(AvisoDePresencia mensaje) {
				onAvisoDePresenciaRecibido(mensaje);
			}
		});
	}

	/**
	 * Invocado al recibir un nuevo aviso de presencia
	 */
	protected void onAvisoDePresenciaRecibido(AvisoDePresencia mensaje) {
		String nombreDeOtroPresente = mensaje.getUsuario();
		if (cliente.getUsuarioActual() == nombreDeOtroPresente) {
			// Es un aviso propio porque es la misma instancia!
			return;
		}
		otrosPresentes.add(nombreDeOtroPresente);
		listenerDeEstado.onCanalHabitado(this);
	}

	/**
	 * Invocado al recibir un mensaje de chat
	 */
	protected void onMensajeDeChatRecibido(MensajeDeChat mensaje) {
		mensajesDelCanal.add(mensaje);
		listenerDeMensajes.onMensajeNuevo(mensaje);
	}

	/**
	 * @see net.gaia.vortex.comm.api.CanalDeChat#getNombre()
	 */
	public String getNombre() {
		return nombreDeCanal;
	}

	/**
	 * @see net.gaia.vortex.comm.api.CanalDeChat#enviar(java.lang.String)
	 */
	public void enviar(String texto) {
		String usuarioActual = cliente.getUsuarioActual();
		MensajeDeChat mensajeDeChat = MensajeDeChat.create(usuarioActual, texto, nombreDeCanal);
		this.portalDeSalida.enviar(mensajeDeChat);
	}

	/**
	 * @see net.gaia.vortex.comm.api.CanalDeChat#setListenerDeMensajes(net.gaia.vortex.comm.api.ListenerDeMensajesDeChat)
	 */
	public void setListenerDeMensajes(ListenerDeMensajesDeChat listener) {
		this.listenerDeMensajes = listener;
	}

	/**
	 * @see net.gaia.vortex.comm.api.CanalDeChat#setListenerDeEstado(net.gaia.vortex.comm.api.ListenerDeEstadoDeCanal)
	 */
	public void setListenerDeEstado(ListenerDeEstadoDeCanal listener) {
		this.listenerDeEstado = listener;
	}

	/**
	 * @see net.gaia.vortex.comm.api.CanalDeChat#getMensajes()
	 */
	public List<MensajeDeChat> getMensajes() {
		return mensajesDelCanal;
	}

	/**
	 * @see net.gaia.vortex.comm.api.CanalDeChat#getOtrosPresentes()
	 */
	public List<String> getOtrosPresentes() {
		return otrosPresentes;
	}

	/**
	 * @see net.gaia.vortex.comm.api.CanalDeChat#vaciarPresentes()
	 */
	public void vaciarPresentes() {
		otrosPresentes.clear();
		listenerDeEstado.onCanalVacio(this);
	}

}
