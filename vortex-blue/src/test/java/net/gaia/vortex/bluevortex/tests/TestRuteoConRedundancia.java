/**
 * 12/05/2012 22:59:11 Copyright (C) 2011 Darío L. García
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
 * Esta clase prueba varios casos de ruteo en los que se verifica que el mensaje llegue aunque haya
 * pérdida de topología en la red
 * 
 * @author D. García
 */
public class TestRuteoConRedundancia {

	/**
	 * Prueba que en una conectividad doble entre emisor y receptor, si se cae una de las opciones
	 * de ruteo, se usa la otra
	 */
	@Test
	public void deberiaRecibirElMensajeSiSeCaeUnoDeDosCaminos() {

	}

	/**
	 * Prueba que cortando la red por un lado, pero agregando conectividad por otro, el mensaje
	 * llega
	 */
	@Test
	public void deberiaRecibirElMensajeSiSePierdeElCaminoPeroSeGanaUnoAlternativo() {

	}

	/**
	 * Prueba que en una conexión triángulo entre un emisor y dos receptores, si un receptor pierde
	 * la conectividad directa con el emisor, el mensaje es entregado de todas maneras por el otro
	 * receptor
	 */
	@Test
	public void siSeCaeUnLadoDelTrianguloElmensajeDeberiaLlegarALosDosReceptores() {

	}
}
