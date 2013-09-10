/**
 * 19/01/2013 12:09:47 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.impl.mensajes.clones;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import net.gaia.vortex.impl.helpers.VortexMap;
import ar.com.dgarcia.lang.strings.ToString;

/**
 * Esta clase representa la tarea de copia de un mapa con sus valores primitivos y submapas
 * 
 * @author D. García
 */
public class CopiarMapaVortex {

	private Map<String, Object> mapaOriginal;
	public static final String mapaOriginal_FIELD = "mapaOriginal";

	private Map<String, Object> mapaCopia;
	public static final String mapaCopia_FIELD = "mapaCopia";

	public static CopiarMapaVortex create(final Map<String, Object> mapaOriginal, final Map<String, Object> mapaCopia) {
		final CopiarMapaVortex copiar = new CopiarMapaVortex();
		copiar.mapaCopia = mapaCopia;
		copiar.mapaOriginal = mapaOriginal;
		return copiar;
	}

	/**
	 * Copia todos los valores del mapa original a la copia, generando tareas de copia pendiente por
	 * cada mapa encontrado como valor
	 * 
	 * @return La lista de las copias pendientes por sub mapas
	 */
	public List<CopiarMapaVortex> copiar() {
		final List<CopiarMapaVortex> copiasPendientes = new ArrayList<CopiarMapaVortex>();

		final Set<Entry<String, Object>> entriesOriginal = mapaOriginal.entrySet();
		for (final Entry<String, Object> entryOriginal : entriesOriginal) {
			final String key = entryOriginal.getKey();
			Object value = entryOriginal.getValue();
			if (value instanceof Map) {
				// Si es un mapa generamos otra copia para que sea modificable
				@SuppressWarnings("unchecked")
				final Map<String, Object> subMapaOriginal = (Map<String, Object>) value;
				final Map<String, Object> subMapaCopia = crearMapaVortex();
				// La operación de copia queda para despues
				copiasPendientes.add(CopiarMapaVortex.create(subMapaOriginal, subMapaCopia));
				// Reemplazamos el valor para que se use el mapa que creamos
				value = subMapaCopia;
			}
			mapaCopia.put(key, value);
		}
		return copiasPendientes;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	
	public String toString() {
		return ToString.de(this).con(mapaOriginal_FIELD, mapaOriginal).con(mapaCopia_FIELD, mapaCopia).toString();
	}

	/**
	 * Crea un mapa para usar anidado en contenido vortex. Case insensitive
	 * 
	 * @return El mapa a utilizar
	 */
	public static Map<String, Object> crearMapaVortex() {
		return new VortexMap();
	}

	/**
	 * Crea una version adaptada a vortex del mapa que es case insensitive. El contenido es el
	 * mismo.<br>
	 * Cualquier submapa es convertido también
	 * 
	 * @param mapaOriginal
	 *            El mapa a convertir
	 * @return El mapa clonado
	 */
	public static Map<String, Object> convertirAMapaVortex(final Map<String, Object> mapaOriginal) {
		final Map<String, Object> mapaCopia = crearMapaVortex();
		copiarContenidoVortexDesde(mapaOriginal, mapaCopia);
		return mapaCopia;
	}

	/**
	 * Copia el contenido de un mapa en el otro, generado mapas Case Insensitive por cada submapa
	 * que debe clonar
	 * 
	 * @param mapaOrigen
	 *            El mapa original con los datos
	 * @param mapaDestino
	 *            El mapa destino donde se va a clonar el contenido
	 */
	public static void copiarContenidoVortexDesde(final Map<String, Object> mapaOrigen,
			final Map<String, Object> mapaDestino) {
		final List<CopiarMapaVortex> pendientes = new ArrayList<CopiarMapaVortex>();
		pendientes.add(CopiarMapaVortex.create(mapaOrigen, mapaDestino));

		while (!pendientes.isEmpty()) {
			final CopiarMapaVortex copiaActual = pendientes.remove(0);

			final List<CopiarMapaVortex> pendientesAdicionales = copiaActual.copiar();
			pendientes.addAll(pendientesAdicionales);
		}
	}

}
