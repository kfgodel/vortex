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
import net.gaia.vortex.api.moleculas.Distribuidor;
import net.gaia.vortex.api.moleculas.Terminal;

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

	/**
	 * Interconecta dos distribuidores utilizando terminales creadas para conectar cada uno.<br>
	 * 
	 * @param unDistribuidor
	 *            Uno de los distribuidores a conectar
	 * @param otroDtritribuidor
	 *            El otro distribuidor a conectar
	 */
	public static void interconectarDistribuidores(final Distribuidor unDistribuidor,
			final Distribuidor otroDtritribuidor) {
		final Terminal unaTerminal = unDistribuidor.crearTerminal();
		final Terminal otraTerminal = otroDtritribuidor.crearTerminal();
		interconectar(unaTerminal, otraTerminal);
	}

	/**
	 * Interconecta el nodo pasado con el distribuidor utilizando una terminal creada en el
	 * distribuidor para que el nodo no reciba sus propios mensajes al enviar al distribuidor
	 * 
	 * @param unNodo
	 *            El nodo a interconectar
	 * @param unDistribuidor
	 *            El distribuidor del cual se creara una terminal
	 */
	public static void interconectarConDistribuidor(final Nodo unNodo, final Distribuidor unDistribuidor) {
		final Terminal unaTerminal = unDistribuidor.crearTerminal();
		interconectar(unNodo, unaTerminal);
	}

}
