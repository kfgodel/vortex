/**
 * 14/07/2012 23:08:25 Copyright (C) 2011 Darío L. García
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
import net.gaia.vortex.comm.api.ClienteDeChatVortex;
import net.gaia.vortex.comm.api.ListenerDeEstadoDeCanal;
import net.gaia.vortex.comm.api.messages.AvisoDeAusencia;
import net.gaia.vortex.comm.api.messages.AvisoDePresencia;
import net.gaia.vortex.comm.api.messages.PedidoDePresencia;
import net.gaia.vortex.comm.impl.messages.VortexChatSupport;
import net.gaia.vortex.core.api.Nodo;
import net.gaia.vortex.core.impl.atomos.condicional.NexoFiltro;
import net.gaia.vortex.core.impl.atomos.receptores.ReceptorNulo;
import net.gaia.vortex.core.impl.moleculas.NodoMultiplexor;
import net.gaia.vortex.portal.api.moleculas.Portal;
import net.gaia.vortex.portal.impl.moleculas.HandlerTipado;
import net.gaia.vortex.portal.impl.moleculas.PortalMapeador;

/**
 * Esta clase representa el conjunto de los canales utilizables por un cliente de chat
 * 
 * @author D. García
 */
public class ClienteDeChatVortexImpl implements ClienteDeChatVortex {

	private List<CanalDeChat> canales;

	private Nodo nodoCentral;
	private NexoFiltro entradaConectadaDesdeElCentral;
	private NodoMultiplexor nodoCoreDelCliente;
	private Portal portalDelCliente;

	private String usuarioActual;

	private ListenerDeEstadoDeCanal listener;
	private TaskProcessor processor;

	public String getUsuarioActual() {
		return usuarioActual;
	}

	public void setUsuarioActual(String usuarioActual) {
		this.usuarioActual = usuarioActual;
	}

	/**
	 * Crea un cliente de chat que utilizará el nodo pasado como nodo central de las comunicaciones
	 * 
	 * @return El cliente creado
	 */
	public static ClienteDeChatVortexImpl create(TaskProcessor processor, String nombreDeUsuario) {
		ClienteDeChatVortexImpl cliente = new ClienteDeChatVortexImpl();
		cliente.usuarioActual = nombreDeUsuario;
		cliente.processor = processor;
		cliente.initialize();
		return cliente;
	}

	public NodoMultiplexor getNodoCoreDelCliente() {
		return nodoCoreDelCliente;
	}

	public void setNodoCoreDelCliente(NodoMultiplexor nodoCoreDelCliente) {
		this.nodoCoreDelCliente = nodoCoreDelCliente;
	}

	/**
	 * Inicializa esta instancia
	 * 
	 * @param processor
	 *            El procesador para los componetnes vortex
	 */
	private void initialize() {
		// Usamos el listener nulo como inicial
		listener = ListenerNuloDeCanal.getInstancia();

		// Toda salida del cliente se conecta a este multiplexor (canales y central)
		nodoCoreDelCliente = NodoMultiplexor.create(processor);

		// De afuera solo aceptamos mensajes que sean de chat
		AtributoIgual esAceptableParaElCliente = AtributoIgual.create(VortexChatSupport.aplicacion_FIELD,
				VortexChatSupport.NOMBRE_APLICACION_CHAT);
		entradaConectadaDesdeElCentral = NexoFiltro.create(processor, esAceptableParaElCliente, nodoCoreDelCliente);

		this.portalDelCliente = PortalMapeador.createForOutputWith(processor, ReceptorNulo.getInstancia());
		nodoCoreDelCliente.conectarCon(portalDelCliente);
		this.portalDelCliente.recibirCon(new HandlerTipado<PedidoDePresencia>(AtributoIgual.create(
				PedidoDePresencia.tipoDeMensaje_FIELD, PedidoDePresencia.PEDIDO_DE_PRESENCIA)) {
			public void onMensajeRecibido(PedidoDePresencia mensaje) {
				onPedidoDePresenciaRecibido(mensaje);
			}
		});
		this.portalDelCliente.recibirCon(new HandlerTipado<AvisoDePresencia>(AtributoIgual.create(
				AvisoDePresencia.tipoDeMensaje_FIELD, AvisoDePresencia.AVISO_DE_PRESENCIA)) {
			public void onMensajeRecibido(AvisoDePresencia mensaje) {
				onAvisoDePresenciaRecibido(mensaje);
			}
		});
		this.portalDelCliente.recibirCon(new HandlerTipado<AvisoDeAusencia>(AtributoIgual.create(
				AvisoDeAusencia.tipoDeMensaje_FIELD, AvisoDeAusencia.AVISO_DE_AUSENCIA)) {
			public void onMensajeRecibido(AvisoDeAusencia mensaje) {
				onAvisoDeAusenciaRecibido(mensaje);
			}
		});

	}

	/**
	 * Invocado al recibir un aviso de ausencia
	 */
	protected void onAvisoDeAusenciaRecibido(AvisoDeAusencia mensaje) {
		// Verificamos si nos afecta
		List<String> canalesAusentados = mensaje.getCanales();
		List<CanalDeChat> allCanales = getCanales();
		for (CanalDeChat canalDeChat : allCanales) {
			String nombreDelCanal = canalDeChat.getNombre();
			if (canalesAusentados.contains(nombreDelCanal)) {
				actualizarPresentismo();
				return;
			}
		}
	}

	/**
	 * Invocado al recibir una notificación de presencia
	 * 
	 * @param mensaje
	 *            El mensaje recibido
	 */
	protected void onAvisoDePresenciaRecibido(AvisoDePresencia mensaje) {
		List<String> canalesExternos = mensaje.getCanales();
		for (String canalExterno : canalesExternos) {
			CanalDeChat canalInterno = getCanal(canalExterno);
			if (canalInterno == null) {
				// No tenemos ese canal
				continue;
			}
			listener.onCanalHabitado(canalInterno);
		}
	}

	/**
	 * Invocado cuando se recibe un pedido de presencia
	 * 
	 * @param mensaje
	 *            El pedido recibido
	 */
	protected void onPedidoDePresenciaRecibido(PedidoDePresencia mensaje) {
		// No importa cual nos piden avisamos en todos los que estamos
		enviarAvisoDePresencia();
	}

	/**
	 * Envia un aviso de presencia de todos los canales que participamos
	 */
	private void enviarAvisoDePresencia() {
		List<String> canalesActuales = getNombresDeCanalesUsados();
		AvisoDePresencia avisoDePresencia = new AvisoDePresencia();
		avisoDePresencia.setUsuario(usuarioActual);
		avisoDePresencia.setCanales(canalesActuales);
		this.portalDelCliente.enviar(avisoDePresencia);
	}

	/**
	 * Calcula los nombres de los canales usados actualmente
	 * 
	 * @return La lista de los nombres
	 */
	private List<String> getNombresDeCanalesUsados() {
		List<CanalDeChat> allCanales = getCanales();
		List<String> canalesActuales = new ArrayList<String>(allCanales.size());
		for (CanalDeChat canalDeChat : allCanales) {
			String nombre = canalDeChat.getNombre();
			canalesActuales.add(nombre);
		}
		return canalesActuales;
	}

	/**
	 * @see net.gaia.vortex.comm.api.ClienteDeChatVortex#getCanales()
	 */
	public List<CanalDeChat> getCanales() {
		if (canales == null) {
			canales = new CopyOnWriteArrayList<CanalDeChat>();
		}
		return canales;
	}

	/**
	 * @see net.gaia.vortex.comm.api.ClienteDeChatVortex#agregarCanal(java.lang.String)
	 */
	public CanalDeChat agregarCanal(String nombreDeCanal) {
		CanalDeChatImpl canalCreado = CanalDeChatImpl.create(processor, nombreDeCanal, this);
		getCanales().add(canalCreado);
		enviarAvisoDePresencia();
		return canalCreado;
	}

	/**
	 * @see net.gaia.vortex.comm.api.ClienteDeChatVortex#quitarCanal(java.lang.String)
	 */
	public void quitarCanal(String nombreDeCanal) {
		CanalDeChat canalQuitado = getCanal(nombreDeCanal);
		if (canalQuitado == null) {
			return;
		}
		getCanales().remove(canalQuitado);
		enviarAvisoDeAusencia(nombreDeCanal);
	}

	/**
	 * Envía un aviso de que este cliente abandono el canal indicado
	 * 
	 * @param nombreDeCanal
	 */
	private void enviarAvisoDeAusencia(String nombreDeCanal) {
		AvisoDeAusencia aviso = new AvisoDeAusencia();
		aviso.getCanales().add(nombreDeCanal);
		aviso.setUsuario(usuarioActual);
		this.portalDelCliente.enviar(aviso);
	}

	/**
	 * @see net.gaia.vortex.comm.api.ClienteDeChatVortex#getCanal(java.lang.String)
	 */
	public CanalDeChat getCanal(String nombreDeCanal) {
		List<CanalDeChat> allCanales = getCanales();
		for (CanalDeChat canal : allCanales) {
			if (canal.getNombre().equals(nombreDeCanal)) {
				return canal;
			}
		}
		return null;
	}

	/**
	 * @see net.gaia.vortex.comm.api.ClienteDeChatVortex#actualizarPresentismo()
	 */
	public void actualizarPresentismo() {
		// Primero vaciamos el estado
		List<CanalDeChat> allCanales = getCanales();
		for (CanalDeChat canal : allCanales) {
			canal.vaciarPresentes();
			listener.onCanalVacio(canal);
		}
		// Solicitamos que nos avisen si hay alguien
		enviarPedidoDePresencia();
	}

	/**
	 * Envia un pedido de presencia al resto de los clientes
	 */
	private void enviarPedidoDePresencia() {
		List<String> nombresDeCanalesUsados = getNombresDeCanalesUsados();
		PedidoDePresencia pedidoDePresencia = new PedidoDePresencia();
		pedidoDePresencia.setCanales(nombresDeCanalesUsados);
		this.portalDelCliente.enviar(pedidoDePresencia);
	}

	/**
	 * @see net.gaia.vortex.comm.api.ClienteDeChatVortex#setListenerDeEstado(net.gaia.vortex.comm.api.ListenerDeEstadoDeCanal)
	 */
	public void setListenerDeEstado(ListenerDeEstadoDeCanal listenerDeEstadoDeCanal) {
		this.listener = listenerDeEstadoDeCanal;
	}

	/**
	 * @see net.gaia.vortex.comm.api.ClienteDeChatVortex#desconectar()
	 */
	public void desconectar() {
		nodoCentral.desconectarDe(entradaConectadaDesdeElCentral);
		this.nodoCoreDelCliente.desconectarDe(nodoCentral);
		this.portalDelCliente.desconectarDe(nodoCentral);
	}

	/**
	 * @see net.gaia.vortex.comm.api.ClienteDeChatVortex#conectarA(net.gaia.vortex.core.api.Nodo)
	 */
	public void conectarA(Nodo nodoCentral) {
		this.nodoCentral = nodoCentral;
		this.portalDelCliente.conectarCon(nodoCentral);
		this.nodoCoreDelCliente.conectarCon(nodoCentral);
		nodoCentral.conectarCon(entradaConectadaDesdeElCentral);
		actualizarPresentismo();
	}
}
