/**
 * 07/06/2012 23:25:09 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.sets.tests;

import java.util.List;
import java.util.concurrent.TimeUnit;

import junit.framework.Assert;
import net.gaia.vortex.sets.api.FiltroDeMensajes;
import net.gaia.vortex.sets.api.HandlerTipadoDeMensajes;
import net.gaia.vortex.sets.api.PortalSelectivo;
import net.gaia.vortex.sets.impl.filters.FiltroAceptaTodo;
import net.gaia.vortex.sets.impl.filters.InstanciasNoNulas;
import net.gaia.vortex.sets.test.HandlerTipadoEncolador;

import org.junit.Before;
import org.junit.Test;

import ar.com.dgarcia.coding.exceptions.TimeoutExceededException;
import ar.com.dgarcia.lang.time.TimeMagnitude;

/**
 * Esta clase prueba el comportamiento de vortex para manejo de eventos (como se define en A02 –
 * Comunicación de eventos)
 * 
 * @author D. García
 */
public class TestVortexSets {

	private PortalSelectivo portalEmisor;
	private PortalSelectivo portalReceptor;
	private PortalSelectivo portalReceptor2;

	@Before
	public void crearPortalSelectivo() {

	}

	/**
	 * T011. El receptor debería poder especificar el tipo eventos que recibe.<br>
	 * Sólo especifica el modo de uso de la api, sin asertar nada realmente. Es más que nada una
	 * especificación de referencia de la api
	 */
	@Test
	public void el_receptor_debería_poder_especificar_el_tipo_eventos_que_recibe() {
		final FiltroDeMensajes filtroDeMensajes = FiltroAceptaTodo.create();
		final HandlerTipadoDeMensajes<Object> handlerTipado = HandlerTipadoEncolador.create();
		portalReceptor.recibirMensajesCon(handlerTipado, filtroDeMensajes);
	}

	/**
	 * Este test es más que nada una especificación de la api para los emisores de mensajes
	 */
	@Test
	public void elEmisorDeberiaPoderEnviarEventos() {
		final Object objetoSerializable = new Object();
		portalEmisor.enviar(objetoSerializable);
	}

	/**
	 * T012. El mensaje enviado por el emisor debería llegar a los receptores interesados.<br>
	 * Prueba que teniendo dos receptores posibles el mensaje llega a los receptores interesados
	 */
	@Test
	public void el_mensaje_enviado_por_el_emisor_debería_llegar_a_los_receptores_interesados() {
		final FiltroDeMensajes instanciasDeString = InstanciasNoNulas.de(String.class);
		final HandlerTipadoEncolador handlerReceptor1 = HandlerTipadoEncolador.create();
		portalReceptor.recibirMensajesCon(handlerReceptor1, instanciasDeString);

		final HandlerTipadoEncolador handlerReceptor2 = HandlerTipadoEncolador.create();
		portalReceptor2.recibirMensajesCon(handlerReceptor2, instanciasDeString);

		final String mensajeEnviado = "texto de mensaje loco";
		portalEmisor.enviar(mensajeEnviado);

		final Object mensajeRecibido = handlerReceptor1.esperarPorMensaje(TimeMagnitude.of(1, TimeUnit.SECONDS));
		Assert.assertEquals("El enviado y recibido deberían ser iguales", mensajeEnviado, mensajeRecibido);

		final Object mensajeRecibidoEn2 = handlerReceptor2.esperarPorMensaje(TimeMagnitude.of(1, TimeUnit.SECONDS));
		Assert.assertEquals("El enviado y recibido deberían ser iguales", mensajeEnviado, mensajeRecibidoEn2);
	}

	/**
	 * T013. El mensaje enviado NO debería llegar a los receptores NO interesados.<br>
	 * Prueba que teniendo un receptor que no desea recibir el mensaje, el mensaje no llega (utiliza
	 * el tiempo como comprobación de que no llegó)
	 */
	@Test
	public void el_mensaje_enviado_NO_debería_llegar_a_los_receptores_NO_interesados() {
		final HandlerTipadoEncolador handlerReceptor1 = HandlerTipadoEncolador.create();
		portalReceptor.recibirMensajesCon(handlerReceptor1, InstanciasNoNulas.de(List.class));

		final HandlerTipadoEncolador handlerReceptor2 = HandlerTipadoEncolador.create();
		portalReceptor2.recibirMensajesCon(handlerReceptor2, InstanciasNoNulas.de(Number.class));

		final String mensajeEnviado = "texto de mensaje loco";
		portalEmisor.enviar(mensajeEnviado);

		try {
			handlerReceptor1.esperarPorMensaje(TimeMagnitude.of(1, TimeUnit.SECONDS));
			Assert.fail("El receptor 1 no debería haber recibido el mensaje porque no matchea el filtro");
		} catch (final TimeoutExceededException e) {
			// Excepción correcta
		}

		try {
			handlerReceptor2.esperarPorMensaje(TimeMagnitude.of(1, TimeUnit.SECONDS));
			Assert.fail("El receptor 2 no debería haber recibido el mensaje porque no matchea el filtro");
		} catch (final TimeoutExceededException e) {
			// Excepción correcta
		}
	}

	/**
	 * Resumen de T012 y T013, verifica las dos a la vez
	 */
	@Test
	public void elMensajeDeberiaLlegarAlInteresadoYNoAlNoInteresado() {
		final HandlerTipadoEncolador handlerReceptor1 = HandlerTipadoEncolador.create();
		portalReceptor.recibirMensajesCon(handlerReceptor1, InstanciasNoNulas.de(String.class));

		final HandlerTipadoEncolador handlerReceptor2 = HandlerTipadoEncolador.create();
		portalReceptor2.recibirMensajesCon(handlerReceptor2, InstanciasNoNulas.de(Number.class));

		final String mensajeEnviado = "texto de mensaje loco";
		portalEmisor.enviar(mensajeEnviado);

		final Object mensajeRecibido = handlerReceptor1.esperarPorMensaje(TimeMagnitude.of(1, TimeUnit.SECONDS));
		Assert.assertEquals("El enviado y recibido deberían ser iguales", mensajeEnviado, mensajeRecibido);

		try {
			handlerReceptor2.esperarPorMensaje(TimeMagnitude.of(1, TimeUnit.SECONDS));
			Assert.fail("El receptor 2 no debería haber recibido el mensaje porque no matchea el filtro");
		} catch (final TimeoutExceededException e) {
			// Excepción correcta
		}

	}

	/**
	 * Este test aserta un detalle de la implementación a modo de recordatorio por si alguna vez
	 * cambia, y a modo de documentación porque puede ser tricky
	 */
	@Test
	public void alDefinirOtroHandlerParaElMismoFiltroSePisaElAnterior() {
		final InstanciasNoNulas instanciasDeString = InstanciasNoNulas.de(String.class);
		final HandlerTipadoEncolador handlerReceptor1 = HandlerTipadoEncolador.create();
		portalReceptor.recibirMensajesCon(handlerReceptor1, instanciasDeString);

		final HandlerTipadoEncolador handlerReceptor2 = HandlerTipadoEncolador.create();
		portalReceptor.recibirMensajesCon(handlerReceptor2, instanciasDeString);

		final String mensajeEnviado = "texto de mensaje loco";
		portalEmisor.enviar(mensajeEnviado);

		final Object mensajeRecibido = handlerReceptor2.esperarPorMensaje(TimeMagnitude.of(1, TimeUnit.SECONDS));
		Assert.assertEquals("El enviado y recibido deberían ser iguales", mensajeEnviado, mensajeRecibido);

		try {
			handlerReceptor1.esperarPorMensaje(TimeMagnitude.of(1, TimeUnit.SECONDS));
			Assert.fail("El receptor 1 no debería haber recibido el mensaje porque no matchea el filtro");
		} catch (final TimeoutExceededException e) {
			// Excepción correcta
		}

	}

	/**
	 * T014. En memoria el tiempo de entrega debería ser inferior a 1 ms (o 1000 msg/s).<br>
	 * Mide la recepción de los mensajes aplicando como filtro el tipo del mensaje para verificar
	 * que el tiempo de entrega está dentro de los rangos esperados
	 */
	@Test
	public void en_memoria_el_tiempo_de_entrega_debería_ser_inferior_a_1_ms_o_1000_msg_por_segundo() {

	}

	/**
	 * Verifica que si se utilizan RegEx para filtrar los mensajes recibidos (junto con el tipo del
	 * mesaje) aún así el tiempo de entrega esté dentro de los valores esperados
	 */
	@Test
	public void enMemoriaElTiempoDeEntregaDeberiaSerInferiorA1msAunUsandoRegex() {

	}

}
