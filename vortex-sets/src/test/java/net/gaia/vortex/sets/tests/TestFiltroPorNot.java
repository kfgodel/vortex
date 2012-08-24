/**
 * 24/08/2012 15:57:55 Copyright (C) 2011 Darío L. García
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

import junit.framework.Assert;
import net.gaia.vortex.core.api.mensaje.MensajeVortex;
import net.gaia.vortex.core.impl.condiciones.SiempreFalse;
import net.gaia.vortex.core.impl.condiciones.SiempreTrue;
import net.gaia.vortex.core.impl.mensaje.MensajeConContenido;
import net.gaia.vortex.sets.impl.ContieneA;
import net.gaia.vortex.sets.impl.Not;
import net.gaia.vortex.sets.impl.ValorEsperadoIgual;

import org.junit.Test;

/**
 * Esta clase prueba la combinación de filtros usando NOT
 * 
 * @author D. García
 */
public class TestFiltroPorNot {

	@Test
	public void deberiaDevolverFalseParaUnaCondicionTrue() {
		Assert.assertFalse(Not.de(SiempreTrue.getInstancia()).esCumplidaPor(null));
	}

	@Test
	public void deberiaDevolverTrueParaUnaCondicionFalse() {
		Assert.assertTrue(Not.de(SiempreFalse.getInstancia()).esCumplidaPor(null));
	}

	/**
	 * Por la manera en que se toma false si el atributo no existe, al negarlo se consideraría true
	 */
	@Test
	public void deberiaDevolverTrueSiSeNiegaLaCondicionEqualsYElAtributoNoExiste() {
		final MensajeVortex mensajeVacio = MensajeConContenido.crearVacio();
		Assert.assertTrue(Not.de(ValorEsperadoIgual.a(null, "inexistente")).esCumplidaPor(mensajeVacio));
	}

	/**
	 * Por la manera en que se toma false si el atributo no existe, al negarlo se consideraría true
	 */
	@Test
	public void deberiaDevolverTrueSiSeNievaLaCondicionContainsYElAtributoNoExiste() {
		final MensajeVortex mensajeVacio = MensajeConContenido.crearVacio();
		Assert.assertTrue(Not.de(ContieneA.valor(null, "inexistente")).esCumplidaPor(mensajeVacio));
	}

}
