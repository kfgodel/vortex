/**
 * 23/08/2012 08:57:15 Copyright (C) 2011 10Pines S.R.L.
 */
package net.gaia.vortex.sets.tests;

import java.util.HashMap;

import junit.framework.Assert;
import net.gaia.vortex.core.api.condiciones.ResultadoDeCondicion;
import net.gaia.vortex.core.api.mensaje.MensajeVortex;
import net.gaia.vortex.core.impl.mensaje.MensajeConContenido;
import net.gaia.vortex.sets.impl.AtributoEmpieza;

import org.junit.Before;
import org.junit.Test;

/**
 * Esta clase testea el filtro de comienzo de string
 * 
 * @author D. García
 */
public class TestFiltroEmpiezaCon {

	private static final String PREFIJO_VALIDO = "Hola";
	private static final String PREFIJO_CON_ACENTO = "Hóla";
	private static final String PREFIJO_CON_DISTINTO_CASE = "HOLA";
	private static final String PREFIJO_INVALIDO = "Chau";
	private static final String PRIMER_TEXTO = "HolasAmigos";
	private static final String TEXTO_FIELD = "texto";
	private static final double PRIMER_NUMERO = 1.0;
	private static final String NUMERO_FIELD = "numero";
	private static final String INEXISTENTE_FIELD = "inexistente";
	private static final String OBJETO_FIELD = "objeto";
	private static final String NULL_FIELD = "nulo";

	private MensajeVortex mensaje;
	private HashMap<String, Object> objeto;

	@Before
	public void crearMensaje() {
		mensaje = MensajeConContenido.crearVacio();
		mensaje.getContenido().put(NUMERO_FIELD, PRIMER_NUMERO);
		mensaje.getContenido().put(TEXTO_FIELD, PRIMER_TEXTO);
		mensaje.getContenido().put(NULL_FIELD, null);
		objeto = new HashMap<String, Object>();
		objeto.put(NUMERO_FIELD, PRIMER_NUMERO);
		objeto.put(TEXTO_FIELD, PRIMER_TEXTO);
		objeto.put(NULL_FIELD, null);
		mensaje.getContenido().put(OBJETO_FIELD, objeto);
	}

	@Test
	public void deberiaIndicarTrueSiLaPropiedadDelObjetoEmpiezaConElPrefijo() {
		Assert.assertEquals(ResultadoDeCondicion.TRUE,
				AtributoEmpieza.conPrefijo(PREFIJO_VALIDO, TEXTO_FIELD).esCumplidaPor(mensaje));
	}

	@Test
	public void deberiaIndicarFalseSiLaPropiedadDelObjetoNoEmpiezaConElPrefijo() {
		Assert.assertEquals(ResultadoDeCondicion.FALSE, AtributoEmpieza.conPrefijo(PREFIJO_INVALIDO, TEXTO_FIELD)
				.esCumplidaPor(mensaje));
	}

	@Test
	public void deberiaIndicarFalseSiElPrefijoDifiereEnUnAcento() {
		Assert.assertEquals(ResultadoDeCondicion.FALSE, AtributoEmpieza.conPrefijo(PREFIJO_CON_ACENTO, TEXTO_FIELD)
				.esCumplidaPor(mensaje));
	}

	@Test
	public void deberiaIndicarFalseSiElPrefijoTieneDistintoCase() {
		Assert.assertEquals(ResultadoDeCondicion.FALSE, AtributoEmpieza.conPrefijo(PREFIJO_CON_DISTINTO_CASE, TEXTO_FIELD)
				.esCumplidaPor(mensaje));
	}

	@Test
	public void deberiaIndicarFalseSiElValorDeLaPropiedadEsNull() {
		Assert.assertEquals(ResultadoDeCondicion.FALSE,
				AtributoEmpieza.conPrefijo(PREFIJO_VALIDO, NULL_FIELD).esCumplidaPor(mensaje));
	}

	@Test
	public void deberiaIndicarTrueSiLaPropiedadAnidadaEmpiezaConElPrefijo() {
		Assert.assertEquals(ResultadoDeCondicion.TRUE,
				AtributoEmpieza.conPrefijo(PREFIJO_VALIDO, OBJETO_FIELD + "." + TEXTO_FIELD).esCumplidaPor(mensaje));
	}

	@Test
	public void deberiaIndicarFalseSiLaPropiedadIntermediaEsNull() {
		Assert.assertEquals(ResultadoDeCondicion.FALSE,
				AtributoEmpieza.conPrefijo(PREFIJO_VALIDO, NULL_FIELD + "." + TEXTO_FIELD).esCumplidaPor(mensaje));
	}

	@Test
	public void deberiaIndicarFalseSiLaPropiedadIntermediaNoEsUnMapa() {
		Assert.assertEquals(ResultadoDeCondicion.FALSE,
				AtributoEmpieza.conPrefijo(PREFIJO_VALIDO, NUMERO_FIELD + "." + TEXTO_FIELD).esCumplidaPor(mensaje));
	}

	@Test
	public void deberiaIndicarFalseSiLaPropiedadNoExiste() {
		Assert.assertEquals(ResultadoDeCondicion.FALSE, AtributoEmpieza.conPrefijo(PREFIJO_VALIDO, INEXISTENTE_FIELD)
				.esCumplidaPor(mensaje));
	}

	@Test
	public void deberiaIndicarFalseSiElValorDeLaPropiedadNoEsString() {
		Assert.assertEquals(ResultadoDeCondicion.FALSE, AtributoEmpieza.conPrefijo(PREFIJO_VALIDO, NUMERO_FIELD)
				.esCumplidaPor(mensaje));
	}
}
