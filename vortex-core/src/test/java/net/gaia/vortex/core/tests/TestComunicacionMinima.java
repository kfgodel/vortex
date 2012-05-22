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

import junit.framework.Assert;
import net.gaia.taskprocessor.api.TimeMagnitude;
import net.gaia.taskprocessor.api.exceptions.TimeoutExceededException;
import net.gaia.vortex.core.api.HandlerDeMensajesVecinos;
import net.gaia.vortex.core.impl.NodoMinimo;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Esta clase testea las aserciones mínimas de vortex para permitir la comunicación entre dos partes
 * sin indicar intereses
 * 
 * @author D. García
 */
public class TestComunicacionMinima {
	private static final Logger LOG = LoggerFactory.getLogger(TestComunicacionMinima.class);

	private NodoMinimo nodo;

	private NodoMinimo nodoEmisor;
	private NodoMinimo nodoReceptor;

	@Before
	public void crearNodos() {
		nodo = NodoMinimo.create();
		nodoEmisor = NodoMinimo.create();
		nodoReceptor = NodoMinimo.create();
		nodoEmisor.conectarCon(nodoReceptor);
	}

	@After
	public void eliminarNodos() {
		nodo.liberarRecursos();
		nodoEmisor.liberarRecursos();
		nodoReceptor.liberarRecursos();
	}

	/**
	 * T001. El emisor debería poder enviar por vortex cualquier objeto serializable
	 */
	@Test
	public void el_Emisor_Deberia_Poder_Enviar_Por_Vortex_Cualquier_Objeto_Serializable() {
		final Object mensaje = new Object();
		nodo.enviarAVecinos(mensaje);
	}

	/**
	 * T002. El receptor debería poder recibir de vortex cualquier objeto serializable
	 */
	@Test
	public void el_Receptor_Debería_Poder_Recibir_De_Vortex_Cualquier_Objeto_Serializable() {
		final HandlerDeMensajesVecinos handlerParaMensajesRecibidos = HandlerEncolador.create();
		nodo.setHandlerDeMensajesVecinos(handlerParaMensajesRecibidos);
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
	 * T004. El thread del emisor no debería bloquearse durante la entrega del mensaje
	 */
	@Test
	public void el_Thread_Del_Emisor_No_Deberia_Bloquearse_Durante_La_Entrega_Del_Mensaje() {

	}

	/**
	 * T005. El thread del receptor debería poder ser independiente del usado para la entrega del
	 * mensaje
	 */
	@Test
	public void el_Thread_Del_Receptro_Debería_Poder_Ser_Independiente_Del_Usado_Para_La_Entrega_Del_Mensaje() {

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
		final NodoMinimo nodoEmisor = NodoMinimo.create();
		final NodoMinimo nodoIntermedio = NodoMinimo.create();
		final NodoMinimo nodoReceptor = NodoMinimo.create();
		final HandlerEncolador handlerReceptor = HandlerEncolador.create();
		nodoReceptor.setHandlerDeMensajesVecinos(handlerReceptor);

		nodoEmisor.conectarCon(nodoIntermedio);
		nodoIntermedio.conectarCon(nodoReceptor);

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

		// System.out.println(elapsedNanos / 1000 + "segs elapsed in " + cantidadDeMensajes +
		// " msgs");
		// final double milisPorMensaje = elapsedNanos / cantidadDeMensajes;
		// System.out.println(milisPorMensaje + "ms por mensaje");
		//
		// final double cantidadDeMensajesPorSegundos = cantidadDeMensajes / (elapsedNanos / 1000);
		// System.out.println(cantidadDeMensajesPorSegundos + " mensajes por segundo");
		// Assert.assertTrue("La cantidad de mensajes entregados deberían ser mayor a 1000 por segundo: "
		// + cantidadDeMensajesPorSegundos, cantidadDeMensajesPorSegundos > 1000d);
	}

}
