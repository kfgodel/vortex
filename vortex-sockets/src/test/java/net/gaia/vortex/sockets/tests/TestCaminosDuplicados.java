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
import net.gaia.vortex.api.builder.Nodos;
import net.gaia.vortex.api.builder.VortexSockets;
import net.gaia.vortex.api.moleculas.Distribuidor;
import net.gaia.vortex.api.moleculas.Portal;
import net.gaia.vortex.api.moleculas.SocketHost;
import net.gaia.vortex.core.tests.MensajeModeloParaTests;
import net.gaia.vortex.impl.builder.VortexCoreBuilder;
import net.gaia.vortex.impl.builder.VortexPortalBuilder;
import net.gaia.vortex.impl.builder.VortexSocketsBuilder;
import net.gaia.vortex.impl.condiciones.SiempreTrue;
import net.gaia.vortex.impl.support.HandlerTipado;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ar.com.dgarcia.lang.conc.WaitBarrier;
import ar.com.dgarcia.lang.metrics.impl.MetricasPorTiempoImpl;
import ar.com.dgarcia.lang.time.TimeMagnitude;
import ar.com.dgarcia.testing.FreePortFinder;

/**
 * Esta clase prueba que si existen caminos duplicados por sockets los mensajes no lleguen
 * duplicados
 * 
 * @author D. García
 */
public class TestCaminosDuplicados {
	private static final Logger LOG = LoggerFactory.getLogger(TestCaminosDuplicados.class);

	private SocketHost<Distribuidor> serverEmisor;
	private SocketHost<Distribuidor> serverReceptor;
	private SocketHost<Distribuidor> clienteIntermedio;
	private SocketHost<Distribuidor> clienteEmisor;
	private SocketHost<Distribuidor> clienteReceptor;

	private Portal portalReceptor;

	private Portal portalEmisor;

	private SocketHost<Distribuidor> serverParalelo;

	private SocketHost<Distribuidor> clienteReceptorParalelo;

	private SocketHost<Distribuidor> clienteIntermedioParalelo;

	private VortexSockets builder;

	private VortexPortalBuilder builderPortal;

	@Before
	public void crearProcesador() {
		final VortexCoreBuilder core = VortexCoreBuilder.create(null);
		builder = VortexSocketsBuilder.create(core);
		builderPortal = VortexPortalBuilder.create(core);
	}

	@After
	public void eliminarProcesador() {
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
	 * Crea una red linea con dos servidores conectados entre sí a través de un cliente intermedio,
	 * y dos clientes socket en los extremos con portales para enviar y recibir
	 */
	private void crearNodosConIntermedios() {
		// Creamos el primer server que hará de emisor
		final InetSocketAddress direccionServerEmisor = new InetSocketAddress(FreePortFinder.getFreePort());
		serverEmisor = builder.distribuidorSocketSinDuplicados();
		serverEmisor.listenConnectionsOn(direccionServerEmisor);

		// Creamos el cliente del emisor y su portal
		clienteEmisor = builder.distribuidorSocket();
		clienteEmisor.connectTo(direccionServerEmisor);

		portalEmisor = builderPortal.portalIdentificador();
		Nodos.interconectarConDistribuidor(portalEmisor, clienteEmisor.getSalida());

		// Creamos el segundo server que hará de receptor
		final InetSocketAddress direccionServerReceptor = new InetSocketAddress(FreePortFinder.getFreePort());
		serverReceptor = builder.distribuidorSocketSinDuplicados();
		serverReceptor.listenConnectionsOn(direccionServerReceptor);

		// Creamos el cliente del receptor y su portal
		clienteReceptor = builder.distribuidorSocket();
		clienteReceptor.connectTo(direccionServerReceptor);

		portalReceptor = builderPortal.portalIdentificador();
		Nodos.interconectarConDistribuidor(portalReceptor, clienteReceptor.getSalida());

		// Creamos uno intermedio que esta conectado al receptor por socket y al emisor en memoria
		clienteIntermedio = builder.distribuidorSocketSinDuplicados();
		clienteIntermedio.connectTo(direccionServerReceptor);
		Nodos.interconectarDistribuidores(clienteIntermedio.getSalida(), clienteEmisor.getSalida());

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
		final InetSocketAddress direccionServerParalelo = new InetSocketAddress(FreePortFinder.getFreePort());
		serverParalelo = builder.distribuidorSocketSinDuplicados();
		serverParalelo.listenConnectionsOn(direccionServerParalelo);

		// Creamos un cliente como camino paralelo desde el server emisor
		clienteIntermedioParalelo = builder.distribuidorSocket();
		clienteIntermedioParalelo.connectTo(direccionServerParalelo);
		Nodos.interconectarDistribuidores(clienteIntermedioParalelo.getSalida(), serverEmisor.getSalida());

		// Y el otro cliente como camino paralelo desde el portal receptor
		clienteReceptorParalelo = builder.distribuidorSocket();
		Nodos.interconectarConDistribuidor(portalReceptor, clienteReceptorParalelo.getSalida());

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
