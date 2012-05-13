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

import org.junit.Test;

/**
 * Esta clase prueba varios casos en los que existe un nodo en el medio de otros dos comunicantes
 * 
 * @author D. García
 */
public class TestRuteoConNodoIntermediario {
	/**
	 * Prueba que si existe un tercero en el medio el mensaje más simple llega igual
	 */
	@Test
	public void elMensajeMasSimpleDeberiaLlegarADestinoPasandoPorNodoIntermedio() {

	}

	/**
	 * Prueba que en una red con topología de "Y" el mensaje llegue a ambas puntas
	 */
	@Test
	public void elMensajeDeberiaLlegaraExtremosDeUnaY() {

	}

	/**
	 * Prueba que si el nodo del medio no puede discriminar la diferencia entre mensajes, de todas
	 * formas, el mensaje sea entregado correctamente
	 */
	@Test
	public void elMensajeDeberiaLlegarAlExtremoCorrectoAunqueElIntermedioNoEntiendaLaDiferenciaEntreMensajes() {

	}

	/**
	 * Verifica que el mensaje enviado no sea retornado al emisor
	 */
	@Test
	public void elMensajeNoDeberiaRetornarAlEmisorAunqueSeaInteresado() {

	}

}
