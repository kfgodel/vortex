/**
 * 14/07/2012 23:06:03 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.comm.tests;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import junit.framework.Assert;
import net.gaia.taskprocessor.api.TaskProcessor;
import net.gaia.taskprocessor.executor.ExecutorBasedTaskProcesor;
import net.gaia.vortex.comm.api.CanalDeChat;
import net.gaia.vortex.comm.api.ClienteDeChatVortex;
import net.gaia.vortex.comm.api.ListenerDeEstadoDeCanal;
import net.gaia.vortex.comm.api.ListenerDeMensajesDeChat;
import net.gaia.vortex.comm.api.messages.MensajeDeChat;
import net.gaia.vortex.comm.impl.ClienteDeChatVortexImpl;
import net.gaia.vortex.core.api.Nodo;
import net.gaia.vortex.core.impl.moleculas.NodoMultiplexor;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ar.com.dgarcia.lang.conc.WaitBarrier;
import ar.com.dgarcia.lang.time.TimeMagnitude;

/**
 * Esta clase prueba el uso del adminstrador de canales
 * 
 * @author D. García
 */
public class TestClienteDeChatVortex {

	private static final String CANAL2 = "canal2";
	private static final String CANAL1 = "canal1";

	private TaskProcessor procesador;
	private Nodo nodoCentral;
	private ClienteDeChatVortex clienteTesteado;

	@Before
	public void crearNodo() {
		procesador = ExecutorBasedTaskProcesor.createOptimun();
		nodoCentral = NodoMultiplexor.create(procesador);
		clienteTesteado = ClienteDeChatVortexImpl.create(procesador, "Testeado");
		clienteTesteado.conectarA(nodoCentral);
	}

	@After
	public void liberarRecursos() {
		procesador.detener();
	}

	@Test
	public void alCrearloNoDeberiaTenerCanales() {
		List<CanalDeChat> canales = clienteTesteado.getCanales();
		Assert.assertEquals("No debería tener canales", 0, canales.size());
	}

	@Test
	public void deberiaPermitirAgregarCanales() {
		Set<String> nombresIniciales = new HashSet<String>(Arrays.asList(new String[] { CANAL1, CANAL2 }));
		for (String nombreInicial : nombresIniciales) {
			CanalDeChat canalCreado = clienteTesteado.agregarCanal(nombreInicial);
			Assert.assertNotNull("Debería existir un canal creado", canalCreado);
		}
		verificarCanalesEsperados(nombresIniciales);
	}

	private void verificarCanalesEsperados(Set<String> nombresIniciales) {
		List<CanalDeChat> canales = clienteTesteado.getCanales();
		int cantidadEsperada = nombresIniciales.size();
		Assert.assertEquals("Deberían existir " + cantidadEsperada + " canales", cantidadEsperada, canales.size());
		Set<String> nombresCreados = new HashSet<String>();
		for (CanalDeChat canalDeChat : canales) {
			String nombreDelCanalCreado = canalDeChat.getNombre();
			nombresCreados.add(nombreDelCanalCreado);
		}

		Assert.assertEquals("Los canales creados deberían tener los mismos nombres que los iniciales",
				nombresIniciales, nombresCreados);
	}

	@Test
	public void deberiaPermitirQuitarCanales() {
		clienteTesteado.agregarCanal(CANAL1);
		clienteTesteado.agregarCanal(CANAL2);

		clienteTesteado.quitarCanal(CANAL1);
		verificarCanalesEsperados(new HashSet<String>(Arrays.asList(new String[] { CANAL2 })));
	}

	@Test
	public void deberiaPermitirObtenerUnCanalPorNombre() {
		clienteTesteado.agregarCanal(CANAL1);

		CanalDeChat canal = clienteTesteado.getCanal(CANAL1);
		Assert.assertNotNull("Debería existir el canal pedido", canal);
	}

	@Test
	public void deberiaPermitirMandarUnMensajeEnUnCanal() {
		CanalDeChat canalCreado = clienteTesteado.agregarCanal(CANAL1);
		canalCreado.enviar("Texto");
	}

	@Test
	public void deberiaPermitirConocerLosMensajesDeUnCanal() {
		CanalDeChat canalCreado = clienteTesteado.agregarCanal(CANAL1);
		List<MensajeDeChat> mensajesIniciales = canalCreado.getMensajes();
		Assert.assertEquals("No debería tener canales iniciales", 0, mensajesIniciales.size());

		final WaitBarrier esperarRecepcion = WaitBarrier.create();
		canalCreado.setListenerDeMensajes(new ListenerDeMensajesDeChat() {
			public void onMensajeNuevo(MensajeDeChat mensaje) {
				esperarRecepcion.release();
			}
		});

		String textoDelMensaje = "Texto";
		canalCreado.enviar(textoDelMensaje);

		// Esperamos a que el canal indique que recibio el propio mensaje
		esperarRecepcion.waitForReleaseUpTo(TimeMagnitude.of(1, TimeUnit.SECONDS));

		List<MensajeDeChat> mensajesRecibidos = canalCreado.getMensajes();
		Assert.assertEquals("Debería tener un solo mensaje recibido", 1, mensajesRecibidos.size());
		MensajeDeChat mensajeRecibido = mensajesRecibidos.get(0);
		Assert.assertEquals("Debería ser el mismo texto que mandamos", textoDelMensaje, mensajeRecibido.getTexto());
	}

	@Test
	public void deberiPermitirActualizarElEstadoDePresentismo() {
		clienteTesteado.actualizarPresentismo();
	}

	@Test
	public void deberiaPermitirSaberSiHayOtrosClientesEnUnCanal() {
		CanalDeChat canalTesteado = clienteTesteado.agregarCanal(CANAL1);
		List<String> presentesIniciales = canalTesteado.getOtrosPresentes();
		Assert.assertEquals("No deberían haber otros presentes", 0, presentesIniciales.size());

		final WaitBarrier esperarConexionDelOtro = WaitBarrier.create();
		canalTesteado.setListenerDeEstado(new ListenerDeEstadoDeCanal() {
			public void onCanalVacio(CanalDeChat canal) {
			}

			public void onCanalHabitado(CanalDeChat canal) {
				esperarConexionDelOtro.release();
			}
		});

		ClienteDeChatVortex otroCliente = ClienteDeChatVortexImpl.create(procesador, "Otro");
		otroCliente.conectarA(nodoCentral);
		otroCliente.agregarCanal(CANAL1);

		esperarConexionDelOtro.waitForReleaseUpTo(TimeMagnitude.of(1, TimeUnit.SECONDS));
		List<String> presentesActuales = canalTesteado.getOtrosPresentes();
		Assert.assertEquals("Debería existir otro cliente presente", 1, presentesActuales.size());
	}

	@Test
	public void deberiaPermitiSaberQueCanalesEstanHabitadosEnUnMomentoArbitrario() throws InterruptedException {
		CanalDeChat canalTesteado = clienteTesteado.agregarCanal(CANAL1);
		clienteTesteado.agregarCanal(CANAL2);

		final WaitBarrier esperarOtroClienteConectado = WaitBarrier.create();

		clienteTesteado.setListenerDeEstado(new ListenerDeEstadoDeCanal() {
			public void onCanalVacio(CanalDeChat canal) {
			}

			public void onCanalHabitado(CanalDeChat canal) {
				esperarOtroClienteConectado.release();
			}
		});

		final WaitBarrier esperarNotificacionAlCanal = WaitBarrier.create();
		canalTesteado.setListenerDeEstado(new ListenerDeEstadoDeCanal() {
			public void onCanalVacio(CanalDeChat canal) {
			}

			public void onCanalHabitado(CanalDeChat canal) {
				esperarNotificacionAlCanal.release();
			}
		});

		verificarCantidadDeOtrosEn(CANAL1, 0);
		verificarCantidadDeOtrosEn(CANAL2, 0);

		ClienteDeChatVortex otroCliente = ClienteDeChatVortexImpl.create(procesador, "Otro");
		otroCliente.conectarA(nodoCentral);
		otroCliente.agregarCanal(CANAL1);

		// Esperamos que se notifique al cliente
		esperarOtroClienteConectado.waitForReleaseUpTo(TimeMagnitude.of(1, TimeUnit.SECONDS));
		// Esperamos que se notifique al canal
		esperarNotificacionAlCanal.waitForReleaseUpTo(TimeMagnitude.of(1, TimeUnit.SECONDS));

		verificarCantidadDeOtrosEn(CANAL1, 1);
		verificarCantidadDeOtrosEn(CANAL2, 0);

		// Al desconectar el otro, la notificación seguro no llega es necesario actualizar el
		// presentismo
		otroCliente.desconectar();
		clienteTesteado.actualizarPresentismo();

		Thread.sleep(2000);

		verificarCantidadDeOtrosEn(CANAL1, 0);
		verificarCantidadDeOtrosEn(CANAL2, 0);
	}

	/**
	 * @param nombreDelCanal
	 * @param expectedOtros
	 */
	private void verificarCantidadDeOtrosEn(String nombreDelCanal, int expectedOtros) {
		CanalDeChat canal1 = clienteTesteado.getCanal(nombreDelCanal);
		Assert.assertEquals("Debería indicar que está vacio", expectedOtros, canal1.getOtrosPresentes().size());
	}
}
