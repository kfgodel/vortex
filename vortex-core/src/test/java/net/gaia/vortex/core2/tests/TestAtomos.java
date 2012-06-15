/**
 * 13/06/2012 12:15:18 Copyright (C) 2011 10Pines S.R.L.
 */
package net.gaia.vortex.core2.tests;

import java.util.concurrent.TimeUnit;

import net.gaia.taskprocessor.api.TaskProcessor;
import net.gaia.taskprocessor.executor.ExecutorBasedTaskProcesor;
import net.gaia.vortex.core2.api.atomos.ComponenteVortex;
import net.gaia.vortex.core2.impl.atomos.bifurcador.ProxyBifurcador;
import net.gaia.vortex.core2.impl.atomos.condicional.ProxyCondicional;
import net.gaia.vortex.core2.impl.atomos.condicional.SiempreFalse;
import net.gaia.vortex.core2.impl.atomos.condicional.SiempreTrue;
import net.gaia.vortex.core2.impl.atomos.ejecutor.ProxyEjecutor;
import net.gaia.vortex.core2.impl.atomos.multiplexor.MultiplexorParalelo;
import net.gaia.vortex.core2.impl.atomos.transformador.ProxyTransformador;
import net.gaia.vortex.core2.impl.mensajes.MensajeMapa;
import net.gaia.vortex.core3.api.atomos.mensaje.MensajeVortex;
import net.gaia.vortex.core3.api.atomos.transformacion.Transformacion;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import ar.com.dgarcia.coding.exceptions.TimeoutExceededException;
import ar.com.dgarcia.lang.time.TimeMagnitude;

/**
 * Esta clase prueba minimamente el comportamiento de los átomos vortex
 * 
 * @author D. García
 */
public class TestAtomos {

	private MensajeVortex mensaje1;
	private MensajeVortex mensaje2;

	private TaskProcessor processor;

	@Before
	public void crearProcesador() {
		mensaje1 = MensajeMapa.create();
		mensaje2 = MensajeMapa.create();
		processor = ExecutorBasedTaskProcesor.create();
	}

	@Test
	public void elEjecutorDeberiaInvocarElComponenteIndicadoAlRecibirUnMensaje() {
		final ComponenteEncolador receptor = ComponenteEncolador.create();
		final ProxyEjecutor ejecutor = ProxyEjecutor.create(processor, receptor);
		checkMensajeEnviadoYRecibido(mensaje1, mensaje1, ejecutor, receptor);
	}

	@Test
	public void elMultiplexorDeberiaEntregarAVariosDestinosAlRecibirUnMensaje() {
		final ComponenteEncolador receptor1 = ComponenteEncolador.create();
		final ComponenteEncolador receptor2 = ComponenteEncolador.create();
		final MultiplexorParalelo multiplexor = MultiplexorParalelo.create(processor);
		multiplexor.agregarDestino(receptor1);
		multiplexor.agregarDestino(receptor2);

		checkMensajeEnviadoYRecibido(mensaje1, mensaje1, multiplexor, receptor1, receptor2);
	}

	@Test
	public void elCondicionalDeberiaEntregarElMensajeSiCumpleLaCondicion() {
		final ComponenteEncolador receptor = ComponenteEncolador.create();
		final ProxyCondicional condicional = ProxyCondicional.create(processor, SiempreTrue.getInstancia(), receptor);
		checkMensajeEnviadoYRecibido(mensaje1, mensaje1, condicional, receptor);
	}

	@Test
	public void elCondicionalNoDeberiaEntregarElMensajeSiNoCumpleLaCondicion() {
		final ComponenteEncolador receptor = ComponenteEncolador.create();
		final ProxyCondicional condicional = ProxyCondicional.create(processor, SiempreFalse.getInstancia(), receptor);
		checkMensajeEnviadoYNoRecibido(mensaje1, mensaje1, condicional, receptor);
	}

	@Test
	public void elTransformadorDeberiaModificarElMensajeEntregado() {
		final ComponenteEncolador receptor = ComponenteEncolador.create();
		final Transformacion transformacion = new Transformacion() {
			@Override
			public MensajeVortex transformar(@SuppressWarnings("unused") final MensajeVortex mensaje) {
				return mensaje2;
			}
		};
		final ProxyTransformador transformador = ProxyTransformador.create(processor, transformacion, receptor);
		checkMensajeEnviadoYRecibido(mensaje1, mensaje2, transformador, receptor);
	}

	@Test
	public void elBifurcadorDeberiaElegirElDelegadoPorTrueSiLaCondicionEsCumplida() {
		final ComponenteEncolador receptorPorTrue = ComponenteEncolador.create();
		final ComponenteEncolador receptorPorFalse = ComponenteEncolador.create();
		final ProxyBifurcador bifurcador = ProxyBifurcador.create(processor, SiempreTrue.create(), receptorPorTrue,
				receptorPorFalse);
		checkMensajeEnviadoYRecibido(mensaje1, mensaje1, bifurcador, receptorPorTrue);
		verificarMensajeNoRecibido(0, receptorPorFalse);
	}

	@Test
	public void elBifurcadorDeberiaElegirElDelegadoPorFalseSiLaCondicionNoEsCumplida() {
		final ComponenteEncolador receptorPorTrue = ComponenteEncolador.create();
		final ComponenteEncolador receptorPorFalse = ComponenteEncolador.create();
		final ProxyBifurcador bifurcador = ProxyBifurcador.create(processor, SiempreFalse.create(), receptorPorTrue,
				receptorPorFalse);
		checkMensajeEnviadoYRecibido(mensaje1, mensaje1, bifurcador, receptorPorFalse);
		verificarMensajeNoRecibido(0, receptorPorTrue);
	}

	/**
	 * Verifica que después de enviar el mensaje, se haya recibido el esperado en todos los
	 * receptores
	 * 
	 * @param mensajeEnviado
	 *            El mensaje a enviar desde el emisor
	 * @param mensajeEsperado
	 *            El mensaje que deberían haber recibido los receptores
	 * @param emisor
	 *            El emisor al que se le pasa el mensaje para enviar
	 * @param receptores
	 *            Los receptores que deberían haber recibido el mensaje
	 */
	public void checkMensajeEnviadoYRecibido(final MensajeVortex mensajeEnviado, final MensajeVortex mensajeEsperado,
			final ComponenteVortex emisor, final ComponenteEncolador... receptores) {
		// Enviamos el mensaje
		emisor.recibirMensaje(mensajeEnviado);
		// Verificamos la respuesta
		for (int i = 0; i < receptores.length; i++) {
			final ComponenteEncolador receptor = receptores[i];
			verificarQueRecibio(mensajeEsperado, i, receptor);
		}
	}

	/**
	 * Verifica que el receptor pasado tenga el mensaje esperado
	 */
	private void verificarQueRecibio(final MensajeVortex mensajeEsperado, final int i,
			final ComponenteEncolador receptor) {
		final MensajeVortex recibidoPorElReceptor = receptor.esperarPorMensaje(TimeMagnitude.of(1, TimeUnit.SECONDS));
		Assert.assertSame("El receptor " + i + " deberia haber recibido el mensaje esperado", mensajeEsperado,
				recibidoPorElReceptor);
	}

	/**
	 * Verifica que después de enviar el mensaje, se haya recibido el esperado en todos los
	 * receptores
	 * 
	 * @param mensajeEnviado
	 *            El mensaje a enviar desde el emisor
	 * @param mensajeEsperado
	 *            El mensaje que deberían haber recibido los receptores
	 * @param emisor
	 *            El emisor al que se le pasa el mensaje para enviar
	 * @param receptores
	 *            Los receptores que deberían haber recibido el mensaje
	 */
	public void checkMensajeEnviadoYNoRecibido(final MensajeVortex mensajeEnviado, final MensajeVortex mensajeEsperado,
			final ComponenteVortex emisor, final ComponenteEncolador... receptores) {
		// Enviamos el mensaje
		emisor.recibirMensaje(mensajeEnviado);
		// Verificamos la respuesta
		for (int i = 0; i < receptores.length; i++) {
			final ComponenteEncolador receptor = receptores[i];
			verificarMensajeNoRecibido(i, receptor);
		}
	}

	/**
	 * Verifica que el receptor pasado no recibio ningun mensaje
	 */
	private void verificarMensajeNoRecibido(final int i, final ComponenteEncolador receptor) {
		try {
			receptor.esperarPorMensaje(TimeMagnitude.of(1, TimeUnit.SECONDS));
			Assert.fail("El mensaje no debería haber llegado al receptor " + i);
		} catch (final TimeoutExceededException e) {
			// Es la excepción esperada
		}
	}

}
