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

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import junit.framework.Assert;
import net.gaia.vortex.hilevel.api.ListenerDeTagsDelNodo;
import net.gaia.vortex.hilevel.api.MensajeVortexApi;
import net.gaia.vortex.hilevel.api.entregas.ReporteDeEntregaApi;
import net.gaia.vortex.hilevel.api.entregas.StatusDeEntrega;
import net.gaia.vortex.hilevel.api.impl.ClienteVortexImpl;
import net.gaia.vortex.hilevel.api.impl.ConfiguracionClienteVortex;
import net.gaia.vortex.lowlevel.api.NodoVortex;
import net.gaia.vortex.lowlevel.impl.mensajes.EncoladorDeMensajesHandler;
import net.gaia.vortex.lowlevel.impl.nodo.NodoVortexConTasks;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ar.com.dgarcia.coding.exceptions.TimeoutExceededException;
import ar.com.dgarcia.lang.time.TimeMagnitude;

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
		nodo = NodoVortexConTasks.create("NodoTest");
	}

	@After
	public void limpiarTest() {
		nodo.detenerYDevolverRecursos();
	}

	@Test
	public void deberiaPermitirMandarUnMensajeAOtroClienteInteresadoYNoAUnoQueNo() {
		// Creamos el receptor con el tag del mensaje
		final EncoladorDeMensajesHandler encoladorDelReceptor = EncoladorDeMensajesHandler.create();
		final ClienteVortexImpl clienteReceptor = ClienteVortexImpl.create(ConfiguracionClienteVortex.create(nodo,
				encoladorDelReceptor));
		clienteReceptor.getFiltroDeMensajes().agregarATagsActivos(Sets.newHashSet("java.lang.Object"));

		// Creamos el emisor que no necesita un tag
		final EncoladorDeMensajesHandler encoladorDelEmisor = EncoladorDeMensajesHandler.create();
		final ClienteVortexImpl clienteEmisor = ClienteVortexImpl.create(ConfiguracionClienteVortex.create(nodo,
				encoladorDelEmisor));
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
		// Creamos el emisor del mensaje
		final EncoladorDeMensajesHandler encoladorDelEmisor = EncoladorDeMensajesHandler.create();
		final ClienteVortexImpl clienteEmisor = ClienteVortexImpl.create(ConfiguracionClienteVortex.create(nodo,
				encoladorDelEmisor));

		// Enviamos el mensaje
		final MensajeVortexApi mensaje = MensajeVortexApi.create("Hola", "tipo1Tesst1", "TAG1");
		clienteEmisor.enviar(mensaje);

		// Verificamos que no debería llegarle a nadie
		final ReporteDeEntregaApi reporte = encoladorDelEmisor.esperarProximoReporte(TimeMagnitude.of(4,
				TimeUnit.SECONDS));
		Assert.assertFalse("Deberia indicar que el mensaje no fue exitoso", reporte.fueExitoso());
		Assert.assertEquals("Deberia indicar que no hubo interesados", reporte.getStatus(),
				StatusDeEntrega.SIN_INTERESADOS);
	}

	public static class ListenerDeTagsDeTest implements ListenerDeTagsDelNodo {
		public AtomicReference<Set<String>> tagsDelNodo = new AtomicReference<Set<String>>();

		@Override
		public void onCambiosDeTags(final Set<String> tagsDelNodo) {
			this.tagsDelNodo.set(tagsDelNodo);
		}
	}

	@Test
	public void deberiaPermitirSaberQueTagsTieneElNodoAlConectarse() {
		// Creamos el listener que vamos a usar para las notificaciones
		final ListenerDeTagsDeTest listenerDeTags = new ListenerDeTagsDeTest();
		// Creamos la sesión para recibir la notificación de los tags
		final EncoladorDeMensajesHandler encoladorDelAtento = EncoladorDeMensajesHandler.create();
		ClienteVortexImpl.create(ConfiguracionClienteVortex.create(nodo, encoladorDelAtento, listenerDeTags));

		// Creamos otra sesión con tags que deberían llegar a conocimiento de la primera
		final ClienteVortexImpl clientePublicante = ClienteVortexImpl.create(ConfiguracionClienteVortex.create(nodo,
				EncoladorDeMensajesHandler.create()));
		final HashSet<String> tagsPublicados = Sets.newHashSet("TAG1", "TAG2");
		clientePublicante.getFiltroDeMensajes().agregarATagsActivos(tagsPublicados);

		// Verificamos que no haya llegado ningun mensaje para el primero ,y damos tiempo que se
		// impacte la notificacion en el listener
		try {
			encoladorDelAtento.esperarProximoMensaje(TimeMagnitude.of(2, TimeUnit.SECONDS));
			Assert.fail("No deberíamos recibir ningun mensaje");
		} catch (final TimeoutExceededException e) {
			// Es la excepción correcta
		}

		Assert.assertEquals("Deberíamos haber recibido como notificacion los tags de la otra sesion", tagsPublicados,
				listenerDeTags.tagsDelNodo.get());

	}

	@Test
	public void deberiaPermitirCambiarLosTagsDinamicamente() {

	}
}
