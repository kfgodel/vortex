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
import net.gaia.vortex.core.impl.NodoPortalSinThreads;
import net.gaia.vortex.core.impl.NodoRuteadorMinimo;
import net.gaia.vortex.core3.api.mensaje.MensajeVortex;
import net.gaia.vortex.core3.api.moleculas.portal.Portal;
import net.gaia.vortex.core3.api.moleculas.ruteo.NodoHub;
import net.gaia.vortex.core3.impl.condiciones.SiempreTrue;
import net.gaia.vortex.core3.impl.mensaje.MensajeMapa;
import net.gaia.vortex.core3.impl.moleculas.portal.HandlerTipado;
import net.gaia.vortex.core3.tests.ReceptorEncolador;

import org.junit.Before;
import org.junit.Test;

import ar.com.dgarcia.coding.exceptions.TimeoutExceededException;
import ar.com.dgarcia.lang.conc.WaitBarrier;
import ar.com.dgarcia.lang.time.TimeMagnitude;

/**
 * Esta clase realiza las pruebas definidas en A01 de la documentacion pero utilizando el la api del
 * {@link HubMinimo}
 * 
 * @author D. García
 */
public class TestA01ConNodoPortal {

	private Portal nodoEmisor;
	private Portal nodoReceptor;
	private MensajeVortex mensaje1;
	private TaskProcessor processor;

	@Before
	public void crearNodos() {
		processor = ExecutorBasedTaskProcesor.create();
		mensaje1 = MensajeMapa.create();

	}

	/**
	 * T001. El emisor debería poder enviar por vortex cualquier objeto serializable
	 */
	@Test
	public void el_Emisor_Deberia_Poder_Enviar_Por_Vortex_Cualquier_Objeto_Serializable() {
		// Para enviar al red se le indica que reciba un mensaje
		final Object cualquierObjeto = new Object();
		nodoEmisor.enviar(cualquierObjeto);
	}

	/**
	 * T002. El receptor debería poder recibir de vortex cualquier objeto serializable
	 */
	@Test
	public void el_Receptor_Debería_Poder_Recibir_De_Vortex_Cualquier_Objeto_Serializable() {
		// Aceptamos todo lo que ande dando vueltas como object
		nodoReceptor.recibirCon(new HandlerTipado<Object>(SiempreTrue.getInstancia()) {
			@Override
			public void onMensajeRecibido(final Object mensaje) {
				// Invocado al recibir un mensaje
			}
		});
	}

	/**
	 * T003. El mensaje enviado desde el emisor debería llegar al receptor
	 */
	@Test
	public void el_Mensaje_Enviado_Desde_El_Emisor_Y_El_Recibido_Por_El_Receptor_Deberian_Ser_Iguales() {
		final ReceptorEncolador handlerReceptor = ReceptorEncolador.create();
		nodoReceptor.recibirCon(handlerReceptor);

		nodoEmisor.recibir(mensaje1);

		final Object mensajeRecibido = handlerReceptor.esperarPorMensaje(TimeMagnitude.of(1, TimeUnit.SECONDS));
		Assert.assertEquals("El enviado y recibido deberían ser iguales", mensaje1, mensajeRecibido);
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
		final ComponenteVortex handlerReceptor = new ComponenteVortex() {

			@Override
			public void recibirMensaje(final MensajeVortex mensaje) {
				receptorBloqueado.set(true);
				// Hacemos que el emisor siga ejecutando si nos estaba esperando
				bloqueoDelEmisor.release();
				// Esperamos que el emisor nos de permiso de ejecutar
				bloqueoDelReceptor.waitForReleaseUpTo(TimeMagnitude.of(1, TimeUnit.SECONDS));
				receptorBloqueado.set(false);
			}
		};
		nodoReceptor.agregarDestino(handlerReceptor);

		nodoEmisor.recibirMensaje(mensaje1);

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
		final ComponenteVortex handlerReceptor = new ComponenteVortex() {
			@Override
			public void recibirMensaje(final MensajeVortex mensaje) {
				final Thread threadActual = Thread.currentThread();
				threadDeEntrega.set(threadActual);
				threadDeEntregaDefinido.release();
			}
		};
		nodoReceptor.agregarDestino(handlerReceptor);

		nodoEmisor.recibirMensaje(mensaje1);

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
		final ReceptorEncolador handlerReceptor = ReceptorEncolador.create();
		nodoEmisor.agregarDestino(handlerReceptor);

		mensaje1.setEmisor(handlerReceptor);
		nodoEmisor.recibirMensaje(mensaje1);

		try {
			handlerReceptor.esperarPorMensaje(TimeMagnitude.of(1, TimeUnit.SECONDS));
			Assert.fail("Nunca debería salir de la espera sin excepción");
		} catch (final TimeoutExceededException e) {
			// Es la excepción que esperábamos
		}

	}

	/**
	 * Verifica que el mensaje llegue si hay intermediario
	 */
	@Test
	public void elMensajeDeberiaLlegarSiHayUnNodoEnElMedio() {
		final NodoHub nodoEmisor = NodoPortalSinThreads.create();
		final NodoHub nodoIntermedio1 = NodoRuteadorMinimo.create();
		final NodoHub nodoIntermedio2 = NodoRuteadorMinimo.create();
		final NodoHub nodoReceptor = NodoPortalSinThreads.create();

		final ReceptorEncolador handlerReceptor = ReceptorEncolador.create();
		nodoReceptor.agregarDestino(handlerReceptor);

		nodoEmisor.agregarDestino(nodoIntermedio1);
		nodoIntermedio1.agregarDestino(nodoIntermedio2);
		nodoIntermedio2.agregarDestino(nodoReceptor);

		nodoReceptor.agregarDestino(nodoIntermedio2);
		nodoIntermedio2.agregarDestino(nodoIntermedio1);
		nodoIntermedio1.agregarDestino(nodoEmisor);

		nodoEmisor.recibirMensaje(mensaje1);
		final Object mensajeRecibido = handlerReceptor.esperarPorMensaje(TimeMagnitude.of(1, TimeUnit.SECONDS));
		Assert.assertEquals("El mensaje debería llegar igual al receptor", mensaje1, mensajeRecibido);
	}

	@Test
	public void elMensajeDeberiaLlegarADosReceptoresIndependientes() {
		// Aramamos la red con otro receptor
		final ReceptorEncolador handlerReceptor1 = ReceptorEncolador.create();
		nodoReceptor.agregarDestino(handlerReceptor1);

		final ReceptorEncolador handlerReceptor2 = ReceptorEncolador.create();
		final NodoHub nodoReceptor2;
		nodoReceptor2.agregarDestino(handlerReceptor2);

		final NodoHub nodoRuteador;
		nodoRuteador.agregarDestino(nodoReceptor2);
		nodoReceptor2.agregarDestino(nodoRuteador);

		// Mandamos el mensaje
		nodoEmisor.recibirMensaje(mensaje1);

		// Verificamos que haya llegado a los dos
		final Object mensajeRecibidoPor1 = handlerReceptor1.esperarPorMensaje(TimeMagnitude.of(1, TimeUnit.SECONDS));
		Assert.assertEquals("El primer receptor debería haber recibido el mensaje", mensaje1, mensajeRecibidoPor1);

		final Object mensajeRecibidoPor2 = handlerReceptor2.esperarPorMensaje(TimeMagnitude.of(1, TimeUnit.SECONDS));
		Assert.assertEquals("El segundo receptor debería haber recibido el mensaje", mensaje1, mensajeRecibidoPor2);
	}

}
