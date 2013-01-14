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
import net.gaia.vortex.core.api.mensaje.ids.IdDeMensaje;
import net.gaia.vortex.core.api.moleculas.ids.IdDeComponenteVortex;
import net.gaia.vortex.core.external.VortexProcessorFactory;
import net.gaia.vortex.core.impl.atomos.memoria.MultiplexorSinDuplicados;
import net.gaia.vortex.core.impl.atomos.memoria.NexoSinDuplicados;
import net.gaia.vortex.core.impl.ids.IdsSecuencialesParaMensajes;
import net.gaia.vortex.core.impl.mensaje.MensajeConContenido;
import net.gaia.vortex.core.impl.moleculas.ids.IdsEstatiscosParaComponentes;

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

	private NexoSinDuplicados nexo;
	private MensajeVortex mensajeEnviado;
	private ReceptorEncolador receptorFinal1;
	private TaskProcessor processor;
	private MultiplexorSinDuplicados multiplexor;
	private ReceptorEncolador receptorFinal2;

	@Before
	public void crearDependencias() {
		processor = VortexProcessorFactory.createProcessor();
		receptorFinal1 = ReceptorEncolador.create();
		receptorFinal2 = ReceptorEncolador.create();
		nexo = NexoSinDuplicados.create(processor, receptorFinal1);
		multiplexor = MultiplexorSinDuplicados.create(processor);
		multiplexor.conectarCon(receptorFinal1);
		multiplexor.conectarCon(receptorFinal2);

		mensajeEnviado = MensajeConContenido.crearVacio();
		final IdDeComponenteVortex idDeNodo = IdsEstatiscosParaComponentes.getInstancia().generarId();
		final IdsSecuencialesParaMensajes generador = IdsSecuencialesParaMensajes.create(idDeNodo);
		final IdDeMensaje idDelMensaje = generador.generarId();
		mensajeEnviado.asignarId(idDelMensaje);
	}

	@After
	public void liberarRecursos() {
		processor.detener();
	}

	@Test
	public void elMensajeDeberiaLlegarSiNuncaPasoPorElNexo() {
		Assert.assertFalse("El mensaje no debería tener registro del pasaje por el ID", nexo.yaRecibio(mensajeEnviado));
		nexo.recibir(mensajeEnviado);
		final MensajeVortex mensajeRecibido = receptorFinal1.esperarPorMensaje(TimeMagnitude.of(1, TimeUnit.SECONDS));
		Assert.assertEquals("El mensaje recibido debería ser el enviado", mensajeEnviado, mensajeRecibido);
		Assert.assertTrue("El mensaje debería registrar el pasajo por el identificador", nexo.yaRecibio(mensajeEnviado));
	}

	@Test
	public void elMensajeNoDeberiaLlegarSiYaPasoPorElNexo() {
		nexo.recibir(mensajeEnviado);
		receptorFinal1.esperarPorMensaje(TimeMagnitude.of(1, TimeUnit.SECONDS));
		Assert.assertTrue("El mensaje debería registrar el pasajo por el identificador", nexo.yaRecibio(mensajeEnviado));

		// Lo mandamos una segunda vez
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
				multiplexor.yaRecibio(mensajeEnviado));
		multiplexor.recibir(mensajeEnviado);

		final MensajeVortex mensajeRecibido1 = receptorFinal1.esperarPorMensaje(TimeMagnitude.of(1, TimeUnit.SECONDS));
		Assert.assertEquals("El mensaje recibido debería ser el enviado", mensajeEnviado, mensajeRecibido1);
		Assert.assertTrue("El mensaje debería registrar el pasajo por el identificador",
				multiplexor.yaRecibio(mensajeRecibido1));

		final MensajeVortex mensajeRecibido2 = receptorFinal2.esperarPorMensaje(TimeMagnitude.of(1, TimeUnit.SECONDS));
		Assert.assertEquals("El mensaje recibido debería ser el enviado", mensajeEnviado, mensajeRecibido2);
		Assert.assertTrue("El mensaje debería registrar el pasajo por el identificador",
				multiplexor.yaRecibio(mensajeRecibido2));
	}

	@Test
	public void elMensajeNoDeberiaLlegarANingunoSiYaPasoPorElMultiplexor() {
		multiplexor.recibir(mensajeEnviado);
		final MensajeVortex mensajeRecibido1 = receptorFinal1.esperarPorMensaje(TimeMagnitude.of(1, TimeUnit.SECONDS));
		Assert.assertTrue("El mensaje debería registrar el pasajo por el identificador",
				multiplexor.yaRecibio(mensajeRecibido1));
		final MensajeVortex mensajeRecibido2 = receptorFinal2.esperarPorMensaje(TimeMagnitude.of(1, TimeUnit.SECONDS));
		Assert.assertTrue("El mensaje debería registrar el pasajo por el identificador",
				multiplexor.yaRecibio(mensajeRecibido2));

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
