/**
 * 20/01/2013 19:43:26 Copyright (C) 2011 Darío L. García
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
import net.gaia.vortex.core.api.condiciones.ResultadoDeCondicion;
import net.gaia.vortex.core.impl.condiciones.SiempreFalse;
import net.gaia.vortex.core.impl.condiciones.SiempreTrue;
import net.gaia.vortex.sets.impl.AndCompuesto;
import net.gaia.vortex.sets.impl.CondicionDesconocida;
import net.gaia.vortex.sets.impl.Negacion;
import net.gaia.vortex.sets.impl.OrCompuesto;

import org.junit.Test;

/**
 * Esta clase prueba la evaluacion de distintas condiciones con partes desconocidas
 * 
 * @author D. García
 */
public class TestFiltroDesconocido {

	@Test
	public void deberiaSerResultadoDesconocidoSiLaCondicionEsDesconocida() {
		Assert.assertEquals(ResultadoDeCondicion.INDECIDIBLE, CondicionDesconocida.getInstancia().esCumplidaPor(null));
	}

	@Test
	public void trueAndDesconocidoDeberiaSerDesconocido() {
		Assert.assertEquals(ResultadoDeCondicion.INDECIDIBLE,
				AndCompuesto.de(SiempreTrue.getInstancia(), CondicionDesconocida.getInstancia()).esCumplidaPor(null));
	}

	@Test
	public void falseAndDesconocidoDeberiaSerFalse() {
		Assert.assertEquals(ResultadoDeCondicion.FALSE,
				AndCompuesto.de(SiempreFalse.getInstancia(), CondicionDesconocida.getInstancia()).esCumplidaPor(null));
	}

	@Test
	public void desconocidoAndDesconocidoDeberiaSerDesconocido() {
		Assert.assertEquals(ResultadoDeCondicion.INDECIDIBLE,
				AndCompuesto.de(CondicionDesconocida.getInstancia(), CondicionDesconocida.getInstancia())
						.esCumplidaPor(null));
	}

	@Test
	public void desconocidoOrDesconocidoDeberiaSerDesconocido() {
		Assert.assertEquals(
				ResultadoDeCondicion.INDECIDIBLE,
				OrCompuesto.de(CondicionDesconocida.getInstancia(), CondicionDesconocida.getInstancia()).esCumplidaPor(
						null));
	}

	@Test
	public void falseOrDesconocidoDeberiaSerDesconocido() {
		Assert.assertEquals(ResultadoDeCondicion.INDECIDIBLE,
				OrCompuesto.de(SiempreFalse.getInstancia(), CondicionDesconocida.getInstancia()).esCumplidaPor(null));
	}

	@Test
	public void trueOrDesconocidoDeberiaSerTrue() {
		Assert.assertEquals(ResultadoDeCondicion.TRUE,
				OrCompuesto.de(SiempreTrue.getInstancia(), CondicionDesconocida.getInstancia()).esCumplidaPor(null));
	}

	@Test
	public void notDesconocidoDeberiaSerDesconocido() {
		Assert.assertEquals(ResultadoDeCondicion.INDECIDIBLE, Negacion.de(CondicionDesconocida.getInstancia())
				.esCumplidaPor(null));
	}

}
