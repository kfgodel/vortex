/**
 * 27/06/2012 17:37:25 Copyright (C) 2011 Darío L. García
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
import net.gaia.vortex.api.atomos.Filtro;
import net.gaia.vortex.api.basic.emisores.MultiConectable;
import net.gaia.vortex.api.builder.VortexCore;
import net.gaia.vortex.api.ids.componentes.IdDeComponenteVortex;
import net.gaia.vortex.api.ids.mensajes.IdDeMensaje;
import net.gaia.vortex.api.mensajes.MensajeVortex;
import net.gaia.vortex.api.moleculas.Compuesto;
import net.gaia.vortex.impl.builder.VortexCoreBuilder;
import net.gaia.vortex.impl.ids.componentes.GeneradorDeIdsGlobalesParaComponentes;
import net.gaia.vortex.impl.ids.mensajes.GeneradorSecuencialDeIdDeMensaje;
import net.gaia.vortex.impl.mensajes.MensajeConContenido;

import org.junit.Before;
import org.junit.Test;

import ar.com.dgarcia.coding.exceptions.TimeoutExceededException;
import ar.com.dgarcia.lang.time.TimeMagnitude;

/**
 * Esta clase prueba que el nexo identificador funcione correctamente
 * 
 * @author D. García
 */
public class TestComponentesIdentificadores {

	private MensajeVortex mensajeEnviado;
	private ReceptorEncolador receptorFinal1;
	private ReceptorEncolador receptorFinal2;
	private VortexCore builder;
	private Filtro filtro;
	private Compuesto<MultiConectable> multiplexorSinDuplicados;

	@Before
	public void crearDependencias() {
		// Para estos test no requerimos procesador
		builder = VortexCoreBuilder.create(null);
		receptorFinal1 = ReceptorEncolador.create();
		receptorFinal2 = ReceptorEncolador.create();

		filtro = builder.filtrarMensajesDuplicadosA(receptorFinal1);
		multiplexorSinDuplicados = builder.multiplexarSinDuplicados(receptorFinal1, receptorFinal2);

		mensajeEnviado = MensajeConContenido.crearVacio();
		final IdDeComponenteVortex idDeNodo = GeneradorDeIdsGlobalesParaComponentes.getInstancia().generarId();
		final GeneradorSecuencialDeIdDeMensaje generador = GeneradorSecuencialDeIdDeMensaje.create(idDeNodo);
		final IdDeMensaje idDelMensaje = generador.generarId();
		mensajeEnviado.asignarId(idDelMensaje);
	}

	@Test
	public void elMensajeDeberiaLlegarSiNuncaPasoPorElFiltro() {
		Assert.assertFalse("No debería tener registro del mensaje", receptorFinal1.tieneMensajes());
		filtro.recibir(mensajeEnviado);
		final MensajeVortex mensajeRecibido = receptorFinal1.esperarPorMensaje(TimeMagnitude.of(1, TimeUnit.SECONDS));
		Assert.assertEquals("El mensaje recibido debería ser el enviado", mensajeEnviado, mensajeRecibido);
	}

	@Test
	public void elMensajeNoDeberiaLlegarSiYaPasoPorElNexo() {
		filtro.recibir(mensajeEnviado);
		final MensajeVortex mensajeRecibido = receptorFinal1.esperarPorMensaje(TimeMagnitude.of(1, TimeUnit.SECONDS));
		Assert.assertEquals("El mensaje recibido debería ser el enviado", mensajeEnviado, mensajeRecibido);

		// Lo mandamos una segunda vez
		filtro.recibir(mensajeEnviado);
		try {
			receptorFinal1.esperarPorMensaje(TimeMagnitude.of(1, TimeUnit.SECONDS));
			Assert.fail("El mensaje no debería haber llegado a destino");
		}
		catch (final TimeoutExceededException e) {
			// Es la excepción que esperabamos
		}
	}

	@Test
	public void elMensajeDeberiaLlegarAAmbosDestinosSiNuncaPasoPorElMultiplexor() {
		Assert.assertFalse("No debería tener registro del mensaje", receptorFinal1.tieneMensajes());
		Assert.assertFalse("No debería tener registro del mensaje", receptorFinal2.tieneMensajes());

		multiplexorSinDuplicados.recibir(mensajeEnviado);

		final MensajeVortex mensajeRecibido1 = receptorFinal1.esperarPorMensaje(TimeMagnitude.of(1, TimeUnit.SECONDS));
		Assert.assertEquals("El mensaje recibido debería ser el enviado", mensajeEnviado, mensajeRecibido1);

		final MensajeVortex mensajeRecibido2 = receptorFinal2.esperarPorMensaje(TimeMagnitude.of(1, TimeUnit.SECONDS));
		Assert.assertEquals("El mensaje recibido debería ser el enviado", mensajeEnviado, mensajeRecibido2);
	}

	@Test
	public void elMensajeNoDeberiaLlegarANingunoSiYaPasoPorElMultiplexor() {
		multiplexorSinDuplicados.recibir(mensajeEnviado);

		final MensajeVortex mensajeRecibido1 = receptorFinal1.esperarPorMensaje(TimeMagnitude.of(1, TimeUnit.SECONDS));
		Assert.assertEquals("El mensaje recibido debería ser el enviado", mensajeEnviado, mensajeRecibido1);
		final MensajeVortex mensajeRecibido2 = receptorFinal2.esperarPorMensaje(TimeMagnitude.of(1, TimeUnit.SECONDS));
		Assert.assertEquals("El mensaje recibido debería ser el enviado", mensajeEnviado, mensajeRecibido2);

		multiplexorSinDuplicados.recibir(mensajeEnviado);
		try {
			receptorFinal1.esperarPorMensaje(TimeMagnitude.of(1, TimeUnit.SECONDS));
			Assert.fail("El mensaje no debería haber llegado a destino");
		}
		catch (final TimeoutExceededException e) {
			// Es la excepción que esperabamos
		}
		try {
			receptorFinal2.esperarPorMensaje(TimeMagnitude.of(1, TimeUnit.SECONDS));
			Assert.fail("El mensaje no debería haber llegado a destino");
		}
		catch (final TimeoutExceededException e) {
			// Es la excepción que esperabamos
		}
	}
}
