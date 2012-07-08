/**
 * 02/07/2012 01:11:57 Copyright (C) 2011 Darío L. García
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

import java.util.Collections;
import java.util.Map;

import net.gaia.vortex.core.api.mensaje.ContenidoVortex;

/**
 * Esta clase representa la implementación default del contenido de un mensaje vortex como un mapa
 * de valores
 * 
 * @author D. García
 */
public class ContenidoMapa extends ContenidoVortexSupport implements ContenidoVortex {

	/**
	 * Crea un contenido vortex tomando el estado pasado como inicial
	 * 
	 * @param estadoInicial
	 *            El estado de este contenido
	 * @return El contenido creado
	 */
	public static ContenidoMapa create(final Map<String, Object> estadoInicial) {
		final ContenidoMapa contenido = new ContenidoMapa();
		contenido.putAll(estadoInicial);
		return contenido;
	}

	/**
	 * Crea un contenido vortex vacío
	 * 
	 * @return El mapa de valores completamente vacío
	 */
	@SuppressWarnings("unchecked")
	public static ContenidoMapa create() {
		return create(Collections.EMPTY_MAP);
	}

}
