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
package net.gaia.vortex.core.tests;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import junit.framework.Assert;
import net.gaia.vortex.core.api.HandlerDeMensajesVecinos;
import net.gaia.vortex.core.api.NodoPortal;
import net.gaia.vortex.core.impl.NodoPortalSinThreads;
import net.gaia.vortex.core.impl.NodoRuteadorMinimo;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ar.com.dgarcia.coding.exceptions.TimeoutExceededException;
import ar.com.dgarcia.lang.conc.WaitBarrier;
import ar.com.dgarcia.lang.time.TimeMagnitude;

/**
 * Esta clase testea las aserciones mínimas de vortex para permitir la comunicación entre dos partes
 * sin indicar intereses
 * 
 * @author D. García
 */
public class TestComunicacionMinima {
	private static final Logger LOG = LoggerFactory.getLogger(TestComunicacionMinima.class);

	private NodoRuteadorMinimo nodoRuteador;

	private NodoPortal nodoEmisor;
	private NodoPortal nodoReceptor;

	@Before
	public void crearNodos() {
		nodoRuteador = NodoRuteadorMinimo.create();
		nodoEmisor = NodoPortalSinThreads.create();
		nodoReceptor = NodoPortalSinThreads.create();

		nodoEmisor.conectarCon(nodoRuteador);

		nodoRuteador.conectarCon(nodoReceptor);
		nodoRuteador.conectarCon(nodoEmisor);

		nodoReceptor.conectarCon(nodoRuteador);
	}

	@After
	public void eliminarNodos() {
		nodoRuteador.liberarRecursos();
	}

	/**
	 * T001. El emisor debería poder enviar por vortex cualquier objeto serializable
	 */
	@Test
	public void el_Emisor_Deberia_Poder_Enviar_Por_Vortex_Cualquier_Objeto_Serializable() {
		final Object mensaje = new Object();
		nodoEmisor.enviarAVecinos(mensaje);
	}

	/**
	 * T002. El receptor debería poder recibir de vortex cualquier objeto serializable
	 */
	@Test
	public void el_Receptor_Debería_Poder_Recibir_De_Vortex_Cualquier_Objeto_Serializable() {
		final HandlerDeMensajesVecinos handlerParaMensajesRecibidos = HandlerEncolador.create();
		nodoReceptor.setHandlerDeMensajesVecinos(handlerParaMensajesRecibidos);
	}

	/**
	 * T003. El mensaje enviado desde el emisor debería llegar al receptor
	 */
	@Test
	public void el_Mensaje_Enviado_Desde_El_Emisor_Y_El_Recibido_Por_El_Receptor_Deberian_Ser_Iguales() {
		final HandlerEncolador handlerReceptor = HandlerEncolador.create();
		nodoReceptor.setHandlerDeMensajesVecinos(handlerReceptor);

		final String mensajeEnviado = "texto de mensaje loco";
		nodoEmisor.enviarAVecinos(mensajeEnviado);

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
		final AtomicBoolean receptorBloqueado = new AtomicBoolean(false);
		final HandlerDeMensajesVecinos handlerReceptor = new HandlerDeMensajesVecinos() {

			@Override
			public void onMensajeDeVecinoRecibido(final Object mensaje) {
				receptorBloqueado.set(true);
				// Hacemos que el emisor siga ejecutando si nos estaba esperando
				bloqueoDelEmisor.release();
				// Esperamos que el emisor nos de permiso de ejecutar
				bloqueoDelReceptor.waitForReleaseUpTo(TimeMagnitude.of(1, TimeUnit.SECONDS));
				receptorBloqueado.set(false);
			}
		};
		nodoReceptor.setHandlerDeMensajesVecinos(handlerReceptor);

		final String mensajeEnviado = "texto de ejemplo";
		nodoEmisor.enviarAVecinos(mensajeEnviado);

		// Esperamos que el receptor reciba el mensaje
		bloqueoDelEmisor.waitForReleaseUpTo(TimeMagnitude.of(1, TimeUnit.SECONDS));
		// En este punto el receptor debería estar bloqueado y nosotros como thread emisor libres
		Assert.assertEquals("El receptor todavía debería estar bloqueado", true, receptorBloqueado.get());
		bloqueoDelReceptor.release();
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
		final HandlerDeMensajesVecinos handlerReceptor = new HandlerDeMensajesVecinos() {
			@Override
			public void onMensajeDeVecinoRecibido(final Object mensaje) {
				final Thread threadActual = Thread.currentThread();
				threadDeEntrega.set(threadActual);
				threadDeEntregaDefinido.release();
			}
		};
		nodoReceptor.setHandlerDeMensajesVecinos(handlerReceptor);

		final String mensajeEnviado = "texto de ejemplo";
		nodoEmisor.enviarAVecinos(mensajeEnviado);

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
		final HandlerEncolador handlerReceptor = HandlerEncolador.create();
		nodoEmisor.setHandlerDeMensajesVecinos(handlerReceptor);

		final String mensajeEnviado = "texto de mensaje loco";
		nodoEmisor.enviarAVecinos(mensajeEnviado);

		try {
			handlerReceptor.esperarPorMensaje(TimeMagnitude.of(1, TimeUnit.SECONDS));
			Assert.fail("Nunca debería salir de la espera sin excepción");
		} catch (final TimeoutExceededException e) {
			// Es la excepción que esperábamos
		}

	}

	/**
	 * T007. En memoria, el mensaje después de entrega debería conservar su identidad
	 */
	public void en_Memoria_El_Mensaje_Despues_De_Entrega_Debería_Conservar_Su_Identidad() {
		final HandlerEncolador handlerReceptor = HandlerEncolador.create();
		nodoReceptor.setHandlerDeMensajesVecinos(handlerReceptor);

		final Object mensajeEnviado = new Object();
		nodoEmisor.enviarAVecinos(mensajeEnviado);

		final Object mensajeRecibido = handlerReceptor.esperarPorMensaje(TimeMagnitude.of(1, TimeUnit.SECONDS));
		Assert.assertSame("El enviado y recibido deberían ser la misma instancia", mensajeEnviado, mensajeRecibido);

	}

	/**
	 * Verifica que el mensaje llegue si hay intermediario
	 */
	@Test
	public void elMensajeDeberiaLlegarSiHayUnNodoEnElMedio() {
		final NodoPortal nodoEmisor = NodoPortalSinThreads.create();
		final NodoRuteadorMinimo nodoIntermedio1 = NodoRuteadorMinimo.create();
		final NodoRuteadorMinimo nodoIntermedio2 = NodoRuteadorMinimo.create();
		final NodoPortal nodoReceptor = NodoPortalSinThreads.create();

		final HandlerEncolador handlerReceptor = HandlerEncolador.create();
		nodoReceptor.setHandlerDeMensajesVecinos(handlerReceptor);

		nodoEmisor.conectarCon(nodoIntermedio1);
		nodoIntermedio1.conectarCon(nodoIntermedio2);
		nodoIntermedio2.conectarCon(nodoReceptor);

		nodoReceptor.conectarCon(nodoIntermedio2);
		nodoIntermedio2.conectarCon(nodoIntermedio1);
		nodoIntermedio1.conectarCon(nodoEmisor);

		final String mensajeEnviado = "Mensaje";
		nodoEmisor.enviarAVecinos(mensajeEnviado);
		final Object mensajeRecibido = handlerReceptor.esperarPorMensaje(TimeMagnitude.of(1, TimeUnit.SECONDS));
		Assert.assertEquals("El mensaje debería llegar igual al receptor", mensajeEnviado, mensajeRecibido);
	}

	/**
	 * T008. En memoria, el tiempo de entrega normal debería ser inferior a 1ms (final o 1000
	 * mensajes por segundo)
	 */
	@Test
	public void en_Memoria_El_Tiempo_De_Entrega_Normal_Debería_Ser_Menosr_A_1Milisegundo() {
		final int cantidadDeMensajes = 1000000;
		final HandlerCronometro handlerCronometro = HandlerCronometro.create(cantidadDeMensajes);
		nodoReceptor.setHandlerDeMensajesVecinos(handlerCronometro);

		final long startNanos = System.nanoTime();
		LOG.debug("Nanos inicio: {}", startNanos);
		for (int i = 0; i < cantidadDeMensajes; i++) {
			final MensajeCronometro mensajeCronometro = MensajeCronometro.create();
			mensajeCronometro.marcarInicio();
			nodoEmisor.enviarAVecinos(mensajeCronometro);
		}

		handlerCronometro.esperarEntregaDeMensajes(TimeMagnitude.of(30, TimeUnit.SECONDS));
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
		// Aramamos la red con otro receptor
		final HandlerEncolador handlerReceptor1 = HandlerEncolador.create();
		nodoReceptor.setHandlerDeMensajesVecinos(handlerReceptor1);

		final HandlerEncolador handlerReceptor2 = HandlerEncolador.create();
		final NodoPortal nodoReceptor2 = NodoPortalSinThreads.create();
		nodoReceptor2.setHandlerDeMensajesVecinos(handlerReceptor2);

		nodoRuteador.conectarCon(nodoReceptor2);
		nodoReceptor2.conectarCon(nodoRuteador);

		// Mandamos el mensaje
		final Object mensajeEnviado = new Object();
		nodoEmisor.enviarAVecinos(mensajeEnviado);

		// Verificamos que haya llegado a los dos
		final Object mensajeRecibidoPor1 = handlerReceptor1.esperarPorMensaje(TimeMagnitude.of(1, TimeUnit.SECONDS));
		Assert.assertEquals("El primer receptor debería haber recibido el mensaje", mensajeEnviado, mensajeRecibidoPor1);

		final Object mensajeRecibidoPor2 = handlerReceptor2.esperarPorMensaje(TimeMagnitude.of(1, TimeUnit.SECONDS));
		Assert.assertEquals("El segundo receptor debería haber recibido el mensaje", mensajeEnviado,
				mensajeRecibidoPor2);
	}

}
