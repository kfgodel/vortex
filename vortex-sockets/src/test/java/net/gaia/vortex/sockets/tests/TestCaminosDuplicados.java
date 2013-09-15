/**
 * 29/08/2012 23:36:24 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.sockets.tests;

import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

import junit.framework.Assert;
import net.gaia.taskprocessor.api.processor.TaskProcessor;
import net.gaia.vortex.core.tests.MensajeModeloParaTests;
import net.gaia.vortex.deprecated.NodoSocketViejo;
import net.gaia.vortex.deprecated.PortalMapeadorViejo;
import net.gaia.vortex.impl.condiciones.SiempreTrue;
import net.gaia.vortex.impl.helpers.VortexProcessorFactory;
import net.gaia.vortex.impl.support.HandlerTipado;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ar.com.dgarcia.lang.conc.WaitBarrier;
import ar.com.dgarcia.lang.metrics.impl.MetricasPorTiempoImpl;
import ar.com.dgarcia.lang.time.TimeMagnitude;

/**
 * Esta clase prueba que si existen caminos duplicados por sockets los mensajes no lleguen
 * duplicados
 * 
 * @author D. García
 */
public class TestCaminosDuplicados {
	private static final Logger LOG = LoggerFactory.getLogger(TestCaminosDuplicados.class);

	private TaskProcessor procesador;
	private NodoSocketViejo serverEmisor;
	private NodoSocketViejo serverReceptor;
	private NodoSocketViejo clienteIntermedio;
	private NodoSocketViejo clienteEmisor;
	private NodoSocketViejo clienteReceptor;

	private PortalMapeadorViejo portalReceptor;

	private PortalMapeadorViejo portalEmisor;

	private NodoSocketViejo serverParalelo;

	private NodoSocketViejo clienteReceptorParalelo;

	private NodoSocketViejo clienteIntermedioParalelo;

	@Before
	public void crearProcesador() {
		procesador = VortexProcessorFactory.createProcessor();
	}

	@After
	public void eliminarProcesador() {
		procesador.detener();
	}

	@After
	public void liberarPuertos() {
		if (clienteEmisor != null) {
			clienteEmisor.closeAndDispose();
		}
		if (clienteReceptor != null) {
			clienteReceptor.closeAndDispose();
		}
		if (clienteIntermedio != null) {
			clienteIntermedio.closeAndDispose();
		}
		if (clienteReceptorParalelo != null) {
			clienteReceptorParalelo.closeAndDispose();
		}
		if (clienteIntermedioParalelo != null) {
			clienteIntermedioParalelo.closeAndDispose();
		}
		if (serverEmisor != null) {
			serverEmisor.closeAndDispose();
		}
		if (serverReceptor != null) {
			serverReceptor.closeAndDispose();
		}
		if (serverParalelo != null) {
			serverParalelo.closeAndDispose();
		}
	}

	/**
	 * Prueba que poniendo nodos en el medio no se producen rebotes innecesarios
	 * 
	 * @throws InterruptedException
	 */
	@Test
	public void elMensajeDeberiaLlegarSoloUnaVezSiExistenDosNodosIntermediosEnLaRed() throws InterruptedException {
		crearNodosConIntermedios();

		final WaitBarrier esperarPrimerMensaje = WaitBarrier.create();

		// Creamos la métricas para medir los recibidos
		final MetricasPorTiempoImpl metricas = MetricasPorTiempoImpl.create();
		portalReceptor.recibirCon(new HandlerTipado<MensajeModeloParaTests>(SiempreTrue.getInstancia()) {

			public void onObjetoRecibido(final MensajeModeloParaTests mensaje) {
				esperarPrimerMensaje.release();
				metricas.registrarOutput();
			}
		});

		// Enviamos un mensaje para verificar que llega
		portalEmisor.enviar(MensajeModeloParaTests.create());

		// Esperamos que llegue algo el primer mensaje
		esperarPrimerMensaje.waitForReleaseUpTo(TimeMagnitude.of(1, TimeUnit.MINUTES));
		LOG.debug("Esperando otros mensajes a parte del primero");

		// Esperamos unos 20s más para registrar si llegan otros mensajes
		Thread.sleep(5000);

		Assert.assertEquals("Sólo debería haber llegado un mensaje", 1, metricas.getCantidadDeOutputs());
	}

	/**
	 * Prueba que no se produzca un rebote inicial innecesario
	 * 
	 * @throws InterruptedException
	 */
	@Test
	public void elMensajeNoDeberiaVolverAlEmisorSiHayDosNodosIntermedios() throws InterruptedException {
		crearNodosConIntermedios();

		// Definimos un lock para que nos avise cuando llega el mensaje
		final WaitBarrier esperarPrimerMensaje = WaitBarrier.create();
		portalReceptor.recibirCon(new HandlerTipado<MensajeModeloParaTests>(SiempreTrue.getInstancia()) {

			public void onObjetoRecibido(final MensajeModeloParaTests mensaje) {
				esperarPrimerMensaje.release();
			}
		});

		// Creamos la métricas para medir los recibidos por el emisor
		final MetricasPorTiempoImpl metricas = MetricasPorTiempoImpl.create();
		portalEmisor.recibirCon(new HandlerTipado<MensajeModeloParaTests>(SiempreTrue.getInstancia()) {

			public void onObjetoRecibido(final MensajeModeloParaTests mensaje) {
				metricas.registrarOutput();
			}
		});

		// Enviamos un mensaje para verificar los rebotes
		portalEmisor.enviar(MensajeModeloParaTests.create());

		// Esperamos que llegue algo el primer mensaje
		esperarPrimerMensaje.waitForReleaseUpTo(TimeMagnitude.of(1, TimeUnit.MINUTES));

		// Verificamos que el emisor no lo recibio
		Assert.assertEquals("El emisor no debería haberlo recibido", 0, metricas.getCantidadDeOutputs());
	}

	/**
	 * Crea los nodos conectados mediante sockets con 2 servidores intermediarios
	 */
	private void crearNodosConIntermedios() {
		// Creamos el primer server
		final InetSocketAddress direccionServerEmisor = new InetSocketAddress(21111);
		serverEmisor = NodoSocketViejo.createAndListenTo(direccionServerEmisor, procesador);

		// Creamos el segundo server
		final InetSocketAddress direccionServerReceptor = new InetSocketAddress(21112);
		serverReceptor = NodoSocketViejo.createAndListenTo(direccionServerReceptor, procesador);

		// Conectamos el primero con el segundo
		clienteIntermedio = NodoSocketViejo.createAndConnectTo(direccionServerReceptor, procesador);
		serverEmisor.conectarCon(clienteIntermedio);
		clienteIntermedio.conectarCon(serverEmisor);

		// Creamos el cliente del emisor y su portal
		clienteEmisor = NodoSocketViejo.createAndConnectTo(direccionServerEmisor, procesador);
		portalEmisor = PortalMapeadorViejo.createForIOWith(procesador, clienteEmisor);

		// Creamos el cliente del receptor y su portal
		clienteReceptor = NodoSocketViejo.createAndConnectTo(direccionServerReceptor, procesador);
		portalReceptor = PortalMapeadorViejo.createForIOWith(procesador, clienteReceptor);
	}

	/**
	 * Verifica que en una topología diamante el mensaje no llega 2 veces al destino
	 * 
	 * @throws InterruptedException
	 */
	@Test
	public void elMensajeNoDeberiaLlegarDuplicadoSiExistenCaminosParalelos() throws InterruptedException {
		// Creamos una de las rutas posibles
		crearNodosConIntermedios();

		// Agregamos otro server paralelo
		final InetSocketAddress direccionServerParalelo = new InetSocketAddress(21113);
		serverParalelo = NodoSocketViejo.createAndListenTo(direccionServerParalelo, procesador);

		// Y las conexiones para que llegue desde el emisor al receptor
		clienteIntermedioParalelo = NodoSocketViejo.createAndConnectTo(direccionServerParalelo, procesador);
		serverEmisor.conectarCon(clienteIntermedioParalelo);
		clienteIntermedioParalelo.conectarCon(serverEmisor);

		clienteReceptorParalelo = NodoSocketViejo.createAndConnectTo(direccionServerParalelo, procesador);
		clienteReceptorParalelo.conectarCon(portalReceptor);
		portalReceptor.conectarCon(clienteReceptorParalelo);

		final WaitBarrier esperarPrimerMensaje = WaitBarrier.create();
		// Creamos la métricas para medir los recibidos
		final MetricasPorTiempoImpl metricas = MetricasPorTiempoImpl.create();
		portalReceptor.recibirCon(new HandlerTipado<MensajeModeloParaTests>(SiempreTrue.getInstancia()) {

			public void onObjetoRecibido(final MensajeModeloParaTests mensaje) {
				esperarPrimerMensaje.release();
				metricas.registrarOutput();
			}
		});

		// Enviamos un mensaje para verificar que llega
		portalEmisor.enviar(MensajeModeloParaTests.create());

		// Esperamos que llegue algo el primer mensaje
		esperarPrimerMensaje.waitForReleaseUpTo(TimeMagnitude.of(1, TimeUnit.MINUTES));
		LOG.debug("Esperando otros mensajes a parte del primero");

		// Esperamos unos segundos más para registrar si llegan otros mensajes
		Thread.sleep(5000);

		Assert.assertEquals("Sólo debería haber llegado un mensaje", 1, metricas.getCantidadDeOutputs());

	}
}
