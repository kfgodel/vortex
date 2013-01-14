/**
 * 13/06/2012 14:42:44 Copyright (C) 2011 10Pines S.R.L.
 */
package net.gaia.vortex.core.tests;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import junit.framework.Assert;
import net.gaia.taskprocessor.api.TaskProcessor;
import net.gaia.vortex.core.api.Nodo;
import net.gaia.vortex.core.api.atomos.Receptor;
import net.gaia.vortex.core.api.mensaje.MensajeVortex;
import net.gaia.vortex.core.external.VortexProcessorFactory;
import net.gaia.vortex.core.impl.atomos.forward.MultiplexorParalelo;
import net.gaia.vortex.core.impl.atomos.support.basicos.ReceptorSupport;
import net.gaia.vortex.core.impl.mensaje.MensajeConContenido;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ar.com.dgarcia.coding.exceptions.TimeoutExceededException;
import ar.com.dgarcia.lang.conc.WaitBarrier;
import ar.com.dgarcia.lang.time.TimeMagnitude;

/**
 * Esta clase realiza las pruebas definidas en A01 de la documentación pero utilizando multiplexores
 * como api mínima de Atomos, por lo que no se respeta exactamente los mismos casos.<br>
 * De esta manera verificamos que es posible armar una red básica con atomos pero requiere especial
 * atención en el diseño de la red y en la direccionalidad de las conexiones
 * 
 * @author D. García
 */
public class TestRedA01ConMultiplexores {
	private static final Logger LOG = LoggerFactory.getLogger(TestRedA01ConMultiplexores.class);

	private Nodo nodoEmisor;
	private Nodo nodoReceptor;
	private MensajeVortex mensaje1;
	private TaskProcessor processor;

	@Before
	public void crearNodos() {
		processor = VortexProcessorFactory.createProcessor();
		mensaje1 = MensajeConContenido.crearVacio();
		nodoEmisor = MultiplexorParalelo.create(processor);
		nodoReceptor = MultiplexorParalelo.create(processor);

		nodoEmisor.conectarCon(nodoReceptor);
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
		nodoReceptor.conectarCon(receptorDelCliente);
	}

	/**
	 * T003. El mensaje enviado desde el emisor debería llegar al receptor<br>
	 * En este caso verificamos que el mensaje vortex recibido sea el mismo que el enviado porque
	 * todo pasa en memoria
	 */
	@Test
	public void el_Mensaje_Enviado_Desde_El_Emisor_Y_El_Recibido_Por_El_Receptor_Deberian_Ser_Iguales() {
		final ReceptorEncolador handlerReceptor = ReceptorEncolador.create();
		nodoReceptor.conectarCon(handlerReceptor);

		nodoEmisor.recibir(mensaje1);

		final MensajeVortex mensajeRecibido = handlerReceptor.esperarPorMensaje(TimeMagnitude.of(1, TimeUnit.SECONDS));
		Assert.assertSame("El enviado y recibido deberían ser el mismo", mensaje1, mensajeRecibido);
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
		final Receptor handlerReceptor = new ReceptorSupport() {

			@Override
			public void recibir(final MensajeVortex mensaje) {
				receptorBloqueado.set(true);
				// Hacemos que el emisor siga ejecutando si nos estaba esperando
				bloqueoDelEmisor.release();
				// Esperamos que el emisor nos de permiso de ejecutar
				bloqueoDelReceptor.waitForReleaseUpTo(TimeMagnitude.of(1, TimeUnit.SECONDS));
				receptorBloqueado.set(false);
			}
		};
		nodoReceptor.conectarCon(handlerReceptor);

		nodoEmisor.recibir(mensaje1);

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
		final Receptor handlerReceptor = new ReceptorSupport() {
			@Override
			public void recibir(final MensajeVortex mensaje) {
				final Thread threadActual = Thread.currentThread();
				threadDeEntrega.set(threadActual);
				threadDeEntregaDefinido.release();
			}
		};
		nodoReceptor.conectarCon(handlerReceptor);

		nodoEmisor.recibir(mensaje1);

		// Esperamos que el thread utilizado para la entrega sea definido
		threadDeEntregaDefinido.waitForReleaseUpTo(TimeMagnitude.of(1, TimeUnit.SECONDS));

		final Thread threadParaEntrega = threadDeEntrega.get();
		Assert.assertNotSame("Deberían ser threads distintos", Thread.currentThread(), threadParaEntrega);

	}

	/**
	 * T006. El emisor no debería recibir su propio mensaje.<br>
	 * El multiplexor no puede distinguir origen del mensaje, por lo que si se conecta en forma
	 * bidireccional se produce un loop. La única manera que podemos asegurar que el mensaje no
	 * llegue al emisor con atomos es por diseño de la red, de manera que el emisor no pueda
	 * escuchar los mensajes que envía, en caso contrario se reciben siempre
	 */
	@Test
	public void el_Emisor_No_Deberia_Recibir_Su_Propio_Mensaje() {
		// Creamos el receptor pero sin conectarlo al emisor
		final ReceptorEncolador handlerReceptor = ReceptorEncolador.create();
		nodoEmisor.recibir(mensaje1);

		try {
			handlerReceptor.esperarPorMensaje(TimeMagnitude.of(1, TimeUnit.SECONDS));
			Assert.fail("Nunca debería salir de la espera sin excepción");
		} catch (final TimeoutExceededException e) {
			// Es la excepción que esperábamos
		}

		// Si ahora lo conectamos al emisor, el mensaje nos llega
		nodoEmisor.conectarCon(handlerReceptor);

		nodoEmisor.recibir(mensaje1);
		final MensajeVortex mensajeRecibido = handlerReceptor.esperarPorMensaje(TimeMagnitude.of(1, TimeUnit.SECONDS));
		Assert.assertSame("La segunda vez el emisor no discrimina de donde manda", mensaje1, mensajeRecibido);
	}

	/**
	 * Verifica que el mensaje llegue si hay intermediarios
	 */
	@Test
	public void elMensajeDeberiaLlegarSiHayUnNodoEnElMedio() {
		final Nodo nodoEmisor = MultiplexorParalelo.create(processor);
		final Nodo nodoIntermedio1 = MultiplexorParalelo.create(processor);
		final Nodo nodoIntermedio2 = MultiplexorParalelo.create(processor);
		final Nodo nodoReceptor = MultiplexorParalelo.create(processor);

		final ReceptorEncolador handlerReceptor = ReceptorEncolador.create();
		nodoReceptor.conectarCon(handlerReceptor);

		nodoEmisor.conectarCon(nodoIntermedio1);
		nodoIntermedio1.conectarCon(nodoIntermedio2);
		nodoIntermedio2.conectarCon(nodoReceptor);

		nodoEmisor.recibir(mensaje1);
		final MensajeVortex mensajeRecibido = handlerReceptor.esperarPorMensaje(TimeMagnitude.of(1, TimeUnit.SECONDS));
		Assert.assertSame("El mensaje debería llegar igual al receptor", mensaje1, mensajeRecibido);
	}

	/**
	 * Verifica que el mensaje llegue a puntas distintas
	 */
	@Test
	public void elMensajeDeberiaLlegarADosReceptoresIndependientes() {
		// Armamos la red con otro receptor
		final ReceptorEncolador handlerReceptor1 = ReceptorEncolador.create();
		nodoReceptor.conectarCon(handlerReceptor1);

		final ReceptorEncolador handlerReceptor2 = ReceptorEncolador.create();
		final Nodo nodoReceptor2 = MultiplexorParalelo.create(processor);
		nodoReceptor2.conectarCon(handlerReceptor2);
		nodoEmisor.conectarCon(nodoReceptor2);

		// Mandamos el mensaje
		nodoEmisor.recibir(mensaje1);

		// Verificamos que haya llegado a los dos
		final MensajeVortex mensajeRecibidoPor1 = handlerReceptor1.esperarPorMensaje(TimeMagnitude.of(1,
				TimeUnit.SECONDS));
		Assert.assertSame("El primer receptor debería haber recibido el mensaje", mensaje1, mensajeRecibidoPor1);

		final MensajeVortex mensajeRecibidoPor2 = handlerReceptor2.esperarPorMensaje(TimeMagnitude.of(1,
				TimeUnit.SECONDS));
		Assert.assertSame("El segundo receptor debería haber recibido el mensaje", mensaje1, mensajeRecibidoPor2);
	}

	/**
	 * T008. En memoria, el tiempo de entrega normal debería ser inferior a 1ms (final o 1000
	 * mensajes por segundo).<br>
	 * Este test crea un mensaje cada vez, haciendolo comparable con el de
	 * {@link TestRedA01ConNodoMultiplexor#en_Memoria_El_Tiempo_De_Entrega_Normal_Debería_Ser_Menosr_A_1Milisegundo()}
	 */
	@Test
	public void en_Memoria_El_Tiempo_De_Entrega_Normal_Debería_Ser_Menosr_A_1Milisegundo_Con_Creacion_De_Mensaje_Por_Vez() {
		final int cantidadDeMensajes = 100000;
		final WaitBarrier espeerarEntregas = WaitBarrier.create(cantidadDeMensajes);
		nodoReceptor.conectarCon(new ReceptorSupport() {
			@Override
			public void recibir(final MensajeVortex mensaje) {
				espeerarEntregas.release();
			}
		});

		final long startNanos = System.nanoTime();
		LOG.debug("Nanos inicio: {}", startNanos);
		for (int i = 0; i < cantidadDeMensajes; i++) {
			final MensajeConContenido mensaje = MensajeConContenido.crearVacio();
			nodoEmisor.recibir(mensaje);
		}

		espeerarEntregas.waitForReleaseUpTo(TimeMagnitude.of(1, TimeUnit.MINUTES));
		LOG.debug("Nanos Fin: {}", System.nanoTime());
		final long endNanos = System.nanoTime();
		final long elapsedNanos = endNanos - startNanos;
		LOG.debug("Milis totales en procesar {} mensajes: {}", cantidadDeMensajes, elapsedNanos / 1000000d);
		LOG.debug("Milis dedicado por mensaje: {}", (elapsedNanos / 1000000d) / cantidadDeMensajes);
		final double mensajesPorSegundo = (cantidadDeMensajes * 1000000000d) / elapsedNanos;
		LOG.debug("Cantidad proyectada de mensajes procesables por segundo: {}", mensajesPorSegundo);
	}

	/**
	 * T008. En memoria, el tiempo de entrega normal debería ser inferior a 1ms (final o 1000
	 * mensajes por segundo)
	 */
	@Test
	public void en_Memoria_El_Tiempo_De_Entrega_Normal_Debería_Ser_Menosr_A_1Milisegundo_Sin_Creacion_De_mensaje() {
		final int cantidadDeMensajes = 100000;
		final WaitBarrier espeerarEntregas = WaitBarrier.create(cantidadDeMensajes);
		nodoReceptor.conectarCon(new ReceptorSupport() {
			@Override
			public void recibir(final MensajeVortex mensaje) {
				espeerarEntregas.release();
			}
		});

		final long startNanos = System.nanoTime();
		LOG.debug("Nanos inicio: {}", startNanos);
		for (int i = 0; i < cantidadDeMensajes; i++) {
			nodoEmisor.recibir(mensaje1);
		}

		espeerarEntregas.waitForReleaseUpTo(TimeMagnitude.of(1, TimeUnit.MINUTES));
		LOG.debug("Nanos Fin: {}", System.nanoTime());
		final long endNanos = System.nanoTime();
		final long elapsedNanos = endNanos - startNanos;
		LOG.debug("Milis totales en procesar {} mensajes: {}", cantidadDeMensajes, elapsedNanos / 1000000d);
		LOG.debug("Milis dedicado por mensaje: {}", (elapsedNanos / 1000000d) / cantidadDeMensajes);
		final double mensajesPorSegundo = (cantidadDeMensajes * 1000000000d) / elapsedNanos;
		LOG.debug("Cantidad proyectada de mensajes procesables por segundo: {}", mensajesPorSegundo);
	}
}
