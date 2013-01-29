/**
 * 12/10/2012 19:07:33 Copyright (C) 2011 Darío L. García
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

import net.gaia.vortex.tests.router2.impl.PortalBidireccional;
import net.gaia.vortex.tests.router2.impl.RouterBidireccional;
import net.gaia.vortex.tests.router2.mensajes.MensajeNormal;
import net.gaia.vortex.tests.router2.mensajes.MensajeSupport;
import net.gaia.vortex.tests.router2.simulador.Simulador;
import net.gaia.vortex.tests.router2.simulador.SimuladorImpl;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import ar.com.dgarcia.lang.time.TimeMagnitude;

/**
 * esta clase prueba que la simulación del ruteo funcione como esperamos
 * 
 * @author D. García
 */
public class TestRuteoDeMensajes {

	private Simulador simulador;

	@Before
	public void crearSimulador() {
		MensajeSupport.resetIds();
		simulador = SimuladorImpl.create();
	}

	/**
	 * Prueba que si dos portales con intereses distintos publican sus filtros, el portal no
	 * interesado, no recibe mensajes que no quiere
	 */
	@Test
	public void enUnaRedTipoYElqueQuiereTortaNoDeberiaRecibirPan() {
		// armar la red conectando las cosas
		final PortalBidireccional pan = PortalBidireccional.create("Pan", simulador);
		final PortalBidireccional torta = PortalBidireccional.create("Torta", simulador);
		final RouterBidireccional r1 = RouterBidireccional.create("R1", simulador);
		final RouterBidireccional r2 = RouterBidireccional.create("R2", simulador);
		final PortalBidireccional panificador = PortalBidireccional.create("Panificador", simulador);

		pan.simularConexionBidi(r1);
		torta.simularConexionBidi(r1);
		r1.simularConexionBidi(r2);
		r2.simularConexionBidi(panificador);

		simulador.ejecutarTodos(TimeMagnitude.of(10, TimeUnit.SECONDS));

		// publicar los filtros de ambos portales
		pan.simularSeteoDeFiltros("pan");
		torta.simularSeteoDeFiltros("torta");
		simulador.ejecutarTodos(TimeMagnitude.of(5, TimeUnit.MINUTES));

		// enviar el mensaje desde el origen
		final MensajeNormal nuevoPan = MensajeNormal.create("pan", "Nuevo pan desde panificador");
		panificador.simularEnvioDe(nuevoPan);
		simulador.ejecutarTodos(TimeMagnitude.of(5, TimeUnit.MINUTES));

	}

	@Ignore("Por ahora lo saco")
	@Test
	public void ruteoConConexionesEnOrden() {
		// armar la red conectando las cosas
		final PortalBidireccional pan = PortalBidireccional.create("Pan", simulador);
		final PortalBidireccional torta = PortalBidireccional.create("Torta", simulador);
		final RouterBidireccional r1 = RouterBidireccional.create("R1", simulador);
		final RouterBidireccional r2 = RouterBidireccional.create("r2", simulador);
		final PortalBidireccional panificador = PortalBidireccional.create("Panificador", simulador);

		pan.simularConexionBidi(r1);
		simulador.ejecutarTodos(TimeMagnitude.of(1, TimeUnit.SECONDS));
		pan.simularSeteoDeFiltros("pan");
		simulador.ejecutarTodos(TimeMagnitude.of(1, TimeUnit.SECONDS));

		torta.simularConexionBidi(r1);
		simulador.ejecutarTodos(TimeMagnitude.of(1, TimeUnit.SECONDS));
		torta.simularSeteoDeFiltros("torta");
		simulador.ejecutarTodos(TimeMagnitude.of(1, TimeUnit.SECONDS));

		r1.simularConexionBidi(r2);
		r2.simularConexionBidi(panificador);
		simulador.ejecutarTodos(TimeMagnitude.of(1, TimeUnit.SECONDS));
	}

}
