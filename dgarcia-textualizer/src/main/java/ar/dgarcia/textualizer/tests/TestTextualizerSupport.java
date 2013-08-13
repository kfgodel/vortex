/**
 * 31/05/2012 23:29:05 Copyright (C) 2011 Darío L. García
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
package ar.dgarcia.textualizer.tests;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ar.com.dgarcia.lang.time.TimeMagnitude;
import ar.com.dgarcia.testing.stress.StressGenerator;
import ar.dgarcia.textualizer.api.ObjectTextualizer;

/**
 * Esta clase prueba el comportamiento de los conversores a y desde texto
 * 
 * @author D. García
 */
public abstract class TestTextualizerSupport {
	private static final Logger LOG = LoggerFactory.getLogger(TestTextualizerSupport.class);

	protected ObjectTextualizer textualizer;

	private TestBean textualized;

	@Before
	public void crearTextualizable() {
		textualized = new TestBean();
		textualized.numero = 3;
	}

	@Before
	public abstract void crearTextualizer();

	public static class TestBean {
		private Integer numero;

		public Integer getNumero() {
			return numero;
		}

		public void setNumero(final Integer numero) {
			this.numero = numero;
		}
	}

	/**
	 * Prueba que se pueda generar una representación textual de un objeto
	 */
	@Test
	public void deberiaPoderConvertirUnObjetoEnTexto() {
		final String convertedValue = textualizer.convertToString(textualized);
		Assert.assertNotNull(convertedValue);
		Assert.assertTrue("Debería ser una cadena de un par de ", convertedValue.length() > 10);
	}

	/**
	 * Prueba que se pueda recuperar el objeto original de la representación
	 */
	@Test
	public void deberíaPoderRecuperarUnObjectoDesdeTexto() {
		final String convertedValue = textualizer.convertToString(textualized);
		final Object equivalentValue = textualizer.convertFromString(convertedValue);
		Assert.assertTrue(equivalentValue instanceof TestBean);
		final TestBean duplicatedBean = (TestBean) equivalentValue;
		Assert.assertEquals(textualized.numero, duplicatedBean.numero);
	}

	/**
	 * Para poder compartir la instancia del
	 */
	@Test
	public void elSerializadorDeberiaPermitirEjecucionesConcurrentes() {
		final StressGenerator generator = StressGenerator.create();
		final AtomicLong cantidadDeErrores = new AtomicLong();
		generator.setCantidadDeEjecucionesPorThread(10000);
		generator.setCantidadDeThreadsEnEjecucion(4);
		generator.setEsperaEntreEjecucionesEnMilis(0);
		generator.setEjecutable(new Runnable() {

			public void run() {
				try {
					final String original = "Hola";
					final String convertido = textualizer.convertToString(original);
					final Object desconvertido = textualizer.convertFromString(convertido);
					if (!original.equals(desconvertido)) {
						cantidadDeErrores.incrementAndGet();
					}
				} catch (final Exception e) {
					LOG.error("Fallo la textualizacion concurrente", e);
					cantidadDeErrores.incrementAndGet();
				}
			}
		});
		generator.start();

		generator.esperarTerminoDeThreads(TimeMagnitude.of(5, TimeUnit.SECONDS));

		Assert.assertEquals("No deberían haber errores al ejecutar en paralelo la serializacion", 0,
				cantidadDeErrores.get());
	}

}
