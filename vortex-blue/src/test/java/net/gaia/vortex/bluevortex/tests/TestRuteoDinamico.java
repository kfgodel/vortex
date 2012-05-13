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

import org.junit.Test;

/**
 * Esta clase prueba que si hay variaciones en la topología de la red los mensajes lleguen si es
 * posible
 * 
 * @author D. García
 */
public class TestRuteoDinamico {

	/**
	 * Prueba que al producir la conectividad entre partes el mensaje llega a más destinos
	 */
	@Test
	public void deberiaPermitirrecibirUnMensajeSiSeAgregaUnNodoIntermedio() {

	}

	/**
	 * Prueba que al cortar la conectividad el mensaje no es recibido
	 */
	@Test
	public void deberiaDejarDeRecibirUnMensajeSiSeQuitaElNodoNexo() {

	}

	/**
	 * Prueba que si un nodo intermedio tiene mayor capacidad para entender la semántica de los
	 * mensajes, entonces puede elegir mejor a quién le envía los mensajes
	 */
	@Test
	public void deberiaCambiarMejorarElRuteoSiElNodoIntermedioEntiendeMasOperadores() {

	}

	/**
	 * Prueba que al desconectarse físicamente el emisor sea notificado que ya no existe su receptor
	 * interesado
	 */
	@Test
	public void deberiaNotificarAlEmisorLaAusenciaDereceptorSiSePierdeLaConectividad() {

	}

	/**
	 * Prueba que al desconectarse físicamente el receptor sea notificado que ya no existe su emisor
	 */
	@Test
	public void deberiaNotificarAlReceptorLaAusenciaEmisorSiSePierdeLaConectividad() {

	}

	/**
	 * Prueba que al producirse conectividad, el emisor sea notificado por receptor interesado
	 */
	@Test
	public void deberiaNotificarAEmisorSiSeProduceConectividadConReceptorInteresado() {

	}

	/**
	 * Prueba que al producirse conectividad, el receptor sea notificado de un posible emisor
	 */
	@Test
	public void deberiaNotificarAlReceptorSiSeProduceConectividadConEmisorInteresante() {

	}

}
