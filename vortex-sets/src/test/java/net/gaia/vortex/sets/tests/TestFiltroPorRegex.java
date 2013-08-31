/**
 * 24/08/2012 16:55:13 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.sets.tests;

import java.util.Arrays;
import java.util.HashMap;

import net.gaia.vortex.api.condiciones.ResultadoDeCondicion;
import net.gaia.vortex.core.api.mensaje.MensajeVortex;
import net.gaia.vortex.core.impl.mensaje.MensajeConContenido;
import net.gaia.vortex.sets.impl.condiciones.TextoRegexMatchea;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Esta clase testea el uso de la condición por expresión regular
 * 
 * @author D. García
 */
public class TestFiltroPorRegex {

	private static final String SEGUNDO_TEXTO = "Chau";
	private static final double SEGUNDO_NUMERO = 2.0;
	private static final String PRIMER_TEXTO = "Hola";
	private static final double PRIMER_NUMERO = 1.0;
	private static final String NUMERO_FIELD = "numero";
	private static final String INEXISTENTE_FIELD = "inexistente";
	private static final String TEXTO_FIELD = "texto";
	private static final String COLECCION_FIELD = "coleccion";
	private static final String OBJETO_FIELD = "objeto";
	private static final String NULL_FIELD = "nulo";

	private MensajeVortex mensaje;
	private HashMap<String, Object> objeto;

	@Before
	public void crearMensaje() {
		mensaje = MensajeConContenido.crearVacio();
		mensaje.getContenido().put(NUMERO_FIELD, PRIMER_NUMERO);
		mensaje.getContenido().put(TEXTO_FIELD, PRIMER_TEXTO);
		objeto = new HashMap<String, Object>();
		objeto.put(NUMERO_FIELD, SEGUNDO_NUMERO);
		objeto.put(TEXTO_FIELD, SEGUNDO_TEXTO);
		objeto.put(NULL_FIELD, null);
		objeto.put(COLECCION_FIELD, Arrays.asList(PRIMER_TEXTO, PRIMER_NUMERO, null));
		mensaje.getContenido().put(COLECCION_FIELD, Arrays.asList(PRIMER_TEXTO, PRIMER_NUMERO, null));
		mensaje.getContenido().put(OBJETO_FIELD, objeto);
		mensaje.getContenido().put(NULL_FIELD, null);
	}

	@Test
	public void deberiaIndicarTrueSiLaPropiedadMatcheaLaExpresionRegular() {
		Assert.assertEquals(ResultadoDeCondicion.TRUE,
				TextoRegexMatchea.laExpresion(".*", TEXTO_FIELD).esCumplidaPor(mensaje));
		Assert.assertEquals(ResultadoDeCondicion.TRUE,
				TextoRegexMatchea.laExpresion("^Hola", TEXTO_FIELD).esCumplidaPor(mensaje));
	}

	@Test
	public void deberiaIndicarFalseSiLaProiedadNoMactcheaLaExpresion() {
		Assert.assertEquals(ResultadoDeCondicion.FALSE,
				TextoRegexMatchea.laExpresion("Chau$", TEXTO_FIELD).esCumplidaPor(mensaje));
	}

	@Test
	public void deberiaIndicarFalseSiLaPropiedadNoExiste() {
		Assert.assertEquals(ResultadoDeCondicion.FALSE, TextoRegexMatchea.laExpresion(".*", INEXISTENTE_FIELD)
				.esCumplidaPor(mensaje));
	}

	@Test
	public void deberiaIndicarFalseSiLaPropiedadEsNull() {
		Assert.assertEquals(ResultadoDeCondicion.FALSE,
				TextoRegexMatchea.laExpresion(".*", NULL_FIELD).esCumplidaPor(mensaje));
	}

	@Test
	public void deberiaIndicarTrueSiLaExpresionEsIgualALaPropiedad() {
		Assert.assertEquals(ResultadoDeCondicion.TRUE, TextoRegexMatchea.laExpresion(PRIMER_TEXTO, TEXTO_FIELD)
				.esCumplidaPor(mensaje));
	}

	@Test
	public void deberiaIndicarFalseSiLaPropiedadNoEsDeTipoCharSequence() {
		Assert.assertEquals(ResultadoDeCondicion.FALSE, TextoRegexMatchea.laExpresion(PRIMER_TEXTO, NUMERO_FIELD)
				.esCumplidaPor(mensaje));
	}

	@Test
	public void deberiaIndicarFalseSiLaPropiedadItermediaEsNull() {
		Assert.assertEquals(ResultadoDeCondicion.FALSE,
				TextoRegexMatchea.laExpresion(PRIMER_TEXTO, NULL_FIELD + "." + TEXTO_FIELD).esCumplidaPor(mensaje));
	}

	@Test
	public void deberiaIndicarTrueSiLaPropiedadAnidadaMatchea() {
		Assert.assertEquals(ResultadoDeCondicion.TRUE,
				TextoRegexMatchea.laExpresion(SEGUNDO_TEXTO, OBJETO_FIELD + "." + TEXTO_FIELD).esCumplidaPor(mensaje));
	}

	@Test
	public void deberiaIndicarFalseSiLaPropiedadIntermediaNoEsUnMapa() {
		Assert.assertEquals(ResultadoDeCondicion.FALSE,
				TextoRegexMatchea.laExpresion(SEGUNDO_TEXTO, COLECCION_FIELD + "." + TEXTO_FIELD).esCumplidaPor(mensaje));
	}
}
