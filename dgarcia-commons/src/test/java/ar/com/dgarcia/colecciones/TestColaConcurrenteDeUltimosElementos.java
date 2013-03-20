/**
 * 31/08/2012 22:25:46 Copyright (C) 2011 Darío L. García
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
package ar.com.dgarcia.colecciones;

import java.util.Collection;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import junit.framework.Assert;

import org.junit.Test;

import ar.com.dgarcia.colecciones.queue.ColaLimitadaConDescarte;
import ar.com.dgarcia.lang.time.TimeMagnitude;
import ar.com.dgarcia.testing.stress.StressGenerator;

/**
 * Esta clase prueba la colección que representa una cola concurrente con los últimos N elementos
 * retenidos
 * 
 * @author D. García
 */
public class TestColaConcurrenteDeUltimosElementos {

	@Test
	public void alAccederConcurrentementeDeberiaRetenerLosUltimosNElementos() {
		// Creamos la cola a testear
		final int cantidadMaximaDeElementos = 1000;
		final Collection<Number> cola = new ColaLimitadaConDescarte<Number>(cantidadMaximaDeElementos);

		// Creamos el stressor que va a agregar elementos concurrentemente
		final StressGenerator stressor = StressGenerator.create();
		stressor.setCantidadDeEjecucionesPorThread(100000);
		stressor.setCantidadDeThreadsEnEjecucion(20);
		stressor.setEsperaEntreEjecucionesEnMilis(0);
		final AtomicLong numero = new AtomicLong(0);
		stressor.setEjecutable(new Runnable() {

			public void run() {
				final Long valor = numero.incrementAndGet();
				cola.add(valor);
			}
		});

		// Ejecutamos el stressor
		stressor.start();
		stressor.esperarTerminoDeThreads(TimeMagnitude.of(60, TimeUnit.SECONDS));

		// Verificamos que la cantidad de elementos en cola no es más que el permitido
		Assert.assertEquals("Deberían existir como muchos los elementos permitidos", cantidadMaximaDeElementos,
				cola.size());
	}
}
