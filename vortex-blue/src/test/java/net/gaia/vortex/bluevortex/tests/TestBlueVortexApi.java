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

import junit.framework.Assert;
import net.gaia.vortex.bluevortex.api.ConexionEnPreparacion;
import net.gaia.vortex.bluevortex.api.ConexionVortex;
import net.gaia.vortex.bluevortex.nn.HandlerConColaDeMensajes;
import net.gaia.vortex.bluevortex.nn.TrueMessageFilter;

import org.junit.Test;

/**
 * Esta clase prueba las formas de uso de la API de blueVortex
 * 
 * @author D. García
 */
public class TestBlueVortexApi extends BlueVortexTestSupport {

	/**
	 * Prueba que api no pida excesivas llamadas para el uso más simple
	 */
	@Test
	public void deberiaPermitirCrearUnaConexionSimpleParaEnvios() {
		final ConexionVortex conexion = getVortex().crearConexion();
		Assert.assertNotNull(conexion);
	}

	@Test
	public void deberiaPermitirCrearUnaConexionParaRecibirTodosLosMensajes() {
		final ConexionEnPreparacion preConexion = getVortex().prepararConexion();
		preConexion.setMessageHandler(HandlerConColaDeMensajes.create());
		preConexion.setMessageFilter(TrueMessageFilter.create());
		final ConexionVortex conexionReceptora = preConexion.crearConexion();
		Assert.assertNotNull(conexionReceptora);
	}
}
