/**
 * 09/05/2012 22:04:38 Copyright (C) 2011 Darío L. García
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

import net.gaia.vortex.bluevortex.api.ConexionVortex;

import org.junit.Test;

/**
 * Esta clase prueba ejemplos de ruteos de mensajes que deben ser recibidos por ciertos interesados
 * y por otros no
 * 
 * @author D. García
 */
public class TestBlueVortexRuteo extends BlueVortexTestSupport {

	/**
	 * Prueba que el caso más simple de uno a uno sea resuelto
	 */
	@Test
	public void deberiaPermitirRecibirUnMensajeSinFiltros() {
		final ConexionVortex conexionEmisora = getVortex().crearConexion();

		final ConexionVortex conexionReceptora = getVortex().crearConexion();
	}

	/**
	 * Verifica que al emisor no le llegue el mensaje que mandó
	 */
	@Test
	public void elEmisorNoDeberiaRecibirSuPropioMensaje() {

	}

}
