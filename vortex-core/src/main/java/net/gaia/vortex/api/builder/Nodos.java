/**
 * 14/09/2013 02:53:12 Copyright (C) 2013 Darío L. García
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
package net.gaia.vortex.api.builder;

import net.gaia.vortex.api.basic.Nodo;

/**
 * Esta clase ofrece algunos métodos de operaciones comunes entre nodos
 * 
 * @author D. García
 */
public class Nodos {

	/**
	 * Crea dos conexiones unidireccionales, una desde el primer nodo al segundo, y la otra en
	 * sentido inverso para interconectarlos entre sí como si fueran bidireccionales
	 * 
	 * @param unNodo
	 *            Uno de los nodos a conectar
	 * @param otroNodo
	 *            El otro nodo que sera conectado
	 */
	public static void interconectar(final Nodo unNodo, final Nodo otroNodo) {
		unNodo.conectarCon(otroNodo);
		otroNodo.conectarCon(unNodo);
	}

	/**
	 * Desconecta mutuamente los nodos pasados entre sí.
	 * 
	 * @param unNodo
	 *            Un nodo a desconectar del otro
	 * @param otroNodo
	 *            El otro nodo a desconectar del primero
	 */
	public static void desinterconectar(final Nodo unNodo, final Nodo otroNodo) {
		otroNodo.desconectarDe(unNodo);
		unNodo.desconectarDe(otroNodo);
	}

}
