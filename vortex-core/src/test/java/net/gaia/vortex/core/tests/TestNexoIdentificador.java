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
import net.gaia.taskprocessor.executor.ExecutorBasedTaskProcesor;
import net.gaia.vortex.core.api.mensaje.MensajeVortex;
import net.gaia.vortex.core.api.moleculas.ids.IdentificadorVortex;
import net.gaia.vortex.core.impl.mensaje.MensajeMapa;
import net.gaia.vortex.core.impl.moleculas.ids.GeneradorDeIdsEstaticos;
import net.gaia.vortex.core.impl.moleculas.ids.NexoIdentificador;

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
public class TestNexoIdentificador {

	private NexoIdentificador nexo;
	private MensajeVortex mensajeEnviado;
	private ReceptorEncolador receptorFinal;
	private IdentificadorVortex identificador;
	private TaskProcessor processor;

	@Before
	public void crearDependencias() {
		processor = ExecutorBasedTaskProcesor.create(4);
		identificador = GeneradorDeIdsEstaticos.getInstancia().generarId();
		receptorFinal = ReceptorEncolador.create();
		nexo = NexoIdentificador.create(processor, identificador, receptorFinal);
		mensajeEnviado = MensajeMapa.create();
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
		final MensajeVortex mensajeRecibido = receptorFinal.esperarPorMensaje(TimeMagnitude.of(1, TimeUnit.SECONDS));
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
			receptorFinal.esperarPorMensaje(TimeMagnitude.of(1, TimeUnit.SECONDS));
			Assert.fail("El mensaje no debería haber llegado a destino");
		} catch (final TimeoutExceededException e) {
			// Es la excepción que esperabamos
		}
	}

}
