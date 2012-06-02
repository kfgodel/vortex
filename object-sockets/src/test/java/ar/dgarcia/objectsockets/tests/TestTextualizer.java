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

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import ar.dgarcia.objectsockets.api.ObjectTextualizer;
import ar.dgarcia.objectsockets.external.xml.XmlTextualizer;

/**
 * Esta clase prueba el comportamiento de los conversores a y desde texto
 * 
 * @author D. García
 */
public class TestTextualizer {

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
	 * Por la manera en la que cree el {@link SerializerCodecFactory} las instancias de serializer
	 * son compartidas en la máquina por lo que varios threads podrían invocar al mismo tiempo
	 */
	@Test
	public void elSerializadorDeberiaPermitirEjecucionesConcurrentes() {

	}

}
