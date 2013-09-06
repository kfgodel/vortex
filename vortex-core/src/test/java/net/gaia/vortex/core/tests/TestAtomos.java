/**
 * 13/06/2012 12:15:18 Copyright (C) 2011 10Pines S.R.L.
 */
package net.gaia.vortex.core.tests;

import java.util.concurrent.TimeUnit;

import net.gaia.taskprocessor.api.processor.TaskProcessor;
import net.gaia.vortex.api.atomos.Bifurcador;
import net.gaia.vortex.api.atomos.Filtro;
import net.gaia.vortex.api.atomos.Multiplexor;
import net.gaia.vortex.api.atomos.Secuenciador;
import net.gaia.vortex.api.atomos.Transformador;
import net.gaia.vortex.api.basic.Receptor;
import net.gaia.vortex.api.builder.VortexCore;
import net.gaia.vortex.api.ids.componentes.IdDeComponenteVortex;
import net.gaia.vortex.api.ids.mensajes.IdDeMensaje;
import net.gaia.vortex.api.mensajes.MensajeVortex;
import net.gaia.vortex.api.moleculas.Selector;
import net.gaia.vortex.api.transformaciones.Transformacion;
import net.gaia.vortex.impl.builder.VortexCoreBuilder;
import net.gaia.vortex.impl.condiciones.SiempreFalse;
import net.gaia.vortex.impl.condiciones.SiempreTrue;
import net.gaia.vortex.impl.helpers.VortexProcessorFactory;
import net.gaia.vortex.impl.ids.componentes.GeneradorDeIdsGlobalesParaComponentes;
import net.gaia.vortex.impl.ids.mensajes.GeneradorSecuencialDeIdDeMensaje;
import net.gaia.vortex.impl.mensajes.MensajeConContenido;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import ar.com.dgarcia.coding.exceptions.TimeoutExceededException;
import ar.com.dgarcia.lang.time.TimeMagnitude;

/**
 * Esta clase prueba el caso feliz para cada uno de los átomos base, de manera de registrar su
 * comportamiento esperado y uso
 * 
 * @author D. García
 */
public class TestAtomos {

	private MensajeVortex mensaje1;
	private MensajeVortex mensaje2;

	private TaskProcessor processor;

	private VortexCore builder;

	@Before
	public void crearProcesadorYNodos() {
		mensaje1 = MensajeConContenido.crearVacio();
		mensaje2 = MensajeConContenido.crearVacio();
		processor = VortexProcessorFactory.createProcessor();
		builder = VortexCoreBuilder.create(processor);
	}

	@After
	public void eliminarProcesador() {
		processor.detener();
	}

	@Test
	public void elEjecutorDeberiaInvocarElComponenteIndicadoAlRecibirUnMensaje() {
		final ReceptorEncolador ejecutante = ReceptorEncolador.create();
		final ReceptorEncolador receptor = ReceptorEncolador.create();
		final Secuenciador secuenciador = builder.secuenciar(ejecutante, receptor);
		checkMensajeEnviadoYRecibido(mensaje1, mensaje1, secuenciador, ejecutante, receptor);
	}

	@Test
	public void elMultiplexorDeberiaEntregarAVariosDestinosAlRecibirUnMensaje() {
		final ReceptorEncolador receptor1 = ReceptorEncolador.create();
		final ReceptorEncolador receptor2 = ReceptorEncolador.create();
		final Multiplexor multiplexor = builder.multiplexar(receptor1, receptor2);
		checkMensajeEnviadoYRecibido(mensaje1, mensaje1, multiplexor, receptor1, receptor2);
	}

	@Test
	public void elCondicionalDeberiaEntregarElMensajeSiCumpleLaCondicion() {
		final ReceptorEncolador receptor = ReceptorEncolador.create();
		final Filtro filtro = builder.filtrarEntradaCon(SiempreTrue.getInstancia(), receptor);
		checkMensajeEnviadoYRecibido(mensaje1, mensaje1, filtro, receptor);
	}

	@Test
	public void elCondicionalNoDeberiaEntregarElMensajeSiNoCumpleLaCondicion() {
		final ReceptorEncolador receptor = ReceptorEncolador.create();
		final Filtro filtro = builder.filtrarEntradaCon(SiempreFalse.getInstancia(), receptor);
		checkMensajeEnviadoYNoRecibido(mensaje1, mensaje1, filtro, receptor);
	}

	@Test
	public void elTransformadorDeberiaModificarElMensajeEntregado() {
		final ReceptorEncolador receptor = ReceptorEncolador.create();
		final Transformacion transformacion = new Transformacion() {

			public MensajeVortex transformar(@SuppressWarnings("unused") final MensajeVortex mensaje) {
				return mensaje2;
			}
		};
		final Transformador transformador = builder.transformarCon(transformacion, receptor);
		checkMensajeEnviadoYRecibido(mensaje1, mensaje2, transformador, receptor);
	}

	@Test
	public void elBifurcadorDeberiaElegirElDelegadoPorTrueSiLaCondicionEsCumplida() {
		final ReceptorEncolador receptorPorTrue = ReceptorEncolador.create();
		final ReceptorEncolador receptorPorFalse = ReceptorEncolador.create();
		final Bifurcador bifurcador = builder.bifurcarSi(SiempreTrue.getInstancia(), receptorPorTrue, receptorPorFalse);
		checkMensajeEnviadoYRecibido(mensaje1, mensaje1, bifurcador, receptorPorTrue);
		verificarMensajeNoRecibido(0, receptorPorFalse);
	}

	@Test
	public void elBifurcadorDeberiaElegirElDelegadoPorFalseSiLaCondicionNoEsCumplida() {
		final ReceptorEncolador receptorPorTrue = ReceptorEncolador.create();
		final ReceptorEncolador receptorPorFalse = ReceptorEncolador.create();
		final Bifurcador bifurcador = builder
				.bifurcarSi(SiempreFalse.getInstancia(), receptorPorTrue, receptorPorFalse);
		checkMensajeEnviadoYRecibido(mensaje1, mensaje1, bifurcador, receptorPorFalse);
		verificarMensajeNoRecibido(0, receptorPorTrue);
	}

	@Test
	public void elFiltroDeMensajesConocidosDeberiaDescartarElMensajeLaSegundaVezSITieneId() {
		final IdDeComponenteVortex idDeNodo = GeneradorDeIdsGlobalesParaComponentes.getInstancia().generarId();
		final GeneradorSecuencialDeIdDeMensaje generadorDeIdsMensajes = GeneradorSecuencialDeIdDeMensaje
				.create(idDeNodo);
		final IdDeMensaje idDelMensaje = generadorDeIdsMensajes.generarId();
		final MensajeVortex mensaje = MensajeConContenido.crearVacio();
		mensaje.asignarId(idDelMensaje);

		final ReceptorEncolador receptor = ReceptorEncolador.create();
		final Filtro filtro = builder.filtrarMensajesDuplicadosA(receptor);

		// La primera vez debería llegar
		checkMensajeEnviadoYRecibido(mensaje, mensaje, filtro, receptor);

		// La segundas vez NO debería llegar
		checkMensajeEnviadoYNoRecibido(mensaje, mensaje, filtro, receptor);
	}

	@Test
	public void elFiltroDeMensajesConocidosDeberiaGenerarUnErrorSiElMensajeNoTieneId() {
		final ReceptorEncolador receptor = ReceptorEncolador.create();
		final Filtro filtro = builder.filtrarMensajesDuplicadosA(receptor);
		// La primera vez debería llegar
		checkMensajeEnviadoYNoRecibido(mensaje1, mensaje1, filtro, receptor);
	}

	/**
	 * Verifica que el selector deriva bien los mensajes
	 */
	@Test
	public void elSelectorDeberiaEntregarElMensajeAlQueCumpleLaCondicion() {
		final ReceptorEncolador receptorSiempreTrue = ReceptorEncolador.create();
		final ReceptorEncolador receptorSiempreFalse = ReceptorEncolador.create();

		final Selector selector = builder.selector();
		selector.crearConectorCon(SiempreTrue.getInstancia()).conectarCon(receptorSiempreTrue);
		selector.crearConectorCon(SiempreFalse.getInstancia()).conectarCon(receptorSiempreFalse);

		checkMensajeEnviadoYRecibido(mensaje1, mensaje1, selector, receptorSiempreTrue);
		verificarMensajeNoRecibido(1, receptorSiempreFalse);
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
		}
		catch (final TimeoutExceededException e) {
			// Es la excepción esperada
		}
	}

}
