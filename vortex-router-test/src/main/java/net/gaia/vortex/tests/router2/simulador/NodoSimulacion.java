/**
 * 07/12/2012 18:09:54 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.tests.router2.simulador;

import net.gaia.vortex.tests.router2.api.Nodo;

/**
 * Interfaz para definir comportamiento común utilizable desde los pasos de la simulación
 * 
 * @author D. García
 */
public interface NodoSimulacion extends Nodo, Nombrable {

	/**
	 * Método artificial para simular la conexión como parte de la simulación
	 * 
	 * @param otro
	 *            El nodo al que se conectará unidireccionalmente
	 */
	void simularConexionCon(NodoSimulacion otro);

	/**
	 * Método artificial para crear una conexión bidireccional entre este nodo y el pasado en un
	 * solo paso de simulador.<br>
	 * 
	 * @param otro
	 *            El otro nodo al que se conectará
	 */
	public void simularConexionBidi(final NodoSimulacion otro);

	/**
	 * Método artificial que desconecta una de las puntas en un paso de simulador
	 * 
	 * @param otro
	 *            El nodo del que se desconectará este
	 */
	void simularDesconexionUniDe(NodoSimulacion otro);

	/**
	 * Método artificial que desconecta las dos puntas en un sólo paso de simulador
	 * 
	 * @param otroConectado
	 *            el nodo del cual se desconectará
	 */
	void simularDesconexionBidiDe(NodoSimulacion otroConectado);

	/**
	 * Indica si este nodo tiene al pasado como destino
	 * 
	 * @param otro
	 *            El nodo a comprobar
	 * @return true si es parte de los receptores de este nodo
	 */
	boolean tieneComoDestinoA(Nodo otro);

	/**
	 * Indica si este nodo utiliza los filtros indicados con el nodo pasado.<br>
	 * Este método ayuda a verificar por fuera como están ruteando los routers
	 * 
	 * 
	 * @param nodo
	 *            El nodo para el que se quieren verificar los filtros usados
	 * @param filtros
	 *            Los filtros que debería usar
	 * @return false si el nodo no está conectado a este router o el filtro no esta contenido en los
	 *         usados
	 */
	public abstract boolean usaFiltrosCon(final Nodo nodo, final String... filtros);

}
