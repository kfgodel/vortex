/**
 * 24/08/2012 15:52:03 Copyright (C) 2011 Darío L. García
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

import java.util.ArrayList;

import junit.framework.Assert;
import net.gaia.vortex.core.api.condiciones.Condicion;
import net.gaia.vortex.core.api.condiciones.ResultadoDeCondicion;
import net.gaia.vortex.core.impl.condiciones.SiempreFalse;
import net.gaia.vortex.core.impl.condiciones.SiempreTrue;
import net.gaia.vortex.sets.impl.Or;

import org.junit.Test;

/**
 * Esta clase prueba el funcionaiento del filtro que combina condiciones con OR
 * 
 * @author D. García
 */
public class TestFiltroPorOr {

	@Test
	public void deberiaDarFalseParaDosCondicionesFalse() {
		Assert.assertEquals(ResultadoDeCondicion.FALSE,
				Or.create(SiempreFalse.getInstancia(), SiempreFalse.getInstancia()).esCumplidaPor(null));
	}

	@Test
	public void deberiaDarTrueSiAlgunaEsTrue() {
		Assert.assertEquals(ResultadoDeCondicion.TRUE,
				Or.create(SiempreFalse.getInstancia(), SiempreTrue.getInstancia(), SiempreTrue.getInstancia())
						.esCumplidaPor(null));
	}

	@Test
	public void noDeberiaEvaluarElRestoSiLaPrimeraEsTrue() {
		final CondicionTestWrapper condicionEvaluada = CondicionTestWrapper.create(SiempreTrue.getInstancia());
		final CondicionTestWrapper condicionNoEvaluada = CondicionTestWrapper.create(SiempreFalse.getInstancia());
		Assert.assertEquals(ResultadoDeCondicion.TRUE,
				Or.create(condicionEvaluada, condicionNoEvaluada).esCumplidaPor(null));
		Assert.assertTrue(condicionEvaluada.isEvaluada());
		Assert.assertFalse(condicionNoEvaluada.isEvaluada());
	}

	/**
	 * Por la implementación actual esto es más una consecuencia que algo buscado. Este test es sólo
	 * para documentarlo
	 */
	@Test
	public void deberiaDarFalseSiNoTieneCondiciones() {
		Assert.assertEquals(ResultadoDeCondicion.FALSE, Or.create(new ArrayList<Condicion>()).esCumplidaPor(null));
	}
}
