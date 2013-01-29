/**
 * 20/01/2013 19:07:15 Copyright (C) 2011 Darío L. García
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

import net.gaia.vortex.core.api.condiciones.ResultadoDeCondicion;
import net.gaia.vortex.core.api.mensaje.MensajeVortex;
import net.gaia.vortex.core.impl.mensaje.MensajeConContenido;
import net.gaia.vortex.sets.impl.condiciones.AtributoPresente;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Esta clase prueba la definición del filtro por propiedad existente
 * 
 * @author D. García
 */
public class TestFiltroPorAtributoPresente {

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

	@SuppressWarnings("unchecked")
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
	public void deberiaIndicarTrueSiLaPropiedadExite() {
		Assert.assertEquals(ResultadoDeCondicion.TRUE, AtributoPresente.conNombre(COLECCION_FIELD).esCumplidaPor(mensaje));
		Assert.assertEquals(ResultadoDeCondicion.TRUE, AtributoPresente.conNombre(COLECCION_FIELD).esCumplidaPor(mensaje));
	}

	@Test
	public void deberiaIndicarFalseSiLaPropiedadNoExiste() {
		Assert.assertEquals(ResultadoDeCondicion.FALSE, AtributoPresente.conNombre(INEXISTENTE_FIELD).esCumplidaPor(mensaje));
	}

	@Test
	public void deberiaIndicarTrueSiLaPropiedadAnidadaExiste() {
		Assert.assertEquals(ResultadoDeCondicion.TRUE, AtributoPresente.conNombre(OBJETO_FIELD + "." + COLECCION_FIELD)
				.esCumplidaPor(mensaje));
		Assert.assertEquals(ResultadoDeCondicion.TRUE, AtributoPresente.conNombre(OBJETO_FIELD + "." + COLECCION_FIELD)
				.esCumplidaPor(mensaje));
	}

	@Test
	public void deberiaIndicarFalseSiLaPropiedadIntermediaEsNull() {
		Assert.assertEquals(ResultadoDeCondicion.FALSE, AtributoPresente.conNombre(NULL_FIELD + "." + COLECCION_FIELD)
				.esCumplidaPor(mensaje));
	}

	@Test
	public void deberiaIndicarFalseSiLaPropiedadIntermediaNoEsUnMapa() {
		Assert.assertEquals(ResultadoDeCondicion.FALSE, AtributoPresente.conNombre(COLECCION_FIELD + "." + COLECCION_FIELD)
				.esCumplidaPor(mensaje));
	}

}
