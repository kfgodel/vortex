/**
 * 13/06/2012 12:15:18 Copyright (C) 2011 10Pines S.R.L.
 */
package net.gaia.vortex.core.tests;

import java.util.concurrent.TimeUnit;

import net.gaia.taskprocessor.api.processor.TaskProcessor;
import net.gaia.vortex.api.basic.Receptor;
import net.gaia.vortex.core.api.ids.componentes.IdDeComponenteVortex;
import net.gaia.vortex.core.api.ids.mensajes.IdDeMensaje;
import net.gaia.vortex.core.api.mensaje.MensajeVortex;
import net.gaia.vortex.core.api.moleculas.condicional.Selector;
import net.gaia.vortex.core.api.transformaciones.Transformacion;
import net.gaia.vortex.core.external.VortexProcessorFactory;
import net.gaia.vortex.core.impl.atomos.memoria.NexoSinDuplicados;
import net.gaia.vortex.core.impl.condiciones.SiempreFalse;
import net.gaia.vortex.core.impl.condiciones.SiempreTrue;
import net.gaia.vortex.core.impl.ids.componentes.GeneradorDeIdsGlobalesParaComponentes;
import net.gaia.vortex.core.impl.ids.mensajes.GeneradorSecuencialDeIdDeMensaje;
import net.gaia.vortex.core.impl.mensaje.MensajeConContenido;
import net.gaia.vortex.core.impl.moleculas.condicional.SelectorConFiltros;
import net.gaia.vortex.impl.atomos.AtomoBifurcador;
import net.gaia.vortex.impl.atomos.AtomoMultiplexor;
import net.gaia.vortex.impl.atomos.AtomoSecuenciador;
import net.gaia.vortex.impl.atomos.AtomoTransformador;

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

	@Before
	public void crearProcesadorYNodos() {
		mensaje1 = MensajeConContenido.crearVacio();
		mensaje2 = MensajeConContenido.crearVacio();
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
		final AtomoSecuenciador secuenciador = AtomoSecuenciador.create(ejecutante);
		secuenciador.getConectorUnico().conectarCon(receptor);
		checkMensajeEnviadoYRecibido(mensaje1, mensaje1, secuenciador, ejecutante, receptor);
	}

	@Test
	public void elMultiplexorDeberiaEntregarAVariosDestinosAlRecibirUnMensaje() {
		final ReceptorEncolador receptor1 = ReceptorEncolador.create();
		final ReceptorEncolador receptor2 = ReceptorEncolador.create();
		final AtomoMultiplexor multiplexor = AtomoMultiplexor.create();
		multiplexor.crearConector().conectarCon(receptor1);
		multiplexor.crearConector().conectarCon(receptor2);

		checkMensajeEnviadoYRecibido(mensaje1, mensaje1, multiplexor, receptor1, receptor2);
	}

	@Test
	public void elCondicionalDeberiaEntregarElMensajeSiCumpleLaCondicion() {
		final ReceptorEncolador receptor = ReceptorEncolador.create();
		final AtomoBifurcador filtro = AtomoBifurcador.create(SiempreTrue.getInstancia());
		filtro.getConectorPorTrue().conectarCon(receptor);
		checkMensajeEnviadoYRecibido(mensaje1, mensaje1, filtro, receptor);
	}

	@Test
	public void elCondicionalNoDeberiaEntregarElMensajeSiNoCumpleLaCondicion() {
		final ReceptorEncolador receptor = ReceptorEncolador.create();
		final AtomoBifurcador filtro = AtomoBifurcador.create(SiempreFalse.getInstancia());
		filtro.getConectorPorTrue().conectarCon(receptor);
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
		final AtomoTransformador transformador = AtomoTransformador.create(transformacion);
		transformador.getConectorUnico().conectarCon(receptor);
		checkMensajeEnviadoYRecibido(mensaje1, mensaje2, transformador, receptor);
	}

	@Test
	public void elBifurcadorDeberiaElegirElDelegadoPorTrueSiLaCondicionEsCumplida() {
		final ReceptorEncolador receptorPorTrue = ReceptorEncolador.create();
		final ReceptorEncolador receptorPorFalse = ReceptorEncolador.create();
		final AtomoBifurcador bifurcador = AtomoBifurcador.create(SiempreTrue.getInstancia());
		bifurcador.getConectorPorTrue().conectarCon(receptorPorTrue);
		bifurcador.getConectorPorFalse().conectarCon(receptorPorFalse);
		checkMensajeEnviadoYRecibido(mensaje1, mensaje1, bifurcador, receptorPorTrue);
		verificarMensajeNoRecibido(0, receptorPorFalse);
	}

	@Test
	public void elBifurcadorDeberiaElegirElDelegadoPorFalseSiLaCondicionNoEsCumplida() {
		final ReceptorEncolador receptorPorTrue = ReceptorEncolador.create();
		final ReceptorEncolador receptorPorFalse = ReceptorEncolador.create();
		final AtomoBifurcador bifurcador = AtomoBifurcador.create(SiempreFalse.getInstancia());
		bifurcador.getConectorPorTrue().conectarCon(receptorPorTrue);
		bifurcador.getConectorPorFalse().conectarCon(receptorPorFalse);
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
		final NexoSinDuplicados filtro = NexoSinDuplicados.create(processor, receptor);
		// La primera vez debería llegar
		checkMensajeEnviadoYRecibido(mensaje, mensaje, filtro, receptor);

		// La segundas vez NO debería llegar
		checkMensajeEnviadoYNoRecibido(mensaje, mensaje, filtro, receptor);
	}

	@Test
	public void elFiltroDeMensajesConocidosDeberiaGenerarUnErrorSiElMensajeNoTieneId() {
		final ReceptorEncolador receptor = ReceptorEncolador.create();
		final NexoSinDuplicados filtro = NexoSinDuplicados.create(processor, receptor);
		// La primera vez debería llegar
		checkMensajeEnviadoYNoRecibido(mensaje1, mensaje1, filtro, receptor);
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

	/**
	 * Verifica que el selector deriva bien los mensajes
	 */
	@Test
	public void elSelectorDeberiaEntregarElMensajeAlQueCumpleLaCondicion() {
		final ReceptorEncolador receptorSiempreTrue = ReceptorEncolador.create();
		final ReceptorEncolador receptorSiempreFalse = ReceptorEncolador.create();

		final Selector selector = SelectorConFiltros.create(processor);
		selector.conectarCon(receptorSiempreTrue, SiempreTrue.getInstancia());
		selector.conectarCon(receptorSiempreFalse, SiempreFalse.getInstancia());

		checkMensajeEnviadoYRecibido(mensaje1, mensaje1, selector, receptorSiempreTrue);
		verificarMensajeNoRecibido(1, receptorSiempreFalse);
	}
}
