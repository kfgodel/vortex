/**
 * 01/07/2012 20:22:47 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.portal.tests;

import java.util.Arrays;

import net.gaia.vortex.core.api.mensaje.MensajeVortex;
import net.gaia.vortex.core.tests.MedicionesDePerformance;
import net.gaia.vortex.core.tests.MensajeModeloParaTests;
import net.gaia.vortex.portal.api.moleculas.MapeadorVortex;
import net.gaia.vortex.portal.impl.moleculas.mapeador.MapeadorDefault;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ar.com.dgarcia.lang.metrics.impl.MetricasPorTiempoImpl;
import ar.com.dgarcia.lang.metrics.impl.SnapshotDeMetricaPorTiempo;
import ar.com.dgarcia.testing.stress.StressGenerator;

/**
 * Esta clase prueba la perfomance del mapeador de objetos a vortex, utilizando los lineamientos de
 * la clase como modelo
 * 
 * @author D. García
 */
public class TestDePerformanceMapeadorVortex {
	private static final Logger LOG = LoggerFactory.getLogger(TestDePerformanceMapeadorVortex.class);

	private MapeadorVortex mapeador;

	@Before
	public void crearMapeador() {
		mapeador = MapeadorDefault.create();
	}

	@Test
	public void medirPerformanceConPrimitivaString() throws InterruptedException {
		testearMapeoIdaYVuelta("String", "Texto más o menos corto como para darle un poco de trabajo al conversor");
	}

	@Test
	public void medirPerformanceConPrimitivaNumber() throws InterruptedException {
		testearMapeoIdaYVuelta("Number", Double.valueOf(129.0));
	}

	@Test
	public void medirPerformanceConPrimitivaArrayDeStrings() throws InterruptedException {
		testearMapeoIdaYVuelta("List<String>", Arrays.asList("texto1", "texto2", "texto3", "texto4", "texto5",
				"texto6", "texto7", "texto8", "texto9", "texto10"));
	}

	@Test
	public void medirPerformanceConObjetomodelo() throws InterruptedException {
		testearMapeoIdaYVuelta("modelo", MensajeModeloParaTests.create());
	}

	/**
	 * Prueba la performance utilizando un thread que genera el mensaje y lo entrega
	 * 
	 * @param nombreDelTest
	 *            El nombre para el log
	 * @param cantidadDeThreads
	 *            La cantidad de threads en paralelo
	 * @param nombreDelTest
	 * @throws InterruptedException
	 *             Si vuela todo
	 */
	private void testearMapeoIdaYVuelta(final String nombreDelTest, final Object objeto) throws InterruptedException {
		// Generamos el testeador
		final StressGenerator stress = StressGenerator.create();
		stress.setCantidadDeThreadsEnEjecucion(1);

		// Creamos la metricas para medir
		final MetricasPorTiempoImpl metricas = MetricasPorTiempoImpl.create();

		final Class<? extends Object> tipoDelObjeto = objeto.getClass();
		// Por cada ejecucion convertimos y desconvertimos el objeto
		stress.setEjecutable(new Runnable() {
			@Override
			public void run() {
				final MensajeVortex mensaje = mapeador.convertirAVortex(objeto);
				metricas.registrarInput();
				mapeador.convertirDesdeVortex(mensaje, tipoDelObjeto);
				metricas.registrarOutput();
			}
		});

		testearYMostrarResultados(nombreDelTest, stress, metricas);
	}

	/**
	 * Comienza el test, esperando mientras mide. Muestras los resultados al final
	 * 
	 * @param nombreDelTest
	 *            El nombre para mostrar como test
	 * @param stress
	 *            El generador de stress que se usa para testear
	 * @param metricas
	 *            La métricas modificadas durante la medición
	 * @throws InterruptedException
	 *             Si vuela algo
	 */
	private void testearYMostrarResultados(final String nombreDelTest, final StressGenerator stress,
			final MetricasPorTiempoImpl metricas) throws InterruptedException {
		// Comenzamos el test
		LOG.info("[{}] Comenzando mediciones", nombreDelTest);
		metricas.resetear();
		stress.start();

		// Medimos durante un tiempo
		Thread.sleep(MedicionesDePerformance.TIEMPO_DE_TEST.getMillis());
		// Freezamos la medición
		final SnapshotDeMetricaPorTiempo medicion = SnapshotDeMetricaPorTiempo.createFrom(metricas);
		// Detenemos el stress
		stress.detenerThreads();

		// Mostramos los resultados
		final long cantidadDeInputs = medicion.getCantidadDeInputs();
		final long cantidadDeOutputs = medicion.getCantidadDeOutputs();
		final long millisTranscurridos = medicion.getDuracionDeMedicionEnMilis();
		LOG.info("[{}]: En {} ms se convirtieron {} objetos a mensaje y se desconvirtieron {}", new Object[] {
				nombreDelTest, millisTranscurridos, cantidadDeInputs, cantidadDeOutputs });

		LOG.info("[{}]: Obj->Vortex:{} obj/ms Vortex->Obj():{} obj/ms",
				new Object[] { nombreDelTest, medicion.getVelocidadDeInput(), medicion.getVelocidadDeOutput() });
		LOG.info("[{}] Fin", nombreDelTest);
	}

}
