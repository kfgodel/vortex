/**
 * 20/05/2012 16:00:05 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.portal.tests;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import junit.framework.Assert;
import net.gaia.taskprocessor.api.processor.TaskProcessor;
import net.gaia.vortex.api.basic.emisores.MultiConectable;
import net.gaia.vortex.api.moleculas.Compuesto;
import net.gaia.vortex.api.moleculas.Portal;
import net.gaia.vortex.api.proto.Conector;
import net.gaia.vortex.impl.builder.VortexCoreBuilder;
import net.gaia.vortex.impl.builder.VortexPortalBuilder;
import net.gaia.vortex.impl.condiciones.SiempreTrue;
import net.gaia.vortex.impl.helpers.VortexProcessorFactory;
import net.gaia.vortex.portal.impl.condiciones.SoloInstancias;
import net.gaia.vortex.portal.impl.mensaje.HandlerTipado;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ar.com.dgarcia.coding.exceptions.TimeoutExceededException;
import ar.com.dgarcia.lang.conc.WaitBarrier;
import ar.com.dgarcia.lang.time.TimeMagnitude;

/**
 * Esta clase realiza las pruebas definidas en A01 de la documentación utilizando un portal como
 * punto de acceso para código cliente.<br>
 * De esta manera verificamos el uso básico de vortex para usuarios externos a la red
 * 
 * @author D. García
 */
public class TestRedA01ConPortal {
	private static final Logger LOG = LoggerFactory.getLogger(TestRedA01ConPortal.class);

	private Compuesto<MultiConectable> nodoRuteador;

	private Portal nodoEmisor;
	private Portal nodoReceptor;
	private TaskProcessor processor;

	private VortexPortalBuilder builder;

	@Before
	public void crearNodos() {
		processor = VortexProcessorFactory.createProcessor();
		builder = VortexPortalBuilder.create(VortexCoreBuilder.create(processor));
		// Creamos un nodo central
		nodoRuteador = builder.getCore().multiplexarSinDuplicados();
		// Le agregamos las interconexiones en los extremos
		nodoEmisor = builder.portalIdentificador();
		nodoReceptor = builder.portalIdentificador();

		// Interconectamos ida y vuelta ambos portales
		final Conector ruteadorAsincrono = builder.getCore().asincronizar(nodoRuteador);
		nodoEmisor.getConectorDeSalida().conectarCon(ruteadorAsincrono);
		nodoRuteador.getSalida().crearConector().conectarCon(nodoReceptor);

		nodoReceptor.getConectorDeSalida().conectarCon(nodoRuteador);
		nodoRuteador.getSalida().crearConector().conectarCon(nodoEmisor);
	}

	@After
	public void eliminarProcesador() {
		processor.detener();
	}

	@Test
	public void siSeEsperaUnMapaSeRecibenTodasLasClavesDelEstadoDelMensaje() {
		final HandlerEncolador<Map<String, Object>> handlerReceptor = new HandlerEncolador<Map<String, Object>>() {
		};
		nodoReceptor.recibirCon(handlerReceptor);

		final Map<String, Object> mensajeEnviado = new HashMap<String, Object>();
		mensajeEnviado.put("hola", "manola");
		nodoEmisor.enviar(mensajeEnviado);

		final Object mensajeRecibido = handlerReceptor.esperarPorMensaje(TimeMagnitude.of(1, TimeUnit.SECONDS));
		Assert.assertTrue("Deberíamos recibir un mapa", mensajeRecibido instanceof Map);

		@SuppressWarnings("unchecked")
		final Map<String, Object> mapaRecibido = (Map<String, Object>) mensajeRecibido;
		Assert.assertEquals("El mapa debería tener los valores del mapa original", "manola", mapaRecibido.get("hola"));

		Assert.assertTrue("Pero además tener otras kesy que no mandamos!", mensajeEnviado.size() < mapaRecibido.size());
	}

	/**
	 * T001. El emisor debería poder enviar por vortex cualquier objeto serializable
	 */
	@Test
	public void el_Emisor_Deberia_Poder_Enviar_Por_Vortex_Cualquier_Objeto_Serializable() {
		final String mensaje = "Hola";
		nodoEmisor.enviar(mensaje);
	}

	/**
	 * T002. El receptor debería poder recibir de vortex cualquier objeto serializable
	 */
	@Test
	public void el_Receptor_Debería_Poder_Recibir_De_Vortex_Cualquier_Objeto_Serializable() {
		nodoReceptor.recibirCon(new HandlerTipado<Object>(SiempreTrue.getInstancia()) {

			public void onObjetoRecibido(final Object mensaje) {
				// Recibimos cualquier cosa que ande dando vueltas
			}
		});
	}

	/**
	 * T003. El mensaje enviado desde el emisor debería llegar al receptor
	 */
	@Test
	public void el_Mensaje_Enviado_Desde_El_Emisor_Y_El_Recibido_Por_El_Receptor_Deberian_Ser_Iguales() {
		final HandlerEncoladorDeStrings handlerReceptor = HandlerEncoladorDeStrings.create();
		nodoReceptor.recibirCon(handlerReceptor);

		final String mensajeEnviado = "texto de mensaje loco";
		nodoEmisor.enviar(mensajeEnviado);

		final Object mensajeRecibido = handlerReceptor.esperarPorMensaje(TimeMagnitude.of(1, TimeUnit.SECONDS));
		Assert.assertEquals("El enviado y recibido deberían ser iguales", mensajeEnviado, mensajeRecibido);
	}

	/**
	 * T004. El thread del emisor no debería bloquearse durante la entrega del mensaje.<br>
	 * Como implementación del test verificamos que el emisor puede seguir ejecutando mientras el
	 * receptor está bloqueado
	 */
	@Test
	public void el_Thread_Del_Emisor_No_Deberia_Bloquearse_Durante_La_Entrega_Del_Mensaje() {
		final WaitBarrier bloqueoDelEmisor = WaitBarrier.create();
		final WaitBarrier bloqueoDelReceptor = WaitBarrier.create();
		final AtomicBoolean receptorBloqueado = new AtomicBoolean(true);
		final HandlerTipado<String> handlerReceptor = new HandlerTipado<String>(SoloInstancias.de(String.class)) {

			public void onObjetoRecibido(final String mensaje) {
				// Esperamos que el emisor nos de permiso de ejecutar
				bloqueoDelReceptor.waitForReleaseUpTo(TimeMagnitude.of(1, TimeUnit.SECONDS));
				receptorBloqueado.set(false);

				// Hacemos que el emisor siga ejecutando si nos estaba esperando
				bloqueoDelEmisor.release();
			}
		};
		nodoReceptor.recibirCon(handlerReceptor);

		final String mensajeEnviado = "texto de ejemplo";
		nodoEmisor.enviar(mensajeEnviado);

		// En este punto el receptor debería estar bloqueado y nosotros como thread emisor libres
		Assert.assertEquals("El receptor todavía debería estar bloqueado", true, receptorBloqueado.get());

		// Dejamos que inicie la ejecucion
		bloqueoDelReceptor.release();

		// Esperamos que el receptor reciba el mensaje
		bloqueoDelEmisor.waitForReleaseUpTo(TimeMagnitude.of(1, TimeUnit.SECONDS));
		// En este punto el receptor debería estar bloqueado y nosotros como thread emisor libres
		Assert.assertEquals("El receptor todavía debería estar bloqueado", false, receptorBloqueado.get());
	}

	/**
	 * T005. El thread del receptor debería poder ser independiente del usado para la entrega del
	 * mensaje.<br>
	 * Como prueba verificamos que el thread utilizado para entregar el mensaje es distinto del
	 * usado en el test y por lo tanto se podría cualquier otro thread como receptor del mensaje en
	 * sí
	 */
	@Test
	public void el_Thread_Del_Receptor_Debería_Poder_Ser_Independiente_Del_Usado_Para_La_Entrega_Del_Mensaje() {
		final WaitBarrier threadDeEntregaDefinido = WaitBarrier.create();
		final AtomicReference<Thread> threadDeEntrega = new AtomicReference<Thread>();
		final HandlerTipado<String> handlerReceptor = new HandlerTipado<String>(SoloInstancias.de(String.class)) {

			public void onObjetoRecibido(final String mensaje) {
				final Thread threadActual = Thread.currentThread();
				threadDeEntrega.set(threadActual);
				threadDeEntregaDefinido.release();
			}
		};
		nodoReceptor.recibirCon(handlerReceptor);

		final String mensajeEnviado = "texto de ejemplo";
		nodoEmisor.enviar(mensajeEnviado);

		// Esperamos que el thread utilizado para la entrega sea definido
		threadDeEntregaDefinido.waitForReleaseUpTo(TimeMagnitude.of(1, TimeUnit.SECONDS));

		final Thread threadParaEntrega = threadDeEntrega.get();
		Assert.assertNotSame("Deberían ser threads distintos", Thread.currentThread(), threadParaEntrega);

	}

	/**
	 * T006. El emisor no debería recibir su propio mensaje
	 */
	@Test
	public void el_Emisor_No_Deberia_Recibir_Su_Propio_Mensaje() {
		final HandlerEncoladorDeStrings handlerReceptor = HandlerEncoladorDeStrings.create();
		nodoEmisor.recibirCon(handlerReceptor);

		final String mensajeEnviado = "texto de mensaje loco";
		nodoEmisor.enviar(mensajeEnviado);

		try {
			handlerReceptor.esperarPorMensaje(TimeMagnitude.of(1, TimeUnit.SECONDS));
			Assert.fail("Nunca debería salir de la espera sin excepción");
		}
		catch (final TimeoutExceededException e) {
			// Es la excepción que esperábamos
		}

	}

	/**
	 * T007. En memoria, el mensaje después de entrega debería conservar su identidad
	 */
	@Ignore("No es posible enviar objetos")
	public void en_Memoria_El_Mensaje_Despues_De_Entrega_Debería_Conservar_Su_Identidad() {
		final HandlerEncoladorDeStrings handlerReceptor = HandlerEncoladorDeStrings.create();
		nodoReceptor.recibirCon(handlerReceptor);

		final Object mensajeEnviado = new Object();
		nodoEmisor.enviar(mensajeEnviado);

		final Object mensajeRecibido = handlerReceptor.esperarPorMensaje(TimeMagnitude.of(1, TimeUnit.SECONDS));
		Assert.assertSame("El enviado y recibido deberían ser la misma instancia", mensajeEnviado, mensajeRecibido);

	}

	/**
	 * Verifica que el mensaje llegue si hay más de un intermediario
	 */
	@Test
	public void elMensajeDeberiaLlegarSiHayDosNodosEnElMedio() {
		// Creamos los nodos centrales interconectados
		final Compuesto<MultiConectable> nodoIntermedio1 = builder.getCore().multiplexarSinDuplicados();
		final Compuesto<MultiConectable> nodoIntermedio2 = builder.getCore().multiplexarSinDuplicados();
		interconectar(nodoIntermedio1, nodoIntermedio2);

		// Le agregamos los extremos portales
		final Portal nodoEmisor = builder.portalIdentificador();
		nodoEmisor.getConectorDeSalida().conectarCon(nodoIntermedio1);
		nodoIntermedio1.getSalida().crearConector().conectarCon(nodoEmisor);

		final Portal nodoReceptor = builder.portalIdentificador();
		nodoReceptor.getConectorDeSalida().conectarCon(nodoIntermedio2);
		nodoIntermedio2.getSalida().crearConector().conectarCon(nodoReceptor);

		final HandlerEncoladorDeStrings handlerReceptor = HandlerEncoladorDeStrings.create();
		nodoReceptor.recibirCon(handlerReceptor);

		final String mensajeEnviado = "Mensaje";
		nodoEmisor.enviar(mensajeEnviado);

		final Object mensajeRecibido = handlerReceptor.esperarPorMensaje(TimeMagnitude.of(1, TimeUnit.SECONDS));
		Assert.assertEquals("El mensaje debería llegar igual al receptor", mensajeEnviado, mensajeRecibido);
	}

	/**
	 * Verifica que los hubs no generan loops entre sí al haber varios conectados
	 */
	@Test
	public void elMensajeNoDeberiaLlegarMasDeUnaVezSiHayDosHubsEnElMedioInterconectados() {
		// Creamos los nodos centrales interconectados
		final Compuesto<MultiConectable> nodoIntermedio1 = builder.getCore().multiplexarSinDuplicados();
		final Compuesto<MultiConectable> nodoIntermedio2 = builder.getCore().multiplexarSinDuplicados();
		interconectar(nodoIntermedio1, nodoIntermedio2);

		// Le agregamos los extremos portales
		final Portal nodoEmisor = builder.portalIdentificador();
		nodoEmisor.getConectorDeSalida().conectarCon(nodoIntermedio1);
		nodoIntermedio1.getSalida().crearConector().conectarCon(nodoEmisor);

		final Portal nodoReceptor = builder.portalIdentificador();
		nodoReceptor.getConectorDeSalida().conectarCon(nodoIntermedio2);
		nodoIntermedio2.getSalida().crearConector().conectarCon(nodoReceptor);

		final HandlerEncoladorDeStrings handlerReceptor = HandlerEncoladorDeStrings.create();
		nodoReceptor.recibirCon(handlerReceptor);

		final String mensajeEnviado = "Mensaje";
		nodoEmisor.enviar(mensajeEnviado);

		final Object mensajeRecibido = handlerReceptor.esperarPorMensaje(TimeMagnitude.of(1, TimeUnit.SECONDS));
		Assert.assertEquals("El mensaje debería llegar la primera vez", mensajeEnviado, mensajeRecibido);

		try {
			handlerReceptor.esperarPorMensaje(TimeMagnitude.of(1, TimeUnit.SECONDS));
			Assert.fail("No deberíamos haber recibido otro mensaje");
		}
		catch (final TimeoutExceededException e) {
			// Es la excepción que esperábamos
		}
	}

	/**
	 * T008. En memoria, el tiempo de entrega normal debería ser inferior a 1ms (final o 1000
	 * mensajes por segundo)
	 */
	@Test
	public void en_Memoria_El_Tiempo_De_Entrega_Normal_Debería_Ser_Menosr_A_1Milisegundo() {
		final int cantidadDeMensajes = 100000;
		final HandlerCronometro handlerCronometro = HandlerCronometro.create(cantidadDeMensajes);
		nodoReceptor.recibirCon(handlerCronometro);

		final long startNanos = System.nanoTime();
		LOG.debug("Nanos inicio: {}", startNanos);
		for (int i = 0; i < cantidadDeMensajes; i++) {
			final MensajeCronometro mensajeCronometro = MensajeCronometro.create();
			mensajeCronometro.marcarInicio();
			nodoEmisor.enviar(mensajeCronometro);
		}

		handlerCronometro.esperarEntregaDeMensajes(TimeMagnitude.of(60, TimeUnit.SECONDS));
		LOG.debug("Nanos Fin: {}", System.nanoTime());
		final long endNanos = System.nanoTime();
		final long elapsedNanos = endNanos - startNanos;
		LOG.debug("Milis totales en procesar {} mensajes: {}", cantidadDeMensajes, elapsedNanos / 1000000d);
		LOG.debug("Milis dedicado por mensaje: {}", (elapsedNanos / 1000000d) / cantidadDeMensajes);
		final double mensajesPorSegundo = (cantidadDeMensajes * 1000000000d) / elapsedNanos;
		LOG.debug("Cantidad proyectada de mensajes procesables por segundo: {}", mensajesPorSegundo);

		final double promedioNanosPorMensaje = handlerCronometro.getPromedioDeEsperaPorMensaje();
		LOG.debug("Milis de espera promedio por mensaje: {}", promedioNanosPorMensaje / 1000000d);
	}

	@Test
	public void elMensajeDeberiaLlegarADosReceptoresIndependientes() {
		// Armamos la red con otro receptor
		final HandlerEncoladorDeStrings handlerReceptor1 = HandlerEncoladorDeStrings.create();
		nodoReceptor.recibirCon(handlerReceptor1);

		final HandlerEncoladorDeStrings handlerReceptor2 = HandlerEncoladorDeStrings.create();
		final Portal nodoReceptor2 = builder.portalIdentificador();
		nodoReceptor2.getConectorDeSalida().conectarCon(nodoRuteador);
		nodoRuteador.getSalida().crearConector().conectarCon(nodoReceptor2);

		nodoReceptor2.recibirCon(handlerReceptor2);

		// Mandamos el mensaje
		final String mensajeEnviado = "Hola manola";
		nodoEmisor.enviar(mensajeEnviado);

		// Verificamos que haya llegado a los dos
		final Object mensajeRecibidoPor1 = handlerReceptor1.esperarPorMensaje(TimeMagnitude.of(1, TimeUnit.SECONDS));
		Assert.assertEquals("El primer receptor debería haber recibido el mensaje", mensajeEnviado, mensajeRecibidoPor1);

		final Object mensajeRecibidoPor2 = handlerReceptor2.esperarPorMensaje(TimeMagnitude.of(1, TimeUnit.SECONDS));
		Assert.assertEquals("El segundo receptor debería haber recibido el mensaje", mensajeEnviado,
				mensajeRecibidoPor2);
	}

	/**
	 * Crea una conexión bidireccional entre los nodos pasados
	 */
	public void interconectar(final Compuesto<MultiConectable> nodoIntermedio1,
			final Compuesto<MultiConectable> nodoIntermedio2) {
		nodoIntermedio1.getSalida().crearConector().conectarCon(nodoIntermedio2);
		nodoIntermedio2.getSalida().crearConector().conectarCon(nodoIntermedio1);
	}

}
