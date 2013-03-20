/**
 * 01/07/2012 02:34:42 Copyright (C) 2011 Darío L. García
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

import ar.com.dgarcia.lang.strings.ToString;

/**
 * Esta clase representa un valor entero que está definido en un rango de valores.<br>
 * Este entero puede ser incrementado y al llegar al final empieza de nuevo, esta clase es
 * utilizable como índice en buffers circulares
 * 
 * @author D. García
 */
public class IndiceCicular implements Iterator<Long> {

	private long minimoInclusivo;
	public static final String minimoInclusivo_FIELD = "minimoInclusivo";

	private long maximoExclusivo;
	public static final String maximoExclusivo_FIELD = "maximoExclusivo";

	private long valorActual;
	public static final String valorActual_FIELD = "valorActual";

	private long proximoValor;
	public static final String proximoValor_FIELD = "proximoValor";

	/**
	 * @see java.lang.Object#toString()
	 */
	
	public String toString() {
		return ToString.de(this).con(valorActual_FIELD, valorActual).con(minimoInclusivo_FIELD, minimoInclusivo)
				.con(maximoExclusivo_FIELD, maximoExclusivo).con(proximoValor_FIELD, proximoValor).toString();
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
	
	public Long next() {
		valorActual = proximoValor;
		proximoValor++;
		ajustarProximoValor();
		return valorActual;
	}

	/**
	 * Asegura que el proximo valor esté dentro del rango posible
	 */
	private void ajustarProximoValor() {
		if (proximoValor >= maximoExclusivo || proximoValor < minimoInclusivo) {
			proximoValor = minimoInclusivo;
		}
	}

	/**
	 * @see java.util.Iterator#remove()
	 */
	
	public void remove() {
		throw new UnsupportedOperationException("No se puede quitar elementos del LongCircular");
	}

	/**
	 * Crea un indice circular que comenzará en el valor 0, e irá incrementando hasta el valor
	 * indicado, sin incluirlo
	 * 
	 * @param maximoExcluyente
	 *            El valor máximo como referencia de límite
	 * @return El indice creado
	 */
	public static IndiceCicular desdeCeroExcluyendoA(final int maximoExcluyente) {
		final IndiceCicular indice = new IndiceCicular();
		indice.maximoExclusivo = maximoExcluyente;
		indice.minimoInclusivo = 0;
		indice.resetear();
		return indice;
	}

	/**
	 * Inicializa el conteo de este índice en el valor mínimo
	 */
	public void resetear() {
		this.proximoValor = minimoInclusivo;
	}

	/**
	 * Devuelve el valor siguiente como un entero
	 * 
	 * @return El valor siguiente de esta instancia
	 */
	public int nextInt() {
		return next().intValue();
	}

	public long getMinimoInclusivo() {
		return minimoInclusivo;
	}

	public void setMinimoInclusivo(final long minimoInclusivo) {
		if (minimoInclusivo >= maximoExclusivo) {
			throw new IllegalArgumentException("El mínimo[" + minimoInclusivo + "] debe ser menor al máximo["
					+ maximoExclusivo + "]");
		}
		this.minimoInclusivo = minimoInclusivo;
		ajustarProximoValor();
	}

	public long getMaximoExclusivo() {
		return maximoExclusivo;
	}

	public void setMaximoExclusivo(final long maximoExclusivo) {
		if (maximoExclusivo <= minimoInclusivo) {
			throw new IllegalArgumentException("El máximo[" + maximoExclusivo + "] debe ser mayor al mínimo["
					+ minimoInclusivo + "]");
		}
		this.maximoExclusivo = maximoExclusivo;
		ajustarProximoValor();
	}

	public long getProximoValor() {
		return proximoValor;
	}

	public void setProximoValor(final long proximoValor) {
		this.proximoValor = proximoValor;
		ajustarProximoValor();
	}

}
