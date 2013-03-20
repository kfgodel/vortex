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
package ar.dgarcia.textualizer.xml.tests;

import org.junit.Before;

import ar.dgarcia.textualizer.tests.TestTextualizerSupport;
import ar.dgarcia.textualizer.xml.XmlTextualizer;

/**
 * Esta clase prueba el comportamiento de los conversores a y desde texto
 * 
 * @author D. García
 */
public class TestXmlTextualizer extends TestTextualizerSupport {

	
	@Before
	public void crearTextualizer() {
		textualizer = XmlTextualizer.create();
	}
}
