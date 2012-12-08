/**
 * 13/10/2012 11:00:21 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.tests.router;

import java.util.concurrent.TimeUnit;

import junit.framework.Assert;
import net.gaia.vortex.tests.router2.impl.PortalBidireccional;
import net.gaia.vortex.tests.router2.impl.RouterBidireccional;
import net.gaia.vortex.tests.router2.mensajes.MensajeNormal;
import net.gaia.vortex.tests.router2.mensajes.MensajeSupport;
import net.gaia.vortex.tests.router2.simulador.Simulador;
import net.gaia.vortex.tests.router2.simulador.SimuladorImpl;

import org.junit.Before;
import org.junit.Test;

import ar.com.dgarcia.lang.time.TimeMagnitude;

/**
 * Este test verifica que se impriman los pasos que quiero en la simulacion
 * 
 * @author D. García
 */
public class TestPasosDeSimulacion {

	private Simulador simulador;
	private RouterBidireccional r1;
	private PortalBidireccional p1;
	private RouterBidireccional r2;
	private PortalBidireccional p2;

	@Before
	public void crearSimulador() {
		MensajeSupport.resetIds();
		simulador = SimuladorImpl.create();
		r1 = RouterBidireccional.create("R1", simulador);
		r2 = RouterBidireccional.create("R2", simulador);
		p1 = PortalBidireccional.create("P1", simulador);
		p2 = PortalBidireccional.create("P2", simulador);
	}

	@Test
	public void deberiaMostrarLaConexionUniDireccionalComoUnSoloPaso() {
		p1.conectarCon(r1);

		Assert.assertEquals(1, simulador.getCantidadDePasosPendientes());
	}

	@Test
	public void deberiaMostrarLaConexionBidiComoUnSoloPaso() {
		p1.simularConexionBidi(r1);
		Assert.assertEquals(1, simulador.getCantidadDePasosPendientes());
	}

	@Test
	public void siNoTieneAQuienPublicarNoDeberiaGenerarPasos() {
		p1.setFiltros("tag1");
		Assert.assertEquals(0, simulador.getCantidadDePasosPendientes());
	}

	@Test
	public void siNoHayComunicacionBidiElRouterNoDeberiaPoderConfirmarLaPublicacion() {
		p1.conectarCon(r1);
		simulador.ejecutarTodos(TimeMagnitude.of(1, TimeUnit.SECONDS));

		p1.setFiltros("tag1");
		// El paso de pedido de id al router
		Assert.assertEquals(1, simulador.getCantidadDePasosPendientes());
		simulador.ejecutarSiguiente();

		// El router nunca contestó
		Assert.assertEquals(0, simulador.getCantidadDePasosPendientes());
	}

	@Test
	public void siElRouterEstaConectadoAOtroPortalNoDeberiaRecepcionDeConfirmacion() {
		p1.conectarCon(r1);
		r1.conectarCon(p2);
		simulador.ejecutarTodos(TimeMagnitude.of(1, TimeUnit.SECONDS));

		p1.setFiltros("tag1");
		// El paso de pedido de id al router
		Assert.assertEquals(1, simulador.getCantidadDePasosPendientes());
		simulador.ejecutarSiguiente();

		// El paso de respuesta de Id al portal equivocado
		Assert.assertEquals(1, simulador.getCantidadDePasosPendientes());
		simulador.ejecutarSiguiente();

		// El portal nunca contesto
		Assert.assertEquals(0, simulador.getCantidadDePasosPendientes());
	}

	@Test
	public void deberiaSeguirLosPasosQueDefinimosConJavierAlPublicarUnoAUno() {
		p1.simularConexionBidi(r1);
		// Se realizan toda la interconexion bidireccional
		simulador.ejecutarTodos(TimeMagnitude.of(1, TimeUnit.SECONDS));

		p1.setFiltros("tag1");
		// El paso de pedido de id al router
		Assert.assertEquals(1, simulador.getCantidadDePasosPendientes());
		simulador.ejecutarSiguiente();

		// No deberían quedar más tareas
		Assert.assertEquals(0, simulador.getCantidadDePasosPendientes());

		// El router debería haber adaptado sus filtros para el portal
		Assert.assertTrue(r1.usaFiltrosCon(p1, "tag1"));
	}

	@Test
	public void deberiaPropagarLosFiltrosEntreRouters() {
		p1.simularConexionBidi(r1);
		r1.simularConexionBidi(r2);
		simulador.ejecutarTodos(TimeMagnitude.of(1, TimeUnit.SECONDS));

		p1.setFiltros("tag1");
		// El paso de pedido de id al router
		Assert.assertEquals(1, simulador.getCantidadDePasosPendientes());
		simulador.ejecutarTodos(TimeMagnitude.of(1, TimeUnit.SECONDS));

		Assert.assertTrue("El tercer nodo deberia usar el filtro", r2.usaFiltrosCon(r1, "tag1"));
	}

	@Test
	public void deberiaRevertirLosFiltrosAlDesconectar() {
		p1.simularConexionBidi(r1);
		r1.simularConexionBidi(r2);
		p1.setFiltros("tag1");
		simulador.ejecutarTodos(TimeMagnitude.of(1, TimeUnit.SECONDS));

		// Verificamos que el estado inicial es que r2 filtra lo que necesita p1
		Assert.assertTrue("El tercer nodo deberia usar el filtro", r2.usaFiltrosCon(r1, "tag1"));

		p1.simularDesconexionBidiDe(r1);
		simulador.ejecutarTodos(TimeMagnitude.of(1, TimeUnit.SECONDS));

		// Verificamos que ahora r2 ya no filtra para p1
		Assert.assertFalse("El tercer nodo NO deberia usar el filtro", r2.usaFiltrosCon(r1, "tag1"));
	}

	@Test
	public void deberiaPermitirLaReconexionSiEsDesconexionParcial() {
		p1.simularConexionBidi(r1);
		r1.simularConexionBidi(r2);
		p1.setFiltros("tag1");
		simulador.ejecutarTodos(TimeMagnitude.of(1, TimeUnit.SECONDS));

		// Verificamos que el estado inicial es que r2 filtra lo que necesita p1
		Assert.assertTrue("El tercer nodo deberia usar el filtro", r2.usaFiltrosCon(r1, "tag1"));

		r1.desconectarDe(r2);
		simulador.ejecutarTodos(TimeMagnitude.of(1, TimeUnit.SECONDS));

		// Verificamos que R2 no se enteró del corte
		Assert.assertTrue("El tercer nodo deberia usar el filtro", r2.usaFiltrosCon(r1, "tag1"));

		r1.conectarCon(r2);
		simulador.ejecutarTodos(TimeMagnitude.of(1, TimeUnit.SECONDS));
	}

	@Test
	public void deberiaRecibirElMensajeElRouter() {
		p1.simularConexionBidi(r1);
		simulador.ejecutarTodos(TimeMagnitude.of(1, TimeUnit.SECONDS));

		final MensajeNormal mensaje = MensajeNormal.create("tag1", "texto");
		p1.enviar(mensaje);

		simulador.ejecutarTodos(TimeMagnitude.of(1, TimeUnit.SECONDS));
		Assert.assertTrue("R1 debería haber recibido el mensaje", r1.getRecibidos().contains(mensaje));
	}

}
