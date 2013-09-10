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
import net.gaia.vortex.api.condiciones.ResultadoDeCondicion;
import net.gaia.vortex.api.mensajes.MensajeVortex;
import net.gaia.vortex.impl.condiciones.SiempreFalse;
import net.gaia.vortex.impl.condiciones.SiempreTrue;
import net.gaia.vortex.impl.mensajes.MensajeConContenido;
import net.gaia.vortex.sets.impl.condiciones.ColeccionContiene;
import net.gaia.vortex.sets.impl.condiciones.Negacion;
import net.gaia.vortex.sets.impl.condiciones.ValorEsperadoEn;

import org.junit.Test;

/**
 * Esta clase prueba la combinación de filtros usando NOT
 * 
 * @author D. García
 */
public class TestFiltroPorNot {

	@Test
	public void deberiaDevolverFalseParaUnaCondicionTrue() {
		Assert.assertEquals(ResultadoDeCondicion.FALSE, Negacion.de(SiempreTrue.getInstancia()).esCumplidaPor(null));
	}

	@Test
	public void deberiaDevolverTrueParaUnaCondicionFalse() {
		Assert.assertEquals(ResultadoDeCondicion.TRUE, Negacion.de(SiempreFalse.getInstancia()).esCumplidaPor(null));
	}

	/**
	 * Por la manera en que se toma false si el atributo no existe, al negarlo se consideraría true
	 */
	@Test
	public void deberiaDevolverTrueSiSeNiegaLaCondicionEqualsYElAtributoNoExiste() {
		final MensajeVortex mensajeVacio = MensajeConContenido.crearVacio();
		Assert.assertEquals(ResultadoDeCondicion.TRUE,
				Negacion.de(ValorEsperadoEn.elAtributo("inexistente", null)).esCumplidaPor(mensajeVacio));
	}

	/**
	 * Por la manera en que se toma false si el atributo no existe, al negarlo se consideraría true
	 */
	@Test
	public void deberiaDevolverTrueSiSeNievaLaCondicionContainsYElAtributoNoExiste() {
		final MensajeVortex mensajeVacio = MensajeConContenido.crearVacio();
		Assert.assertEquals(ResultadoDeCondicion.TRUE,
				Negacion.de(ColeccionContiene.alValor(null, "inexistente")).esCumplidaPor(mensajeVacio));
	}

}
