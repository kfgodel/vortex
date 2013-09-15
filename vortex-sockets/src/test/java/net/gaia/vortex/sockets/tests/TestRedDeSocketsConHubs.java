/**
 * 02/06/2012 18:38:05 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.sockets.tests;

import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

import junit.framework.Assert;
import net.gaia.taskprocessor.api.processor.TaskProcessor;
import net.gaia.vortex.deprecated.ClienteDeNexoSocketViejo;
import net.gaia.vortex.deprecated.ClienteDeSocketVortexViejo;
import net.gaia.vortex.deprecated.MultiplexorSinDuplicadosViejo;
import net.gaia.vortex.deprecated.NexoSocketViejo;
import net.gaia.vortex.deprecated.NodoViejo;
import net.gaia.vortex.deprecated.PortalMapeadorViejo;
import net.gaia.vortex.deprecated.PortalViejo;
import net.gaia.vortex.deprecated.RealizarConexionesPorFueraViejo;
import net.gaia.vortex.deprecated.RealizarConexionesViejo;
import net.gaia.vortex.deprecated.ServidorDeNexoSocketViejo;
import net.gaia.vortex.deprecated.ServidorDeSocketVortexViejo;
import net.gaia.vortex.impl.helpers.VortexProcessorFactory;
import net.gaia.vortex.portal.tests.HandlerEncoladorDeStrings;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ar.com.dgarcia.lang.time.TimeMagnitude;
import ar.com.dgarcia.testing.FreePortFinder;

/**
 * Esta clase prueba el uso básico del nodo socket
 * 
 * @author D. García
 */
public class TestRedDeSocketsConHubs {
	private static final Logger LOG = LoggerFactory.getLogger(TestRedDeSocketsConHubs.class);

	private PortalViejo nodoEmisor;
	private PortalViejo nodoReceptor;

	private ClienteDeSocketVortexViejo socketConnector;
	private ServidorDeSocketVortexViejo socketAcceptor;

	private NodoViejo hubServidor;
	private NodoViejo hubCliente;

	private TaskProcessor processor;

	private ClienteDeNexoSocketViejo segundoCliente;
	private InetSocketAddress sharedTestAddress;

	@Before
	public void crearNodos() {
		final int freePort = FreePortFinder.getFreePort();
		sharedTestAddress = new InetSocketAddress(freePort);
		LOG.debug("Puerto libre para el test: {}", freePort);

		processor = VortexProcessorFactory.createProcessor();

		// Creamos el hub al que se conectan los nexos del servidor
		hubServidor = MultiplexorSinDuplicadosViejo.create(processor);
		// Creamos el portal receptor conectado al hub del servidor
		nodoReceptor = PortalMapeadorViejo.createForIOWith(processor, hubServidor);

		// Creamos el hub al que se conectan los nexos del cliente
		hubCliente = MultiplexorSinDuplicadosViejo.create(processor);
		// Creamos el portal emisor conectado al hub cliente
		nodoEmisor = PortalMapeadorViejo.createForOutputWith(processor, hubCliente);

		// Creamos el server de sockets que conectará los nexos entrantes al hub servidor
		socketAcceptor = ServidorDeNexoSocketViejo.create(processor, sharedTestAddress, RealizarConexionesViejo.con(hubServidor));
		socketAcceptor.aceptarConexionesRemotas();

		// Creamos el cliente de sockets que conectará los nexos salientes al hub cliente
		socketConnector = ClienteDeNexoSocketViejo.create(processor, sharedTestAddress, RealizarConexionesViejo.con(hubCliente));
		socketConnector.conectarASocketRomoto();
	}

	@After
	public void eliminarNodos() {
		if (segundoCliente != null) {
			segundoCliente.closeAndDispose();
		}
		socketAcceptor.closeAndDispose();
		socketConnector.closeAndDispose();
		processor.detener();
	}

	/**
	 * Prueba el caso más simple de recepción entre cliente y servidor a través de sockets
	 */
	@Test
	public void deberiaPermitirEnviarUnMensajeATravesDeNodoSockets() {
		final HandlerEncoladorDeStrings handlerReceptor = HandlerEncoladorDeStrings.create();
		nodoReceptor.recibirCon(handlerReceptor);

		final String mensajeEnviado = "texto de mensaje loco";
		nodoEmisor.enviar(mensajeEnviado);

		final Object mensajeRecibido = handlerReceptor.esperarPorMensaje(TimeMagnitude.of(10, TimeUnit.SECONDS));
		Assert.assertEquals("El enviado y recibido deberían ser iguales", mensajeEnviado, mensajeRecibido);
	}

	/**
	 * Prueba que agregando un portal receptor en el servidor también recibe el mensaje
	 */
	@Test
	public void deberiaRecibirElMensajeDesdeDosReceptoresDistintosEnElServidor() {
		final PortalMapeadorViejo receptor2 = PortalMapeadorViejo.createForIOWith(processor, hubServidor);
		final HandlerEncoladorDeStrings handlerReceptor2 = HandlerEncoladorDeStrings.create();
		receptor2.recibirCon(handlerReceptor2);

		verificarRecepcionEnServidorYEn(handlerReceptor2);
	}

	/**
	 * Verifica que al mandar un mensaje desde el cliente, se reciba en el servidor y también en el
	 * handler pasado
	 * 
	 * @param handlerReceptor2
	 *            El receptor adicional a verificar
	 */
	private void verificarRecepcionEnServidorYEn(final HandlerEncoladorDeStrings handlerReceptor2) {
		final HandlerEncoladorDeStrings handlerReceptor = HandlerEncoladorDeStrings.create();
		nodoReceptor.recibirCon(handlerReceptor);

		final String mensajeEnviado = "texto de mensaje loco";
		nodoEmisor.enviar(mensajeEnviado);

		final Object mensajeRecibido = handlerReceptor.esperarPorMensaje(TimeMagnitude.of(10, TimeUnit.SECONDS));
		Assert.assertEquals("El enviado y recibido en servidor deberían ser iguales", mensajeEnviado, mensajeRecibido);

		final Object mensajeRecibido2 = handlerReceptor2.esperarPorMensaje(TimeMagnitude.of(10, TimeUnit.SECONDS));
		Assert.assertEquals("El enviado y recibido en el handler adicional deberían ser iguales", mensajeEnviado,
				mensajeRecibido2);
	}

	/**
	 * Prueba que agregando un portal en el hub cliente también recibe el mensaje
	 */
	@Test
	public void deberiaRecibirElMensajeDesdeDosReceptoresDistintosUnoEnElClienteYOtroEnElServidor() {
		final PortalMapeadorViejo receptor2 = PortalMapeadorViejo.createForIOWith(processor, hubCliente);
		final HandlerEncoladorDeStrings handlerReceptor2 = HandlerEncoladorDeStrings.create();
		receptor2.recibirCon(handlerReceptor2);

		verificarRecepcionEnServidorYEn(handlerReceptor2);
	}

	/**
	 * Prueba que si dos clientes se conectan al mismo servidor, uno recibe también el mensaje
	 */
	@Test
	public void deberiaRecibirElMensajeDesdeDosReceptoresDistintosUnoEnElServidorYOtroEnOtroClienteSocket() {
		segundoCliente = ClienteDeNexoSocketViejo.create(processor, sharedTestAddress,
				RealizarConexionesPorFueraViejo.getInstancia());
		final NexoSocketViejo nexoDelSegundoCliente = segundoCliente.conectarASocketRomoto();

		final PortalMapeadorViejo receptor2 = PortalMapeadorViejo.createForIOWith(processor, nexoDelSegundoCliente);
		final HandlerEncoladorDeStrings handlerReceptor2 = HandlerEncoladorDeStrings.create();
		receptor2.recibirCon(handlerReceptor2);

		verificarRecepcionEnServidorYEn(handlerReceptor2);
	}

}
