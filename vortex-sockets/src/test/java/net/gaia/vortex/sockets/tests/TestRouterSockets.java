/**
 * 26/01/2013 12:09:58 Copyright (C) 2011 Darío L. García
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
import net.gaia.taskprocessor.api.TaskProcessor;
import net.gaia.vortex.core.api.condiciones.Condicion;
import net.gaia.vortex.core.external.VortexProcessorFactory;
import net.gaia.vortex.portal.tests.HandlerEncolador;
import net.gaia.vortex.router.api.tests.MensajeParaTestDeRuteo;
import net.gaia.vortex.router.impl.moleculas.PortalBidi;
import net.gaia.vortex.sets.impl.condiciones.ValorEsperadoEn;
import net.gaia.vortex.sockets.impl.moleculas.RouterSocket;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ar.com.dgarcia.coding.exceptions.UnsuccessfulWaitException;
import ar.com.dgarcia.lang.time.TimeMagnitude;

/**
 * Esta clase prueba el funcionamiento basico del {@link RouterSocket}
 * 
 * @author D. García
 */
public class TestRouterSockets {

	public static final int PUERTO_BASE_TESTS = 21113;

	private TaskProcessor processor;

	private RouterSocket routerServidor;

	private RouterSocket routerCliente;

	private PortalBidi emisor;

	private PortalBidi receptor;

	@Before
	public void crearComponentes() {
		processor = VortexProcessorFactory.createProcessor();
		routerServidor = RouterSocket.createAndListenTo(new InetSocketAddress(PUERTO_BASE_TESTS), processor);
		routerCliente = RouterSocket.createAndConnectTo(new InetSocketAddress(PUERTO_BASE_TESTS), processor);
		emisor = PortalBidi.create(processor);
		receptor = PortalBidi.create(processor);
	}

	@After
	public void liberarRecursos() {
		routerCliente.closeAndDispose();
		routerServidor.closeAndDispose();
		processor.detener();
	}

	@Test
	public void elMensajeDeberiaLlegarAlOtroLadoDelSocketSiHayInteresado() throws InterruptedException {
		// El receptor quiere mensajes con atributo="hola"
		final HandlerEncolador<MensajeParaTestDeRuteo> handlerReceptor = new HandlerEncolador<MensajeParaTestDeRuteo>() {
			
			public Condicion getCondicionSuficiente() {
				return ValorEsperadoEn.elAtributo(MensajeParaTestDeRuteo.atributo_FIELD, "hola");
			}
		};
		receptor.recibirCon(handlerReceptor);

		// Conectamos los componentes
		emisor.conectarCon(routerCliente);
		routerCliente.conectarCon(emisor);
		receptor.conectarCon(routerServidor);
		routerServidor.conectarCon(receptor);

		// Esperamos que se intercambien los filtros
		Thread.sleep(2000);

		final MensajeParaTestDeRuteo mensajeEnviado = new MensajeParaTestDeRuteo("hola");
		emisor.enviar(mensajeEnviado);

		// Verificamos que haya llegado
		final Object mensajeRecibidoPor1 = handlerReceptor.esperarPorMensaje(TimeMagnitude.of(1, TimeUnit.SECONDS));
		Assert.assertEquals("El receptor debería haber recibido el mensaje", mensajeEnviado, mensajeRecibidoPor1);
	}

	@Test
	public void noDeberiaLlegarUnMensajeQueNoInteresa() throws InterruptedException {
		// El receptor quiere mensajes con atributo="chau"
		final HandlerEncolador<MensajeParaTestDeRuteo> handlerReceptor = new HandlerEncolador<MensajeParaTestDeRuteo>() {
			
			public Condicion getCondicionSuficiente() {
				return ValorEsperadoEn.elAtributo(MensajeParaTestDeRuteo.atributo_FIELD, "chau");
			}
		};
		receptor.recibirCon(handlerReceptor);

		// Conectamos los componentes
		emisor.conectarCon(routerCliente);
		routerCliente.conectarCon(emisor);
		receptor.conectarCon(routerServidor);
		routerServidor.conectarCon(receptor);

		// Esperamos que se intercambien los filtros
		Thread.sleep(2000);

		final MensajeParaTestDeRuteo mensajeEnviado = new MensajeParaTestDeRuteo("hola");
		emisor.enviar(mensajeEnviado);

		// Verificamos que haya llegado
		try {
			handlerReceptor.esperarPorMensaje(TimeMagnitude.of(1, TimeUnit.SECONDS));
			Assert.fail("No deberíamos recibir el mensaje");
		} catch (final UnsuccessfulWaitException e) {
			// Es la excepcion esperada
		}
	}
}