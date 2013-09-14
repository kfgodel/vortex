/**
 * 14/09/2013 02:46:03 Copyright (C) 2013 Darío L. García
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
package net.gaia.vortex.impl.generadores;

import net.gaia.vortex.api.basic.Nodo;
import net.gaia.vortex.api.builder.Nodos;
import net.gaia.vortex.api.generadores.EstrategiaDeConexionDeNodos;
import ar.com.dgarcia.lang.strings.ToString;

/**
 * Esta clase representa la estrategia de conexion que utiliza un nodo central como punto de
 * conexión para los nuevos componentes
 * 
 * @author D. García
 */
public class UsarNodoCentral implements EstrategiaDeConexionDeNodos {

	private Nodo puntoCentral;
	public static final String puntoCentral_FIELD = "puntoCentral";

	/**
	 * @see net.gaia.vortex.api.generadores.EstrategiaDeConexionDeNodos#conectarNodo(net.gaia.vortex.api.basic.Nodo)
	 */
	public void conectarNodo(final Nodo nodoConectable) {
		Nodos.interconectar(puntoCentral, nodoConectable);
	}

	/**
	 * @see net.gaia.vortex.api.generadores.EstrategiaDeConexionDeNodos#desconectarNodo(net.gaia.vortex.api.basic.Nodo)
	 */
	public void desconectarNodo(final Nodo nodoDesconectable) {
		Nodos.desinterconectar(puntoCentral, nodoDesconectable);
	}

	public static UsarNodoCentral create(final Nodo nodoCentral) {
		final UsarNodoCentral estrategia = new UsarNodoCentral();
		estrategia.puntoCentral = nodoCentral;
		return estrategia;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return ToString.de(this).add(puntoCentral_FIELD, puntoCentral).toString();
	}

}
