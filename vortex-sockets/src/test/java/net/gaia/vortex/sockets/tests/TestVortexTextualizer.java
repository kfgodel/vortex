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
import net.gaia.vortex.core.impl.mensaje.MensajeConContenido;
import net.gaia.vortex.core.tests.MensajeModeloParaTests;
import net.gaia.vortex.portal.impl.moleculas.mapeador.ContenidoVortexLazy;
import net.gaia.vortex.portal.impl.moleculas.mapeador.MapeadorJackson;
import net.gaia.vortex.sockets.external.json.VortexSocketTextualizer;

import org.junit.Before;
import org.junit.Test;

/**
 * Esta clase pruebas las conversiones de el textualizador vortex
 * 
 * @author D. García
 */
public class TestVortexTextualizer {

	private VortexSocketTextualizer textualizer;

	@Before
	public void crearTextualizador() {
		textualizer = VortexSocketTextualizer.create();
	}

	@Test
	public void deberiaConvertirJsonDeObjetoVacioUnMensajeVacioSoloConTraza() {
		final MensajeConContenido mensaje = MensajeConContenido.create();
		final String texto = textualizer.convertToString(mensaje);
		Assert.assertEquals("Debería ser el objeto vacio de json", "{\"traza_identificadores\":[]}", texto);
	}

	@Test
	public void deberiaConvertirEnMapaDePrimitivaUnMensajeConPrimitiva() {
		final MensajeConContenido mensaje = MensajeConContenido.create(ContenidoPrimitiva.create("unTexto"));
		final String texto = textualizer.convertToString(mensaje);
		Assert.assertNotNull("Debería existir un texto", texto);
		Assert.assertNotNull("Debería tener la clave esperada",
				texto.contains("\"CLASSNAME_KEY\":\"java.lang.String\""));
		Assert.assertNotNull("Debería tener la clave esperada", texto.contains("\"traza_identificadores\":[]"));
		Assert.assertNotNull("Debería tener la clave esperada", texto.contains("\"PRIMITIVA_VORTEX_KEY\":\"unTexto\""));
	}

	@Test
	public void deberíaConvertirEnMapaDePrimitivaUnMensajeConPrimitivaLazy() {
		final MensajeConContenido mensaje = MensajeConContenido.create(ContenidoVortexLazy.create(
				MensajeModeloParaTests.create(), MapeadorJackson.create()));
		final String texto = textualizer.convertToString(mensaje);
		Assert.assertNotNull("Debería existir un texto", texto);
		Assert.assertNotNull("Debería tener la clave esperada",
				texto.contains("\"CLASSNAME_KEY\":\"net.gaia.vortex.core.tests.MensajeModeloParaTests\""));
		Assert.assertNotNull("Debería tener la clave esperada", texto.contains("\"traza_identificadores\":[]"));
		Assert.assertNotNull("Debería tener la clave esperada", texto.contains("\"estadoAdicionalAlMensaje\":{"));
		Assert.assertNotNull("Debería tener la clave esperada", texto.contains("\"numeroConComa\":120.0"));
		Assert.assertNotNull("Debería tener la clave esperada", texto.contains("\"booleano\":true"));
		Assert.assertNotNull("Debería tener la clave esperada", texto
				.contains("\"variosStrings\":[\"s1\",\"s2\",\"s3\",\"s4\",\"s5\",\"s6\",\"s7\",\"s8\",\"s9\",\"s10\""));
		Assert.assertNotNull("Debería tener la clave esperada",
				texto.contains("\"posibleDominio\":\"net.gaia.vortex.tests\""));
	}

	@Test
	public void deberiaConvertirUnaInstanciaDeObjectEnMensajeVacio() {
		final MensajeConContenido mensaje = MensajeConContenido.create(ContenidoVortexLazy.create(new Object(),
				MapeadorJackson.create()));
		final String texto = textualizer.convertToString(mensaje);
		Assert.assertNotNull("Debería existir un texto", texto);
		Assert.assertNotNull("Debería tener la clave esperada",
				texto.contains("\"CLASSNAME_KEY\":\"java.lang.Object\""));
		Assert.assertNotNull("Debería tener la clave esperada", texto.contains("\"traza_identificadores\":[]"));
	}
}
