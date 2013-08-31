/**
 * 26/01/2013 15:18:37 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.http.impl.cliente.server;

import java.util.concurrent.TimeUnit;

import junit.framework.Assert;
import net.gaia.taskprocessor.api.processor.TaskProcessor;
import net.gaia.vortex.api.condiciones.Condicion;
import net.gaia.vortex.core.external.VortexProcessorFactory;
import net.gaia.vortex.http.impl.moleculas.RouterClienteHttp;
import net.gaia.vortex.http.impl.moleculas.RouterServerHttp;
import net.gaia.vortex.portal.tests.HandlerEncolador;
import net.gaia.vortex.router.api.tests.MensajeParaTestDeRuteo;
import net.gaia.vortex.router.impl.moleculas.PortalBidi;
import net.gaia.vortex.sets.impl.condiciones.ValorEsperadoEn;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ar.com.dgarcia.coding.exceptions.UnsuccessfulWaitException;
import ar.com.dgarcia.lang.time.TimeMagnitude;
import ar.com.dgarcia.testing.FreePortFinder;
import ar.dgarcia.http.simple.impl.ApacheResponseProvider;

/**
 * Esta clase prueba la conexión básica entre routers http
 * 
 * @author D. García
 */
public class TestRouterHttp {
	private static final Logger LOG = LoggerFactory.getLogger(TestRouterHttp.class);

	private TaskProcessor processor;

	private RouterServerHttp routerServidor;

	private RouterClienteHttp routerCliente;

	private PortalBidi emisor;

	private PortalBidi receptor;

	@Before
	public void crearComponentes() {
		processor = VortexProcessorFactory.createProcessor();

		final int freePort = FreePortFinder.getFreePort();
		LOG.debug("Puerto libre para tests: {}", freePort);
		routerServidor = RouterServerHttp.createAndAcceptRequestsOnPort(freePort, processor);
		routerCliente = RouterClienteHttp.createAndConnectTo("http://localhost:" + freePort + "/", processor,
				ApacheResponseProvider.create());
		emisor = PortalBidi.create(processor);
		receptor = PortalBidi.create(processor);
	}

	@After
	public void liberarRecursos() {
		routerCliente.cerrarYLiberar();
		routerServidor.cerrarYLiberar();
		processor.detener();
	}

	@Test
	public void elMensajeDeberiaLlegarAlOtroLadoDelSocketSiHayInteresado() throws InterruptedException {
		// El receptor quiere mensajes con atributo="hola"
		final HandlerEncolador<MensajeParaTestDeRuteo> handlerReceptor = new HandlerEncolador<MensajeParaTestDeRuteo>() {

			@Override
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
		final Object mensajeRecibidoPor1 = handlerReceptor.esperarPorMensaje(TimeMagnitude.of(3, TimeUnit.SECONDS));
		Assert.assertEquals("El receptor debería haber recibido el mensaje", mensajeEnviado, mensajeRecibidoPor1);
	}

	@Test
	public void noDeberiaLlegarUnMensajeQueNoInteresa() throws InterruptedException {
		// El receptor quiere mensajes con atributo="chau"
		final HandlerEncolador<MensajeParaTestDeRuteo> handlerReceptor = new HandlerEncolador<MensajeParaTestDeRuteo>() {

			@Override
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
			handlerReceptor.esperarPorMensaje(TimeMagnitude.of(3, TimeUnit.SECONDS));
			Assert.fail("No deberíamos recibir el mensaje");
		} catch (final UnsuccessfulWaitException e) {
			// Es la excepcion esperada
		}
	}

}
