/**
 * 09/05/2012 20:56:05 Copyright (C) 2011 Darío L. García
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
 * Esta clase prueba las formas de uso de la API de blueVortex
 * 
 * @author D. García
 */
public class TestBlueVortexApi extends BlueVortexTestSupport {

	/**
	 * Prueba que la api permite crear una conexión a vortex para enviar y recibir cualquier cosa,
	 * sin tener demasiado código para ello
	 */
	@Test
	public void deberiaPermitirCrearLaConexionMasSimpleDeFormaSimple() {
	}

	/**
	 * Prueba que la api permite enviar cualquier objeto
	 */
	@Test
	public void deberiaPermitirEnviarCualquierObjetoDesdeUnaConexion() {
	}

	/**
	 * Prueba que la api permite recibir cualquier objeto de la red
	 */
	@Test
	public void deberiaPermitirRecibirCualquierObjetoDesdeUnaConexion() {

	}

	/**
	 * Prueba que la api brinda una forma de declarar que tipo de mensajes se envían
	 */
	@Test
	public void deberiaPermitirDeclararQueTipoDeMensajesSeEnviaranComoFiltro() {

	}

	/**
	 * Prueba que nos es necesaria una única declaración, pueden convivir varias a la vez como
	 * sub-filtros de la principal
	 */
	@Test
	public void deberiaPermitirTenerVariasDeclaracionesDeMensajesEnviablesComoSubFiltros() {

	}

	/**
	 * Prueba que la api brinda una forma de declarar los tipos de mensajes que se reciben
	 */
	@Test
	public void deberiaPermitirDeclararQueTipoDeMensajesSeAceptanComoFiltro() {

	}

	/**
	 * Prueba que la api permite modificar la declaración una vez que se está usando
	 */
	@Test
	public void deberiaPermitirModificarLaDeclaracionDeMensajesEnviablesDespuesDeCrearLaConexion() {

	}

	/**
	 * Prueba que la api permite modificar la declaración una vez que se está usando
	 */
	@Test
	public void deberiaPermitirModificarLaDeclaracionDeMensajesRecibiblesDespuesDeCrearLaConexion() {

	}

	/**
	 * Prueba que nos es necesaria una única declaración, pueden convivir varias a la vez como
	 * sub-filtros de la principal
	 */
	@Test
	public void deberiaPermitirTenerVariasDeclaracionesDeMensajesRecibiblesComoSubFiltros() {

	}

	/**
	 * Prueba que después de crear una conexión el código emisor puede ser notificado de la
	 * existencia o ausencia de receptores interesados.<br>
	 */
	@Test
	public void deberiaPermitirSaberCuandoHayReceptoresInteresadosEnMisMensajesEnviables() {

	}

	/**
	 * Prueba que después de crear una conexión el código receptor puede ser notificado de la
	 * existencia o ausencia de emisores interesantes.<br>
	 */
	@Test
	public void deberiaPermitirSaberCuandoHayEmisoresDeLosMensajesQueMeInteresan() {

	}

	/**
	 * Prueba que la api permite definir un handler por filtro
	 */
	@Test
	public void deberiaPermitirAsociarUnHandlerPorFiltro() {

	}

	/**
	 * Prueba que la api brinde un mecanismo de feedback sobre el envío de un mensaje
	 */
	@Test
	public void deberiaPermitirSaberSiElMensajeFueAceptadoPorLaRed() {

	}

	/**
	 * Prueba que la APi permita solicitar confirmaciones de recepción como algo opcional
	 */
	@Test
	public void deberiaPermitirIndicarSiSeNecesitanConfirmacionesDeRecepcion() {

	}

	/**
	 * Prueba que la api ofrece una manera de conocer los destinos alcanzados por un mensaje que
	 * solicita confirmación de recepción
	 */
	@Test
	public void deberiaPermitirConocerACuantosDestinosLlegoHastaElMomentoUnMensajeConConfirmacionDeRecepcion() {

	}

	/**
	 * Prueba que exista una Api para poder agregar conexiones entre nodos
	 */
	@Test
	public void deberiaPermitirConectarUnNodoALaTopologiaExistente() {

	}

	/**
	 * Prueba que exista una api para quitar conexiones entre nodos
	 */
	@Test
	public void deberiaPermitirQuitarUnNodoDeLaTopologiaExistente() {

	}

	/**
	 * Prueba que exista api para definir qué operadores entiende un nodo
	 */
	@Test
	public void deberiaPermitirAgregarOperadoresCustomAUnNodo() {

	}

	/**
	 * Prueba que exista forma de referenciar y utilizar los operadores custom en los filtros
	 */
	@Test
	public void deberiaPermitirUtilizarOperadoresCustomEnUnFiltro() {

	}
}
