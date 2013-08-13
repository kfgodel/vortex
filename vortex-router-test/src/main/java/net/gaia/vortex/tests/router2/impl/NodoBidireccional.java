/**
 * 08/12/2012 14:03:03 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.tests.router2.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.gaia.vortex.tests.router2.api.ListenerDeFiltros;
import net.gaia.vortex.tests.router2.api.Mensaje;
import net.gaia.vortex.tests.router2.api.Nodo;
import net.gaia.vortex.tests.router2.impl.filtros.Filtro;
import net.gaia.vortex.tests.router2.impl.filtros.FiltroPasaNada;
import net.gaia.vortex.tests.router2.impl.filtros.ListenerDeFiltrosNulo;
import net.gaia.vortex.tests.router2.impl.patas.PataBidireccional;
import net.gaia.vortex.tests.router2.mensajes.MensajeNormal;
import net.gaia.vortex.tests.router2.simulador.NodoSimulacion;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ar.com.dgarcia.lang.strings.ToString;

/**
 * Esta clase representa el comporamiento base de un nodo que es bidireccional en sus
 * comunicaciones.<br>
 * A diferencia de los viejos nodos, este puede determinar con quien intercambia mensajes
 * 
 * @author D. García
 */
public abstract class NodoBidireccional extends ComponenteNodo implements ListenerDeFiltros {
	private static final Logger LOG = LoggerFactory.getLogger(NodoBidireccional.class);

	private List<PataBidireccional> patas;
	public static final String patas_FIELD = "patas";

	private Filtro ultimoFiltroNotificadoAlListener;

	private ListenerDeFiltros listenerDeFiltros = ListenerDeFiltrosNulo.create();

	/**
	 * @see net.gaia.vortex.tests.router2.api.Nodo#conectarCon(net.gaia.vortex.tests.router2.api.Nodo)
	 */
	public void conectarCon(final Nodo otro) {
		final PataBidireccional nuevaPata = PataBidireccional.create(this, (NodoSimulacion) otro, getSimulador());
		nuevaPata.setListener(this);
		getPatas().add(nuevaPata);
		nuevaPata.conseguirIdRemoto();
		evento_cambioEstadoFiltrosRemotos();
	}

	/**
	 * Invocado cuando cambian los filtros externos de las patas conectoras.<br>
	 * Cuando cambia lo que los nodos remotos quieren.<br>
	 * En todos los nodos notificamos al listener del estado global
	 */
	protected void evento_cambioEstadoFiltrosRemotos() {
		// Obtenemos el filtro unificado de todas las patas sin excepcion (una sola por ser portal)
		final Filtro filtroUnificadoRemoto = mergearFiltrosDePatasExcluyendoA(null);
		if (filtroUnificadoRemoto.equals(ultimoFiltroNotificadoAlListener)) {
			// No es necesario notificar porque no hubo cambio de estado en el filtro
			return;
		}
		// Es un filtro distinto del que informamos la ultima vez
		ultimoFiltroNotificadoAlListener = filtroUnificadoRemoto;
		this.listenerDeFiltros.onCambioDeFiltro(filtroUnificadoRemoto);

		// Al cambiar los filtros remotos es posible que cambien los locales
		evento_cambioEstadoFiltrosLocales();
	}

	/**
	 * @see net.gaia.vortex.tests.router2.api.Nodo#desconectarDe(net.gaia.vortex.tests.router2.api.Nodo)
	 */
	public void desconectarDe(final Nodo destino) {
		final PataBidireccional pataDesconectada = getPataPorNodo(destino);
		if (pataDesconectada == null) {
			LOG.debug("En [{}] no se puede quitar la pata correspondiente al nodo[{}] porque no existe",
					this.getNombre(), destino);
			return;
		}
		getPatas().remove(pataDesconectada);
		evento_cambioEstadoFiltrosRemotos();
	}

	public List<PataBidireccional> getPatas() {
		if (patas == null) {
			patas = new ArrayList<PataBidireccional>();
		}
		return patas;
	}

	protected PataBidireccional getPataPorIdLocal(final Long idLocal) {
		for (final PataBidireccional pataSalida : getPatas()) {
			final Long idDePata = pataSalida.getIdLocal();
			if (idDePata.equals(idLocal)) {
				return pataSalida;
			}
		}
		return null;
	}

	/**
	 * Devuelve la pata de salida identificándola por nodo
	 * 
	 * @param nodo
	 *            El nodo cuya pata quiere obtenerse
	 * @return La pata del nodo o null si no existe
	 */
	protected PataBidireccional getPataPorNodo(final Nodo nodo) {
		final List<PataBidireccional> allDestinos = getPatas();
		for (final PataBidireccional pataConectora : allDestinos) {
			if (pataConectora.getNodoRemoto().equals(nodo)) {
				return pataConectora;
			}
		}
		return null;
	}

	/**
	 * @see net.gaia.vortex.tests.router2.impl.ComponenteNodo#evento_recibirMensajeEnNodo(net.gaia.vortex.tests.router2.api.Mensaje)
	 */
	@Override
	protected void evento_recibirMensajeEnNodo(final Mensaje mensaje) {
		// Si es un metamensaje no es procesable por los handlers
		if (mensaje instanceof MensajeNormal) {
			procesarConHandlersInternos(mensaje);
		}

		propagarAPatas(mensaje);

	}

	/**
	 * Envía el mensaje indicado a cada una de las patas para que lo procesen.<br>
	 * Cada pata decidirá si enviarlo o no
	 * 
	 * @param mensaje
	 *            El mensaje que se intentará enviar por cada pata.
	 */
	private void propagarAPatas(final Mensaje mensaje) {
		final List<PataBidireccional> allPatas = getPatas();
		if (allPatas.isEmpty()) {
			LOG.debug("  Rechazando mensaje {}{} en [{}] porque no existen patas de comunicacion para procesar",
					new Object[] { mensaje.getClass().getSimpleName(), mensaje, this.getNombre() });
			return;
		}

		for (final PataBidireccional pataLocal : allPatas) {
			pataLocal.derivar(mensaje);
		}
	}

	/**
	 * Procesa el mensaje internamente en el nodo según el tipo de nodo
	 * 
	 * @param mensaje
	 *            El mensaje recibido
	 */
	protected abstract void procesarConHandlersInternos(Mensaje mensaje);

	/**
	 * Invocado cuando cambian los filtros internos del nodo, y por lo tanto se debe notificar a los
	 * nodos remotos
	 */
	protected void evento_cambioEstadoFiltrosLocales() {
		final List<PataBidireccional> allPatas = getPatas();
		for (final PataBidireccional pataConectora : allPatas) {
			final Filtro nuevoFiltro = calcularFiltroDeEntradaPara(pataConectora);
			pataConectora.actualizarFiltroDeEntrada(nuevoFiltro);
		}
	}

	/**
	 * Se calcula el filtro que correspondería a la pata pasada de acuerdo al tipo de esta
	 * instancia.<br>
	 * En el caso del portal el filtro es fijo. En el caso del router es la suma de los filtros de
	 * otras patas
	 * 
	 * @param pataConectora
	 *            La pata para la que se pide el calculo
	 * @return El filtro correspondiente a la pata
	 */
	protected abstract Filtro calcularFiltroDeEntradaPara(PataBidireccional pataConectora);

	/**
	 * Calcula un filtro unificado resultate de los filtros de todas las patas menos la indicada
	 * 
	 * @param pataExcluida
	 *            La pata a excluir
	 * @return El filtro unificado
	 */
	protected Filtro mergearFiltrosDePatasExcluyendoA(final PataBidireccional pataExcluida) {
		Filtro filtroResultante = FiltroPasaNada.create();
		final List<PataBidireccional> allPatas = getPatas();
		for (final PataBidireccional pataConectora : allPatas) {
			if (pataConectora.equals(pataExcluida)) {
				// No queremos inlcuirla!
				continue;
			}
			final Filtro filtroDePata = pataConectora.getFiltroDeSalida();
			if (filtroResultante == null) {
				// Es la primera iteracion
				filtroResultante = filtroDePata;
			} else {
				filtroResultante = filtroResultante.mergearCon(filtroDePata);
			}
		}
		return filtroResultante;
	}

	/**
	 * @see net.gaia.vortex.tests.router2.api.ListenerDeFiltros#onCambioDeFiltro(net.gaia.vortex.tests.router2.impl.filtros.Filtro)
	 */
	public void onCambioDeFiltro(final Filtro nuevoFiltro) {
		// Cuando cambia una pata es un cambio de filtro remoto
		evento_cambioEstadoFiltrosRemotos();
	}

	/**
	 * @see net.gaia.vortex.tests.router2.simulador.NodoSimulacion#tieneComoDestinoA(net.gaia.vortex.tests.router2.api.Nodo)
	 */
	public boolean tieneComoDestinoA(final Nodo otro) {
		final PataBidireccional pataConectora = getPataPorNodo(otro);
		final boolean tieneConexionConDestino = pataConectora != null;
		return tieneConexionConDestino;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return ToString.de(this).con(nombre_FIELD, getNombre()).con(patas_FIELD, getPatas()).toString();
	}

	/**
	 * @see net.gaia.vortex.tests.router2.simulador.NodoSimulacion#usaFiltrosCon(net.gaia.vortex.tests.router2.api.Nodo,
	 *      java.lang.String[])
	 */
	public boolean usaFiltrosCon(final Nodo nodo, final String... filtros) {
		final Filtro filtroDelNodo = getFiltroDeSalidaPara(nodo);
		if (filtroDelNodo == null) {
			// Si no tiene filtro, no esta aplicando los pasados
			return false;
		}
		final boolean usaFiltro = filtroDelNodo.usaA(Arrays.asList(filtros));
		return usaFiltro;
	}

	/**
	 * @see net.gaia.vortex.tests.router2.simulador.NodoSimulacion#getFiltroDeSalidaPara(net.gaia.vortex.tests.router2.api.Nodo)
	 */
	public Filtro getFiltroDeSalidaPara(final Nodo nodo) {
		final PataBidireccional pataSalida = getPataPorNodo(nodo);
		if (pataSalida == null) {
			return null;
		}
		return pataSalida.getFiltroDeSalida();
	}

	/**
	 * @see net.gaia.vortex.tests.router2.simulador.NodoSimulacion#yaProceso(net.gaia.vortex.tests.router2.mensajes.MensajeNormal)
	 */
	public boolean yaProceso(final MensajeNormal mensaje) {
		final boolean yaSeProceso = getRecibidos().contains(mensaje);
		return yaSeProceso;
	}

	/**
	 * @see net.gaia.vortex.tests.router2.api.Nodo#setListenerDeFiltrosExternos(net.gaia.vortex.tests.router2.api.ListenerDeFiltros)
	 */
	public void setListenerDeFiltrosExternos(final ListenerDeFiltros listenerDeExternos) {
		this.listenerDeFiltros = listenerDeExternos;
	}

}
