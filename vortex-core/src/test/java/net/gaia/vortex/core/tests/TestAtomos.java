/**
 * 13/06/2012 12:15:18 Copyright (C) 2011 10Pines S.R.L.
 */
package net.gaia.vortex.core.tests;

import java.util.concurrent.TimeUnit;

import net.gaia.taskprocessor.api.TaskProcessor;
import net.gaia.vortex.core.api.atomos.Receptor;
import net.gaia.vortex.core.api.mensaje.MensajeVortex;
import net.gaia.vortex.core.api.transformaciones.Transformacion;
import net.gaia.vortex.core.external.VortexProcessorFactory;
import net.gaia.vortex.core.impl.atomos.condicional.NexoBifurcador;
import net.gaia.vortex.core.impl.atomos.condicional.NexoFiltro;
import net.gaia.vortex.core.impl.atomos.forward.MultiplexorParalelo;
import net.gaia.vortex.core.impl.atomos.forward.NexoEjecutor;
import net.gaia.vortex.core.impl.atomos.transformacion.NexoTransformador;
import net.gaia.vortex.core.impl.condiciones.SiempreFalse;
import net.gaia.vortex.core.impl.condiciones.SiempreTrue;
import net.gaia.vortex.core.impl.mensaje.MensajeMapa;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import ar.com.dgarcia.coding.exceptions.TimeoutExceededException;
import ar.com.dgarcia.lang.time.TimeMagnitude;

/**
 * Esta clase el caso feliz para cada uno de los átomos base, de manera de registrar su
 * comportamiento esperado y uso
 * 
 * @author D. García
 */
public class TestAtomos {

	private MensajeVortex mensaje1;
	private MensajeVortex mensaje2;

	private TaskProcessor processor;

	@Before
	public void crearProcesadorYNodos() {
		mensaje1 = MensajeMapa.create();
		mensaje2 = MensajeMapa.create();
		processor = VortexProcessorFactory.createProcessor();
	}

	@After
	public void eliminarProcesador() {
		processor.detener();
	}

	@Test
	public void elEjecutorDeberiaInvocarElComponenteIndicadoAlRecibirUnMensaje() {
		final ReceptorEncolador ejecutante = ReceptorEncolador.create();
		final ReceptorEncolador receptor = ReceptorEncolador.create();
		final NexoEjecutor ejecutor = NexoEjecutor.create(processor, ejecutante, receptor);
		checkMensajeEnviadoYRecibido(mensaje1, mensaje1, ejecutor, ejecutante, receptor);
	}

	@Test
	public void elMultiplexorDeberiaEntregarAVariosDestinosAlRecibirUnMensaje() {
		final ReceptorEncolador receptor1 = ReceptorEncolador.create();
		final ReceptorEncolador receptor2 = ReceptorEncolador.create();
		final MultiplexorParalelo multiplexor = MultiplexorParalelo.create(processor);
		multiplexor.conectarCon(receptor1);
		multiplexor.conectarCon(receptor2);

		checkMensajeEnviadoYRecibido(mensaje1, mensaje1, multiplexor, receptor1, receptor2);
	}

	@Test
	public void elCondicionalDeberiaEntregarElMensajeSiCumpleLaCondicion() {
		final ReceptorEncolador receptor = ReceptorEncolador.create();
		final NexoFiltro condicional = NexoFiltro.create(processor, SiempreTrue.getInstancia(), receptor);
		checkMensajeEnviadoYRecibido(mensaje1, mensaje1, condicional, receptor);
	}

	@Test
	public void elCondicionalNoDeberiaEntregarElMensajeSiNoCumpleLaCondicion() {
		final ReceptorEncolador receptor = ReceptorEncolador.create();
		final NexoFiltro condicional = NexoFiltro.create(processor, SiempreFalse.getInstancia(), receptor);
		checkMensajeEnviadoYNoRecibido(mensaje1, mensaje1, condicional, receptor);
	}

	@Test
	public void elTransformadorDeberiaModificarElMensajeEntregado() {
		final ReceptorEncolador receptor = ReceptorEncolador.create();
		final Transformacion transformacion = new Transformacion() {
			@Override
			public MensajeVortex transformar(@SuppressWarnings("unused") final MensajeVortex mensaje) {
				return mensaje2;
			}
		};
		final NexoTransformador transformador = NexoTransformador.create(processor, transformacion, receptor);
		checkMensajeEnviadoYRecibido(mensaje1, mensaje2, transformador, receptor);
	}

	@Test
	public void elBifurcadorDeberiaElegirElDelegadoPorTrueSiLaCondicionEsCumplida() {
		final ReceptorEncolador receptorPorTrue = ReceptorEncolador.create();
		final ReceptorEncolador receptorPorFalse = ReceptorEncolador.create();
		final NexoBifurcador bifurcador = NexoBifurcador.create(processor, SiempreTrue.create(), receptorPorTrue,
				receptorPorFalse);
		checkMensajeEnviadoYRecibido(mensaje1, mensaje1, bifurcador, receptorPorTrue);
		verificarMensajeNoRecibido(0, receptorPorFalse);
	}

	@Test
	public void elBifurcadorDeberiaElegirElDelegadoPorFalseSiLaCondicionNoEsCumplida() {
		final ReceptorEncolador receptorPorTrue = ReceptorEncolador.create();
		final ReceptorEncolador receptorPorFalse = ReceptorEncolador.create();
		final NexoBifurcador bifurcador = NexoBifurcador.create(processor, SiempreFalse.create(), receptorPorTrue,
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
			final Receptor emisor, final ReceptorEncolador... receptores) {
		// Enviamos el mensaje
		emisor.recibir(mensajeEnviado);
		// Verificamos la respuesta
		for (int i = 0; i < receptores.length; i++) {
			final ReceptorEncolador receptor = receptores[i];
			verificarQueRecibio(mensajeEsperado, i, receptor);
		}
	}

	/**
	 * Verifica que el receptor pasado tenga el mensaje esperado
	 */
	private void verificarQueRecibio(final MensajeVortex mensajeEsperado, final int i, final ReceptorEncolador receptor) {
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
			final Receptor emisor, final ReceptorEncolador... receptores) {
		// Enviamos el mensaje
		emisor.recibir(mensajeEnviado);
		// Verificamos la respuesta
		for (int i = 0; i < receptores.length; i++) {
			final ReceptorEncolador receptor = receptores[i];
			verificarMensajeNoRecibido(i, receptor);
		}
	}

	/**
	 * Verifica que el receptor pasado no recibio ningun mensaje
	 */
	private void verificarMensajeNoRecibido(final int i, final ReceptorEncolador receptor) {
		try {
			receptor.esperarPorMensaje(TimeMagnitude.of(1, TimeUnit.SECONDS));
			Assert.fail("El mensaje no debería haber llegado al receptor " + i);
		} catch (final TimeoutExceededException e) {
			// Es la excepción esperada
		}
	}

}
