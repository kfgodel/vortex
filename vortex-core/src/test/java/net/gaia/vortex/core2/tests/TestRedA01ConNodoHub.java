/**
 * 13/06/2012 14:42:44 Copyright (C) 2011 10Pines S.R.L.
 */
package net.gaia.vortex.core2.tests;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import junit.framework.Assert;
import net.gaia.taskprocessor.api.TaskProcessor;
import net.gaia.taskprocessor.executor.ExecutorBasedTaskProcesor;
import net.gaia.vortex.core.api.atomos.Receptor;
import net.gaia.vortex.core.api.mensaje.MensajeVortex;
import net.gaia.vortex.core.api.moleculas.ruteo.NodoHub;
import net.gaia.vortex.core.api.moleculas.ruteo.NodoHubConNexoCore;
import net.gaia.vortex.core.impl.atomos.ReceptorNulo;
import net.gaia.vortex.core.impl.atomos.forward.NexoEjecutor;
import net.gaia.vortex.core.impl.mensaje.MensajeMapa;
import net.gaia.vortex.core.impl.moleculas.ruteo.HubConNexo;
import net.gaia.vortex.core.tests.ReceptorEncolador;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ar.com.dgarcia.coding.exceptions.TimeoutExceededException;
import ar.com.dgarcia.lang.conc.WaitBarrier;
import ar.com.dgarcia.lang.time.TimeMagnitude;

/**
 * Esta clase realiza las pruebas definidas en A01 de la documentación pero utilizando
 * {@link NodoHub}, por lo que no se respeta exactamente los mismos casos pero es más aproximado que
 * {@link TestRedA01ConMultiplexores}.<br>
 * De esta manera verificamos que es posible armar una red básica sin tomar en cuenta la
 * direccionalidad de las conexiones
 * 
 * @author D. García
 */
public class TestRedA01ConNodoHub {

	private NodoHubConNexoCore nodoEmisor;
	private NodoHub nodoRuteador;
	private NodoHub nodoReceptor;
	private MensajeVortex mensaje1;
	private TaskProcessor processor;

	@Before
	public void crearNodos() {
		processor = ExecutorBasedTaskProcesor.create();
		mensaje1 = MensajeMapa.create();
		nodoEmisor = HubConNexo.create(processor);
		nodoRuteador = HubConNexo.create(processor);
		nodoReceptor = HubConNexo.create(processor);

		interconectar(nodoEmisor, nodoRuteador);
		interconectar(nodoRuteador, nodoReceptor);
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
		final Receptor handlerReceptor = new Receptor() {

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
		final Receptor handlerReceptor = new Receptor() {
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
	 * Verificamos que si mandamos un mensaje desde el emisor, el mensaje no viene de vuelta desde
	 * el ruteador
	 */
	@Test
	public void el_Emisor_No_Deberia_Recibir_Su_Propio_Mensaje() {
		final ReceptorEncolador recibidosPorElEmisor = ReceptorEncolador.create();
		nodoEmisor.setNexoCore(NexoEjecutor.create(processor, recibidosPorElEmisor, ReceptorNulo.getInstancia()));

		nodoEmisor.recibir(mensaje1);

		final MensajeVortex recibidoPrimero = recibidosPorElEmisor.esperarPorMensaje(TimeMagnitude.of(1,
				TimeUnit.SECONDS));
		Assert.assertSame("Deberíamos tener el mensaje como recibido al enviar", mensaje1, recibidoPrimero);

		try {
			recibidosPorElEmisor.esperarPorMensaje(TimeMagnitude.of(1, TimeUnit.SECONDS));
			Assert.fail("No deberíamos haber recibido un segundo mensaje");
		} catch (final TimeoutExceededException e) {
			// Es la excepción que esperábamos
		}
	}

	/**
	 * Verifica que el mensaje llegue si hay intermediarios
	 */
	@Test
	public void elMensajeDeberiaLlegarSiHayUnNodoEnElMedio() {
		final NodoHub nodoEmisor = HubConNexo.create(processor);
		final NodoHub nodoIntermedio1 = HubConNexo.create(processor);
		final NodoHub nodoIntermedio2 = HubConNexo.create(processor);
		final NodoHub nodoReceptor = HubConNexo.create(processor);

		final ReceptorEncolador handlerReceptor = ReceptorEncolador.create();
		nodoReceptor.conectarCon(handlerReceptor);

		interconectar(nodoEmisor, nodoIntermedio1);
		interconectar(nodoIntermedio1, nodoIntermedio2);
		interconectar(nodoIntermedio2, nodoReceptor);

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
		final NodoHub nodoReceptor2 = HubConNexo.create(processor);
		nodoReceptor2.conectarCon(handlerReceptor2);

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
	public void interconectar(final NodoHub origen, final NodoHub destino) {
		origen.conectarCon(destino);
		destino.conectarCon(origen);
	}

}