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

import net.gaia.vortex.tests.router.impl.PortalImpl;
import net.gaia.vortex.tests.router.impl.RouterImpl;
import net.gaia.vortex.tests.router.impl.SimuladorImpl;
import net.gaia.vortex.tests.router.impl.mensajes.MensajeSupport;

import org.junit.Before;
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
		final PortalImpl pan = PortalImpl.create("Pan", simulador);
		final PortalImpl torta = PortalImpl.create("Torta", simulador);
		final RouterImpl r1 = RouterImpl.create("R1", simulador);
		final RouterImpl r2 = RouterImpl.create("r2", simulador);
		final PortalImpl panificador = PortalImpl.create("Panificador", simulador);

		pan.conectarBidi(r1);
		torta.conectarBidi(r1);
		r1.conectarBidi(r2);
		r2.conectarBidi(panificador);

		simulador.ejecutarTodos(TimeMagnitude.of(1, TimeUnit.SECONDS));

		// publicar los filtros de ambos portales
		pan.setearYPublicarFiltros("pan");
		torta.setearYPublicarFiltros("torta");
		simulador.ejecutarTodos(TimeMagnitude.of(5, TimeUnit.MINUTES));

		// enviar el mensaje desde el origen
		// correr la simulacion
		// verificar que el mensaje llego a donde debía y no a donde no
	}

	@Test
	public void ruteoConConexionesEnOrden() {
		// armar la red conectando las cosas
		final PortalImpl pan = PortalImpl.create("Pan", simulador);
		final PortalImpl torta = PortalImpl.create("Torta", simulador);
		final RouterImpl r1 = RouterImpl.create("R1", simulador);
		final RouterImpl r2 = RouterImpl.create("r2", simulador);
		final PortalImpl panificador = PortalImpl.create("Panificador", simulador);

		pan.conectarBidi(r1);
		simulador.ejecutarTodos(TimeMagnitude.of(1, TimeUnit.SECONDS));
		pan.setearYPublicarFiltros("pan");
		simulador.ejecutarTodos(TimeMagnitude.of(1, TimeUnit.SECONDS));

		torta.conectarBidi(r1);
		simulador.ejecutarTodos(TimeMagnitude.of(1, TimeUnit.SECONDS));
		torta.setearYPublicarFiltros("torta");
		simulador.ejecutarTodos(TimeMagnitude.of(1, TimeUnit.SECONDS));

		r1.conectarBidi(r2);
		r2.conectarBidi(panificador);
		simulador.ejecutarTodos(TimeMagnitude.of(1, TimeUnit.SECONDS));
	}

}
