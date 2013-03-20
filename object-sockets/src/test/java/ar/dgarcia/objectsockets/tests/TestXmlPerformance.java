/**
 * 02/06/2012 14:54:02 Copyright (C) 2011 Darío L. García
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

import ar.dgarcia.textualizer.api.ObjectTextualizer;
import ar.dgarcia.textualizer.xml.XmlTextualizer;

/**
 * Esta clase prueba la velocidad de transferencia de los objetos entre dos sockets usando XML como
 * textualizador
 * 
 * @author D. García
 */
public class TestXmlPerformance extends TestSocketPerformance {
	/**
	 * Crea el textualizador para usar en los tests
	 * 
	 * @return
	 */
	
	protected ObjectTextualizer createTextualizer() {
		return XmlTextualizer.create();
	}

}
