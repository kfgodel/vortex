/**
 * 27/06/2012 14:45:45 Copyright (C) 2011 Darío L. García
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
package net.gaia.vortex.core.tests;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import junit.framework.Assert;
import net.gaia.vortex.core.api.moleculas.ids.IdDeComponenteVortex;
import net.gaia.vortex.core.impl.moleculas.ids.GeneradorDeIdsEstaticos;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Esta clase prueba la generacion de IDs vortex
 * 
 * @author D. García
 */
public class TestIdsDeIds {
	private static final Logger LOG = LoggerFactory.getLogger(TestIdsDeIds.class);

	@Test
	public void elIdDeVortexDeberíaTener24CaracteresEnElMomento0() {
		final IdDeComponenteVortex nuevoId = GeneradorDeIdsEstaticos.getInstancia().generarId();
		final String idComoString = nuevoId.getValorActual();
		LOG.debug("ID generado: {}", idComoString);
		Assert.assertTrue("El largo debería ser mayor al minimo", GeneradorDeIdsEstaticos.LONGITUD_INCIAL_SECUENCIA
				+ GeneradorDeIdsEstaticos.LONGITUD_INICIAL_GENERADOR_TIMESTAMP
				+ GeneradorDeIdsEstaticos.LONGITUD_INICIAL_RANDOM_PART
				+ GeneradorDeIdsEstaticos.LONGITUD_INICIAL_VORTEX_TIMESTAMP < idComoString.length());
	}

	@Test
	public void elIdDeberiaRepresentarUnNumeroHexaGrande() {
		final IdDeComponenteVortex nuevoId = GeneradorDeIdsEstaticos.getInstancia().generarId();
		final String idComoString = nuevoId.getValorActual();
		final Matcher matcher = Pattern.compile(
				"[A-F0-9]{" + GeneradorDeIdsEstaticos.LONGITUD_INICIAL_RANDOM_PART + "}-[A-F0-9]{"
						+ GeneradorDeIdsEstaticos.LONGITUD_INICIAL_VORTEX_TIMESTAMP + ",}-[A-F0-9]{"
						+ GeneradorDeIdsEstaticos.LONGITUD_INICIAL_GENERADOR_TIMESTAMP + ",}-[A-F0-9]{"
						+ GeneradorDeIdsEstaticos.LONGITUD_INCIAL_SECUENCIA + ",}").matcher(idComoString);
		LOG.debug("ID generado: {}", idComoString);
		Assert.assertTrue("El id debería matchear la expresion regular", matcher.matches());
	}

}
