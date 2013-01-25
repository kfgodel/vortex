/**
 * 31/08/2012 23:11:49 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.core.api.ids.mensajes;

import java.util.Map;

import net.gaia.vortex.core.api.atomos.ShortStringable;
import net.gaia.vortex.core.api.ids.componentes.IdDeComponenteVortex;

/**
 * Esta interfaz representa el identificador de un mensaje vortex como un elemento compuesto por el
 * identificador del nodo de origen y un número de secuencia.<br>
 * <br>
 * Los ids de mensajes tienen una relación de igualdad bien definida, y de orden relativa al
 * identificador del nodo, más la secuencia
 * 
 * @author D. García
 */
public interface IdDeMensaje extends Comparable<IdDeMensaje>, ShortStringable {

	/**
	 * Nombre del atributo que corresponde al identificador del componente emisor
	 */
	public static final String EMISOR_DEL_ID_KEY = "id_del_emisor";
	/**
	 * Nombre del atributo que corresponde al numero de secuencia del mensaje
	 */
	public static final String SECUENCIA_DEL_ID_KEY = "numero_secuencia";

	/**
	 * Devuelve el identificador del emisor del mensaje
	 * 
	 * @return El objeto que identifica al emisor en la red
	 */
	public IdDeComponenteVortex getIdDelEmisor();

	/**
	 * Devuelve el identificador de la secuencia de este mensaje en el emisor
	 */
	public Long getNumeroDeSecuencia();

	/**
	 * Indica si este identificador tiene como parte emisora al identificador pasado
	 * 
	 * @param idDelNodo
	 *            El identificador de nodo a comprobar
	 * @return true si este id tiene como identificador uno igual al pasado
	 */
	boolean esOriginadoEn(IdDeComponenteVortex idDelNodo);

	/**
	 * Devuelve este ID expresado como mapa serializable
	 * 
	 * @return El mapa cuyo estado es equivalente a esta instancia
	 */
	public Map<String, Object> getAsMap();
}
