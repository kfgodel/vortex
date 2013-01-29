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
import net.gaia.vortex.tests.router2.impl.filtros.FiltroPasaNada;
import net.gaia.vortex.tests.router2.impl.filtros.FiltroPasaTodo;
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
		r1.setListenerDeFiltrosExternos(ListenerDeFiltrosParaLog.create(r1));
		r2 = RouterBidireccional.create("R2", simulador);
		r2.setListenerDeFiltrosExternos(ListenerDeFiltrosParaLog.create(r2));
		p1 = PortalBidireccional.create("P1", simulador);
		p1.setListenerDeFiltrosExternos(ListenerDeFiltrosParaLog.create(p1));
		p2 = PortalBidireccional.create("P2", simulador);
		p2.setListenerDeFiltrosExternos(ListenerDeFiltrosParaLog.create(p2));
	}

	@Test
	public void deberiaMostrarLaConexionUniDireccionalComoUnSoloPaso() {
		p1.simularConexionCon(r1);

		Assert.assertEquals(1, simulador.getCantidadDePasosPendientes());
	}

	@Test
	public void deberiaMostrarLaConexionBidiComoUnSoloPaso() {
		p1.simularConexionBidi(r1);
		Assert.assertEquals(1, simulador.getCantidadDePasosPendientes());
	}

	@Test
	public void siNoTieneAQuienPublicarNoDeberiaGenerarPasos() {
		p1.simularSeteoDeFiltros("tag1");
		Assert.assertEquals("Debería estar el paso del seteo de tags", 1, simulador.getCantidadDePasosPendientes());

		simulador.ejecutarSiguiente();
		Assert.assertEquals("No debería haber otro porque no esta conectado", 0,
				simulador.getCantidadDePasosPendientes());
	}

	@Test
	public void siNoHayComunicacionBidiElRouterNoDeberiaPoderConfirmarLaPublicacion() {
		p1.simularConexionCon(r1);
		simulador.ejecutarTodos(TimeMagnitude.of(1, TimeUnit.SECONDS));

		p1.simularSeteoDeFiltros("tag1");
		Assert.assertEquals("Debería estar el paso del seteo de tags", 1, simulador.getCantidadDePasosPendientes());

		simulador.ejecutarSiguiente();
		// La publicacion no se hace por que no hay ida y vuelta todavía
		Assert.assertEquals(0, simulador.getCantidadDePasosPendientes());
	}

	@Test
	public void siElRouterEstaConectadoAOtroPortalNoDeberiaHaberRecepcionDeConfirmacion() {
		p1.simularConexionCon(r1);
		r1.simularConexionCon(p2);
		simulador.ejecutarTodos(TimeMagnitude.of(10000, TimeUnit.SECONDS));

		p1.simularSeteoDeFiltros("tag1");
		// El paso del seteo que queda sin camino para publicar
		Assert.assertEquals(1, simulador.getCantidadDePasosPendientes());
		simulador.ejecutarSiguiente();

		// El portal nunca contesto
		Assert.assertEquals(0, simulador.getCantidadDePasosPendientes());
	}

	@Test
	public void deberiaPublicarFiltrosAunqueSeSetenAntesDeQueTermineLaConexionBidi() {
		p1.simularConexionBidi(r1);
		p1.simularSeteoDeFiltros("filtro1");

		simulador.ejecutarTodos(TimeMagnitude.of(10, TimeUnit.SECONDS));
		Assert.assertTrue("R1 deberia saber lo que P1 quiere", r1.usaFiltrosCon(p1, "filtro1"));
	}

	/**
	 * Al no tener conexión bidireccional no sabemos lo que el otro quiere. Por eso le mandamos todo
	 * para que descarte. No hay termino medio
	 */
	@Test
	public void siLaConexionEsUniElRouterDeberiaAsumirUnPasaTodo() {
		r1.simularConexionCon(p1);

		simulador.ejecutarTodos(TimeMagnitude.of(10, TimeUnit.SECONDS));
		Assert.assertEquals("R1 deberia asumir pasa todo por no tener conexion bidi", FiltroPasaTodo.class, r1
				.getFiltroDeSalidaPara(p1).getClass());
	}

	/**
	 * Si tenemos conexion bidi, entonces podemos asumir un pasa nada, y que el otro nos publique
	 * exactamente lo que quiere
	 */
	@Test
	public void siLaConexionEsBidiElRouterDeberiaAsumirUnPasaNada() {
		r1.simularConexionBidi(p1);

		simulador.ejecutarTodos(TimeMagnitude.of(10, TimeUnit.SECONDS));
		Assert.assertEquals("R1 deberia asumir pasa todo por no tener conexion bidi", FiltroPasaNada.class, r1
				.getFiltroDeSalidaPara(p1).getClass());
	}

	@Test
	public void deberiaSeguirLosPasosQueDefinimosConJavierAlPublicarUnoAUno() {
		p1.simularConexionBidi(r1);
		// Se realizan toda la interconexion bidireccional
		simulador.ejecutarTodos(TimeMagnitude.of(10000, TimeUnit.SECONDS));

		p1.simularSeteoDeFiltros("tag1");
		// El paso del seteo en sí
		Assert.assertEquals(1, simulador.getCantidadDePasosPendientes());
		simulador.ejecutarSiguiente();

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

		p1.simularSeteoDeFiltros("tag1");
		// El paso de pedido de id al router
		Assert.assertEquals(1, simulador.getCantidadDePasosPendientes());
		simulador.ejecutarTodos(TimeMagnitude.of(1, TimeUnit.SECONDS));

		Assert.assertTrue("El tercer nodo deberia usar el filtro", r2.usaFiltrosCon(r1, "tag1"));
	}

	@Test
	public void deberiaRevertirLosFiltrosAlDesconectar() {
		p1.simularConexionBidi(r1);
		r1.simularConexionBidi(r2);
		p1.simularSeteoDeFiltros("tag1");
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
		p1.simularSeteoDeFiltros("tag1");
		simulador.ejecutarTodos(TimeMagnitude.of(1, TimeUnit.SECONDS));

		// Verificamos que el estado inicial es que r2 filtra lo que necesita p1
		Assert.assertTrue("El tercer nodo deberia usar el filtro", r2.usaFiltrosCon(r1, "tag1"));

		r1.simularDesconexionUniDe(r2);
		simulador.ejecutarTodos(TimeMagnitude.of(1, TimeUnit.SECONDS));

		// Verificamos que R2 no se enteró del corte
		Assert.assertTrue("El tercer nodo deberia usar el filtro", r2.usaFiltrosCon(r1, "tag1"));

		r1.simularConexionCon(r2);
		simulador.ejecutarTodos(TimeMagnitude.of(1, TimeUnit.SECONDS));

		// Verificamos que al reconectar los filtros fueron propagados
		Assert.assertTrue("El tercer nodo deberia usar el filtro", r2.usaFiltrosCon(r1, "tag1"));

	}

	@Test
	public void unSoloPortalConectadoAUnRouterNoDeberiaEnviarMensajesSinInteresados() {
		p1.simularConexionBidi(r1);
		simulador.ejecutarTodos(TimeMagnitude.of(1, TimeUnit.SECONDS));

		final MensajeNormal mensaje = MensajeNormal.create("tag1", "texto");
		p1.simularEnvioDe(mensaje);

		simulador.ejecutarTodos(TimeMagnitude.of(1, TimeUnit.SECONDS));
		Assert.assertFalse("R1 debería descartar el mensaje sin que llegue a R1", r1.getRecibidos().contains(mensaje));
	}

	@Test
	public void deberiaRestablecerLosFiltrosSiSeProduceUnaReconexion() {
		p1.simularConexionBidi(r1);
		r1.simularConexionBidi(r2);
		p2.simularConexionBidi(r2);
		p1.simularSeteoDeFiltros("tag1");
		p2.simularSeteoDeFiltros("tag2");
		simulador.ejecutarTodos(TimeMagnitude.of(1, TimeUnit.SECONDS));

		// Verificamos que cada router tienen los tags correctos para cada lado
		Assert.assertTrue("R1 deberia usar el filtro correcto para P1", r1.usaFiltrosCon(p1, "tag1"));
		Assert.assertTrue("R1 deberia usar el filtro correcto para R2", r1.usaFiltrosCon(r2, "tag2"));
		Assert.assertTrue("R2 deberia usar el filtro correcto para R1", r2.usaFiltrosCon(r1, "tag1"));
		Assert.assertTrue("R2 deberia usar el filtro correcto para P2", r2.usaFiltrosCon(p2, "tag2"));

		// Verificamos que los portales tienen filtros de su contraparte
		Assert.assertTrue("P1 deberia usar el filtro correcto para R1", p1.usaFiltrosCon(r1, "tag2"));
		Assert.assertTrue("P2 deberia usar el filtro correcto para R2", p2.usaFiltrosCon(r2, "tag1"));

		r1.simularDesconexionUniDe(r2);
		simulador.ejecutarTodos(TimeMagnitude.of(1, TimeUnit.SECONDS));

		// Verificamos que R2 no se enteró de los cambios pero R1 sí
		Assert.assertTrue("R1 deberia usar el filtro correcto para P1", r1.usaFiltrosCon(p1, "tag1"));
		Assert.assertFalse("R1 NO deberia usar el filtro correcto para R2", r1.usaFiltrosCon(r2, "tag2"));
		Assert.assertTrue("R2 deberia usar el filtro correcto para R1", r2.usaFiltrosCon(r1, "tag1"));
		Assert.assertTrue("R2 deberia usar el filtro correcto para P2", r2.usaFiltrosCon(p2, "tag2"));

		// Verificamos que P1 fue afectado pero P2
		Assert.assertFalse("P1 NO deberia usar el filtro correcto para R1", p1.usaFiltrosCon(r1, "tag2"));
		Assert.assertTrue("P2 deberia usar el filtro correcto para R2", p2.usaFiltrosCon(r2, "tag1"));

		r1.simularConexionCon(r2);
		simulador.ejecutarTodos(TimeMagnitude.of(1, TimeUnit.SECONDS));

		// Verificamos que al reconectar los filtros fueron propagados
		Assert.assertTrue("R1 deberia usar el filtro correcto para P1", r1.usaFiltrosCon(p1, "tag1"));
		Assert.assertTrue("R1 deberia usar el filtro correcto para R2", r1.usaFiltrosCon(r2, "tag2"));
		Assert.assertTrue("R2 deberia usar el filtro correcto para R1", r2.usaFiltrosCon(r1, "tag1"));
		Assert.assertTrue("R2 deberia usar el filtro correcto para P2", r2.usaFiltrosCon(p2, "tag2"));

		// Verificamos que los portales tienen filtros de su contraparte
		Assert.assertTrue("P1 deberia usar el filtro correcto para R1", p1.usaFiltrosCon(r1, "tag2"));
		Assert.assertTrue("P2 deberia usar el filtro correcto para R2", p2.usaFiltrosCon(r2, "tag1"));

	}

	@Test
	public void deberiaPublicarElFiltroDelPortalAunqueSeDefinaAntesDeConectar() {
		p1.simularSeteoDeFiltros("filtro1");
		p1.simularConexionBidi(r1);

		simulador.ejecutarTodos(TimeMagnitude.of(10, TimeUnit.SECONDS));
		Assert.assertTrue("R1 deberia saber lo que P1 quiere", r1.usaFiltrosCon(p1, "filtro1"));
	}
}
