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

import net.gaia.vortex.core.api.mensaje.ContenidoVortex;
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
public class MensajeConContenido implements MensajeVortex {

	/**
	 * Key utilizada para guardar el registro de moléculas visitadas
	 */
	public static final String TRAZA_IDENTIFICADORES_VORTEX_KEY = "traza_identificadores";

	private ContenidoVortex contenido;
	public static final String contenido_FIELD = "contenido";

	private Set<String> idsVisitados;
	public static final String idsVisitados_FIELD = "idsVisitados";

	public Set<String> getIdsVisitados() {
		return idsVisitados;
	}

	/**
	 * Creamos el set de los nodos visitados a partir de los datos pasados como ids visitados.
	 * Reemplazamos la entrada previa en el mapa con el set creado por esta instancia
	 * 
	 * @param moleculasVisitadasSegunMapa
	 *            El conjunto de IDs visitados según estado previo
	 * @return El conjunto de nodos visitados inicialmente
	 */
	private void inicializarIdsVisitados(final Collection<String> moleculasVisitadasSegunMapa) {
		this.idsVisitados = new ConcurrentHashSet<String>();
		idsVisitados.addAll(moleculasVisitadasSegunMapa);
		getContenido().put(TRAZA_IDENTIFICADORES_VORTEX_KEY, idsVisitados);
	}

	/**
	 * @see net.gaia.vortex.core.api.mensaje.MensajeVortex#getContenido()
	 */
	@Override
	public ContenidoVortex getContenido() {
		return contenido;
	}

	/**
	 * Crea un mensaje vacío sin registro de nodos visitados
	 * 
	 * @return El mensaje creado
	 */
	@SuppressWarnings("unchecked")
	public static MensajeConContenido crearVacio() {
		final ContenidoVortex contenidoVacio = ContenidoMapa.create();
		return create(contenidoVacio, Collections.EMPTY_SET);
	}

	/**
	 * Crea un nuevo mensaje vortex con el estado pasado, pero sin registro de nodos visitados
	 * previamente
	 * 
	 * @param contenido
	 *            El contenido del mensaje
	 * @return El mensaje creado
	 */
	@SuppressWarnings("unchecked")
	public static MensajeConContenido crearSinIds(final ContenidoVortex contenido) {
		return create(contenido, Collections.EMPTY_SET);
	}

	/**
	 * Crea un mensaje vortex con el contenido indicado y el registro de nodos visitados pasado
	 * 
	 * @param contenidoIncial
	 *            El contenido a portar por este mensaje
	 * @param idsVisitados
	 *            Los ids tomados como visitados
	 * @return El mensaje creado
	 */
	public static MensajeConContenido create(final ContenidoVortex contenido, final Collection<String> idsVisitados) {
		final MensajeConContenido mensaje = new MensajeConContenido();
		mensaje.contenido = contenido;
		mensaje.inicializarIdsVisitados(idsVisitados);
		return mensaje;
	}

	/**
	 * Regenera un mensaje vortex desde el mapa de valores pasado. Tomando los Ids de nodos
	 * visitados
	 * 
	 * @param valoresExternos
	 *            El mapa de valores para regenerar el mensaje
	 * @return El mensaje recreado
	 */
	public static MensajeConContenido regenerarDesde(final Map<String, Object> valoresExternos) {
		final ContenidoMapa contenido = ContenidoMapa.create(valoresExternos);
		final Collection<String> idsVisitados = MensajeConContenido.obtenerIdsVisitadosDesde(valoresExternos);
		final MensajeConContenido mensajeReconstruido = MensajeConContenido.create(contenido, idsVisitados);
		return mensajeReconstruido;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return ToString.de(this).add(contenido_FIELD, contenido).add(idsVisitados_FIELD, idsVisitados).toString();
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

	/**
	 * Verifica que el mapa pasado sea tenga una colección de strings como datos de los nodos por
	 * los que pasó.<br>
	 * En caso contrario devuelve una colección vacía o produce una excepción si el atributo existe
	 * pero no es de los tipos esperados
	 * 
	 * @param contenidoRegenerado
	 *            El mapa a revisar por la lista de IDs
	 * 
	 * @return La colección de IDs recuperada del mensaje
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static Collection<String> obtenerIdsVisitadosDesde(final Map<String, Object> contenidoRegenerado) {
		final Object object = contenidoRegenerado.get(MensajeConContenido.TRAZA_IDENTIFICADORES_VORTEX_KEY);
		if (object == null) {
			return Collections.emptySet();
		}
		Collection coleccionDeIds;
		try {
			coleccionDeIds = (Collection) object;
		} catch (final ClassCastException e) {
			throw new UnhandledConditionException("El mensaje tiene como atributo["
					+ MensajeConContenido.TRAZA_IDENTIFICADORES_VORTEX_KEY
					+ "] un valor que no es una coleccion de ids: " + object, e);
		}
		for (final Object posibleId : coleccionDeIds) {
			if (posibleId instanceof String) {
				continue;
			}
			throw new UnhandledConditionException("El atributo[" + MensajeConContenido.TRAZA_IDENTIFICADORES_VORTEX_KEY
					+ "] tiene en la coleccion un ID que no es string: " + posibleId);
		}
		return coleccionDeIds;
	}
}
