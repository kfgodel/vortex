package net.gaia.vortex.http.impl.cliente.server;

import net.gaia.vortex.http.impl.cliente.server.comandos.CerrarSesionCliente;
import net.gaia.vortex.http.impl.cliente.server.comandos.CrearSesionCliente;
import net.gaia.vortex.http.impl.cliente.server.comandos.IntercambiarMensajesCliente;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ar.dgarcia.http.simple.impl.ApacheResponseProvider;

/**
 * 29/07/2012 21:26:33 Copyright (C) 2011 Darío L. García
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

/**
 * Esta clase testea el cliente contra el servidor online
 * 
 * @author D. García
 */
@Ignore("Para ejecutar este test se requiere el servidor mosquito")
public class TestProxyServer {
	private static final Logger LOG = LoggerFactory.getLogger(TestProxyServer.class);

	@Test
	public void deberiaRespnderCorrectamenteALosComandos() {
		final ServerVortexHttpRemoto servidor = ServerVortexHttpRemoto.create("http://kfgodel.info:62626",
				ApacheResponseProvider.create());
		// Creamos una sesion
		final CrearSesionCliente comandoCrearSesion = CrearSesionCliente.create(null);
		servidor.enviarComando(comandoCrearSesion);

		final String idDeSesion = comandoCrearSesion.getIdDeSesionCreada();
		LOG.debug("Sesion creada: {}", idDeSesion);
		Assert.assertNotNull(idDeSesion);

		// Pedimos nuevos mensajes
		final IntercambiarMensajesCliente comandoIntercambio = IntercambiarMensajesCliente.create(idDeSesion, null);
		servidor.enviarComando(comandoIntercambio);

		final String recibidos = comandoIntercambio.getMensajesEnJsonRecibidos();
		LOG.debug("Mensajes recibidos: {}", recibidos);
		Assert.assertNotNull(recibidos);

		// Finalmente cerramos la sesion
		final CerrarSesionCliente comandoCerrar = CerrarSesionCliente.create(idDeSesion);
		servidor.enviarComando(comandoCerrar);
	}
}
