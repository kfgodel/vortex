/**
 * 02/07/2012 01:25:32 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.sockets.tests;

import junit.framework.Assert;
import net.gaia.vortex.core.impl.mensaje.ContenidoPrimitiva;
import net.gaia.vortex.core.impl.mensaje.MensajeMapa;
import net.gaia.vortex.core.tests.MensajeModeloParaTests;
import net.gaia.vortex.portal.impl.moleculas.mapeador.ContenidoVortexLazy;
import net.gaia.vortex.portal.impl.moleculas.mapeador.MapeadorJackson;
import net.gaia.vortex.sockets.external.json.VortexTextualizer;

import org.junit.Before;
import org.junit.Test;

/**
 * Esta clase pruebas las conversiones de el textualizador vortex
 * 
 * @author D. García
 */
public class TestVortexTextualizer {

	private VortexTextualizer textualizer;

	@Before
	public void crearTextualizador() {
		textualizer = VortexTextualizer.create();
	}

	@Test
	public void deberiaConvertirJsonDeObjetoVacioUnMensajeVacioSoloConTraza() {
		final MensajeMapa mensaje = MensajeMapa.create();
		final String texto = textualizer.convertToString(mensaje);
		Assert.assertEquals("Debería ser el objeto vacio de json", "{\"traza_identificadores\":[]}", texto);
	}

	@Test
	public void deberiaConvertirEnMapaDePrimitivaUnMensajeConPrimitiva() {
		final MensajeMapa mensaje = MensajeMapa.create(ContenidoPrimitiva.create("unTexto"));
		final String texto = textualizer.convertToString(mensaje);
		Assert.assertEquals("Debería ser el objeto vacio de json",
				"{\"PRIMITIVA_VORTEX_KEY\":\"unTexto\",\"traza_identificadores\":[]}", texto);
	}

	@Test
	public void deberíaConvertirEnMapaDePrimitivaUnMensajeConPrimitivaLazy() {
		final MensajeMapa mensaje = MensajeMapa.create(ContenidoVortexLazy.create(MensajeModeloParaTests.create(),
				MapeadorJackson.create()));
		final String texto = textualizer.convertToString(mensaje);
		Assert.assertEquals(
				"Debería ser el objeto vacio de json",
				"{\"estadoAdicionalAlMensaje\":{\"valorExtra\":\"Super extra\",\"numeroConComa\":120.0,\"booleano\":true},\"numeroDeSecuencia\":0,\"variosStrings\":[\"s1\",\"s2\",\"s3\",\"s4\",\"s5\",\"s6\",\"s7\",\"s8\",\"s9\",\"s10\"],\"posibleDominio\":\"net.gaia.vortex.tests\",\"posibleEmisor\":\"kfgodel-saraza\",\"traza_identificadores\":[]}",
				texto);
	}
}