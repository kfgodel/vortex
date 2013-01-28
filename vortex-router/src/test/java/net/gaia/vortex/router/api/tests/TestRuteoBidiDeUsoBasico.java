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
import net.gaia.taskprocessor.api.TaskProcessor;
import net.gaia.vortex.core.api.condiciones.Condicion;
import net.gaia.vortex.core.external.VortexProcessorFactory;
import net.gaia.vortex.core.impl.condiciones.SiempreFalse;
import net.gaia.vortex.portal.tests.HandlerEncolador;
import net.gaia.vortex.router.api.moleculas.PortalBidireccional;
import net.gaia.vortex.router.api.moleculas.Router;
import net.gaia.vortex.router.api.tests.listeners.ListenerDeRuteoEnPasos;
import net.gaia.vortex.router.impl.moleculas.PortalBidi;
import net.gaia.vortex.router.impl.moleculas.RouterBidi;
import net.gaia.vortex.router.impl.moleculas.listeners.ConexionBidi;
import net.gaia.vortex.router.impl.moleculas.listeners.EsperarConexionBidi;
import net.gaia.vortex.router.impl.moleculas.listeners.EsperarFiltro;
import net.gaia.vortex.sets.impl.condiciones.OrCompuesto;
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
public class TestRuteoBidiDeUsoBasico {

	private PortalBidireccional emisor;
	private PortalBidireccional receptorPositivo1;
	private PortalBidireccional receptorNegativo1;
	private Router routerCentral;
	private TaskProcessor processor;
	private EsperarConexionBidi listenerDeConexionesEnRouterCentral;
	private EsperarFiltro listenerFiltrosRemotosEnEmisor;

	@Before
	public void crearNodos() {
		processor = VortexProcessorFactory.createProcessor();
		emisor = PortalBidi.create(processor);
		listenerFiltrosRemotosEnEmisor = EsperarFiltro.create();
		emisor.setListenerDeFiltrosRemotos(listenerFiltrosRemotosEnEmisor);
		receptorPositivo1 = PortalBidi.create(processor);
		receptorNegativo1 = PortalBidi.create(processor);
		routerCentral = RouterBidi.create(processor);
		listenerDeConexionesEnRouterCentral = EsperarConexionBidi.create();
		routerCentral.setListenerDeConexiones(listenerDeConexionesEnRouterCentral);
	}

	@After
	public void eliminarNodos() {
		processor.detener();
	}

	@Test
	public void deberiaNotificarAlListenerCuandoSeEstableceLaConexionBidi() {
		final EsperarConexionBidi esperaDeConexiones = EsperarConexionBidi.create();
		emisor.setListenerDeConexiones(esperaDeConexiones);

		// Verificamos que no existe conexión bidi todavía
		try {
			esperaDeConexiones.esperarConexionBidiDesde(emisor, routerCentral, TimeMagnitude.of(1, TimeUnit.SECONDS));
			Assert.fail("No deríamos tener el lock liberado");
		} catch (final UnsuccessfulWaitException e) {
			// Es la excepción esperada
		}

		// Conectamos solo el portal al router
		emisor.conectarCon(routerCentral);

		// Verificamos que no existe conexión bidi todavía
		try {
			esperaDeConexiones.esperarConexionBidiDesde(emisor, routerCentral, TimeMagnitude.of(1, TimeUnit.SECONDS));
			Assert.fail("No deríamos tener el lock liberado");
		} catch (final UnsuccessfulWaitException e) {
			// Es la excepción esperada
		}

		// Al conectar de vuelta se debería detectar la conexión
		routerCentral.conectarCon(emisor);

		// Debería existir una conexión
		final ConexionBidi conexion = esperaDeConexiones.esperarConexionBidiDesde(emisor, routerCentral,
				TimeMagnitude.of(1, TimeUnit.SECONDS));
		Assert.assertNotNull(conexion);
	}

	@Test
	public void elMensajeNoDeberiaLlegarSiNoSeDeclaraNingunFiltro() throws InterruptedException {

		// Definimos el listener para saber los pasos de ruteo del router
		final ListenerDeRuteoEnPasos listenerDeRuteo = ListenerDeRuteoEnPasos.create();
		routerCentral.setListenerDeRuteos(listenerDeRuteo);

		// Interconectamos las partes
		emisor.conectarCon(routerCentral);
		routerCentral.conectarCon(emisor);
		receptorPositivo1.conectarCon(routerCentral);
		routerCentral.conectarCon(receptorPositivo1);

		// Esperamos que la última conexión bidi se establezca para probar los ruteos
		listenerDeConexionesEnRouterCentral.esperarConexionBidiDesde(routerCentral, receptorPositivo1,
				TimeMagnitude.of(1, TimeUnit.SECONDS));

		// Mandamos el mensaje
		final String mensajeEnviado = "Hola manola";
		emisor.enviar(mensajeEnviado);

		try {
			listenerDeRuteo.esperarRuteo(routerCentral, receptorPositivo1, TimeMagnitude.of(1, TimeUnit.SECONDS));
			Assert.fail("No debería existir el ruteo");
		} catch (final TimeoutExceededException e) {
			// Es la excepción que esperabamos
		}
	}

	@Test
	public void elMensajeDeberiaLlegarSiSeDeclaraUnFiltroPositivo() throws InterruptedException {
		// Le definimos al receptor qué es lo que queremos recibir ANTES de conectarlo (para no
		// tener que esperar propagaciones después)
		final String valorEsperado = "hola";
		final HandlerEncolador<MensajeParaTestDeRuteo> handlerReceptor = new HandlerEncolador<MensajeParaTestDeRuteo>() {
			@Override
			public Condicion getCondicionSuficiente() {
				return ValorEsperadoEn.elAtributo(MensajeParaTestDeRuteo.atributo_FIELD, valorEsperado);
			}
		};
		receptorPositivo1.recibirCon(handlerReceptor);

		// Interconectamos las partes
		emisor.conectarCon(routerCentral);
		routerCentral.conectarCon(emisor);
		receptorPositivo1.conectarCon(routerCentral);
		routerCentral.conectarCon(receptorPositivo1);

		// Esperamos que el emisor sea notificado de lo que espera el receptor
		listenerFiltrosRemotosEnEmisor.esperarCambioDeFiltroA(
				ValorEsperadoEn.elAtributo(MensajeParaTestDeRuteo.atributo_FIELD, valorEsperado),
				TimeMagnitude.of(1, TimeUnit.SECONDS));

		// Mandamos el mensaje
		final MensajeParaTestDeRuteo mensajeEnviado = new MensajeParaTestDeRuteo(valorEsperado);
		emisor.enviar(mensajeEnviado);

		// Verificamos que haya llegado
		final Object mensajeRecibidoPor1 = handlerReceptor.esperarPorMensaje(TimeMagnitude.of(1, TimeUnit.SECONDS));
		Assert.assertEquals("El primer receptor debería haber recibido el mensaje", mensajeEnviado, mensajeRecibidoPor1);
	}

	@Test
	public void elMensajeNoDeberiaLlegarSiSeDeclaraUnFiltroNegativo() throws InterruptedException {
		// Le definimos al receptor qué es lo que queremos recibir antes de conectar para no tener
		// que esperar despues
		final String valorEsperado = "hola";
		final HandlerEncolador<MensajeParaTestDeRuteo> handlerReceptor = new HandlerEncolador<MensajeParaTestDeRuteo>() {
			@Override
			public Condicion getCondicionSuficiente() {
				return ValorEsperadoEn.elAtributo(MensajeParaTestDeRuteo.atributo_FIELD, "!" + valorEsperado);
			}
		};
		receptorPositivo1.recibirCon(handlerReceptor);

		// Interconectamos las partes
		emisor.conectarCon(routerCentral);
		routerCentral.conectarCon(emisor);
		receptorPositivo1.conectarCon(routerCentral);
		routerCentral.conectarCon(receptorPositivo1);

		// Esperamos que el emisor sea notificado de lo que espera el receptor
		listenerFiltrosRemotosEnEmisor.esperarCambioDeFiltroA(
				ValorEsperadoEn.elAtributo(MensajeParaTestDeRuteo.atributo_FIELD, "!" + valorEsperado),
				TimeMagnitude.of(1, TimeUnit.SECONDS));

		// Mandamos el mensaje
		final MensajeParaTestDeRuteo mensajeEnviado = new MensajeParaTestDeRuteo(valorEsperado);
		emisor.enviar(mensajeEnviado);

		// Verificamos que no llega
		try {
			handlerReceptor.esperarPorMensaje(TimeMagnitude.of(1, TimeUnit.SECONDS));
		} catch (final UnsuccessfulWaitException e) {
			// Es la excepción que esperabamos
		}
	}

	@Test
	public void elMensajeNoDeberiaSalirDelPortalSiNoHayInteresado() throws InterruptedException {
		// Definimos el listener para saber los pasos de ruteo del portal
		final ListenerDeRuteoEnPasos listenerDeRuteo = ListenerDeRuteoEnPasos.create();
		emisor.setListenerDeRuteos(listenerDeRuteo);

		// Interconectamos las partes
		emisor.conectarCon(routerCentral);
		routerCentral.conectarCon(emisor);

		// Esperamos que el emisor sea notificado de lo que espera el receptor
		listenerFiltrosRemotosEnEmisor.esperarCambioDeFiltroA(SiempreFalse.getInstancia(),
				TimeMagnitude.of(1, TimeUnit.SECONDS));

		// Mandamos el mensaje
		final MensajeParaTestDeRuteo mensajeEnviado = new MensajeParaTestDeRuteo("hola");
		emisor.enviar(mensajeEnviado);

		try {
			listenerDeRuteo.esperarRuteo(emisor, routerCentral, TimeMagnitude.of(1, TimeUnit.SECONDS));
			Assert.fail("No debería existir el ruteo");
		} catch (final TimeoutExceededException e) {
			// Es la excepción que esperabamos
		}
	}

	@Test
	public void elMensajeNoDeberiaEntregarseAPortalNoInteresadoYSiAlInteresado() throws InterruptedException {
		// Definimos el listener para saber los pasos de ruteo del router
		final ListenerDeRuteoEnPasos listenerDeRuteo = ListenerDeRuteoEnPasos.create();
		routerCentral.setListenerDeRuteos(listenerDeRuteo);

		// Definimos qué quiere recibir cada receptor antes de conecarlos para no esperar
		// propagacion de filtros
		final String valorEsperado = "hola";
		final HandlerEncolador<MensajeParaTestDeRuteo> handlerReceptorPositivo = new HandlerEncolador<MensajeParaTestDeRuteo>() {
			@Override
			public Condicion getCondicionSuficiente() {
				return ValorEsperadoEn.elAtributo(MensajeParaTestDeRuteo.atributo_FIELD, valorEsperado);
			}
		};
		receptorPositivo1.recibirCon(handlerReceptorPositivo);

		final String valorNoEsperado = "chau";
		final HandlerEncolador<MensajeParaTestDeRuteo> handlerReceptorNegativo = new HandlerEncolador<MensajeParaTestDeRuteo>() {
			@Override
			public Condicion getCondicionSuficiente() {
				return ValorEsperadoEn.elAtributo(MensajeParaTestDeRuteo.atributo_FIELD, valorNoEsperado);
			}
		};
		receptorNegativo1.recibirCon(handlerReceptorNegativo);

		// Interconectamos las partes
		receptorPositivo1.conectarCon(routerCentral);
		routerCentral.conectarCon(receptorPositivo1);

		receptorNegativo1.conectarCon(routerCentral);
		routerCentral.conectarCon(receptorNegativo1);

		emisor.conectarCon(routerCentral);
		routerCentral.conectarCon(emisor);

		// Esperamos que el emisor sea notificado de lo que esperan los receptores
		listenerFiltrosRemotosEnEmisor.esperarCambioDeFiltroA(OrCompuesto.de(
				ValorEsperadoEn.elAtributo(MensajeParaTestDeRuteo.atributo_FIELD, valorEsperado),
				ValorEsperadoEn.elAtributo(MensajeParaTestDeRuteo.atributo_FIELD, valorNoEsperado)), TimeMagnitude.of(
				1, TimeUnit.SECONDS));

		// Mandamos el mensaje
		final MensajeParaTestDeRuteo mensajeEnviado = new MensajeParaTestDeRuteo("hola");
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

	@Test
	public void elMensajeNoDeberiaRebotarAlEmisor() throws InterruptedException {

		// Definimos el listener para saber los pasos de ruteo del router
		final ListenerDeRuteoEnPasos listenerDeRuteo = ListenerDeRuteoEnPasos.create();
		routerCentral.setListenerDeRuteos(listenerDeRuteo);

		// Definimos al receptor y emisor como interesados en lo mismo
		final String valorEsperado = "hola";
		final HandlerEncolador<MensajeParaTestDeRuteo> handlerEmisor = new HandlerEncolador<MensajeParaTestDeRuteo>() {
			@Override
			public Condicion getCondicionSuficiente() {
				return ValorEsperadoEn.create(valorEsperado,
						PropertyAccessor.create(MensajeParaTestDeRuteo.atributo_FIELD));
			}
		};
		emisor.recibirCon(handlerEmisor);

		final HandlerEncolador<MensajeParaTestDeRuteo> handlerReceptorPositivo = new HandlerEncolador<MensajeParaTestDeRuteo>() {
			@Override
			public Condicion getCondicionSuficiente() {
				return ValorEsperadoEn.create(valorEsperado,
						PropertyAccessor.create(MensajeParaTestDeRuteo.atributo_FIELD));
			}
		};
		receptorPositivo1.recibirCon(handlerReceptorPositivo);

		// Interconectamos las partes
		emisor.conectarCon(routerCentral);
		routerCentral.conectarCon(emisor);
		receptorPositivo1.conectarCon(routerCentral);
		routerCentral.conectarCon(receptorPositivo1);

		// A falta de mejor mecanismo esperamos que las conexiones bidi se establezcan
		Thread.sleep(1000);

		// Mandamos un mensaje desde el emisor que le interesa a ambos
		final MensajeParaTestDeRuteo mensajeEnviado = new MensajeParaTestDeRuteo("hola");
		emisor.enviar(mensajeEnviado);

		// Verificamos que el router ruteo el mensaje al receptor
		listenerDeRuteo.esperarRuteo(routerCentral, receptorPositivo1, TimeMagnitude.of(1, TimeUnit.SECONDS));

		// Y que NO lo ruteo al emisor aunque lo tiene como interesado
		try {
			listenerDeRuteo.esperarRuteo(routerCentral, emisor, TimeMagnitude.of(1, TimeUnit.SECONDS));
			Assert.fail("No debería existir el ruteo hacia atrás");
		} catch (final TimeoutExceededException e) {
			// Es la excepción que esperabamos
		}

		// Verificamos que haya llegado al receptor correcto
		final Object mensajeRecibidoPor1 = handlerReceptorPositivo.esperarPorMensaje(TimeMagnitude.of(1,
				TimeUnit.SECONDS));
		Assert.assertEquals("El primer receptor debería haber recibido el mensaje", mensajeEnviado, mensajeRecibidoPor1);

		// Verificamos que el emisor no recibió el mensaje
		try {
			handlerEmisor.esperarPorMensaje(TimeMagnitude.of(1, TimeUnit.SECONDS));
		} catch (final UnsuccessfulWaitException e) {
			// Es la excepción que esperabamos
		}

	}

}
