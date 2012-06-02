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
package ar.dgarcia.objectsockets.tests;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ar.com.dgarcia.lang.time.TimeMagnitude;
import ar.com.dgarcia.testing.stress.StressGenerator;
import ar.dgarcia.objectsockets.api.ObjectTextualizer;
import ar.dgarcia.objectsockets.external.xml.XmlTextualizer;

/**
 * Esta clase prueba el comportamiento de los conversores a y desde texto
 * 
 * @author D. García
 */
public class TestTextualizer {
	private static final Logger LOG = LoggerFactory.getLogger(TestTextualizer.class);

	protected ObjectTextualizer textualizer;

	@Before
	public void crearTextualizer() {
		textualizer = XmlTextualizer.create();
	}

	/**
	 * Prueba que se pueda generar una representación textual de un objeto
	 */
	@Test
	public void deberiaPoderConvertirUnObjetoEnTexto() {
		final Object value = new Object();
		final String convertedValue = textualizer.convertToString(value);
		Assert.assertNotNull(convertedValue);
		Assert.assertFalse(convertedValue.isEmpty());
	}

	/**
	 * Prueba que se pueda recuperar el objeto original de la representación
	 */
	@Test
	public void deberíaPoderRecuperarUnObjectoDesdeTexto() {
		final String value = "Un objeto";
		final String convertedValue = textualizer.convertToString(value);
		final Object equivalentValue = textualizer.convertFromString(convertedValue);
		Assert.assertTrue(equivalentValue instanceof String);
		Assert.assertEquals(value, equivalentValue);
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
			@Override
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
