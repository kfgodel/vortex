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

	@Before
	public void crearSimulador() {
		simulador = SimuladorImpl.create();
		r1 = RouterImpl.create("R1", simulador);
		p1 = PortalImpl.create("P1", simulador);
	}

	@Test
	public void deberiaMostrarLaConexionUniDireccionalComoUnSoloPaso() {
		p1.conectarCon(r1);

		Assert.assertEquals(1, simulador.getCantidadDePasosPendientes());

		r1.conectarCon(p1);
		Assert.assertEquals(2, simulador.getCantidadDePasosPendientes());

		simulador.ejecutarSiguiente();
		simulador.ejecutarSiguiente();
		Assert.assertEquals(0, simulador.getCantidadDePasosPendientes());

		Assert.assertTrue(p1.tieneComoDestinoA(r1));
		Assert.assertTrue(r1.tieneComoDestinoA(p1));
	}

	@Test
	public void deberiaMostrarLaConexionBidiComoUnSoloPaso() {
		p1.conectarBidi(r1);
		Assert.assertEquals(1, simulador.getCantidadDePasosPendientes());

		simulador.ejecutarSiguiente();
		Assert.assertEquals(0, simulador.getCantidadDePasosPendientes());

		Assert.assertTrue(p1.tieneComoDestinoA(r1));
		Assert.assertTrue(r1.tieneComoDestinoA(p1));
	}

	@Test
	public void deberiaIndicarSiNoTieneAQuienPublicarFiltros() {
		p1.setearYPublicarFiltros("filtro1");
		Assert.assertEquals(1, simulador.getCantidadDePasosPendientes());

		simulador.ejecutarSiguiente();
		Assert.assertEquals(0, simulador.getCantidadDePasosPendientes());
	}

	@Test
	public void deberiaSeguirLosPasosQueDefinimosConJavierAlPublicarUnoAUno() {
		p1.conectarBidi(r1);
		simulador.ejecutarTodos(TimeMagnitude.of(1, TimeUnit.SECONDS));

		p1.setearYPublicarFiltros("filtro1");
		Assert.assertEquals(1, simulador.getCantidadDePasosPendientes());

		simulador.ejecutarSiguiente();
		Assert.assertEquals(0, simulador.getCantidadDePasosPendientes());
	}
}
