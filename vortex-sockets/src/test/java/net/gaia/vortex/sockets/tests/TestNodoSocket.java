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
import net.gaia.vortex.core.api.NodoPortal;
import net.gaia.vortex.core.impl.NodoPortalImpl;
import net.gaia.vortex.core.tests.HandlerEncolador;
import net.gaia.vortex.sockets.api.NodoSocketCliente;
import net.gaia.vortex.sockets.api.NodoSocketServidor;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ar.com.dgarcia.lang.time.TimeMagnitude;

/**
 * Esta clase prueba el uso básico del nodo socket
 * 
 * @author D. García
 */
public class TestNodoSocket {

	private NodoPortal nodoEmisor;
	private NodoSocketCliente nodoCliente;

	private NodoPortal nodoReceptor;
	private NodoSocketServidor nodoServidor;

	@Before
	public void crearNodos() {
		final InetSocketAddress sharedTestAddress = new InetSocketAddress(10488);
		nodoServidor = NodoSocketServidor.create(sharedTestAddress);
		nodoCliente = NodoSocketCliente.create(sharedTestAddress);

		nodoEmisor = NodoPortalImpl.create();
		nodoReceptor = NodoPortalImpl.create();

		nodoEmisor.conectarCon(nodoCliente);
		nodoCliente.conectarCon(nodoEmisor);

		nodoReceptor.conectarCon(nodoServidor);
		nodoServidor.conectarCon(nodoReceptor);
	}

	@After
	public void eliminarNodos() {
		nodoServidor.closeAndDispose();
		nodoCliente.closeAndDispose();
	}

	@Test
	public void deberiaPermitirEnviarUnMensajeATravesDeNodoSockets() {
		final HandlerEncolador handlerReceptor = HandlerEncolador.create();
		nodoReceptor.setHandlerDeMensajesVecinos(handlerReceptor);

		final String mensajeEnviado = "texto de mensaje loco";
		nodoEmisor.enviarAVecinos(mensajeEnviado);

		final Object mensajeRecibido = handlerReceptor.esperarPorMensaje(TimeMagnitude.of(10, TimeUnit.SECONDS));
		Assert.assertEquals("El enviado y recibido deberían ser iguales", mensajeEnviado, mensajeRecibido);
	}

	@Test
	public void deberiaRecibirElMensajeDesdeDosReceptoresDistintosEnElServidor() {
		final NodoPortalImpl nodoReceptor2 = NodoPortalImpl.create();
		nodoReceptor2.conectarCon(nodoServidor);
		nodoServidor.conectarCon(nodoReceptor2);

		final HandlerEncolador handlerReceptor2 = HandlerEncolador.create();
		nodoReceptor2.setHandlerDeMensajesVecinos(handlerReceptor2);

		final HandlerEncolador handlerReceptor = HandlerEncolador.create();
		nodoReceptor.setHandlerDeMensajesVecinos(handlerReceptor);

		final String mensajeEnviado = "texto de mensaje loco";
		nodoEmisor.enviarAVecinos(mensajeEnviado);

		final Object mensajeRecibido = handlerReceptor.esperarPorMensaje(TimeMagnitude.of(10, TimeUnit.SECONDS));
		Assert.assertEquals("El enviado y recibido deberían ser iguales", mensajeEnviado, mensajeRecibido);

		final Object mensajeRecibido2 = handlerReceptor2.esperarPorMensaje(TimeMagnitude.of(10, TimeUnit.SECONDS));
		Assert.assertEquals("El enviado y recibido deberían ser iguales", mensajeEnviado, mensajeRecibido2);
	}

	@Test
	public void deberiaRecibirElMensajeDesdeDosReceptoresDistintosUnoEnElClienteYOtroEnElServidor() {
		final NodoPortalImpl nodoReceptor2 = NodoPortalImpl.create();
		nodoReceptor2.conectarCon(nodoCliente);
		nodoCliente.conectarCon(nodoReceptor2);

		final HandlerEncolador handlerReceptor2 = HandlerEncolador.create();
		nodoReceptor2.setHandlerDeMensajesVecinos(handlerReceptor2);

		final HandlerEncolador handlerReceptor = HandlerEncolador.create();
		nodoReceptor.setHandlerDeMensajesVecinos(handlerReceptor);

		final String mensajeEnviado = "texto de mensaje loco";
		nodoEmisor.enviarAVecinos(mensajeEnviado);

		final Object mensajeRecibido = handlerReceptor.esperarPorMensaje(TimeMagnitude.of(10, TimeUnit.SECONDS));
		Assert.assertEquals("El enviado y recibido deberían ser iguales", mensajeEnviado, mensajeRecibido);

		final Object mensajeRecibido2 = handlerReceptor2.esperarPorMensaje(TimeMagnitude.of(10, TimeUnit.SECONDS));
		Assert.assertEquals("El enviado y recibido deberían ser iguales", mensajeEnviado, mensajeRecibido2);
	}

}
