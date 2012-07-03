/**
 * 02/07/2012 22:19:25 Copyright (C) 2011 Darío L. García
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
package ar.com.dgarcia.lang.extensions.tests;

import junit.framework.Assert;

import org.junit.Test;

import ar.com.dgarcia.lang.extensions.BooleanEstocastico;

/**
 * Esta clase prueba la distribución del booleano estocástico
 * 
 * @author D. García
 */
public class TestBooleanoEstocastico {

	@Test
	public void deberianSalirTodosFalseSiSeUsaCeroComoProbabilidadDeTrue() {
		tirarMuchasVecesYVerificarQuePorcentajeDeTruesEsAprox(0.0);
	}

	@Test
	public void deberianSalirTodosTrueSiSeUsaUnoComoProbabilidadDeTrue() {
		tirarMuchasVecesYVerificarQuePorcentajeDeTruesEsAprox(1.0);
	}

	@Test
	public void deberíaSalirAproximadamenteLaProporcionIndicadaComoProbabilidad() {
		tirarMuchasVecesYVerificarQuePorcentajeDeTruesEsAprox(0.5);
	}

	/**
	 * @param Porcentaje
	 *            de trues esperados
	 */
	private void tirarMuchasVecesYVerificarQuePorcentajeDeTruesEsAprox(final double porcentajeEsperado) {
		final BooleanEstocastico booleano = BooleanEstocastico.create(porcentajeEsperado);
		final int cantidadDeTiradas = 100000;
		double cantidadDeTrues = 0;
		for (int i = 0; i < cantidadDeTiradas; i++) {
			final boolean salioTrue = booleano.nextValue();
			if (salioTrue) {
				cantidadDeTrues++;
			}
		}
		final double proporcionObtenida = cantidadDeTrues / cantidadDeTiradas;
		Assert.assertEquals("El porcentaje debería estar dentro del esperado", porcentajeEsperado, proporcionObtenida,
				0.01);
	}
}
