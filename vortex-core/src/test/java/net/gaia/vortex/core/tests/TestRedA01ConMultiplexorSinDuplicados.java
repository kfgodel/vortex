/**
 * 13/06/2012 14:42:44 Copyright (C) 2011 10Pines S.R.L.
 */
package net.gaia.vortex.core.tests;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import junit.framework.Assert;
import net.gaia.taskprocessor.api.processor.TaskProcessor;
import net.gaia.vortex.api.atomos.Conector;
import net.gaia.vortex.api.atomos.Multiplexor;
import net.gaia.vortex.api.basic.Receptor;
import net.gaia.vortex.api.builder.VortexCore;
import net.gaia.vortex.api.ids.componentes.IdDeComponenteVortex;
import net.gaia.vortex.api.ids.mensajes.IdDeMensaje;
import net.gaia.vortex.api.mensajes.MensajeVortex;
import net.gaia.vortex.api.moleculas.Compuesto;
import net.gaia.vortex.core.prog.Loggers;
import net.gaia.vortex.impl.builder.VortexCoreBuilder;
import net.gaia.vortex.impl.helpers.VortexProcessorFactory;
import net.gaia.vortex.impl.ids.componentes.GeneradorDeIdsGlobalesParaComponentes;
import net.gaia.vortex.impl.ids.mensajes.GeneradorSecuencialDeIdDeMensaje;
import net.gaia.vortex.impl.mensajes.MensajeConContenido;
import net.gaia.vortex.impl.support.ReceptorSupport;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ar.com.dgarcia.coding.exceptions.TimeoutExceededException;
import ar.com.dgarcia.lang.conc.WaitBarrier;
import ar.com.dgarcia.lang.time.TimeMagnitude;

/**
 * Esta clase realiza las pruebas básicas de vortex definidas en A01 de la documentación utilizando
 * un multiplexor sin duplicados desde {@link VortexCore#multiplexarSinDuplicados(Receptor...)} como
 * forma más básica de red con atomos. Verificamos que es posible armar una red básica con
 * conexiones unidireccionales
 * 
 * @author D. García
 */
public class TestRedA01ConMultiplexorSinDuplicados {
	private static final Logger LOG = LoggerFactory.getLogger(TestRedA01ConMultiplexorSinDuplicados.class);

	private Compuesto<Multiplexor> nodoEmisor;
	private Compuesto<Multiplexor> nodoRuteador;
	private Compuesto<Multiplexor> nodoReceptor;
	private MensajeVortex mensaje1;
	private TaskProcessor processor;

	private GeneradorSecuencialDeIdDeMensaje generadorDeIdsDelNodo;

	private VortexCore builder;

	@Before
	public void crearNodos() {
		processor = VortexProcessorFactory.createProcessor();
		builder = VortexCoreBuilder.create(processor);

		mensaje1 = MensajeConContenido.crearVacio();
		final IdDeComponenteVortex idDeNodo = GeneradorDeIdsGlobalesParaComponentes.getInstancia().generarId();
		generadorDeIdsDelNodo = GeneradorSecuencialDeIdDeMensaje.create(idDeNodo);
		final IdDeMensaje generarId = generadorDeIdsDelNodo.generarId();
		mensaje1.asignarId(generarId);

		nodoEmisor = crearNodoDefault();
		nodoRuteador = crearNodoDefault();
		nodoReceptor = crearNodoDefault();

		interconectar(nodoEmisor, nodoRuteador);
		interconectar(nodoRuteador, nodoReceptor);
	}

	@After
	public void liberarProcesador() {
		processor.detener();
	}

	/**
	 * Crea un nodo tipo para las pruebas
	 */
	private Compuesto<Multiplexor> crearNodoDefault() {
		return builder.multiplexarSinDuplicados();
	}

	@After
	public void eliminarProcesador() {
		processor.detener();
	}

	/**
	 * T001. El emisor debería poder enviar por vortex cualquier objeto serializable<br>
	 * En esta implementación no podemos enviar cualquier objeto así que sólo verificamos que se
	 * puedan mandar los mensajes vortex
	 */
	@Test
	public void el_Emisor_Deberia_Poder_Enviar_Por_Vortex_Cualquier_Objeto_Serializable() {
		// Para enviar a la red se le indica a un componente emisor que reciba
		nodoEmisor.recibir(mensaje1);
	}

	/**
	 * T002. El receptor debería poder recibir de vortex cualquier objeto serializable<br>
	 * En esta implementación no podemos mandar cualquier objeto así que verificamos que se puedan
	 * recibir los mensajes vortex
	 */
	@Test
	public void el_Receptor_Debería_Poder_Recibir_De_Vortex_Cualquier_Objeto_Serializable() {
		// Para recibir de la red nos conectamos a un componente receptor
		final ReceptorEncolador receptorDelCliente = ReceptorEncolador.create();
		builder.conectarDesde(nodoReceptor, receptorDelCliente);
	}

	/**
	 * T003. El mensaje enviado desde el emisor debería llegar al receptor<br>
	 * En este caso verificamos que el mensaje vortex recibido sea el mismo que el enviado porque
	 * todo pasa en memoria
	 */
	@Test
	public void el_Mensaje_Enviado_Desde_El_Emisor_Y_El_Recibido_Por_El_Receptor_Deberian_Ser_Iguales() {
		final ReceptorEncolador handlerReceptor = ReceptorEncolador.create();
		builder.conectarDesde(nodoReceptor, handlerReceptor);

		nodoEmisor.recibir(mensaje1);

		final MensajeVortex mensajeRecibido = handlerReceptor.esperarPorMensaje(TimeMagnitude.of(1, TimeUnit.SECONDS));
		Assert.assertSame("El enviado y recibido deberían ser el mismo", mensaje1, mensajeRecibido);
	}

	/**
	 * T004. El thread del emisor no debería bloquearse durante la entrega del mensaje.<br>
	 * Como implementación del test simulamos la realizacion de una tarea en el receptor que solo
	 * puede ser iniciada si el emisor lo indica, despues de enviar el mensaje (lo que requiere un
	 * thread autonomo en el emisor)
	 */
	@Test
	public void el_Thread_Del_Emisor_No_Deberia_Bloquearse_Durante_La_Entrega_Del_Mensaje() {
		final WaitBarrier esperarParaComprobarEnEmisor = WaitBarrier.create();
		final WaitBarrier esperarParaRealizarTareaEnReceptor = WaitBarrier.create();
		final AtomicBoolean tareasCompletadaEnReceptor = new AtomicBoolean(false);
		final Receptor handlerReceptor = new ReceptorSupport() {

			public void recibir(final MensajeVortex mensaje) {
				tareasCompletadaEnReceptor.set(false);
				// Esperamos que el emisor nos de permiso de ejecutar
				esperarParaRealizarTareaEnReceptor.waitForReleaseUpTo(TimeMagnitude.of(1, TimeUnit.SECONDS));
				tareasCompletadaEnReceptor.set(true);
				// Le avisamso al emisor que ya puede revisar el resultado
				esperarParaComprobarEnEmisor.release();
			}
		};
		builder.conectarDesde(nodoReceptor, handlerReceptor);

		final Conector emisorAsincronico = builder.asincronizar(nodoEmisor);
		emisorAsincronico.recibir(mensaje1);

		Assert.assertEquals("La tarea deberia estar pendiente", false, tareasCompletadaEnReceptor.get());
		// Hacemos que el receptor procese la tarea
		esperarParaRealizarTareaEnReceptor.release();

		// si son threads independientes el receptor debería avisarnos cuando termine
		esperarParaComprobarEnEmisor.waitForReleaseUpTo(TimeMagnitude.of(1, TimeUnit.SECONDS));

		// Ya debería estar terminada la tarea
		Assert.assertEquals("La tarea deberia estar terminada", true, tareasCompletadaEnReceptor.get());
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
		final Receptor handlerReceptor = new ReceptorSupport() {

			public void recibir(final MensajeVortex mensaje) {
				final Thread threadActual = Thread.currentThread();
				threadDeEntrega.set(threadActual);
				threadDeEntregaDefinido.release();
			}
		};
		builder.conectarDesde(nodoReceptor, handlerReceptor);

		final Conector emisorAsincrono = builder.asincronizar(nodoEmisor);
		emisorAsincrono.recibir(mensaje1);

		// Esperamos que el thread utilizado para la entrega sea definido
		threadDeEntregaDefinido.waitForReleaseUpTo(TimeMagnitude.of(1, TimeUnit.SECONDS));

		final Thread threadParaEntrega = threadDeEntrega.get();
		Assert.assertNotSame("Deberían ser threads distintos", Thread.currentThread(), threadParaEntrega);

	}

	/**
	 * T006. El emisor no debería recibir su propio mensaje.<br>
	 * Verificamos que si mandamos un mensaje desde el emisor, el mensaje no viene de vuelta desde
	 * el ruteador
	 */
	@Test
	public void el_Emisor_No_Deberia_Recibir_Su_Propio_Mensaje() {
		// Le conectamos un receptor directo al emisor para ver los que le llegan
		final ReceptorEncolador recibidosPorElEmisor = ReceptorEncolador.create();
		builder.conectarDesde(nodoEmisor, recibidosPorElEmisor);

		// Mandamos el mensaje
		nodoEmisor.recibir(mensaje1);

		final MensajeVortex recibidoPrimero = recibidosPorElEmisor.esperarPorMensaje(TimeMagnitude.of(1,
				TimeUnit.SECONDS));
		Assert.assertSame("Deberíamos tener el mensaje como recibido 1 vez por el envio", mensaje1, recibidoPrimero);

		try {
			recibidosPorElEmisor.esperarPorMensaje(TimeMagnitude.of(1, TimeUnit.SECONDS));
			Assert.fail("No deberíamos haber recibido un segundo mensaje");
		}
		catch (final TimeoutExceededException e) {
			// Es la excepción que esperábamos
		}
	}

	/**
	 * Verifica que el mensaje llegue si hay intermediarios
	 */
	@Test
	public void elMensajeDeberiaLlegarSiHayNodosEnElMedio() {
		final Compuesto<Multiplexor> nodoEmisor = crearNodoDefault();
		final Compuesto<Multiplexor> nodoIntermedio1 = crearNodoDefault();
		final Compuesto<Multiplexor> nodoIntermedio2 = crearNodoDefault();
		final Compuesto<Multiplexor> nodoReceptor = crearNodoDefault();

		final ReceptorEncolador handlerReceptor = ReceptorEncolador.create();
		builder.conectarDesde(nodoReceptor, handlerReceptor);

		interconectar(nodoEmisor, nodoIntermedio1);
		interconectar(nodoIntermedio1, nodoIntermedio2);
		interconectar(nodoIntermedio2, nodoReceptor);

		nodoEmisor.recibir(mensaje1);

		final MensajeVortex mensajeRecibido = handlerReceptor.esperarPorMensaje(TimeMagnitude.of(1, TimeUnit.SECONDS));
		Assert.assertSame("El mensaje debería llegar igual al receptor", mensaje1, mensajeRecibido);

		try {
			handlerReceptor.esperarPorMensaje(TimeMagnitude.of(1, TimeUnit.SECONDS));
			Assert.fail("No deberíamos haber recibido otro mensaje");
		}
		catch (final TimeoutExceededException e) {
			// Es la excepción que esperábamos
		}
	}

	/**
	 * Verifica que los intermediarios no generen duplicados de mensajes
	 */
	@Test
	public void elMensajeNoDeberiaLlegarMasDeUnaVezSiHayDosNodosEnElMedioInterconectados() {
		final Compuesto<Multiplexor> nodoEmisor = crearNodoDefault();
		// Le conectamos un receptor directo al emisor para ver los que le llegan
		final ReceptorEncolador recibidosPorElEmisor = ReceptorEncolador.create();
		builder.conectarDesde(nodoEmisor, recibidosPorElEmisor);

		final Compuesto<Multiplexor> nodoIntermedio1 = crearNodoDefault();
		final Compuesto<Multiplexor> nodoIntermedio2 = crearNodoDefault();
		final Compuesto<Multiplexor> nodoReceptor = crearNodoDefault();

		final ReceptorEncolador handlerReceptor = ReceptorEncolador.create();
		builder.conectarDesde(nodoReceptor, handlerReceptor);

		interconectar(nodoEmisor, nodoIntermedio1);
		interconectar(nodoIntermedio1, nodoIntermedio2);
		interconectar(nodoIntermedio2, nodoReceptor);

		nodoEmisor.recibir(mensaje1);

		final MensajeVortex mensajeRecibido = handlerReceptor.esperarPorMensaje(TimeMagnitude.of(1, TimeUnit.SECONDS));
		Assert.assertSame("El mensaje debería llegar igual al receptor", mensaje1, mensajeRecibido);

		final MensajeVortex recibidoPrimero = recibidosPorElEmisor.esperarPorMensaje(TimeMagnitude.of(1,
				TimeUnit.SECONDS));
		Assert.assertSame("Deberíamos tener el mensaje como recibido 1 vez por el envio", mensaje1, recibidoPrimero);

		try {
			recibidosPorElEmisor.esperarPorMensaje(TimeMagnitude.of(1, TimeUnit.SECONDS));
			Assert.fail("No deberíamos haber recibido un segundo mensaje");
		}
		catch (final TimeoutExceededException e) {
			// Es la excepción que esperábamos
		}
	}

	/**
	 * Verifica que el mensaje llegue a puntas distintas
	 */
	@Test
	public void elMensajeDeberiaLlegarADosReceptoresIndependientes() {
		// Armamos la red con otro receptor
		final ReceptorEncolador handlerReceptor1 = ReceptorEncolador.create();
		builder.conectarDesde(nodoReceptor, handlerReceptor1);

		final ReceptorEncolador handlerReceptor2 = ReceptorEncolador.create();
		final Compuesto<Multiplexor> nodoReceptor2 = crearNodoDefault();
		builder.conectarDesde(nodoReceptor2, handlerReceptor2);

		interconectar(nodoRuteador, nodoReceptor2);

		// Mandamos el mensaje
		nodoEmisor.recibir(mensaje1);

		// Verificamos que haya llegado a los dos
		final MensajeVortex mensajeRecibidoPor1 = handlerReceptor1.esperarPorMensaje(TimeMagnitude.of(1,
				TimeUnit.SECONDS));
		Assert.assertEquals("El primer receptor debería haber recibido el mensaje", mensaje1, mensajeRecibidoPor1);

		final Object mensajeRecibidoPor2 = handlerReceptor2.esperarPorMensaje(TimeMagnitude.of(1, TimeUnit.SECONDS));
		Assert.assertEquals("El segundo receptor debería haber recibido el mensaje", mensaje1, mensajeRecibidoPor2);
	}

	/**
	 * Crea una conexión bidireccional entre los nodos pasados
	 */
	public void interconectar(final Compuesto<Multiplexor> nodoEmisor2, final Compuesto<Multiplexor> nodoRuteador2) {
		builder.conectarDesde(nodoEmisor2, nodoRuteador2);
		builder.conectarDesde(nodoRuteador2, nodoEmisor2);
	}

	/**
	 * T008. En memoria, el tiempo de entrega normal debería ser inferior a 1ms (final o 1000
	 * mensajes por segundo)
	 */
	@Test
	public void en_Memoria_El_Tiempo_De_Entrega_Normal_Debería_Ser_Menosr_A_1Milisegundo() {
		final int cantidadDeMensajes = 100000;
		final WaitBarrier espeerarEntregas = WaitBarrier.create(cantidadDeMensajes);
		builder.conectarDesde(nodoReceptor, new ReceptorSupport() {

			public void recibir(final MensajeVortex mensaje) {
				espeerarEntregas.release();
			}
		});

		Loggers.ATOMOS
				.debug("Enviando desde emisor[{}] a receptor[{}] pasando por nodo[{}]",
						new Object[] { nodoEmisor.toShortString(), nodoReceptor.toShortString(),
								nodoRuteador.toShortString() });
		final long startNanos = System.nanoTime();
		LOG.debug("Nanos inicio: {}", startNanos);
		for (int i = 0; i < cantidadDeMensajes; i++) {
			// Necesitamos crear un mensaje por cada envío porque son identificados
			final MensajeConContenido mensaje = MensajeConContenido.crearVacio();
			final IdDeMensaje idDeMensaje = generadorDeIdsDelNodo.generarId();
			mensaje.asignarId(idDeMensaje);
			nodoEmisor.recibir(mensaje);
		}

		espeerarEntregas.waitForReleaseUpTo(TimeMagnitude.of(60, TimeUnit.SECONDS));
		LOG.debug("Nanos Fin: {}", System.nanoTime());
		final long endNanos = System.nanoTime();
		final long elapsedNanos = endNanos - startNanos;
		LOG.debug("Milis totales en procesar {} mensajes: {}", cantidadDeMensajes, elapsedNanos / 1000000d);
		LOG.debug("Milis dedicado por mensaje: {}", (elapsedNanos / 1000000d) / cantidadDeMensajes);
		final double mensajesPorSegundo = (cantidadDeMensajes * 1000000000d) / elapsedNanos;
		LOG.debug("Cantidad proyectada de mensajes procesables por segundo: {}", mensajesPorSegundo);
	}
}
