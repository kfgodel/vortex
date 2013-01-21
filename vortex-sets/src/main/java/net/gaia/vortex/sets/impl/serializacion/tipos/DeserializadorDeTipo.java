/**
 * 21/01/2013 15:38:49 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.sets.impl.serializacion.tipos;

import java.util.Map;


/**
 * Esta interfaz define el método utilizado para deserializar tipos de condiciones en mensajes
 * 
 * @author D. García
 */
public interface DeserializadorDeTipo<C> {
	/**
	 * Deserializa el estado del mapa pasado como origen, hacia el objeto destino
	 * 
	 * @param mapaOrigen
	 *            El mapa con el estado de origen
	 * @param destino
	 *            El objeto que se populará con los datos
	 */
	public C deserializarDesde(final Map<String, Object> mapaOrigen, ContextoDeSerializacion contexto);

}
