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

import java.util.concurrent.TimeUnit;

import junit.framework.Assert;
import net.gaia.taskprocessor.api.TimeMagnitude;
import net.gaia.vortex.bluevortex.api.ConexionVortex;
import net.gaia.vortex.bluevortex.api.ReporteDeEntrega;
import net.gaia.vortex.bluevortex.api.async.AsyncValue;
import net.gaia.vortex.bluevortex.nn.HandlerConColaDeMensajes;

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
	public void deberiaPermitirEnvioYRecepcionSimple() {
		final HandlerConColaDeMensajes colaDeRecepcion = HandlerConColaDeMensajes.create();
		final ConexionVortex conexionReceptora = getVortex().crearConexion();
		conexionReceptora.setHandlerDeMensajes(colaDeRecepcion);

		final ConexionVortex conexionEmisora = getVortex().crearConexion();

		final Object mensajeEnviado = new Object();
		final ReporteDeEntrega reporte = conexionEmisora.enviar(mensajeEnviado);
		final AsyncValue<Long> cantidadEntregados = reporte.getCantidadDeEntregados();
		Assert.assertEquals("Debería indicar que el mensaje fue entregado a un receptor", 1, cantidadEntregados
				.waitForValueUpTo(TimeMagnitude.of(1, TimeUnit.MINUTES)).intValue());

		final Object mensajeRecibido = colaDeRecepcion.getCola().poll();
		Assert.assertNotNull("Debería existir un mensaje recibido", mensajeRecibido);
		Assert.assertSame("El mensaje recibido debería ser la misma instancia enviada", mensajeEnviado, mensajeRecibido);
	}

	/**
	 * Verifica que al emisor no le llegue el mensaje que mandó.<br>
	 * 
	 */
	@Test
	public void elEmisorNoDeberiaRecibirSuPropioMensaje() {
		// TODO Existe la posibilidad que el emisor quiera recibir sus propios mensajes?
	}

}
