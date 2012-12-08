/**
 * 13/10/2012 11:08:14 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.tests.router.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import net.gaia.vortex.tests.router.impl.patas.PataConectoraOld;
import net.gaia.vortex.tests.router2.api.Mensaje;
import net.gaia.vortex.tests.router2.api.Nodo;
import net.gaia.vortex.tests.router2.impl.filtros.Filtro;
import net.gaia.vortex.tests.router2.mensajes.ConfirmacionDeIdRemoto;
import net.gaia.vortex.tests.router2.mensajes.MensajeNormal;
import net.gaia.vortex.tests.router2.mensajes.PedidoDeIdRemoto;
import net.gaia.vortex.tests.router2.mensajes.PublicacionDeFiltros;
import net.gaia.vortex.tests.router2.mensajes.RespuestaDeIdRemoto;
import net.gaia.vortex.tests.router2.simulador.Simulador;
import net.gaia.vortex.tests.router2.simulador.pasos.EnviarMensaje;
import net.gaia.vortex.tests.router2.simulador.pasos.PublicarFiltros;
import net.gaia.vortex.tests.router2.simulador.pasos.bidi.ConfirmarIdRemoto;
import net.gaia.vortex.tests.router2.simulador.pasos.bidi.PedirIdRemoto;
import net.gaia.vortex.tests.router2.simulador.pasos.bidi.ResponderIdRemoto;
import net.gaia.vortex.tests.router2.simulador.pasos.conexion.ConectarBidi;
import net.gaia.vortex.tests.router2.simulador.pasos.conexion.ConectarUni;
import net.gaia.vortex.tests.router2.simulador.pasos.conexion.DesconectarBidi;
import net.gaia.vortex.tests.router2.simulador.pasos.conexion.DesconectarUni;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Esta clase implementa comportamiento comun del nodo
 * 
 * @author D. García
 */
public abstract class NodoSupport implements Nodo {
	private static final Logger LOG = LoggerFactory.getLogger(NodoSupport.class);

	private String nombre;

	private Simulador simulador;

	private final AtomicLong proximoIdPata = new AtomicLong(0);

	private List<PataConectoraOld> destinos;

	private List<Mensaje> enviados;
	private List<Mensaje> recibidos;

	/**
	 * @see net.gaia.vortex.tests.router2.api.Nodo#getNombre()
	 */
	@Override
	public String getNombre() {
		return nombre;
	}

	protected void setNombre(final String nombre) {
		this.nombre = nombre;
	}

	protected Simulador getSimulador() {
		return simulador;
	}

	protected void setSimulador(final Simulador simulador) {
		this.simulador = simulador;
	}

	/**
	 * @see net.gaia.vortex.tests.router2.api.Nodo#conectarCon(net.gaia.vortex.tests.router2.api.Nodo)
	 */
	@Override
	public void conectarCon(final Nodo otro) {
		final ConectarUni nuevoPaso = ConectarUni.create(this, otro);
		simulador.agregar(nuevoPaso);
	}

	/**
	 * @see net.gaia.vortex.tests.router2.api.Nodo#agregarDestino(net.gaia.vortex.tests.router2.api.Nodo)
	 */
	@Override
	public void agregarDestino(final Nodo nodoDestino) {
		final Long idNuevaPata = this.proximoIdPata.getAndIncrement();
		final PataConectoraOld nuevaPata = PataConectoraOld.create(idNuevaPata, nodoDestino);
		getDestinos().add(nuevaPata);

		// Después de agregarlo como salida intentamos ver si es bidireccional
		conseguirIdRemotoDe(nuevaPata);
	}

	/**
	 * @see net.gaia.vortex.tests.router2.api.Nodo#quitarDestino(net.gaia.vortex.tests.router2.api.Nodo)
	 */
	@Override
	public void quitarDestino(final Nodo nodoDestino) {
		final PataConectoraOld pataConectora = getPataPorNodo(nodoDestino);
		if (pataConectora == null) {
			LOG.debug("En [{}] no se puede quitar la pata correspondiente al nodo[{}] porque no existe",
					this.getNombre(), nodoDestino);
			return;
		}
		getDestinos().remove(pataConectora);

		// Republicamos los filtros que se hayan mmodificado
		publicarFiltros();
	}

	public List<PataConectoraOld> getDestinos() {
		if (destinos == null) {
			destinos = new ArrayList<PataConectoraOld>();
		}
		return destinos;
	}

	/**
	 * @see net.gaia.vortex.tests.router2.api.Nodo#tieneComoDestinoA(net.gaia.vortex.tests.router2.api.Nodo)
	 */
	@Override
	public boolean tieneComoDestinoA(final Nodo otro) {
		final List<PataConectoraOld> allDestinos = getDestinos();
		for (final PataConectoraOld pataConectora : allDestinos) {
			if (pataConectora.conectaA(otro)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * @see net.gaia.vortex.tests.router2.api.Nodo#simularConexionBidi(net.gaia.vortex.tests.router2.api.Nodo)
	 */
	@Override
	public void simularConexionBidi(final Nodo otro) {
		simulador.agregar(ConectarBidi.create(this, otro));
	}

	public List<Mensaje> getEnviados() {
		if (enviados == null) {
			enviados = new ArrayList<Mensaje>();
		}
		return enviados;
	}

	public void setEnviados(final List<Mensaje> enviados) {
		this.enviados = enviados;
	}

	/**
	 * @see net.gaia.vortex.tests.router2.api.Nodo#evento_recibirPublicacion(net.gaia.vortex.tests.router2.mensajes.PublicacionDeFiltros)
	 */
	@Override
	public void recibirPublicacion(final PublicacionDeFiltros publicacion) {
		getRecibidos().add(publicacion);

		final Long idLocal = publicacion.getIdDePata();
		final PataConectoraOld pataSalida = getPataPorIdLocal(idLocal);
		if (pataSalida == null) {
			LOG.debug("  Rechazando publicacion en [{},{}] por que ya no existe conexion",
					new Object[] { this.getNombre(), idLocal });
			return;
		}
		final Filtro nuevosFiltros = publicacion.getFiltro();
		pataSalida.filtrarCon(nuevosFiltros);
		LOG.debug("  En [{},{}] solo se enviaran mensajes que cumplan el filtro{}: {}", new Object[] {
				this.getNombre(), pataSalida.getIdLocal(), publicacion, nuevosFiltros });
	}

	public List<Mensaje> getRecibidos() {
		if (recibidos == null) {
			recibidos = new ArrayList<Mensaje>();
		}
		return recibidos;
	}

	public void setRecibidos(final List<Mensaje> recibidos) {
		this.recibidos = recibidos;
	}

	public void agregarComoEnviado(final Mensaje mensaje) {
		getEnviados().add(mensaje);
	}

	/**
	 * @see net.gaia.vortex.tests.router2.api.Nodo#evento_recibirPedidoDeId(net.gaia.vortex.tests.router2.mensajes.PedidoDeIdRemoto)
	 */
	@Override
	public void recibirPedidoDeId(final PedidoDeIdRemoto pedido) {
		getRecibidos().add(pedido);
		responderPedido(pedido);
	}

	/**
	 * @see net.gaia.vortex.tests.router2.api.Nodo#evento_recibirRespuestaDeIdRemoto(net.gaia.vortex.tests.router2.mensajes.RespuestaDeIdRemoto)
	 */
	@Override
	public void recibirRespuestaDeIdRemoto(final RespuestaDeIdRemoto respuesta) {
		getRecibidos().add(respuesta);

		final PedidoDeIdRemoto pedidoOriginal = respuesta.getPedido();
		if (!getEnviados().contains(pedidoOriginal)) {
			// No lo enviamos nosotros
			LOG.debug("  Rechazando en [{}] respuesta{} por pedido{} no realizado", new Object[] { this.getNombre(),
					respuesta, pedidoOriginal });
			return;
		}

		final Long idLocal = pedidoOriginal.getIdDePata();
		final Long idRemoto = respuesta.getIdAsignado();
		final PataConectoraOld pataSalida = asociarIdRemotoAPataLocal(idLocal, idRemoto);
		if (pataSalida == null) {
			// Ya no está más conectado
			LOG.debug("  Publicacion de filtros desde [{},{}] a [?,{}] no posible por que ya no existe conexion",
					new Object[] { this.getNombre(), idLocal, idRemoto });
			return;
		}
		enviarConfirmacionDeIdEn(pataSalida, respuesta);
		// publicarFiltrosEn(pataSalida);
	}

	private void enviarConfirmacionDeIdEn(final PataConectoraOld pataSalida, final RespuestaDeIdRemoto respuesta) {
		final Long idLocal = pataSalida.getIdLocal();
		final ConfirmacionDeIdRemoto confirmacion = ConfirmacionDeIdRemoto.create(respuesta, idLocal);
		agregarComoEnviado(confirmacion);
		getSimulador().agregar(ConfirmarIdRemoto.create(this, pataSalida, confirmacion));
	}

	/**
	 * Busca la pata local referida en la respuesta y le asigna el ID remoto que nos indican
	 * 
	 * @param respuesta
	 *            La respuesta que asigna un id remoto
	 * @param idLocal
	 * @param idRemoto
	 * @return La pata local a la que corresponde la respuesta
	 */
	private PataConectoraOld asociarIdRemotoAPataLocal(final Long idLocal, final Long idRemoto) {
		final PataConectoraOld pataSalida = getPataPorIdLocal(idLocal);
		if (pataSalida == null) {
			// Ya no existe la paga
			return null;
		}
		pataSalida.setIdRemoto(idRemoto);
		return pataSalida;
	}

	protected PataConectoraOld getPataPorIdLocal(final Long idLocal) {
		for (final PataConectoraOld pataSalida : getDestinos()) {
			final Long idDePata = pataSalida.getIdLocal();
			if (idDePata.equals(idLocal)) {
				return pataSalida;
			}
		}
		return null;
	}

	/**
	 * Devuelve la pata de salida identificandola por nodo
	 * 
	 * @param nodo
	 *            El nodo cuya pata quiere obtenerse
	 * @return La pata del nodo o null si no existe
	 */
	protected PataConectoraOld getPataPorNodo(final Nodo nodo) {
		final List<PataConectoraOld> allDestinos = getDestinos();
		for (final PataConectoraOld pataConectora : allDestinos) {
			if (pataConectora.getNodoRemoto().equals(nodo)) {
				return pataConectora;
			}
		}
		return null;
	}

	/**
	 * 
	 */
	public void publicarFiltros() {
		final List<PataConectoraOld> destinos = getDestinos();
		if (destinos.isEmpty()) {
			LOG.debug("  Publicación desde [{}] sin patas de salida. Abortando", this.getNombre());
			return;
		}

		for (final PataConectoraOld pataSalida : destinos) {
			publicarFiltrosEn(pataSalida);
		}
	}

	/**
	 * Intenta publicar los filtros en la pata indicada
	 * 
	 * @param pataSalida
	 *            La pata por la que se intentará comunicar los filtros a otro nodo
	 */
	protected void publicarFiltrosEn(final PataConectoraOld pataSalida) {
		if (!pataSalida.tieneIdRemoto()) {
			// Si no tiene ID tenemos que conseguirlo antes de publicar
			conseguirIdRemotoDe(pataSalida);
		} else {
			actualizarFiltrosEn(pataSalida);
		}
	}

	/**
	 * Calcula los nuevos filtros aplicables a la pata indicada y los publica al nodo destino si hay
	 * cambios
	 * 
	 * @param pataSalida
	 *            La pata que se actualizará
	 */
	private void actualizarFiltrosEn(final PataConectoraOld pataSalida) {
		final Long idRemoto = pataSalida.getIdRemoto();
		final Filtro filtrosParaLaPata = calcularFiltrosPara(pataSalida);
		if (pataSalida.yaSePublico(filtrosParaLaPata)) {
			LOG.debug("En [{},{}] no se publica porque no hubo cambio de mensajes esperados: {}", new Object[] {
					getNombre(), pataSalida.getIdLocal(), filtrosParaLaPata });
			return;
		}
		LOG.debug("En [{},{}] se publica porque cambiaron los mensajes esperados de {} a: {}", new Object[] {
				getNombre(), pataSalida.getIdLocal(), pataSalida.getFiltroPublicado(), filtrosParaLaPata });
		final PublicacionDeFiltros publicacion = PublicacionDeFiltros.create(idRemoto, filtrosParaLaPata);
		agregarComoEnviado(publicacion);
		getSimulador().agregar(PublicarFiltros.create(this, pataSalida, publicacion));
	}

	/**
	 * 
	 * Define cuáles son los filtros que corresponde publicar a la pata pasada
	 * 
	 * @param pataSalida
	 *            la pata a la que se enviara la publicacion de los filtros calculado
	 * @return El conjunto de los filtros que define los mensajes que queremos recibir por esa pata
	 */
	protected abstract Filtro calcularFiltrosPara(final PataConectoraOld pataSalida);

	/**
	 * Intenta intercambiar ids de patas para poder publicar filtros
	 * 
	 * @param pataSalida
	 *            La pata sin id
	 */
	private void conseguirIdRemotoDe(final PataConectoraOld pataSalida) {
		final Long idLocal = pataSalida.getIdLocal();
		final PedidoDeIdRemoto pedidoDeIdRemoto = PedidoDeIdRemoto.create(idLocal);
		agregarComoEnviado(pedidoDeIdRemoto);
		getSimulador().agregar(PedirIdRemoto.create(this, pataSalida, pedidoDeIdRemoto));
	}

	/**
	 * Intenta respo1nder el pedido recibido contestando a cada pata por separado
	 * 
	 * @param pedido
	 *            El pedido recibido
	 */
	private void responderPedido(final PedidoDeIdRemoto pedido) {
		final List<PataConectoraOld> destinos = getDestinos();
		if (destinos.isEmpty()) {
			LOG.debug("  Respuesta desde [{}] sin salidas para pedido{}", this.getNombre(), pedido);
			return;
		}

		for (final PataConectoraOld pataSalida : destinos) {
			responderPedidoA(pataSalida, pedido);
		}
	}

	/**
	 * Envía una respuesta del pedido realizado a la pata indicada
	 * 
	 * @param pataSalida
	 *            La pata con la que se intenta responder
	 * @param pedido
	 *            El pedido que recibimos
	 */
	private void responderPedidoA(final PataConectoraOld pataSalida, final PedidoDeIdRemoto pedido) {
		final Long idLocal = pataSalida.getIdLocal();
		final RespuestaDeIdRemoto respuesta = RespuestaDeIdRemoto.create(pedido, idLocal);
		agregarComoEnviado(respuesta);
		getSimulador().agregar(ResponderIdRemoto.create(this, pataSalida, respuesta));
	}

	/**
	 * @see net.gaia.vortex.tests.router2.api.Nodo#evento_recibirConfirmacionDeIdRemoto(net.gaia.vortex.tests.router2.mensajes.ConfirmacionDeIdRemoto)
	 */
	@Override
	public void recibirConfirmacionDeIdRemoto(final ConfirmacionDeIdRemoto confirmacion) {
		getRecibidos().add(confirmacion);

		final RespuestaDeIdRemoto respuestaOriginal = confirmacion.getRespuesta();
		if (!getEnviados().contains(respuestaOriginal)) {
			// No lo enviamos nosotros
			LOG.debug("  Rechazando en [{}] confirmacion{} por respuesta{} no realizada",
					new Object[] { this.getNombre(), confirmacion, respuestaOriginal });
			return;
		}

		final Long idLocal = respuestaOriginal.getIdAsignado();
		final Long idRemoto = confirmacion.getIdAsignado();
		final PataConectoraOld pataSalida = asociarIdRemotoAPataLocal(idLocal, idRemoto);
		if (pataSalida == null) {
			// Ya no está más conectado
			LOG.debug("  Respuesta de filtros desde [{},{}] a [?,{}] no posible por que ya no existe conexion",
					new Object[] { this.getNombre(), idLocal, idRemoto });
			return;
		}
	}

	/**
	 * @see net.gaia.vortex.tests.router2.api.Nodo#simularDesconexionBidiDe(net.gaia.vortex.tests.router2.api.Nodo)
	 */
	@Override
	public void simularDesconexionBidiDe(final Nodo otroConectado) {
		simulador.agregar(DesconectarBidi.create(this, otroConectado));
	}

	/**
	 * @see net.gaia.vortex.tests.router2.api.Nodo#desconectarDe(net.gaia.vortex.tests.router2.api.Nodo)
	 */
	@Override
	public void desconectarDe(final Nodo destino) {
		simulador.agregar(DesconectarUni.create(this, destino));
	}

	/**
	 * @see net.gaia.vortex.tests.router2.api.Nodo#recibirMensaje(net.gaia.vortex.tests.router2.mensajes.MensajeNormal)
	 */
	@Override
	public void recibirMensaje(final MensajeNormal mensaje) {
		if (getRecibidos().contains(mensaje)) {
			LOG.debug("  Rechazando mensaje[{}] recibido en [{}] por figurar como ya recibido", mensaje.getIdDeMensaje(),
					this.getNombre());
			return;
		}
		if (getEnviados().contains(mensaje)) {
			LOG.debug("  Rechazando mensaje[{}] recibido en [{}] por figurar como ya enviado", mensaje.getIdDeMensaje(),
					this.getNombre());
			return;
		}
		getRecibidos().add(mensaje);
		realizarAccionEspecificaAlRecibir(mensaje);
	}

	/**
	 * Realiza la acción que depdende del tipo de nodo la consecuencia que tiene
	 * 
	 * @param mensaje
	 *            El mensaje recién recibido
	 */
	protected abstract void realizarAccionEspecificaAlRecibir(final MensajeNormal mensaje);

	/**
	 * Envia el mensaje indicado a las patas de salida de este nodo
	 * 
	 * @param mensaje
	 *            El mensaje a enviar
	 */
	protected void propagarMensaje(final MensajeNormal mensaje) {
		// Lo registramos como que lo enviamos
		agregarComoEnviado(mensaje);

		final List<PataConectoraOld> patasDeSalida = getDestinos();
		LOG.debug("Propagando mensaje[{}] desde [{}] a {} patas", new Object[] { mensaje.getIdDeMensaje(), this.getNombre(),
				patasDeSalida.size() });
		for (final PataConectoraOld pataConectora : patasDeSalida) {
			if (pataConectora.getIdLocal().equals(mensaje.getIdDePataRemota())) {
				LOG.debug("  Evitando enviar desde [{},{}] el mensaje[{}] porque provino de esa pata", new Object[] {
						this.getNombre(), pataConectora.getIdLocal(), mensaje.getIdDeMensaje() });
				continue;
			}

			if (pataConectora.puedeEnviar(mensaje)) {
				getSimulador().agregar(EnviarMensaje.create(this, pataConectora, mensaje));
			} else {
				LOG.debug(
						"  Evitando enviar desde [{},{}] el mensaje[{}] porque su filtro no lo admite: {}",
						new Object[] { this.getNombre(), pataConectora.getIdLocal(), mensaje.getIdDeMensaje(),
								pataConectora.getFiltroDeSalida() });
			}
		}
	}

	/**
	 * Indica si este nodo ya envio o recibio el mensaje pasado, por lo tanto no se le debe pasar de
	 * nuevo
	 * 
	 * @param mensaje
	 *            El mensaje a verificar
	 * @return true si ya se envio o recibio este mensaje
	 */
	public boolean yaProceso(final MensajeNormal mensaje) {
		final boolean yaSeProceso = getEnviados().contains(mensaje) || getRecibidos().contains(mensaje);
		return yaSeProceso;
	}
}
