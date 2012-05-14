/**
 * 12/05/2012 22:59:03 Copyright (C) 2011 Darío L. García
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
 * Esta clase prueba que si hay variaciones en la topología de la red los mensajes lleguen si es
 * posible
 * 
 * @author D. García
 */
public class TestRuteoDinamico extends BlueVortexTestSupport {

	/**
	 * Prueba que al producir la conectividad entre partes el mensaje llega a más destinos A->..->C
	 */
	@Test
	public void deberiaPermitirRecibirUnMensajeSiSeAgregaUnNodoIntermedio() {
		// Definimos la topología inicial
		final NodoEnTest nodoA = crearNodo("A");
		final NodoEnTest nodoC = crearNodo("C");

		// Verificamos la pre-condición
		verificarDesconexionEntre(nodoA, nodoC);

		// Modificamos la red
		final NodoEnTest nodoB = crearNodo("B");
		nodoA.conectarA(nodoB);
		nodoB.conectarA(nodoC);

		// Verificamos que ahora sí se pueda
		verificarConectividadEntre(nodoA, nodoC);
	}

	/**
	 * Prueba que al cortar la conectividad el mensaje no es recibido
	 */
	@Test
	public void deberiaDejarDeRecibirUnMensajeSiSeQuitaElNodoNexo() {
		// Armamos la red inicial
		final NodoEnTest nodoA = crearNodo("A");
		final NodoEnTest nodoC = crearNodo("C");
		final NodoEnTest nodoB = crearNodo("B");
		nodoA.conectarA(nodoB);
		nodoB.conectarA(nodoC);

		// Verificamos la pre-condición
		verificarConectividadEntre(nodoA, nodoC);

		// Modificamos la red
		nodoA.desconectarDe(nodoB);

		// Verificamos el caso
		verificarDesconexionEntre(nodoA, nodoC);
	}

	/**
	 * Prueba que si un nodo intermedio tiene mayor capacidad para entender la semántica de los
	 * mensajes, entonces puede elegir mejor a quién le envía los mensajes<br>
	 * A->B->C<br>
	 */
	@Test
	public void deberiaMejorarElRuteoSiElNodoIntermedioEntiendeMasOperadores() {
		// Armamos la red inicial
		final NodoEnTest nodoA = crearNodo("A");
		nodoA.quitarOperadorDeFiltroParaInstancias();

		final NodoEnTest nodoB = crearNodo("B");
		nodoB.quitarOperadorDeFiltroParaInstancias();

		final NodoEnTest nodoC = crearNodo("C");
		nodoC.declararRecepcionDeInstanciasDe(Number.class);

		nodoA.conectarA(nodoB);
		nodoB.conectarA(nodoC);

		// Verificamos la pre-condición
		final String mensaje = "Lala";
		nodoA.enviar(mensaje);
		nodoC.verificarQueNoLlego(mensaje);
		Assert.assertEquals("El mensaje debería haber pasado por el nodo intermedio", 1,
				nodoC.getCantidadDeMensajesRuteados());
		Assert.assertEquals("El mensaje debería haber llegado pero descartado en destino", 1,
				nodoC.getCantidadDeMensajesRecibidosYDescartados());

		// Mejoramos la semántica del nodo intermedio
		nodoB.agregarOperadorDeFiltroParaInstancias();

		// Comprobamos que ahora no llega al nodo C
		final String otroMensaje = "Otro";
		nodoA.enviar(otroMensaje);

		nodoC.verificarQueNoLlego(mensaje);
		Assert.assertEquals("El segundo mensaje debería haber pasado por el nodo intermedio", 2,
				nodoC.getCantidadDeMensajesRuteados());
		Assert.assertEquals("El segundo mensaje nunca debería haber llegar al nodo C", 1,
				nodoC.getCantidadDeMensajesRecibidosYDescartados());

	}

	/**
	 * Prueba que al desconectarse físicamente el emisor sea notificado que ya no existe su receptor
	 * interesado<br>
	 * A->B->C
	 */
	@Test
	public void deberiaNotificarAlEmisorLaAusenciaDeReceptorSiSePierdeLaConectividad() {
		// Armamos la red inicial
		final NodoEnTest nodoA = crearNodo("A");
		nodoA.declararEnvioDeInstanciasDe(String.class);

		final NodoEnTest nodoC = crearNodo("C");
		nodoC.declararRecepcionDeInstanciasDe(String.class);

		final NodoEnTest nodoB = crearNodo("B");
		nodoA.conectarA(nodoB);
		nodoB.conectarA(nodoC);

		// Verificamos la pre-condición
		Assert.assertTrue("El nodo A debería saber que hay interesados en sus mensajes", nodoA.tieneReceptores());

		// Cambiamos la red
		nodoB.desconectarDe(nodoC);

		// Verificamos que se entere del cambio
		Assert.assertFalse("El nodo A debería saber que ya no hay interesados en sus mensajes", nodoA.tieneReceptores());
	}

	/**
	 * Prueba que al desconectarse físicamente el receptor sea notificado que ya no existe su emisor<br>
	 * A->B->C
	 */
	@Test
	public void deberiaNotificarAlReceptorLaAusenciaEmisorSiSePierdeLaConectividad() {
		// Armamos la red inicial
		final NodoEnTest nodoA = crearNodo("A");
		nodoA.declararEnvioDeInstanciasDe(String.class);

		final NodoEnTest nodoC = crearNodo("C");
		nodoC.declararRecepcionDeInstanciasDe(String.class);

		final NodoEnTest nodoB = crearNodo("B");
		nodoA.conectarA(nodoB);
		nodoB.conectarA(nodoC);

		// Verificamos la pre-condición
		Assert.assertTrue("El nodo C debería saber que hay emisores de sus mensajes", nodoC.tieneEmisores());

		// Cambiamos la red
		nodoA.desconectarDe(nodoB);

		// Verificamos que se entere del cambio
		Assert.assertFalse("El nodo C debería saber que ya no hay emisores de sus mensajes", nodoC.tieneEmisores());
	}

	/**
	 * Prueba que al producirse conectividad, el emisor sea notificado por receptor interesado
	 */
	@Test
	public void deberiaNotificarAEmisorSiSeProduceConectividadConReceptorInteresado() {
		// Armamos la red inicial
		final NodoEnTest nodoA = crearNodo("A");
		nodoA.declararEnvioDeInstanciasDe(String.class);

		final NodoEnTest nodoC = crearNodo("C");
		nodoC.declararRecepcionDeInstanciasDe(String.class);

		final NodoEnTest nodoB = crearNodo("B");
		nodoA.conectarA(nodoB);

		// Verificamos la pre-condición
		Assert.assertFalse("El nodo A debería saber que no hay interesados en sus mensajes", nodoA.tieneReceptores());

		// Cambiamos la red
		nodoB.conectarA(nodoC);

		// Verificamos que se entere del cambio
		Assert.assertTrue("El nodo A debería saber que ya hay interesados en sus mensajes", nodoA.tieneReceptores());
	}

	/**
	 * Prueba que al producirse conectividad, el receptor sea notificado de un posible emisor
	 */
	@Test
	public void deberiaNotificarAlReceptorSiSeProduceConectividadConEmisorInteresante() {
		// Armamos la red inicial
		final NodoEnTest nodoA = crearNodo("A");
		nodoA.declararEnvioDeInstanciasDe(String.class);

		final NodoEnTest nodoC = crearNodo("C");
		nodoC.declararRecepcionDeInstanciasDe(String.class);

		final NodoEnTest nodoB = crearNodo("B");
		nodoB.conectarA(nodoC);

		// Verificamos la pre-condición
		Assert.assertFalse("El nodo C debería saber que no hay emisores de sus mensajes", nodoC.tieneEmisores());

		// Cambiamos la red
		nodoA.conectarA(nodoB);

		// Verificamos que se entere del cambio
		Assert.assertTrue("El nodo C debería saber que ya hay emisores de sus mensajes", nodoC.tieneEmisores());
	}

}
