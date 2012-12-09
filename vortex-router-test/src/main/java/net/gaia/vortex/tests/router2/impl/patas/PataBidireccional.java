/**
 * 07/12/2012 18:29:51 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.tests.router2.impl.patas;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import net.gaia.vortex.tests.router2.api.ListenerDeFiltros;
import net.gaia.vortex.tests.router2.api.Mensaje;
import net.gaia.vortex.tests.router2.impl.filtros.Filtro;
import net.gaia.vortex.tests.router2.impl.filtros.ListenerDeFiltrosNulo;
import net.gaia.vortex.tests.router2.impl.filtros.FiltroPasaTodo;
import net.gaia.vortex.tests.router2.mensajes.ConfirmacionDeIdRemoto;
import net.gaia.vortex.tests.router2.mensajes.MensajeNormal;
import net.gaia.vortex.tests.router2.mensajes.PedidoDeIdRemoto;
import net.gaia.vortex.tests.router2.mensajes.PublicacionDeFiltros;
import net.gaia.vortex.tests.router2.mensajes.RespuestaDeIdRemoto;
import net.gaia.vortex.tests.router2.simulador.ComponenteSimulable;
import net.gaia.vortex.tests.router2.simulador.NodoSimulacion;
import net.gaia.vortex.tests.router2.simulador.PasoSimulacion;
import net.gaia.vortex.tests.router2.simulador.Simulador;
import net.gaia.vortex.tests.router2.simulador.pasos.bidi.ConfirmarIdRemoto;
import net.gaia.vortex.tests.router2.simulador.pasos.bidi.PedirIdRemoto;
import net.gaia.vortex.tests.router2.simulador.pasos.bidi.ResponderIdRemoto;
import net.gaia.vortex.tests.router2.simulador.pasos.filtros.PublicarFiltros;
import net.gaia.vortex.tests.router2.simulador.pasos.mensajes.PropagarMensaje;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ar.com.dgarcia.coding.exceptions.UnhandledConditionException;
import ar.com.dgarcia.lang.strings.ToString;

/**
 * Esta clase representa una pata conectora vista desde el nodo que la posee
 * 
 * @author D. García
 */
public class PataBidireccional extends ComponenteSimulable implements PataConectora {
	private static final Logger LOG = LoggerFactory.getLogger(PataBidireccional.class);

	private static final AtomicLong proximoId = new AtomicLong(0);

	private Long idLocal;
	public static final String idLocal_FIELD = "idLocal";

	private Long idRemoto;
	public static final String idRemoto_FIELD = "idRemoto";

	private NodoSimulacion nodoLocal;
	public static final String nodoLocal_FIELD = "nodoLocal";

	private NodoSimulacion nodoRemoto;
	public static final String nodoRemoto_FIELD = "nodoRemoto";

	private Filtro filtroDeSalida;
	public static final String filtroDeSalida_FIELD = "filtroDeSalida";

	private Filtro filtroDeEntrada;
	public static final String filtroDeEntrada_FIELD = "filtroDeEntrada";

	private Filtro filtroDeEntradaPublicado;
	public static final String filtroDeEntradaPublicado_FIELD = "filtroDeEntradaPublicado";

	private List<Mensaje> enviados;
	public static final String enviados_FIELD = "enviados";

	private ListenerDeFiltros listener;

	public static PataBidireccional create(final NodoSimulacion nodoLocal, final NodoSimulacion nodoRemoto,
			final Simulador simulador) {
		final PataBidireccional pata = new PataBidireccional();
		pata.setIdLocal(proximoId.getAndIncrement());
		pata.setSimulador(simulador);
		pata.nodoLocal = nodoLocal;
		pata.nodoRemoto = nodoRemoto;
		pata.filtroDeSalida = FiltroPasaTodo.create();
		pata.filtroDeEntrada = FiltroPasaTodo.create();
		pata.filtroDeEntradaPublicado = FiltroPasaTodo.create();
		pata.listener = ListenerDeFiltrosNulo.create();
		return pata;
	}

	/**
	 * Inicia el proceso de pedido de id remoto
	 */
	public void conseguirIdRemoto() {
		final PedidoDeIdRemoto pedidoDeId = PedidoDeIdRemoto.create(getIdLocal());
		enviarMensaje(pedidoDeId);
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return ToString.de(this).con(idLocal_FIELD, getIdLocal()).con(nodoLocal_FIELD, nodoLocal)
				.con(filtroDeEntrada_FIELD, filtroDeEntrada)
				.con(filtroDeEntradaPublicado_FIELD, filtroDeEntradaPublicado).con(idRemoto_FIELD, idRemoto)
				.con(nodoRemoto_FIELD, nodoRemoto.getNombre()).con(filtroDeSalida_FIELD, filtroDeSalida).toString();
	}

	public NodoSimulacion getNodoLocal() {
		return nodoLocal;
	}

	public void setNodoLocal(final NodoSimulacion nodoLocal) {
		this.nodoLocal = nodoLocal;
	}

	/**
	 * Procesa el mensaje recibido mandándolo a destino si corresponde. Para determinarlo aplica
	 * filtros y evalua metamensajes. Se agrega el id remoto al mensaje antes de enviarlo para
	 * evitar rebotes si es posible
	 * 
	 * @param mensaje
	 *            El mensaje a procesar y posiblemente enviar a destino
	 */
	public void derivar(final Mensaje mensaje) {
		final MensajeNormal mensajeNormal = interpretarMetamensaje(mensaje);
		if (mensajeNormal == null) {
			// Ya fue procesado como metamensaje
			return;
		}
		// Es un mensaje común deberíamos enviarlo al nodo remoto
		enviarMensajeNormal(mensajeNormal);
	}

	/**
	 * Intenta procesar el mensaje pasado como metamensaje conocido
	 * 
	 * @param mensaje
	 *            El mensaje a procesar
	 * @return Un mensaje normal si no es un metamensaje
	 */
	private MensajeNormal interpretarMetamensaje(final Mensaje mensaje) {
		if (mensaje instanceof PedidoDeIdRemoto) {
			evento_recibirPedidoDeIdRemoto((PedidoDeIdRemoto) mensaje);
			return null;
		} else if (mensaje instanceof RespuestaDeIdRemoto) {
			evento_recibirRespuestaDeIdRemoto((RespuestaDeIdRemoto) mensaje);
			return null;
		} else if (mensaje instanceof ConfirmacionDeIdRemoto) {
			evento_recibirConfirmacionDeIdRemoto((ConfirmacionDeIdRemoto) mensaje);
			return null;
		} else if (mensaje instanceof PublicacionDeFiltros) {
			evento_recibirPublicacionDeFiltros((PublicacionDeFiltros) mensaje);
			return null;
		} else if (mensaje instanceof MensajeNormal) {
			return (MensajeNormal) mensaje;
		}
		throw new UnhandledConditionException("El mensaje no es de ninguno de los tipos esperados?!");
	}

	/**
	 * Invocado al recibir una publicacion de filtros
	 */
	private void evento_recibirPublicacionDeFiltros(final PublicacionDeFiltros publicacion) {
		final Long idDePataReceptora = publicacion.getIdDePataLocalAlReceptor();
		if (!getIdLocal().equals(idDePataReceptora)) {
			LOG.debug("  Rechazando en [{},{}] publicacion{} por ser para otra pata", new Object[] {
					this.getNodoLocal().getNombre(), getIdLocal(), publicacion });
			return;
		}
		final Filtro nuevoFiltro = publicacion.getFiltro();
		setFiltroDeSalida(nuevoFiltro);
		LOG.debug("  En [{},{}] solo se enviaran mensajes a [{}] que cumplan el filtro{}: {}", new Object[] {
				this.getNodoLocal().getNombre(), getIdLocal(), getNodoRemoto().getNombre(), publicacion, nuevoFiltro });
		evento_cambioDeFiltroRemoto(nuevoFiltro);
	}

	/**
	 * Invocado al producirse un cambio de estado en el filtro externo (de salida)de esta pata
	 * 
	 * @param nuevoFiltro
	 *            El filtro actualizado
	 */
	private void evento_cambioDeFiltroRemoto(final Filtro nuevoFiltro) {
		listener.onCambioDeFiltro(nuevoFiltro);
	}

	/**
	 * Invocado al recibir una confirmación de id remoto
	 */
	private void evento_recibirConfirmacionDeIdRemoto(final ConfirmacionDeIdRemoto confirmacion) {
		final RespuestaDeIdRemoto respuestaOriginal = confirmacion.getRespuesta();
		final Long idDePataEmisora = respuestaOriginal.getIdDePataLocalAlEmisor();
		if (!getIdLocal().equals(idDePataEmisora)) {
			LOG.debug("  Rechazando en [{},{}] confirmacion{} por respuesta{} que fue emitida en otra pata",
					new Object[] { this.getNodoLocal().getNombre(), getIdLocal(), confirmacion, respuestaOriginal });
			return;
		}
		if (!getEnviados().contains(respuestaOriginal)) {
			LOG.debug("  Rechazando en [{},{}] confirmacion{} por respuesta{} no realizada en esta pata", new Object[] {
					this.getNodoLocal().getNombre(), getIdLocal(), confirmacion, respuestaOriginal });
			return;
		}

		final Long idRemotoDeEstaPata = confirmacion.getIdDePataLocalAlEmisor();
		this.setIdRemoto(idRemotoDeEstaPata);

		evento_nevaConexionBidireccional();
	}

	/**
	 * Invocado al recibir una respuesta de ID remoto
	 */
	private void evento_recibirRespuestaDeIdRemoto(final RespuestaDeIdRemoto respuesta) {
		final PedidoDeIdRemoto pedidoOriginal = respuesta.getPedido();
		final Long idDePataEmisora = pedidoOriginal.getIdDePataLocalAlEmisor();
		if (!getIdLocal().equals(idDePataEmisora)) {
			LOG.debug("  Rechazando en [{},{}] respuesta{} por pedido{} que fue emitido en otra", new Object[] {
					this.getNodoLocal().getNombre(), getIdLocal(), respuesta, pedidoOriginal });
			return;
		}
		if (!getEnviados().contains(pedidoOriginal)) {
			LOG.debug("  Rechazando en [{},{}] respuesta{} por pedido{} no realizado en esta pata", new Object[] {
					this.getNodoLocal().getNombre(), getIdLocal(), respuesta, pedidoOriginal });
			return;
		}

		final Long idRemotoDeEstaPata = respuesta.getIdDePataLocalAlEmisor();
		this.setIdRemoto(idRemotoDeEstaPata);

		final ConfirmacionDeIdRemoto confirmacionDeIdRemoto = ConfirmacionDeIdRemoto.create(respuesta, getIdLocal());
		enviarMensaje(confirmacionDeIdRemoto);

		evento_nevaConexionBidireccional();
	}

	/**
	 * Invocado al completarse los pasos para la comunicación bidireccional de esta pata
	 */
	private void evento_nevaConexionBidireccional() {
		publicarFiltroDeEntrada();
	}

	/**
	 * Invocado al recibir un pedido de de id de otro nodo
	 */
	private void evento_recibirPedidoDeIdRemoto(final PedidoDeIdRemoto pedido) {
		// Esta pata responderá con su ID
		final RespuestaDeIdRemoto respuestaDeIdRemoto = RespuestaDeIdRemoto.create(pedido, getIdLocal());
		enviarMensaje(respuestaDeIdRemoto);
	}

	public Filtro getFiltroDeSalida() {
		return filtroDeSalida;
	}

	public void setFiltroDeSalida(final Filtro filtroDeSalida) {
		this.filtroDeSalida = filtroDeSalida;
	}

	/**
	 * Intenta enviar el mensaje normal pasado sólo si y no lo envío antes y si supera el filtro de
	 * salida
	 * 
	 * @param mensaje
	 *            El mensaje a enviar
	 */
	public void enviarMensajeNormal(final MensajeNormal mensaje) {
		final Long idDePataOrigen = mensaje.getIdDePataLocalAlReceptor();
		if (this.getIdLocal().equals(idDePataOrigen)) {
			LOG.debug("  Evitando enviar desde [{},{}] el mensaje[{}] porque provino de esa pata", new Object[] {
					this.getNodoLocal().getNombre(), getIdLocal(), mensaje.getIdDeMensaje() });
			return;
		}
		if (!getFiltroDeSalida().aceptaA(mensaje)) {
			LOG.debug("  Evitando enviar desde [{},{}] el mensaje[{}] porque su filtro no lo admite: {}", new Object[] {
					this.getNodoLocal().getNombre(), this.getIdLocal(), mensaje.getIdDeMensaje(), getFiltroDeSalida() });
			return;
		}

		// Antes de enviar decimos por qué pata va, para evitar rebote
		final MensajeNormal mensajeAdaptadoAlReceptor = mensaje.clonar();
		final Long idRemotoDePataConductora = getIdRemoto();
		mensajeAdaptadoAlReceptor.setIdDePataLocalAlReceptor(idRemotoDePataConductora);

		enviarMensaje(mensajeAdaptadoAlReceptor);
	}

	/**
	 * Envia el mensaje indicado, sólo si no fue enviado antes
	 * 
	 * @param mensaje
	 *            El mensaje a enviar
	 */
	private void enviarMensaje(final Mensaje mensaje) {
		if (getEnviados().contains(mensaje)) {
			LOG.debug("  Rechazando mensaje[{}] recibido en [{}] por figurar como ya enviado",
					mensaje.getIdDeMensaje(), this.getNodoLocal().getNombre());
			return;
		}
		// Nunca lo mandamos antes, se lo mandamos
		getEnviados().add(mensaje);

		generarElPasoDeSimuladorEspecifico(mensaje);
	}

	/**
	 * Genera un paso de acuerdo al tipo de mensaje.<br>
	 * Este método sólo es necesario para que se vea bonito el log diferenciando cada caso
	 */
	private void generarElPasoDeSimuladorEspecifico(final Mensaje mensaje) {
		PasoSimulacion pasoDelSimulador;

		if (mensaje instanceof PedidoDeIdRemoto) {
			pasoDelSimulador = PedirIdRemoto.create(this, (PedidoDeIdRemoto) mensaje);
		} else if (mensaje instanceof RespuestaDeIdRemoto) {
			pasoDelSimulador = ResponderIdRemoto.create(this, (RespuestaDeIdRemoto) mensaje);
		} else if (mensaje instanceof ConfirmacionDeIdRemoto) {
			pasoDelSimulador = ConfirmarIdRemoto.create(this, (ConfirmacionDeIdRemoto) mensaje);
		} else if (mensaje instanceof PublicacionDeFiltros) {
			pasoDelSimulador = PublicarFiltros.create(this, (PublicacionDeFiltros) mensaje);
		} else if (mensaje instanceof MensajeNormal) {
			pasoDelSimulador = PropagarMensaje.create(this, (MensajeNormal) mensaje);
		} else {
			throw new UnhandledConditionException(
					"No se puede crear el paso especifico para un tipo de mensaje desconocido");
		}

		procesar(pasoDelSimulador);
	}

	public List<Mensaje> getEnviados() {
		if (enviados == null) {
			enviados = new ArrayList<Mensaje>();
		}
		return enviados;
	}

	protected void setIdLocal(final Long id) {
		this.idLocal = id;
	}

	public Long getIdLocal() {
		return idLocal;
	}

	public Long getIdRemoto() {
		return idRemoto;
	}

	public void setIdRemoto(final Long idRemoto) {
		this.idRemoto = idRemoto;
	}

	public void setNodoRemoto(final NodoSimulacion nodoRemoto) {
		this.nodoRemoto = nodoRemoto;
	}

	public NodoSimulacion getNodoRemoto() {
		return nodoRemoto;
	}

	/**
	 * Cambia el filtro actual de entrada
	 * 
	 * @param nuevoFiltro
	 *            El nuevo filtro a utilizar para recibir los mensajes desde esta pata
	 */
	public void actualizarFiltroDeEntrada(final Filtro nuevoFiltro) {
		this.setFiltroDeEntrada(nuevoFiltro);
		publicarFiltroDeEntrada();
	}

	/**
	 * Publica el filtro actual de entrada sólo si es distinto del publicado previamente
	 */
	private void publicarFiltroDeEntrada() {
		final Filtro filtroAPublicar = this.getFiltroDeEntrada();
		final Long idRemotoDeEstaPata = getIdRemoto();
		if (idRemotoDeEstaPata == null) {
			LOG.debug("  En [{},{}] no se publica filtro {} porque no tenemos ID remoto", new Object[] {
					this.getNodoLocal().getNombre(), this.getIdLocal(), filtroAPublicar });
			// Aún no tenemos un ID remoto con el cual publicar los filtros
			return;
		}
		if (filtroAPublicar.equals(this.getFiltroDeEntradaPublicado())) {
			LOG.debug("  En [{},{}] no se publica porque no hubo cambio de filtro de entrada: {}", new Object[] {
					this.getNodoLocal().getNombre(), this.getIdLocal(), filtroAPublicar });
			return;
		}
		LOG.debug("  En [{},{}] se publica el cambio de filtro de entrada de {} a: {}", new Object[] {
				this.getNodoLocal().getNombre(), this.getIdLocal(), getFiltroDeEntradaPublicado(), filtroAPublicar });

		setFiltroDeEntradaPublicado(filtroAPublicar);
		final PublicacionDeFiltros publicacion = PublicacionDeFiltros.create(filtroAPublicar, idRemotoDeEstaPata);
		enviarMensaje(publicacion);
	}

	public Filtro getFiltroDeEntrada() {
		return filtroDeEntrada;
	}

	public void setFiltroDeEntrada(final Filtro filtroDeEntrada) {
		this.filtroDeEntrada = filtroDeEntrada;
	}

	public Filtro getFiltroDeEntradaPublicado() {
		return filtroDeEntradaPublicado;
	}

	public void setFiltroDeEntradaPublicado(final Filtro filtroDeEntradaPublicado) {
		this.filtroDeEntradaPublicado = filtroDeEntradaPublicado;
	}

	public ListenerDeFiltros getListener() {
		return listener;
	}

	public void setListener(final ListenerDeFiltros listener) {
		this.listener = listener;
	}

}
