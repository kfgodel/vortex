/**
 * 09/05/2012 22:05:47 Copyright (C) 2011 Darío L. García
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
 * Esta clase prueba el funcionamiento y especificación de filtros de blue vortex
 * 
 * @author D. García
 */
public class TestBlueVortexFiltros extends BlueVortexTestSupport {

	/**
	 * Prueba que en memoria se pueda indicar un filtro por jerarquía de clases
	 */
	@Test
	public void siNoSeIndicanFiltrosDeberíaPermitirEnviarYRecibirCualquierCosa() {

	}

	/**
	 * Prueba que un mensaje no contemplado como enviable, puede ser enviado de todas formas y
	 * recibido por un receptor interesado
	 */
	@Test
	public void deberiaPermitirEnviarObjetosQueNoCumplenConElFiltroDeclaradoParaEnvios() {

	}

	/**
	 * Prueba que un receptor puede recibir un mensaje aunque no se si existen emisores del mismo
	 */
	@Test
	public void deberiaPermitirRecibirMensajesInteresantesAunqueElEmisorNoLosHayaDeclarado() {

	}

	/**
	 * Prueba que se pueda usar un filtro que sólo deja pasar instancias de una clase
	 */
	@Test
	public void deberíaPermitirRecibirInstanciasDeUnaClase() {

	}

	/**
	 * Prueba que se pueda usar un filtro que sólo deja pasar instancias de una interfaz
	 */
	@Test
	public void deberíaPermitirRecibirInstanciasDeUnaInterfaz() {

	}

	/**
	 * Prueba que sobre valores convertibles a String se puedan usar expresiones regulares como
	 * filtros
	 */
	@Test
	public void deberiaPermitirUsarExpresionesRegularesParaFiltrarValoresString() {

	}

	/**
	 * Prueba que se puedan componer filtros usando AND
	 */
	@Test
	public void deberiaPermitirUsarAndComoOperadorLogicoParaComponerFiltros() {

	}

	/**
	 * Prueba que se puedan componer filtros usando OR
	 */
	@Test
	public void deberiaPermitirUsarOrComoOperadorLogicoParaComponerFiltros() {

	}

	/**
	 * Prueba que se puedan filtrar mensajes por los valores que contenga en un atributo
	 */
	@Test
	public void deberiPermitirUsarContainsParaFiltrarMensajes() {

	}

	/**
	 * Prueba que se puedan filtrar mensajes por valores de un atributo
	 */
	@Test
	public void deberiaPermitirUsarEqualsParaCompararAtributos() {

	}
}
