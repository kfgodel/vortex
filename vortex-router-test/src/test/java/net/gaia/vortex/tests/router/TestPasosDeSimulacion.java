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
import net.gaia.vortex.tests.router.impl.PortalImpl;
import net.gaia.vortex.tests.router.impl.RouterImpl;
import net.gaia.vortex.tests.router.impl.SimuladorImpl;
import net.gaia.vortex.tests.router.impl.mensajes.MensajeSupport;

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
	private RouterImpl r1;
	private PortalImpl p1;
	private RouterImpl r2;
	private PortalImpl p2;

	@Before
	public void crearSimulador() {
		MensajeSupport.resetIds();
		simulador = SimuladorImpl.create();
		r1 = RouterImpl.create("R1", simulador);
		r2 = RouterImpl.create("R2", simulador);
		p1 = PortalImpl.create("P1", simulador);
		p2 = PortalImpl.create("P2", simulador);
	}

	@Test
	public void deberiaMostrarLaConexionUniDireccionalComoUnSoloPaso() {
		p1.conectarCon(r1);

		Assert.assertEquals(1, simulador.getCantidadDePasosPendientes());
	}

	@Test
	public void deberiaMostrarLaConexionBidiComoUnSoloPaso() {
		p1.conectarBidi(r1);
		Assert.assertEquals(1, simulador.getCantidadDePasosPendientes());
	}

	@Test
	public void siNoTieneAQuienPublicarNoDeberiaGenerarPasos() {
		p1.setearYPublicarFiltros("tag1");
		Assert.assertEquals(0, simulador.getCantidadDePasosPendientes());
	}

	@Test
	public void siNoHayComunicacionBidiElRouterNoDeberiaPoderConfirmarLaPublicacion() {
		p1.conectarCon(r1);
		simulador.ejecutarTodos(TimeMagnitude.of(1, TimeUnit.SECONDS));

		p1.setearYPublicarFiltros("tag1");
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

		p1.setearYPublicarFiltros("tag1");
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
		p1.conectarBidi(r1);
		// Se realizan toda la interconexion bidireccional
		simulador.ejecutarTodos(TimeMagnitude.of(1, TimeUnit.SECONDS));

		p1.setearYPublicarFiltros("tag1");
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
		p1.conectarBidi(r1);
		r1.conectarBidi(r2);
		simulador.ejecutarTodos(TimeMagnitude.of(1, TimeUnit.SECONDS));

		p1.setearYPublicarFiltros("tag1");
		// El paso de pedido de id al router
		Assert.assertEquals(1, simulador.getCantidadDePasosPendientes());
		simulador.ejecutarTodos(TimeMagnitude.of(1, TimeUnit.SECONDS));
	}
}
