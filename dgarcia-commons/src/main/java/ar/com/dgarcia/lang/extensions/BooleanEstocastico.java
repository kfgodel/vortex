/**
 * 02/07/2012 22:12:50 Copyright (C) 2011 Darío L. García
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
package ar.com.dgarcia.lang.extensions;

import java.util.Iterator;
import java.util.Random;

import ar.com.dgarcia.lang.strings.ToString;

/**
 * Esta clase representa un valor booleano que cambia cada vez que se le pide el valor con una
 * probabilidad prestablecida de ser true
 * 
 * @author D. García
 */
public class BooleanEstocastico implements Iterator<Boolean> {

	private Random random;
	private double probabilidadDeTrue;
	public static final String probabilidadDeTrue_FIELD = "probabilidadDeTrue";

	public static BooleanEstocastico create(final double probabilidadDeTrue) {
		final BooleanEstocastico booleano = new BooleanEstocastico();
		booleano.probabilidadDeTrue = probabilidadDeTrue;
		booleano.random = new Random();
		return booleano;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	
	public String toString() {
		return ToString.de(this).con(probabilidadDeTrue_FIELD, probabilidadDeTrue).toString();
	}

	/**
	 * @see java.util.Iterator#hasNext()
	 */
	
	public boolean hasNext() {
		return true;
	}

	/**
	 * @see java.util.Iterator#next()
	 */
	
	public Boolean next() {
		return nextValue();
	}

	/**
	 * @see java.util.Iterator#remove()
	 */
	
	public void remove() {
		throw new UnsupportedOperationException("El booleano estocástico no puede remover sus elementos");
	}

	/**
	 * Devuelve el próximo valor calculado
	 */
	public boolean nextValue() {
		final double tirada = random.nextDouble();
		final boolean correspondeUnTrue = tirada < probabilidadDeTrue;
		return correspondeUnTrue;
	}

}
