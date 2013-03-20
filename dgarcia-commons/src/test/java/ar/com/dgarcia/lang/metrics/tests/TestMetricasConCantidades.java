/**
 * 04/08/2012 00:06:08 Copyright (C) 2011 Darío L. García
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
package ar.com.dgarcia.lang.metrics.tests;

import java.util.concurrent.TimeUnit;

import junit.framework.Assert;

import org.junit.Test;

import ar.com.dgarcia.lang.metrics.MetricasPorTiempo;
import ar.com.dgarcia.lang.metrics.impl.MetricasDeCargaImpl;
import ar.com.dgarcia.lang.time.TimeMagnitude;
import ar.com.dgarcia.testing.stress.StressGenerator;

/**
 * Esta clase prueba que las métricas funcionen al indicar cantidades no unitarias
 * 
 * @author D. García
 */
public class TestMetricasConCantidades {

	@Test
	public void deberiaContabilizarBienCon3DeEntradaY2DeSalida() {
		// La metrica a testear
		final MetricasDeCargaImpl metricas = MetricasDeCargaImpl.create();

		// Cada segundo indicamos que entraron 3 y salieron 2
		final StressGenerator stressGenerator = StressGenerator.create();
		stressGenerator.setCantidadDeEjecucionesPorThread(10);
		stressGenerator.setCantidadDeThreadsEnEjecucion(1);
		stressGenerator.setEsperaEntreEjecucionesEnMilis(1000);
		stressGenerator.setEjecutable(new Runnable() {
			
			public void run() {
				metricas.registrarInput(3);
				metricas.registrarOutput(2);
			}
		});

		stressGenerator.start();
		stressGenerator.esperarTerminoDeThreads(TimeMagnitude.of(15, TimeUnit.SECONDS));

		// Vemos si las mediciones están bien
		final MetricasPorTiempo totales = metricas.getMetricasTotales();
		verificarCantidades(30, 20, totales);
		verificarVelocidades(totales, 3 / 1000d, 2 / 1000d, 0.0001);

		final MetricasPorTiempo metricasPorSegundo = metricas.getMetricasEnBloqueDeUnSegundo();
		verificarCantidades(3, 2, metricasPorSegundo);
		verificarVelocidades(metricasPorSegundo, 3 / 1000d, 2 / 1000d, 0.0001);

		final MetricasPorTiempo metricasCada5s = metricas.getMetricasEnBloqueDe5Segundos();
		verificarCantidades(15, 10, metricasCada5s);
		verificarVelocidades(metricasCada5s, 3 / 1000d, 2 / 1000d, 0.0001);

	}

	/**
	 * @param totales
	 * @param velocidadInEsperada
	 * @param velocidadoutEsperada
	 * @param margenDeError
	 */
	private void verificarVelocidades(final MetricasPorTiempo totales, final double velocidadInEsperada,
			final double velocidadoutEsperada, final double margenDeError) {
		final double velocidadDeInput = totales.getVelocidadDeInput();
		Assert.assertEquals(velocidadInEsperada, velocidadDeInput, margenDeError);
		final double velocidadDeOutput = totales.getVelocidadDeOutput();
		Assert.assertEquals(velocidadoutEsperada, velocidadDeOutput, margenDeError);
	}

	/**
	 * @param entradasEsperadas
	 * @param salidasEsperadas
	 * @param totales
	 */
	private void verificarCantidades(final int entradasEsperadas, final int salidasEsperadas,
			final MetricasPorTiempo totales) {
		final long entradasTotales = totales.getCantidadDeInputs();
		Assert.assertEquals(entradasEsperadas, entradasTotales);
		final long salidasTotales = totales.getCantidadDeOutputs();
		Assert.assertEquals(salidasEsperadas, salidasTotales);
	}

}
