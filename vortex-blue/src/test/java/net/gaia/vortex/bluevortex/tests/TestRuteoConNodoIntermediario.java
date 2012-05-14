/**
 * 12/05/2012 22:52:30 Copyright (C) 2011 Darío L. García
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

import junit.framework.Assert;

import org.junit.Test;

/**
 * Esta clase prueba varios casos en los que existe un nodo en el medio de otros dos comunicantes
 * 
 * @author D. García
 */
public class TestRuteoConNodoIntermediario extends BlueVortexTestSupport {
	/**
	 * Prueba que si existe un tercero en el medio el mensaje más simple llega igual<br>
	 * A->B->C
	 */
	@Test
	public void elMensajeMasSimpleDeberiaLlegarADestinoPasandoPorNodoIntermedio() {
		// Creamos la red inicial
		final NodoEnTest nodoA = crearNodo("A");
		final NodoEnTest nodoB = crearNodo("B");
		final NodoEnTest nodoC = crearNodo("C");
		nodoA.conectarA(nodoB);
		nodoB.conectarA(nodoC);

		// Verificamos que un mensaje llegue
		verificarConectividadEntre(nodoA, nodoC);
	}

	/**
	 * Prueba que en una red con topología de "Y" el mensaje llegue a ambas puntas<br>
	 * A->B->C<br>
	 * .....\->D
	 */
	@Test
	public void elMensajeDeberiaLlegaraExtremosDeUnaY() {
		// Armamos la red inicial
		final NodoEnTest nodoA = crearNodo("A");
		final NodoEnTest nodoB = crearNodo("B");
		final NodoEnTest nodoC = crearNodo("C");
		final NodoEnTest nodoD = crearNodo("D");

		nodoA.conectarA(nodoB);
		nodoB.conectarA(nodoC);
		nodoB.conectarA(nodoD);

		// Verificamos que el mensaje llegue a ambas puntas
		verificarConectividadEntre(nodoA, nodoC, nodoD);
	}

	/**
	 * Prueba que si el nodo del medio no puede discriminar la diferencia entre mensajes, de todas
	 * formas, el mensaje sea entregado correctamente<br>
	 * A->B->C
	 */
	@Test
	public void elMensajeDeberiaLlegarAlExtremoCorrectoAunqueElIntermedioNoEntiendaLaDiferenciaEntreMensajes() {
		// Armamos la red
		final NodoEnTest nodoA = crearNodo("A");

		final NodoEnTest nodoB = crearNodo("B");
		nodoB.quitarOperadorDeFiltroParaInstancias();

		final NodoEnTest nodoC = crearNodo("C");
		nodoC.declararRecepcionDeInstanciasDe(String.class);

		// Verificamos que el mensaje llega igual
		final String mensaje = "mensaje";
		nodoA.enviar(mensaje);
		nodoC.verificarQueRecibio(mensaje);
	}

	/**
	 * Prueba que si el nodo del medio no puede discriminar la diferencia entre mensajes, de todas
	 * formas, el mensaje sea entregado correctamente<br>
	 * A->B->C
	 */
	@Test
	public void elMensajeNoDeberiaLlegarAunqueElRestoDeLosNodosNoLoFiltren() {
		// Armamos la red
		final NodoEnTest nodoA = crearNodo("A");
		nodoA.quitarOperadorDeFiltroParaInstancias();

		final NodoEnTest nodoB = crearNodo("B");
		nodoB.quitarOperadorDeFiltroParaInstancias();

		final NodoEnTest nodoC = crearNodo("C");
		nodoC.declararRecepcionDeInstanciasDe(String.class);

		// Verificamos que el mensaje es filtrado al final si es necesario
		final String mensaje = "mensaje";
		nodoA.enviar(mensaje);
		nodoC.verificarQueNoLlego(mensaje);

		Assert.assertEquals("El mensaje debería ser descartado si no es aceptable", 1,
				nodoC.getCantidadDeMensajesRecibidosYDescartados());
	}

	/**
	 * Verifica que el mensaje enviado no sea retornado al emisor
	 */
	@Test
	public void elMensajeNoDeberiaRetornarAlEmisorAunqueSeaInteresado() {
		// Armamos la red
		final NodoEnTest nodoA = crearNodo("A");
		nodoA.declararEnvioDeInstanciasDe(String.class);
		nodoA.declararRecepcionDeInstanciasDe(String.class);

		final NodoEnTest nodoB = crearNodo("B");

		final NodoEnTest nodoC = crearNodo("C");
		nodoC.declararEnvioDeInstanciasDe(String.class);
		nodoC.declararRecepcionDeInstanciasDe(String.class);

		nodoA.conectarA(nodoB);
		nodoB.conectarA(nodoC);

		// Verificamos que el mensaje no es recibido por A después de ser enviado
		final String elMensaje = "Un Mensaje";
		nodoA.enviar(elMensaje);
		nodoC.verificarQueRecibio(elMensaje);
		nodoA.verificarQueNoLlego(elMensaje);
	}
}
