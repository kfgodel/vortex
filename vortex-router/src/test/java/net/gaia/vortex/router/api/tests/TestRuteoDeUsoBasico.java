/**
 * 18/12/2012 19:36:56 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.router.api.tests;

import java.util.concurrent.TimeUnit;

import junit.framework.Assert;
import net.gaia.vortex.core.api.condiciones.Condicion;
import net.gaia.vortex.portal.tests.HandlerEncolador;
import net.gaia.vortex.portal.tests.HandlerEncoladorDeStrings;
import net.gaia.vortex.router.api.moleculas.PortalBidireccional;
import net.gaia.vortex.router.api.moleculas.Router;
import net.gaia.vortex.router.api.tests.listeners.ListenerDeCambioDeFiltrosConCola;
import net.gaia.vortex.router.api.tests.listeners.ListenerDeRuteoEnPasos;
import net.gaia.vortex.sets.impl.condiciones.ValorEsperadoEn;
import net.gaia.vortex.sets.reflection.accessors.PropertyAccessor;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ar.com.dgarcia.coding.exceptions.TimeoutExceededException;
import ar.com.dgarcia.coding.exceptions.UnsuccessfulWaitException;
import ar.com.dgarcia.lang.time.TimeMagnitude;

/**
 * Esta clase contiene algunos tests para verificar el correcto ruteo del esquema con routers
 * 
 * @author D. García
 */
public class TestRuteoDeUsoBasico {

	private PortalBidireccional emisor;
	private PortalBidireccional receptorPositivo1;
	private PortalBidireccional receptorNegativo1;
	private Router routerCentral;

	@Before
	public void crearNodos() {

	}

	@After
	public void eliminarNodos() {

	}

	public static class MensajeDeRuteoParaTest {
		public String atributo;
		public static final String atributo_FIELD = "atributo";

		public MensajeDeRuteoParaTest(final String valor) {
			this.atributo = valor;
		}
	}

	@Test
	public void elMensajeDeberiaLlegarSiNoSeDeclaraNingunFiltro() {
		// Definimos el handler para recibir el mensaje
		final HandlerEncoladorDeStrings handlerReceptor1 = HandlerEncoladorDeStrings.create();
		receptorPositivo1.recibirCon(handlerReceptor1);

		// Interconectamos las partes
		emisor.conectarCon(routerCentral);
		routerCentral.conectarCon(emisor);
		receptorPositivo1.conectarCon(routerCentral);
		routerCentral.conectarCon(receptorPositivo1);

		// Mandamos el mensaje
		final String mensajeEnviado = "Hola manola";
		emisor.enviar(mensajeEnviado);

		// Verificamos que haya llegado
		final Object mensajeRecibidoPor1 = handlerReceptor1.esperarPorMensaje(TimeMagnitude.of(1, TimeUnit.SECONDS));
		Assert.assertEquals("El primer receptor debería haber recibido el mensaje", mensajeEnviado, mensajeRecibidoPor1);
	}

	@Test
	public void elMensajeDeberiaLlegarSiSeDeclaraUnFiltroPositivo() {
		// Definimos el listener para saber cuando el router adaptó sus filtros
		final ListenerDeCambioDeFiltrosConCola listenerFiltrosRouter = new ListenerDeCambioDeFiltrosConCola();
		routerCentral.setListenerDeFiltrosRemotos(listenerFiltrosRouter);

		// Interconectamos las partes
		emisor.conectarCon(routerCentral);
		routerCentral.conectarCon(emisor);
		receptorPositivo1.conectarCon(routerCentral);
		routerCentral.conectarCon(receptorPositivo1);

		// Le definimos al receptor qué es lo que queremos recibir
		final String valorEsperado = "hola";
		final HandlerEncolador<MensajeDeRuteoParaTest> handlerReceptor = new HandlerEncolador<MensajeDeRuteoParaTest>() {
			@Override
			public Condicion getCondicionSuficiente() {
				return ValorEsperadoEn.create(valorEsperado,
						PropertyAccessor.create(MensajeDeRuteoParaTest.atributo_FIELD));
			}
		};
		receptorPositivo1.recibirCon(handlerReceptor);

		// Esperamos que el router adapte sus filtros según lo que pide el receptor
		listenerFiltrosRouter.esperarPorCambio(TimeMagnitude.of(1, TimeUnit.SECONDS));

		// Mandamos el mensaje
		final MensajeDeRuteoParaTest mensajeEnviado = new MensajeDeRuteoParaTest(valorEsperado);
		emisor.enviar(mensajeEnviado);

		// Verificamos que haya llegado
		final Object mensajeRecibidoPor1 = handlerReceptor.esperarPorMensaje(TimeMagnitude.of(1, TimeUnit.SECONDS));
		Assert.assertEquals("El primer receptor debería haber recibido el mensaje", mensajeEnviado, mensajeRecibidoPor1);
	}

	@Test
	public void elMensajeNoDeberiaLlegarSiSeDeclaraUnFiltroNegativo() {
		// Definimos el listener para saber cuando el router adaptó sus filtros
		final ListenerDeCambioDeFiltrosConCola listenerFiltrosRouter = new ListenerDeCambioDeFiltrosConCola();
		routerCentral.setListenerDeFiltrosRemotos(listenerFiltrosRouter);

		// Interconectamos las partes
		emisor.conectarCon(routerCentral);
		routerCentral.conectarCon(emisor);
		receptorPositivo1.conectarCon(routerCentral);
		routerCentral.conectarCon(receptorPositivo1);

		// Le definimos al receptor qué es lo que queremos recibir
		final String valorEsperado = "hola";
		final HandlerEncolador<MensajeDeRuteoParaTest> handlerReceptor = new HandlerEncolador<MensajeDeRuteoParaTest>() {
			@Override
			public Condicion getCondicionSuficiente() {
				return ValorEsperadoEn.create("!" + valorEsperado,
						PropertyAccessor.create(MensajeDeRuteoParaTest.atributo_FIELD));
			}
		};
		receptorPositivo1.recibirCon(handlerReceptor);

		// Esperamos que el router adapte sus filtros según lo que pide el receptor
		listenerFiltrosRouter.esperarPorCambio(TimeMagnitude.of(1, TimeUnit.SECONDS));

		// Mandamos el mensaje
		final MensajeDeRuteoParaTest mensajeEnviado = new MensajeDeRuteoParaTest(valorEsperado);
		emisor.enviar(mensajeEnviado);

		// Verificamos que no llega
		try {
			handlerReceptor.esperarPorMensaje(TimeMagnitude.of(1, TimeUnit.SECONDS));
		} catch (final UnsuccessfulWaitException e) {
			// Es la excepción que esperabamos
		}
	}

	@Test
	public void elMensajeNoDeberiaSalirDelPortalSiNoHayInteresado() {
		// Definimos el listener para saber cuando el portal adapta sus filtros a lo que diga el
		// router
		final ListenerDeCambioDeFiltrosConCola listenerFiltrosPortal = new ListenerDeCambioDeFiltrosConCola();
		emisor.setListenerDeFiltrosRemotos(listenerFiltrosPortal);

		// Definimos el listener para saber los pasos de ruteo del portal
		final ListenerDeRuteoEnPasos listenerDeRuteo = ListenerDeRuteoEnPasos.create();
		emisor.setListenerDeRuteos(listenerDeRuteo);

		// Interconectamos las partes
		emisor.conectarCon(routerCentral);
		routerCentral.conectarCon(emisor);

		// Esperamos que el portal sea notificado de los filtros del router
		listenerFiltrosPortal.esperarPorCambio(TimeMagnitude.of(1, TimeUnit.SECONDS));

		// Mandamos el mensaje
		final MensajeDeRuteoParaTest mensajeEnviado = new MensajeDeRuteoParaTest("hola");
		emisor.enviar(mensajeEnviado);

		listenerDeRuteo.esperarRuteo(emisor, routerCentral, TimeMagnitude.of(1, TimeUnit.SECONDS));
	}

	@Test
	public void elMensajeNoDeberiaEntregarseAPortalNoInteresadoYSiAlInteresado() {
		// Definimos el listener para saber cuando el portal adapta sus filtros a lo que diga el
		// router
		final ListenerDeCambioDeFiltrosConCola listenerFiltrosEmisor = new ListenerDeCambioDeFiltrosConCola();
		emisor.setListenerDeFiltrosRemotos(listenerFiltrosEmisor);

		// Definimos el listener para saber los pasos de ruteo del router
		final ListenerDeRuteoEnPasos listenerDeRuteo = ListenerDeRuteoEnPasos.create();
		routerCentral.setListenerDeRuteos(listenerDeRuteo);

		// Interconectamos las partes
		receptorPositivo1.conectarCon(routerCentral);
		routerCentral.conectarCon(receptorPositivo1);

		receptorNegativo1.conectarCon(routerCentral);
		routerCentral.conectarCon(receptorNegativo1);

		emisor.conectarCon(routerCentral);
		routerCentral.conectarCon(emisor);

		// Esperamos que el emisor sea notificado de los filtros del router
		listenerFiltrosEmisor.esperarPorCambio(TimeMagnitude.of(1, TimeUnit.SECONDS));

		// Definimos qué quiere recibir cada receptor
		final String valorEsperado = "hola";
		final HandlerEncolador<MensajeDeRuteoParaTest> handlerReceptorPositivo = new HandlerEncolador<MensajeDeRuteoParaTest>() {
			@Override
			public Condicion getCondicionSuficiente() {
				return ValorEsperadoEn.create(valorEsperado,
						PropertyAccessor.create(MensajeDeRuteoParaTest.atributo_FIELD));
			}
		};
		receptorPositivo1.recibirCon(handlerReceptorPositivo);

		final String valorNoEsperado = "chau";
		final HandlerEncolador<MensajeDeRuteoParaTest> handlerReceptorNegativo = new HandlerEncolador<MensajeDeRuteoParaTest>() {
			@Override
			public Condicion getCondicionSuficiente() {
				return ValorEsperadoEn.create(valorNoEsperado,
						PropertyAccessor.create(MensajeDeRuteoParaTest.atributo_FIELD));
			}
		};
		receptorNegativo1.recibirCon(handlerReceptorNegativo);

		// Esperamos que el emisor sea notificado de los filtros del router
		listenerFiltrosEmisor.esperarPorCambio(TimeMagnitude.of(1, TimeUnit.SECONDS));

		// Mandamos el mensaje
		final MensajeDeRuteoParaTest mensajeEnviado = new MensajeDeRuteoParaTest("hola");
		emisor.enviar(mensajeEnviado);

		// Esperamos que sea ruteado
		listenerDeRuteo.esperarRuteo(routerCentral, receptorPositivo1, TimeMagnitude.of(1, TimeUnit.SECONDS));

		// Verificamos que haya llegado al receptor correcto
		final Object mensajeRecibidoPor1 = handlerReceptorPositivo.esperarPorMensaje(TimeMagnitude.of(1,
				TimeUnit.SECONDS));
		Assert.assertEquals("El primer receptor debería haber recibido el mensaje", mensajeEnviado, mensajeRecibidoPor1);

		// Verificamos que no se hace ruteo al receptor negativo
		try {
			listenerDeRuteo.esperarRuteo(routerCentral, receptorNegativo1, TimeMagnitude.of(1, TimeUnit.SECONDS));
			Assert.fail("No debería existir el ruteo");
		} catch (final TimeoutExceededException e) {
			// Excepción correcta
		}

		// Verificamos que no recibió el mensaje
		try {
			handlerReceptorNegativo.esperarPorMensaje(TimeMagnitude.of(1, TimeUnit.SECONDS));
		} catch (final UnsuccessfulWaitException e) {
			// Es la excepción que esperabamos
		}
	}

}
