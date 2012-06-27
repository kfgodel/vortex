/**
 * 16/06/2012 19:03:38 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.core.impl.mensaje;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicReference;

import net.gaia.vortex.core.api.atomos.Receptor;
import net.gaia.vortex.core.api.mensaje.MensajeVortex;
import net.gaia.vortex.core.api.moleculas.ids.IdentificadorVortex;
import ar.com.dgarcia.coding.exceptions.UnhandledConditionException;
import ar.com.dgarcia.colecciones.sets.ConcurrentHashSet;
import ar.com.dgarcia.lang.strings.ToString;

/**
 * Esta clase es la implementación de un mensaje vortex utilizando un mapa para conservar la
 * representación de estado de los datos transmitidos
 * 
 * @author D. García
 */
public class MensajeMapa implements MensajeVortex {

	/**
	 * Key utilizada para guardar el registro de moleculas visitadas
	 */
	private static final String MOLECULAS_VORTEX_VISITADAS_KEY = "moleculas_vortex_visitadas";
	/**
	 * Key utilizada para guardar el valor de una primitiva como mapa en este mensaje
	 */
	public static final String PRIMITIVA_VORTEX_KEY = "PRIMITIVA_VORTEX_KEY";
	/**
	 * Clave usada para agregar como dato el nombre completo de la clase a partir de la cual se
	 * origina este mensaje
	 */
	public static final String CLASSNAME_KEY = "CLASSNAME_KEY";

	private AtomicReference<Receptor> remitenteDirecto;
	public static final String remitenteDirecto_FIELD = "remitenteDirecto";

	private ConcurrentMap<String, Object> contenido;
	public static final String contenido_FIELD = "contenido";

	private Set<String> idsVisitados;

	public Set<String> getIdsVisitados() {
		if (idsVisitados == null) {
			idsVisitados = crearSetDeVisitadosDesdeMapa();
		}
		return idsVisitados;
	}

	/**
	 * Creamos el set de los nodos visitados a partir de los datos actuales del mapa, reemplazamos
	 * la entrada en el mapa con el set creado
	 * 
	 * @return El conjunto de nodos visitados inicialmente
	 */
	private Set<String> crearSetDeVisitadosDesdeMapa() {
		final Collection<String> moleculasVisitadasSegunMapa = castearYVerificarContenidoDeVisitados();
		final ConcurrentHashSet<String> visitados = new ConcurrentHashSet<String>();
		visitados.addAll(moleculasVisitadasSegunMapa);
		getContenido().put(MOLECULAS_VORTEX_VISITADAS_KEY, visitados);
		return visitados;
	}

	/**
	 * Verifica que el objeto pasado sea una colección de strings. En caso contrario produce una
	 * excepción
	 * 
	 * @param object
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private Collection<String> castearYVerificarContenidoDeVisitados() {
		final Object object = getContenido().get(MOLECULAS_VORTEX_VISITADAS_KEY);
		if (object == null) {
			return Collections.emptySet();
		}
		Collection coleccionDeIds;
		try {
			coleccionDeIds = (Collection) object;
		} catch (final ClassCastException e) {
			throw new UnhandledConditionException("El mensaje tiene como atributo[" + MOLECULAS_VORTEX_VISITADAS_KEY
					+ "] un valor que no es una coleccion de ids: " + object);
		}
		for (final Object posibleId : coleccionDeIds) {
			if (posibleId instanceof String) {
				continue;
			}
			throw new UnhandledConditionException("El atributo[" + MOLECULAS_VORTEX_VISITADAS_KEY
					+ "] tiene en la coleccion un ID que no es string: " + posibleId);
		}
		return coleccionDeIds;
	}

	/**
	 * @see net.gaia.vortex.core.api.mensaje.MensajeVortex#getContenido()
	 */
	@Override
	public ConcurrentMap<String, Object> getContenido() {
		return contenido;
	}

	/**
	 * @see net.gaia.vortex.core.api.mensaje.MensajeVortex#getRemitenteDirecto()
	 */
	@Override
	public Receptor getRemitenteDirecto() {
		return remitenteDirecto.get();
	}

	/**
	 * @see net.gaia.vortex.core.api.mensaje.MensajeVortex#setRemitenteDirecto(net.gaia.vortex.core.api.atomos.Receptor)
	 */
	@Override
	public void setRemitenteDirecto(final Receptor remitente) {
		this.remitenteDirecto.set(remitente);
	}

	/**
	 * Crea un mensaje vortex con contenido vacío
	 * 
	 * @return El mensaje creado
	 */
	@SuppressWarnings("unchecked")
	public static MensajeMapa create() {
		return create(Collections.EMPTY_MAP);
	}

	/**
	 * Crea un mensaje vortex con el contenido indicado
	 * 
	 * @param contenidoIncial
	 *            El contenido a portar por este mensaje
	 * @return El mensaje creado
	 */
	public static MensajeMapa create(final Map<String, Object> contenidoIncial) {
		final MensajeMapa mensaje = new MensajeMapa();
		mensaje.remitenteDirecto = new AtomicReference<Receptor>();
		mensaje.contenido = new ConcurrentHashMap<String, Object>(contenidoIncial);
		mensaje.idsVisitados = mensaje.crearSetDeVisitadosDesdeMapa();
		return mensaje;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return ToString.de(this).add(remitenteDirecto_FIELD, remitenteDirecto).add(contenido_FIELD, contenido)
				.toString();
	}

	/**
	 * @see net.gaia.vortex.core.api.mensaje.MensajeVortex#getValorComoPrimitiva()
	 */
	@Override
	public Object getValorComoPrimitiva() {
		return getContenido().get(PRIMITIVA_VORTEX_KEY);
	}

	/**
	 * @see net.gaia.vortex.core.api.mensaje.MensajeVortex#setValorComoPrimitiva(java.lang.Object)
	 */
	@Override
	public void setValorComoPrimitiva(final Object valor) {
		if (!esPrimitivaVortex(valor)) {
			throw new IllegalArgumentException("El valor[" + valor + "] no puede ser aceptado como primitiva vortex");
		}
		getContenido().put(PRIMITIVA_VORTEX_KEY, valor);
	}

	/**
	 * Indica si el valor pasado es considerable una primitiva vortex
	 * 
	 * @param valor
	 *            El valor a evaluar
	 * @return true si es un número, string, array o null
	 */
	public static boolean esPrimitivaVortex(final Object valor) {
		if (valor == null) {
			return false;
		}
		if (Number.class.isInstance(valor)) {
			return true;
		}
		if (String.class.isInstance(valor)) {
			return true;
		}
		if (valor.getClass().isArray()) {
			return true;
		}
		return false;
	}

	/**
	 * @see net.gaia.vortex.core.api.mensaje.MensajeVortex#tieneValorComoPrimitiva()
	 */
	@Override
	public boolean tieneValorComoPrimitiva() {
		return getContenido().containsKey(PRIMITIVA_VORTEX_KEY);
	}

	/**
	 * @see net.gaia.vortex.core.api.mensaje.MensajeVortex#setNombreDelTipoOriginal(java.lang.String)
	 */
	@Override
	public void setNombreDelTipoOriginal(final String nombreDeClaseCompleto) {
		getContenido().put(CLASSNAME_KEY, nombreDeClaseCompleto);
	}

	/**
	 * @see net.gaia.vortex.core.api.mensaje.MensajeVortex#getNombreDelTipoOriginal()
	 */
	@Override
	public String getNombreDelTipoOriginal() {
		return (String) getContenido().get(CLASSNAME_KEY);
	}

	/**
	 * @see net.gaia.vortex.core.api.mensaje.MensajeVortex#pasoPreviamentePor(net.gaia.vortex.core.api.moleculas.ids.IdentificadorVortex)
	 */
	@Override
	public boolean pasoPreviamentePor(final IdentificadorVortex identificador) {
		final String valorDelIdentificador = identificador.getValorActual();
		final boolean yaTenemosRegistroDelIdentificador = getIdsVisitados().contains(valorDelIdentificador);
		return yaTenemosRegistroDelIdentificador;
	}

	/**
	 * @see net.gaia.vortex.core.api.mensaje.MensajeVortex#registrarPasajePor(net.gaia.vortex.core.api.moleculas.ids.IdentificadorVortex)
	 */
	@Override
	public void registrarPasajePor(final IdentificadorVortex identificador) {
		final String valorDelIdentificador = identificador.getValorActual();
		getIdsVisitados().add(valorDelIdentificador);
	}
}
