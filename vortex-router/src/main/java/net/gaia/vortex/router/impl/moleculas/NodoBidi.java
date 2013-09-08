/**
 * 22/12/2012 18:59:31 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.router.impl.moleculas;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicReference;

import net.gaia.taskprocessor.api.processor.TaskProcessor;
import net.gaia.vortex.api.basic.Receptor;
import net.gaia.vortex.api.condiciones.Condicion;
import net.gaia.vortex.api.ids.componentes.IdDeComponenteVortex;
import net.gaia.vortex.api.mensajes.MensajeVortex;
import net.gaia.vortex.core.prog.Loggers;
import net.gaia.vortex.deprecated.FlujoInmutableViejo;
import net.gaia.vortex.deprecated.FlujoVortexViejo;
import net.gaia.vortex.deprecated.NodoMoleculaSupportViejo;
import net.gaia.vortex.deprecated.trans.GenerarIdEnMensajeViejo;
import net.gaia.vortex.portal.impl.conversion.api.ConversorDeMensajesVortex;
import net.gaia.vortex.portal.impl.conversion.impl.ConversorDefaultDeMensajes;
import net.gaia.vortex.router.api.listeners.ListenerDeCambiosDeFiltro;
import net.gaia.vortex.router.api.listeners.ListenerDeRuteo;
import net.gaia.vortex.router.api.moleculas.NodoBidireccional;
import net.gaia.vortex.router.impl.filtros.ConjuntoDeCondiciones;
import net.gaia.vortex.router.impl.filtros.ConjuntoSincronizado;
import net.gaia.vortex.router.impl.filtros.ListenerDeConjuntoDeCondiciones;
import net.gaia.vortex.router.impl.filtros.ParteDeCondiciones;
import net.gaia.vortex.router.impl.listeners.IgnorarCambioDeFiltro;
import net.gaia.vortex.router.impl.listeners.IgnorarRuteos;
import net.gaia.vortex.router.impl.moleculas.comport.ComportamientoBidi;
import net.gaia.vortex.router.impl.moleculas.listeners.ListenerDeConexionesBidiEnNodo;
import net.gaia.vortex.router.impl.moleculas.patas.ListenerConexionBidiEnPata;
import net.gaia.vortex.router.impl.moleculas.patas.PataBidi;
import net.gaia.vortex.router.impl.moleculas.patas.PataBidireccional;
import net.gaia.vortex.router.impl.moleculas.support.ListenerNuloDeConexionBidiEnNodo;
import net.gaia.vortex.sets.impl.serializacion.SerializadorDeCondiciones;
import net.gaia.vortex.sets.impl.serializacion.impl.SerializadorDeCondicionesImpl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ar.com.dgarcia.coding.exceptions.FaultyCodeException;
import ar.com.dgarcia.lang.strings.ToString;

/**
 * Esta clase base definir comportamiento comun de los nodos bidireccionales
 * 
 * @author D. García
 */
public abstract class NodoBidi extends NodoMoleculaSupportViejo implements NodoBidireccional,
		ListenerDeConjuntoDeCondiciones, ListenerConexionBidiEnPata {
	private static final Logger LOG = LoggerFactory.getLogger(NodoBidi.class);

	private ConjuntoDeCondiciones conjuntoDeCondiciones;

	private List<PataBidireccional> patas;
	public static final String patas_FIELD = "patas";

	private AtomicReference<ListenerDeCambiosDeFiltro> listenerDeFiltros;
	private AtomicReference<ListenerDeRuteo> listenerDeRuteo;

	private ConversorDeMensajesVortex mapeador;

	private FlujoVortexViejo flujoDeMensajesRecibidos;

	private IdDeComponenteVortex identificador;
	public static final String identificador_FIELD = "identificador";

	private GenerarIdEnMensajeViejo generadorDeIds;

	private SerializadorDeCondiciones serializador;

	private TaskProcessor processor;

	private ListenerDeConexionesBidiEnNodo listenerDeConexiones;

	public TaskProcessor getProcessor() {
		return processor;
	}

	public List<PataBidireccional> getPatas() {
		return patas;
	}

	public void setPatas(final List<PataBidireccional> patas) {
		this.patas = patas;
	}

	/**
	 * @see net.gaia.vortex.deprecated.ComponenteConProcesadorSupport#initializeWith(net.gaia.taskprocessor.api.TaskProcessor)
	 */
	protected void initializeWith(final TaskProcessor processor, final ComportamientoBidi comportamiento) {
		this.processor = processor;
		this.listenerDeConexiones = ListenerNuloDeConexionBidiEnNodo.getInstancia();
		generadorDeIds = comportamiento.obtenerGeneradorDeIdParaMensajes();
		identificador = generadorDeIds.getIdDeComponente();
		patas = new CopyOnWriteArrayList<PataBidireccional>();
		listenerDeFiltros = new AtomicReference<ListenerDeCambiosDeFiltro>(IgnorarCambioDeFiltro.getInstancia());
		listenerDeRuteo = new AtomicReference<ListenerDeRuteo>(IgnorarRuteos.getInstancia());
		conjuntoDeCondiciones = ConjuntoSincronizado.create(this);
		mapeador = ConversorDefaultDeMensajes.create();
		serializador = SerializadorDeCondicionesImpl.create();

		// El flujo que se usará
		flujoDeMensajesRecibidos = comportamiento.crearFlujoParaMensajesRecibidos(processor);

		// La entrada es lo que haya definido el comportamiento y como salida usamos esta propio
		// componente
		final FlujoVortexViejo flujoInterno = FlujoInmutableViejo.create(flujoDeMensajesRecibidos.getEntrada(), this);
		initializeWith(flujoInterno);
	}

	/**
	 * @see net.gaia.vortex.deprecated.EmisorViejo#conectarCon(net.gaia.vortex.api.basic.Receptor)
	 */

	@Override
	public void conectarCon(final Receptor destino) {
		if (destino == null) {
			throw new IllegalArgumentException("El destino del nodo no puede ser null");
		}
		final PataBidireccional pataExistente = getPataPorNodo(destino);
		if (pataExistente != null) {
			throw new FaultyCodeException("No se puede conectar el nodo[" + this + "] al destino[" + destino
					+ "] por segunda vez");
		}

		// Creamos la parte que nos permite conocer el filtro de cada pata
		final Condicion filtroDeEntradaParaPataNueva = calcularFiltroDeEntradaPara(null);

		// Chequeo por debug para evitar el costo de toShortString()
		if (LOG.isDebugEnabled()) {
			LOG.debug("Conectando bidi desde[{}] a [{}]", this.toShortString(), destino.toShortString());
		}
		final PataBidi nuevaPata = PataBidi.create(this, destino, getProcessor(), conjuntoDeCondiciones,
				filtroDeEntradaParaPataNueva, mapeador, generadorDeIds, serializador, listenerDeRuteo);
		nuevaPata.setListenerConexionBidi(this);
		getPatas().add(nuevaPata);

		// Agregamos la pata al conjunto que puede recibir mensajes
		flujoDeMensajesRecibidos.getSalida().conectarCon(nuevaPata);
		// Chequeo por debug para evitar el costo de toShortString()
		if (LOG.isDebugEnabled()) {
			LOG.debug("Pata[{}] creada en [{}] para conectar con [{}]",
					new Object[] { nuevaPata.toShortString(), this.toShortString(), destino.toShortString() });
		}

		// Iniciamos el proceso de identificación bidireccional de la pata
		nuevaPata.conseguirIdRemoto();
	}

	/**
	 * @see net.gaia.vortex.deprecated.EmisorViejo#desconectarDe(net.gaia.vortex.api.basic.Receptor)
	 */

	@Override
	public void desconectarDe(final Receptor destino) {
		final PataBidireccional pataDesconectada = getPataPorNodo(destino);
		if (pataDesconectada == null) {
			LOG.debug("En [{}] no se puede quitar la pata correspondiente al nodo[{}] porque no existe", this, destino);
			return;
		}
		// La quitamos del grupo que recibe mensajes
		flujoDeMensajesRecibidos.getSalida().desconectarDe(pataDesconectada);

		getPatas().remove(pataDesconectada);
		final ParteDeCondiciones parteDeCondicion = pataDesconectada.getParteDeCondicion();
		conjuntoDeCondiciones.eliminarParte(parteDeCondicion);
	}

	/**
	 * Devuelve la pata de salida identificándola por nodo
	 * 
	 * @param nodo
	 *            El nodo cuya pata quiere obtenerse
	 * @return La pata del nodo o null si no existe
	 */
	protected PataBidireccional getPataPorNodo(final Receptor nodo) {
		final List<PataBidireccional> allDestinos = getPatas();
		for (final PataBidireccional pataConectora : allDestinos) {
			if (pataConectora.tieneComoNodoRemotoA(nodo)) {
				return pataConectora;
			}
		}
		return null;
	}

	/**
	 * @see net.gaia.vortex.api.basic.Receptor#recibir(net.gaia.vortex.api.mensajes.MensajeVortex)
	 */

	@Override
	public void recibir(final MensajeVortex mensaje) {
		if (Loggers.BIDI_MSG.isDebugEnabled()) {
			Loggers.BIDI_MSG.debug("Recibido en[{}] el mensaje[{}]",
					new Object[] { this.toShortString(), mensaje.toShortString() });
		}
		super.recibir(mensaje);
	}

	/**
	 * @see net.gaia.vortex.router.api.moleculas.NodoBidireccional#setListenerDeFiltrosRemotos(net.gaia.vortex.router.api.listeners.ListenerDeCambiosDeFiltro)
	 */

	public void setListenerDeFiltrosRemotos(final ListenerDeCambiosDeFiltro listenerDeFiltros) {
		if (listenerDeFiltros == null) {
			throw new IllegalArgumentException("El listener de filtros no puede ser null. A lo sumo una instancia de "
					+ IgnorarCambioDeFiltro.class);
		}
		this.listenerDeFiltros.set(listenerDeFiltros);
	}

	/**
	 * @see net.gaia.vortex.router.api.moleculas.NodoBidireccional#setListenerDeRuteos(net.gaia.vortex.router.api.listeners.ListenerDeRuteo)
	 */

	public void setListenerDeRuteos(final ListenerDeRuteo listenerDeRuteos) {
		if (listenerDeRuteos == null) {
			throw new IllegalArgumentException("El listener de ruteo no puede ser null. A lo sumo una instancia de "
					+ IgnorarCambioDeFiltro.class);
		}
		this.listenerDeRuteo.set(listenerDeRuteos);
	}

	/**
	 * @see net.gaia.vortex.router.impl.filtros.ListenerDeConjuntoDeCondiciones#onCambioDeCondicionEn(net.gaia.vortex.router.impl.filtros.ConjuntoDeCondiciones,
	 *      net.gaia.vortex.api.condiciones.Condicion)
	 */

	public void onCambioDeCondicionEn(final ConjuntoDeCondiciones conjunto, final Condicion nuevaCondicion) {
		// Chequeo por debug para evitar el costo de toShortString()
		if (Loggers.BIDI_MSG.isInfoEnabled()) {
			Loggers.BIDI_MSG.info("El nodo[{}] cambio su estado de filtros remotos a[{}]", this.toShortString(),
					nuevaCondicion);
		}

		final ListenerDeCambiosDeFiltro listenerActual = listenerDeFiltros.get();
		try {
			listenerActual.onCambioDeFiltros(this, nuevaCondicion);
		}
		catch (final Exception e) {
			LOG.error("Se produjo un error en el listener[" + listenerActual + "] de cambios de filtros de este nodo["
					+ this + "]", e);
		}

		// Cambios en filtros externos también pueden implicar cambios en filtros locales
		evento_cambioEstadoFiltrosLocales();
	}

	/**
	 * Devuelve todos los nodos destinos a los que este nodo esta conectado
	 * 
	 * @return La lista de destinos conectados
	 */
	protected List<Receptor> getDestinos() {
		final List<PataBidireccional> allPatas = getPatas();
		final ArrayList<Receptor> destinos = new ArrayList<Receptor>(allPatas.size());
		for (final PataBidireccional pata : allPatas) {
			final Receptor destino = pata.getNodoRemoto();
			destinos.add(destino);
		}
		return destinos;
	}

	/**
	 * Invocado cuando cambian los filtros internos del nodo, y por lo tanto se debe notificar a los
	 * nodos remotos
	 */
	protected void evento_cambioEstadoFiltrosLocales() {
		final List<PataBidireccional> allPatas = getPatas();
		for (final PataBidireccional pataConectora : allPatas) {
			final Condicion nuevoFiltro = calcularFiltroDeEntradaPara(pataConectora);
			pataConectora.actualizarFiltroDeEntrada(nuevoFiltro);
		}
	}

	public ConjuntoDeCondiciones getConjuntoDeCondiciones() {
		return conjuntoDeCondiciones;
	}

	public void setConjuntoDeCondiciones(final ConjuntoDeCondiciones conjuntoDeCondiciones) {
		this.conjuntoDeCondiciones = conjuntoDeCondiciones;
	}

	/**
	 * Se calcula el filtro que correspondería a la pata pasada de acuerdo al tipo de esta
	 * instancia.<br>
	 * En el caso del portal el filtro es fijo. En el caso del router es la suma de los filtros de
	 * otras patas
	 * 
	 * @param pataConectora
	 *            La pata para la que se pide el calculo (puede ser null)
	 * @return El filtro correspondiente a la pata
	 */
	protected abstract Condicion calcularFiltroDeEntradaPara(PataBidireccional pataConectora);

	/**
	 * @see java.lang.Object#toString()
	 */

	@Override
	public String toString() {
		return ToString.de(this).con(numeroDeInstancia_FIELD, getNumeroDeInstancia())
				.con(identificador_FIELD, identificador).con(patas_FIELD, patas).toString();
	}

	public IdDeComponenteVortex getIdentificador() {
		return identificador;
	}

	/**
	 * @see net.gaia.vortex.router.impl.moleculas.patas.ListenerConexionBidiEnPata#onConexionBidiPara(net.gaia.vortex.router.impl.moleculas.patas.PataBidireccional)
	 */

	public void onConexionBidiPara(final PataBidireccional pata) {
		final NodoBidireccional origen = pata.getNodoLocal();
		final Receptor destino = pata.getNodoRemoto();
		try {
			this.listenerDeConexiones.onConexionBidiDe(origen, destino, pata);
		}
		catch (final Exception e) {
			LOG.error("Se produjo un error en el listener de conexiones bidi del nodo[" + this.toShortString() + "]", e);
		}
	}

	public ListenerDeConexionesBidiEnNodo getListenerDeConexiones() {
		return listenerDeConexiones;
	}

	public void setListenerDeConexiones(final ListenerDeConexionesBidiEnNodo listenerDeConexiones) {
		if (listenerDeConexiones == null) {
			throw new IllegalArgumentException("El listener de conexiones no puede ser null");
		}
		this.listenerDeConexiones = listenerDeConexiones;
	}

}
