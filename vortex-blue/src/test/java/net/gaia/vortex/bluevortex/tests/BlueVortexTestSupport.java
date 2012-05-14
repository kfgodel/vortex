/**
 * 10/05/2012 00:25:32 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.bluevortex.tests;

import net.gaia.vortex.bluevortex.api.BlueVortex;

import org.junit.Before;

/**
 * Esta clase es base para los tests de bluevortex
 * 
 * @author D. García
 */
public class BlueVortexTestSupport {

	private BlueVortex vortex;

	/**
	 * Crea la nueva instancia de vortex
	 */
	@Before
	public void crearVortex() {
	}

	public BlueVortex getVortex() {
		return vortex;
	}

	public void setVortex(final BlueVortex vortex) {
		this.vortex = vortex;
	}

	protected NodoEnTest crearNodo(final String nombreNodo) {

	}

	/**
	 * Envía un mensaje desde el nodo origen, verificando que sea recibido en el nodo destino dentro
	 * de un plazo razonable
	 * 
	 * @param nodoOrigen
	 *            El nodo desde el cual se envía el mensaje
	 * @param nodosDestino
	 *            Los nodos en los cuales se comprueba que sea recibido
	 */
	protected void verificarConectividadEntre(final NodoEnTest nodoOrigen, final NodoEnTest... nodosDestino) {
		final Object mensaje = new Object();
		nodoOrigen.enviar(mensaje);
		for (final NodoEnTest nodoDestino : nodosDestino) {
			nodoDestino.verificarQueRecibio(mensaje);
		}
	}

	/**
	 * Verifica que un mensaje mandado desde el nodo origen no llega al nodo destino después de
	 * cierto tiempo "razonable"
	 * 
	 * @param nodoOrigen
	 *            El nodo desde el cual se manda el mensaje
	 * @param nodoDestino
	 *            El nodo en el cual no debería llegar
	 */
	protected void verificarDesconexionEntre(final NodoEnTest nodoOrigen, final NodoEnTest nodoDestino) {
		final Object mensaje = new Object();
		nodoOrigen.enviar(mensaje);
		nodoDestino.verificarQueNoLlego(mensaje);
	}

}