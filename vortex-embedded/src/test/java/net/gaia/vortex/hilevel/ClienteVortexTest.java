/**
 * 27/01/2012 20:45:40 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.hilevel;

import java.util.concurrent.TimeUnit;

import junit.framework.Assert;
import net.gaia.taskprocessor.api.TaskProcessor;
import net.gaia.taskprocessor.api.TaskProcessorConfiguration;
import net.gaia.taskprocessor.api.TimeMagnitude;
import net.gaia.taskprocessor.impl.ExecutorBasedTaskProcesor;
import net.gaia.vortex.hilevel.api.MensajeVortexApi;
import net.gaia.vortex.hilevel.api.impl.ClienteVortexImpl;
import net.gaia.vortex.lowlevel.api.NodoVortex;
import net.gaia.vortex.lowlevel.impl.mensajes.EncoladorDeMensajesHandler;
import net.gaia.vortex.lowlevel.impl.nodo.NodoVortexConTasks;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Sets;

/**
 * Esta clase contiene tests para probar el cliente vortex
 * 
 * @author D. García
 */
public class ClienteVortexTest {

	private NodoVortex nodo;

	@Before
	public void prepararTest() {
		final TaskProcessor processor = ExecutorBasedTaskProcesor.create(TaskProcessorConfiguration.create());
		nodo = NodoVortexConTasks.create(processor, "NodoTest");
	}

	@After
	public void limpiarTest() {

	}

	@Test
	public void deberiaPermitirMandarUnMensajeAOtroClienteInteresadoYNoAUnoQueNo() {
		// Creamos el receptor con el tag del mensaje
		final EncoladorDeMensajesHandler encoladorDelReceptor = EncoladorDeMensajesHandler.create();
		final ClienteVortexImpl clienteReceptor = ClienteVortexImpl.create(nodo, encoladorDelReceptor);
		clienteReceptor.getFiltroDeMensajes().agregarATagsActivos(Sets.newHashSet("java.lang.Object"));

		// Creamos el emisor que no necesita un tag
		final EncoladorDeMensajesHandler encoladorDelEmisor = EncoladorDeMensajesHandler.create();
		final ClienteVortexImpl clienteEmisor = ClienteVortexImpl.create(nodo, encoladorDelEmisor);
		final Object contenidoEnviado = new Object();
		final MensajeVortexApi mensaje = MensajeVortexApi.create(contenidoEnviado);
		clienteEmisor.enviar(mensaje);

		final MensajeVortexApi mensajeRecibido = encoladorDelReceptor.esperarProximoMensaje(TimeMagnitude.of(1,
				TimeUnit.SECONDS));
		final Object contenidoRecibido = mensajeRecibido.getContenido();
		Assert.assertSame(contenidoEnviado, contenidoRecibido);
	}

	@Test
	public void deberiaPermitirEnviarUnMensajeYSaberQueNoLeLlegoANadie() {

	}

	@Test
	public void deberiaPermitirSaberQueTagsTieneElNodoAlConectarse() {

	}

	@Test
	public void deberiaPermitirCambiarLosTagsDinamicamente() {

	}
}
