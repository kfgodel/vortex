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
import net.gaia.taskprocessor.api.TaskProcessor;
import net.gaia.vortex.core.api.mensaje.MensajeVortex;
import net.gaia.vortex.core.api.moleculas.ids.IdentificadorVortex;
import net.gaia.vortex.core.external.VortexProcessorFactory;
import net.gaia.vortex.core.impl.atomos.ids.MultiplexorIdentificador;
import net.gaia.vortex.core.impl.atomos.ids.NexoIdentificador;
import net.gaia.vortex.core.impl.mensaje.MensajeConContenido;
import net.gaia.vortex.core.impl.moleculas.ids.GeneradorDeIdsEstaticos;

import org.junit.After;
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

	private NexoIdentificador nexo;
	private MensajeVortex mensajeEnviado;
	private ReceptorEncolador receptorFinal1;
	private IdentificadorVortex identificador;
	private TaskProcessor processor;
	private MultiplexorIdentificador multiplexor;
	private ReceptorEncolador receptorFinal2;

	@Before
	public void crearDependencias() {
		processor = VortexProcessorFactory.createMostlyMemoryProcessor();
		identificador = GeneradorDeIdsEstaticos.getInstancia().generarId();
		receptorFinal1 = ReceptorEncolador.create();
		receptorFinal2 = ReceptorEncolador.create();
		nexo = NexoIdentificador.create(processor, identificador, receptorFinal1);
		multiplexor = MultiplexorIdentificador.create(processor, identificador);
		multiplexor.conectarCon(receptorFinal1);
		multiplexor.conectarCon(receptorFinal2);
		mensajeEnviado = MensajeConContenido.create();
	}

	@After
	public void liberarRecursos() {
		processor.detener();
	}

	@Test
	public void elMensajeDeberiaLlegarSiNuncaPasoPorElNexo() {
		Assert.assertFalse("El mensaje no debería tener registro del pasaje por el ID",
				mensajeEnviado.pasoPreviamentePor(identificador));
		nexo.recibir(mensajeEnviado);
		final MensajeVortex mensajeRecibido = receptorFinal1.esperarPorMensaje(TimeMagnitude.of(1, TimeUnit.SECONDS));
		Assert.assertEquals("El mensaje recibido debería ser el enviado", mensajeEnviado, mensajeRecibido);
		Assert.assertTrue("El mensaje debería registrar el pasajo por el identificador",
				mensajeRecibido.pasoPreviamentePor(identificador));
	}

	@Test
	public void elMensajeNoDeberiaLlegarSiYaPasoPorElNexo() {
		mensajeEnviado.registrarPasajePor(identificador);
		Assert.assertTrue("El mensaje debería registrar el pasajo por el identificador",
				mensajeEnviado.pasoPreviamentePor(identificador));

		nexo.recibir(mensajeEnviado);
		try {
			receptorFinal1.esperarPorMensaje(TimeMagnitude.of(1, TimeUnit.SECONDS));
			Assert.fail("El mensaje no debería haber llegado a destino");
		} catch (final TimeoutExceededException e) {
			// Es la excepción que esperabamos
		}
	}

	@Test
	public void elMensajeDeberiaLlegarAAmbosDestinosSiNuncaPasoPorElMultiplexor() {
		Assert.assertFalse("El mensaje no debería tener registro del pasaje por el ID",
				mensajeEnviado.pasoPreviamentePor(identificador));
		multiplexor.recibir(mensajeEnviado);

		final MensajeVortex mensajeRecibido1 = receptorFinal1.esperarPorMensaje(TimeMagnitude.of(1, TimeUnit.SECONDS));
		Assert.assertEquals("El mensaje recibido debería ser el enviado", mensajeEnviado, mensajeRecibido1);
		Assert.assertTrue("El mensaje debería registrar el pasajo por el identificador",
				mensajeRecibido1.pasoPreviamentePor(identificador));

		final MensajeVortex mensajeRecibido2 = receptorFinal2.esperarPorMensaje(TimeMagnitude.of(1, TimeUnit.SECONDS));
		Assert.assertEquals("El mensaje recibido debería ser el enviado", mensajeEnviado, mensajeRecibido2);
		Assert.assertTrue("El mensaje debería registrar el pasajo por el identificador",
				mensajeRecibido2.pasoPreviamentePor(identificador));
	}

	@Test
	public void elMensajeNoDeberiaLlegarANingunoSiYaPasoPorElMultiplexor() {
		mensajeEnviado.registrarPasajePor(identificador);
		Assert.assertTrue("El mensaje debería registrar el pasajo por el identificador",
				mensajeEnviado.pasoPreviamentePor(identificador));

		multiplexor.recibir(mensajeEnviado);
		try {
			receptorFinal1.esperarPorMensaje(TimeMagnitude.of(1, TimeUnit.SECONDS));
			Assert.fail("El mensaje no debería haber llegado a destino");
		} catch (final TimeoutExceededException e) {
			// Es la excepción que esperabamos
		}
		try {
			receptorFinal2.esperarPorMensaje(TimeMagnitude.of(1, TimeUnit.SECONDS));
			Assert.fail("El mensaje no debería haber llegado a destino");
		} catch (final TimeoutExceededException e) {
			// Es la excepción que esperabamos
		}
	}
}
