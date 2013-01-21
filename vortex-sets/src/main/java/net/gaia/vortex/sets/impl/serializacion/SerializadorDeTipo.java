/**
 * 21/01/2013 12:18:27 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.sets.impl.serializacion;

import java.util.Map;

/**
 * Esta interfaz define el método que debe implementar un serializador para un tipo concreto de
 * condición
 * 
 * @author D. García
 */
public interface SerializadorDeTipo<C> {

	/**
	 * Serializa el estado del objeto pasado como origen, al mapa destino
	 * 
	 * @param mapaDestino
	 *            El mapa que contrendrá el estado serializado
	 * @param origen
	 *            El objeto origen desde el cual tomar el estado
	 */
	public Map<String, Object> serializarDesde(final C origen, ContextoDeSerializacion contexto);

}
